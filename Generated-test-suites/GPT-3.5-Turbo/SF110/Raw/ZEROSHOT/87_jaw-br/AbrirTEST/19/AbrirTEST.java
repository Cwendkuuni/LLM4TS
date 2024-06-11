import static org.junit.Assert.*;
import org.junit.Test;

public class AbrirTest {
    
    @Test
    public void testAbrirXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "example.jaw.xml");
        
        // Test if the xml file is opened correctly
        assertEquals("example.jaw.xml", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirSerializado() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "example.jaw");
        
        // Test if the serialized file is opened correctly
        assertEquals("example.jaw", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirFileSelectorXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
        
        // Test if the file selector opens correctly and selects the xml file
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirFileSelectorSerializado() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, Abrir.ABRIR);
        
        // Test if the file selector opens correctly and selects the serialized file
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testFiltro2Accept() {
        Abrir.Filtro2 filtro2 = new Abrir.Filtro2();
        
        // Test if the filter accepts a directory
        assertTrue(filtro2.accept(new File("example_directory")));
        
        // Test if the filter accepts a .jaw.xml file
        assertTrue(filtro2.accept(new File("example.jaw.xml")));
        
        // Test if the filter rejects a .jaw file
        assertFalse(filtro2.accept(new File("example.jaw")));
    }
    
    @Test
    public void testFiltro2GetDescription() {
        Abrir.Filtro2 filtro2 = new Abrir.Filtro2();
        
        // Test if the filter description is correct
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltroAccept() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        
        // Test if the filter accepts a directory
        assertTrue(filtro.accept(new File("example_directory")));
        
        // Test if the filter accepts a .jaw file
        assertTrue(filtro.accept(new File("example.jaw")));
        
        // Test if the filter rejects a .jaw.xml file
        assertFalse(filtro.accept(new File("example.jaw.xml")));
    }
    
    @Test
    public void testFiltroGetDescription() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        
        // Test if the filter description is correct
        assertEquals("JAW files", filtro.getDescription());
    }
}