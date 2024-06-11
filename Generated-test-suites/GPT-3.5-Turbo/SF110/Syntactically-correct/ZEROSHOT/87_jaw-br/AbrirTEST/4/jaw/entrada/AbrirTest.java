import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

  private ProcessarEntidades form;

  @Before
  public void setUp() {
    form = new ProcessarEntidades();
  }

  @Test
  public void testAbrirConstructor1() {
    Abrir abrir = new Abrir(form, "filename.jaw.xml");
    // assert statements
    assertEquals("", abrir.xml);
    assertEquals(form, abrir.form);
  }

  @Test
  public void testAbrirConstructor2() {
    Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
    // assert statements
    assertEquals("", abrir.xml);
    assertEquals(form, abrir.form);
  }

  @Test
  public void testAbrirConstructor3() {
    Abrir abrir = new Abrir(form, Abrir.ABRIR);
    // assert statements
    assertEquals("", abrir.xml);
    assertEquals(form, abrir.form);
  }

  @Test
  public void testAbrirSerializado() {
    Abrir abrir = new Abrir(form, "filename.jaw");
    // assert statements
    assertEquals("", abrir.xml);
  }

  @Test
  public void testAbrirXML() {
    Abrir abrir = new Abrir(form, "filename.jaw.xml");
    // assert statements
    assertEquals("", abrir.xml);
  }

  @Test
  public void testFiltro2Accept() {
    Abrir abrir = new Abrir(form, "filename.jaw.xml");
    Abrir.Filtro2 filtro = abrir.new Filtro2();
    boolean result = filtro.accept(new File("filename.jaw.xml"));
    assertEquals(true, result);
  }

  @Test
  public void testFiltro2GetDescription() {
    Abrir abrir = new Abrir(form, "filename.jaw.xml");
    Abrir.Filtro2 filtro = abrir.new Filtro2();
    String result = filtro.getDescription();
    assertEquals("XML JAW files", result);
  }

  @Test
  public void testFiltroAccept() {
    Abrir abrir = new Abrir(form, "filename.jaw.xml");
    Abrir.Filtro filtro = abrir.new Filtro();
    boolean result = filtro.accept(new File("filename.jaw"));
    assertEquals(true, result);
  }

  @Test
  public void testFiltroGetDescription() {
    Abrir abrir = new Abrir(form, "filename.jaw.xml");
    Abrir.Filtro filtro = abrir.new Filtro();
    String result = filtro.getDescription();
    assertEquals("JAW files", result);
  }
}