import org.junit.Test;
import static org.junit.Assert.*;

public class AbrirTest {
    
    @Test
    public void testAbrirXML() {
        // Typical use case
        Abrir abrir = new Abrir(new ProcessarEntidades(), "example.jaw.xml");
        assertNotNull(abrir.form);
    }
    
    @Test
    public void testAbrirSerializado() {
        // Typical use case
        Abrir abrir = new Abrir(new ProcessarEntidades(), "example.jaw");
        assertNotNull(abrir.form);
    }
    
    @Test
    public void testFiltro2Accept() {
        Abrir.Filtro2 filtro2 = new Abrir().new Filtro2();
        assertTrue(filtro2.accept(new File("example.jaw.xml")));
        assertFalse(filtro2.accept(new File("example.txt")));
    }
    
    @Test
    public void testFiltroDescription() {
        Abrir.Filtro filtro = new Abrir().new Filtro();
        assertEquals("XML JAW files", filtro.getDescription());
    }
}