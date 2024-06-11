import static org.junit.Assert.*;
import org.junit.Test;

public class AbrirTest {
    
    @Test
    public void testAbrirXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw.xml");
        assertNotNull(abrir.form);
        assertNotNull(abrir.xml);
    }
    
    @Test
    public void testAbrirSerializado() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw");
        assertNotNull(abrir.form);
        assertNotNull(abrir.xml);
    }
    
    @Test
    public void testFiltro2Accept() {
        Abrir.Filtro2 filtro = new Abrir.Filtro2();
        assertTrue(filtro.accept(new File("test.jaw.xml")));
        assertFalse(filtro.accept(new File("test.jaw")));
        assertFalse(filtro.accept(new File("test.xml")));
    }
    
    @Test
    public void testFiltro2GetDescription() {
        Abrir.Filtro2 filtro = new Abrir.Filtro2();
        assertEquals("XML JAW files", filtro.getDescription());
    }
    
    @Test
    public void testFiltroAccept() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        assertTrue(filtro.accept(new File("test.jaw")));
        assertFalse(filtro.accept(new File("test.jaw.xml")));
        assertFalse(filtro.accept(new File("test.xml")));
    }
    
    @Test
    public void testFiltroGetDescription() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }
    
    @Test
    public void testConstructorArquivoEndsWithXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw.xml");
        assertNotNull(abrir.form);
        assertNotNull(abrir.xml);
    }
    
    @Test
    public void testConstructorArquivoEndsWithJAW() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw");
        assertNotNull(abrir.form);
        assertNotNull(abrir.xml);
    }
    
    @Test
    public void testConstructorTipoAbrir10() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
        assertNotNull(abrir.form);
    }
    
    @Test
    public void testConstructorTipoAbrir20() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, Abrir.ABRIR);
        assertNotNull(abrir.form);
    }
}