import static org.junit.Assert.*;
import org.junit.Test;

public class AbrirTest {
    
    @Test
    public void testConstructorWithStringParameter() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw.xml");
        assertNotNull(abrir);
        assertEquals("test.jaw.xml", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testConstructorWithTypeParameter() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, 20);
        assertNotNull(abrir);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirSerializado() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw");
        abrir.abrirSerializado("test.jaw");
        assertNotNull(abrir.form.getEntidades());
    }
    
    @Test
    public void testAbrirXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw.xml");
        abrir.abrirXML("test.jaw.xml");
        assertNotNull(abrir.form.getEntidades());
    }
    
    @Test
    public void testFiltro2() {
        Abrir.Filtro2 filtro = new Abrir.Filtro2();
        assertTrue(filtro.accept(new File("test.jaw.xml")));
        assertFalse(filtro.accept(new File("test.xml")));
        assertEquals("XML JAW files", filtro.getDescription());
    }
    
    @Test
    public void testFiltro() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        assertTrue(filtro.accept(new File("test.jaw")));
        assertFalse(filtro.accept(new File("test.xml")));
        assertEquals("JAW files", filtro.getDescription());
    }
}