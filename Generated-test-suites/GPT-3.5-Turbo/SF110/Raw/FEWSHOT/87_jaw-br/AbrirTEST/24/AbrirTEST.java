import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AbrirTest {
    @Test
    public void testConstructor1() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "file.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testConstructor2() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoAbrir = 10;
        Abrir abrir = new Abrir(form, tipoAbrir);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirSerializado() {
        Abrir abrir = new Abrir(new ProcessarEntidades(), "file.jaw");
        abrir.abrirSerializado(arquivo);
        // add assertions
    }
    
    @Test
    public void testAbrirXML() {
        Abrir abrir = new Abrir(new ProcessarEntidades(), "file.jaw.xml");
        abrir.abrirXML(arquivo);
        // add assertions
    }
    
    @Test
    public void testFiltro2Accept() {
        Filtro2 filtro = new Filtro2();
        File file = new File("file.jaw.xml");
        boolean accept = filtro.accept(file);
        assertEquals(true, accept);
    }
    
    @Test
    public void testFiltro2GetDescription() {
        Filtro2 filtro = new Filtro2();
        String description = filtro.getDescription();
        assertEquals("XML JAW files", description);
    }
    
    @Test
    public void testFiltroAccept() {
        Filtro filtro = new Filtro();
        File file = new File("file.jaw");
        boolean accept = filtro.accept(file);
        assertEquals(true, accept);
    }
    
    @Test
    public void testFiltroGetDescription() {
        Filtro filtro = new Filtro();
        String description = filtro.getDescription();
        assertEquals("JAW files", description);
    }
}