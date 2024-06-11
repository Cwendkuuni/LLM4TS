import static org.junit.Assert.*;
import org.junit.Test;

public class AbrirTest {
    
    @Test
    public void testConstructor1() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw.xml");
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testConstructor2() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
        assertNotNull(abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirSerializado() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw");
        assertNotNull(abrir.xml);
    }
    
    @Test
    public void testAbrirXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw.xml");
        assertNotNull(abrir.xml);
    }
    
}