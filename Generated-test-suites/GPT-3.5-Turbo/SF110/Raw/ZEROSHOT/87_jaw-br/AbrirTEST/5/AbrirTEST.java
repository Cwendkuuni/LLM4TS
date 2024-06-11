package jaw.entrada;

import static org.junit.Assert.*;

import jaw.gui.ProcessarEntidades;
import org.junit.Before;
import org.junit.Test;

public class AbrirTest {
    
    private Abrir abrir;
    private ProcessarEntidades form;
    private String arquivo;
    
    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        arquivo = "test_file.jaw.xml";
    }

    @Test
    public void testAbrirConstructor1() {
        abrir = new Abrir(form, arquivo);
        assertEquals("", abrir.xml);
        assertNotNull(abrir.form);
        // Add more assertions for the constructor
    }
    
    @Test
    public void testAbrirConstructor2() {
        int tipoAbrir = 10;
        abrir = new Abrir(form, tipoAbrir);
        assertEquals("", abrir.xml);
        assertNotNull(abrir.form);
        // Add more assertions for the constructor
    }
    
    @Test
    public void testAbrirSerializado() {
        abrir = new Abrir(form, arquivo);
        abrir.abrirSerializado(arquivo);
        // Add assertions to verify the behavior of abrirSerializado()
    }
    
    @Test
    public void testAbrirXML() {
        abrir = new Abrir(form, arquivo);
        abrir.abrirXML(arquivo);
        // Add assertions to verify the behavior of abrirXML()
    }
    
    @Test
    public void testFiltro2Accept() {
        Abrir.Filtro2 filtro2 = new Abrir.Filtro2();
        File file = new File("test_file.jaw.xml");
        assertTrue(filtro2.accept(file));
        file = new File("test_file.txt");
        assertFalse(filtro2.accept(file));
    }
    
    @Test
    public void testFiltro2GetDescription() {
        Abrir.Filtro2 filtro2 = new Abrir.Filtro2();
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltroAccept() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        File file = new File("test_file.jaw");
        assertTrue(filtro.accept(file));
        file = new File("test_file.txt");
        assertFalse(filtro.accept(file));
    }
    
    @Test
    public void testFiltroGetDescription() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }

    
}