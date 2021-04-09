package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import com.google.gson.Gson;

import processing.core.PApplet;

public class Main extends PApplet {

	// Globales
	int xBolita = -1000;
	int yBolita = -1000;
	
	int pantalla = 1;

	private Avatar player1;
	private Avatar player2;

	private TCPConnectionP1 conexionJ1;
	private TCPConnectionP2 conexionJ2;
	
	private ArrayList <Bala> shootP1;
	private ArrayList <Bala> shootP2;

	public static void main(String[] args) {
		PApplet.main("main.Main");
	}

	// 1
	public void settings() {
		size(700, 700);
	}

	// 1
	public void setup() {
		conexionJ1 = new TCPConnectionP1();
		// Metodo de suscripcion
		conexionJ1.setMain(this);
		conexionJ1.start();

		conexionJ2 = new TCPConnectionP2();
		// Metodo de suscripcion
		conexionJ2.setMain(this);
		conexionJ2.start();

		player1 = new Avatar(this, 100, 100, color(255, 0, 0));
		player2 = new Avatar(this, 400, 400, color(0, 0, 255));
		
		shootP1 = new ArrayList<>();
		shootP2 = new ArrayList<>();
	}

	// Inifito
	public void draw() {
		
		switch(pantalla) {
		case 1://pantalla juego
			
		background(255);
		fill(255, 0, 0);
		ellipse(xBolita, yBolita, 50, 50);

		player1.pintar();
		player2.pintar();
		
		
		//Balas para el Jugador #1
		for(int i=0; i<shootP1.size();i++) {
			
			Bala b = shootP1.get(i);
			b.pintar();
			
			if(dist(shootP1.get(i).getX(), shootP1.get(i).getY(), player2.getX(), player2.getY())<25) { //como el tamaño de la bola es de 50, se usa la mitad para que la bala lo "toque"
				
				shootP1.remove(i);
				pantalla = 2;
				
			}
			
			if (b.getX()<0 || b.getX()>700) {
				
				shootP1.remove(i);
				
				
			}
			
		}
		
		//Balas para el Jugador #2
				for(int i=0; i<shootP2.size();i++) {
					
					Bala b = shootP2.get(i);
					b.pintar();
					
					if(dist(shootP2.get(i).getX(), shootP2.get(i).getY(), player1.getX(), player1.getY())<25) { //como el tamaño de la bola es de 50, se usa la mitad para que la bala lo "toque"
						
						shootP2.remove(i);
						pantalla = 3;
						
					}
					
					if (b.getX()<0 || b.getX()>700) {
						
						shootP2.remove(i);
						
						
					}
					
				}
			break;
			
		case 2: //pantalla ganador Jugador 1
			background(255,0,0);
			fill(255);
			textSize(20);
			text("GANADOR JUGADOR 1",250,350);
			break;
			
		case 3://pantalla ganador Jugador 2
			background(0,0,255);
			fill(255);
			textSize(20);
			text("GANADOR JUGADOR 2",250,350);
			break;
		}
	}

	// El metodo de notificacion: Aqui se recibe la informacion del evento
	public void notificar(Acciones c, Object obj) {

		//Verificar que clase es o que TCP mando la info 
		if(obj instanceof TCPConnectionP1) {
			
			//imprimo en la consola que sucede
			System.out.println("Jugador 1"+c.getAccion());
			
			//Dice que acción activar 
			switch(c.getAccion()) {
			
			case "UPSTART":
				player1.arribamuevalo();
				
				break;
			
			case "UPSTOP":
				
				player1.arribaparelo();
				
				break;
				
			case "DOWNSTART":
				
				player1.abajomuevalo();
				
				break;
				
			
			case "DOWNSTOP":
				
				player1.abajoparelo();
				
				break;
			
				
			case "LEFTSTART":
				
				player1.izquierdomuevalo();
				
				break;
			
			case "LEFTSTOP":
				
				player1.izquierdoparelo();
				
				break;
				
				
			case "RIGHTSTART":
				
				player1.derechomuevalo();
				
				break;
			
			case "RIGHTSTOP":
				
				player1.derechoparelo();
				
				break;
			case "SHOOT":
				
				Bala bala= new Bala(this,player1.getX(),player1.getY(), true);
				shootP1.add(bala);
				
				break;
			
			}
			
			
			
		}else if(obj instanceof TCPConnectionP2) {
			
			System.out.println("Jugador 2"+c.getAccion());
			
			switch(c.getAccion()) {
			
			case "UPSTART":
				
				player2.arribamuevalo();
				
				break;
			
			case "UPSTOP":
				
				player2.arribaparelo();
				
				break;
				
			case "DOWNSTART":
				
				player2.abajomuevalo();
				
				break;
				
			
			case "DOWNSTOP":
				
				player2.abajoparelo();
				
				break;
			
				
			case "LEFTSTART":
				
				player2.izquierdomuevalo();
				
				break;
			
			case "LEFTSTOP":
				
				player2.izquierdoparelo();
				
				break;
				
				
			case "RIGHTSTART":
				
				player2.derechomuevalo();
				
				break;
			
			case "RIGHTSTOP":
				
				player2.derechoparelo();
				
				break;
			case "SHOOT":
				
				Bala bala= new Bala(this,player2.getX(),player2.getY(), false);
				shootP2.add(bala);
				
				break;
			
			}	
		}
	}
}
