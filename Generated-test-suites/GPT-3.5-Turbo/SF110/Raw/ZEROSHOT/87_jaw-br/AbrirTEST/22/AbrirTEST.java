import jaw.entrada.*;
import jaw.gui.*;
import jaw.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

import org.junit.*;
import static org.junit.Assert.*;

public class AbrirTest {
  
  @Test
  public void testAbrirXML() {
    ProcessarEntidades form = new ProcessarEntidades();
    Abrir abrir = new Abrir(form, 10);
    abrir.abrirXML("test.xml");
    // Assert Statements
  }
  
  @Test
  public void testAbrirSerializado() {
    ProcessarEntidades form = new ProcessarEntidades();
    Abrir abrir = new Abrir(form, 20);
    abrir.abrirSerializado("test.jaw");
    // Assert Statements
  }
  
  @Test
  public void testFiltro2Accept() {
    Abrir.Filtro2 filtro = new Abrir.Filtro2();
    File file = new File("test.jaw.xml");
    boolean result = filtro.accept(file);
    // Assert Statements
  }
  
  @Test
  public void testFiltro2GetDescription() {
    Abrir.Filtro2 filtro = new Abrir.Filtro2();
    String description = filtro.getDescription();
    // Assert Statements
  }
  
  @Test
  public void testFiltroAccept() {
    Abrir.Filtro filtro = new Abrir.Filtro();
    File file = new File("test.jaw");
    boolean result = filtro.accept(file);
    // Assert Statements
  }
  
  @Test
  public void testFiltroGetDescription() {
    Abrir.Filtro filtro = new Abrir.Filtro();
    String description = filtro.getDescription();
    // Assert Statements
  }
}