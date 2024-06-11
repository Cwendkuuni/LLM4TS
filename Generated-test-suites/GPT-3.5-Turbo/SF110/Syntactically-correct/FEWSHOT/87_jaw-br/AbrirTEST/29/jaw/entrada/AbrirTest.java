package jaw.entrada;

import org.junit.*;
import static org.junit.Assert.*;

public class AbrirTest {
  
  @Test
  public void testAbrirConstructor1() {
    ProcessarEntidades form = new ProcessarEntidades();
    String arquivo = "file.xml";
    Abrir abrir = new Abrir(form, arquivo);
    assertEquals("", abrir.xml);
    assertSame(form, abrir.form);
    // add additional assertions for this constructor
  }
  
  @Test
  public void testAbrirConstructor2() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoAbrir = Abrir.ABRIR_XML;
    Abrir abrir = new Abrir(form, tipoAbrir);
    assertEquals("", abrir.xml);
    assertSame(form, abrir.form);
    // add additional assertions for this constructor
  }
  
  @Test
  public void testAbrirSerializado() {
    Abrir abrir = new Abrir();
    String arquivo = "file.jaw";
    abrir.abrirSerializado(arquivo);
    // add assertions for this method
  }
  
  @Test
  public void testAbrirXML() {
    Abrir abrir = new Abrir();
    String arquivo = "file.jaw.xml";
    abrir.abrirXML(arquivo);
    // add assertions for this method
  }
  
  private class Filtro2Test {
    
    @Test
    public void testAccept() {
      Filtro2 filtro = new Filtro2();
      File file = new File("file.jaw.xml");
      assertTrue(filtro.accept(file));
    }
    
    @Test
    public void testGetDescription() {
      Filtro2 filtro = new Filtro2();
      assertEquals("XML JAW files", filtro.getDescription());
    }
  }
  
  private class FiltroTest {
    
    @Test
    public void testAccept() {
      Filtro filtro = new Filtro();
      File file = new File("file.jaw");
      assertTrue(filtro.accept(file));
    }
    
    @Test
    public void testGetDescription() {
      Filtro filtro = new Filtro();
      assertEquals("JAW files", filtro.getDescription());
    }
  }
}