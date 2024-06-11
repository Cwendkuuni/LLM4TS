package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

    private ProcessarEntidades form;

    @Before
    public void setUp() {
        form = new ProcessarEntidades();
    }

    @Test
    public void testConstructorWithArquivo() {
        String arquivo = "example.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }

    @Test
    public void testConstructorWithTypeAbrir10() {
        Abrir abrir = new Abrir(form, 10);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }

    @Test
    public void testConstructorWithTypeAbrir20() {
        Abrir abrir = new Abrir(form, 20);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }

    @Test
    public void testAbrirSerializadoWithValidJawFile() {
        Abrir abrir = new Abrir(form, 20);
        abrir.abrirSerializado("valid.jaw");
        // add assertions here
    }

    @Test
    public void testAbrirSerializadoWithInvalidJawFile() {
        Abrir abrir = new Abrir(form, 20);
        abrir.abrirSerializado("invalid.jaw");
        // add assertions here
    }

    @Test
    public void testAbrirXMLWithValidXmlFile() {
        Abrir abrir = new Abrir(form, 10);
        abrir.abrirXML("valid.xml");
        // add assertions here
    }

    @Test
    public void testAbrirXMLWithInvalidXmlFile() {
        Abrir abrir = new Abrir(form, 10);
        abrir.abrirXML("invalid.xml");
        // add assertions here
    }

    @Test
    public void testFiltro2AcceptWithXmlFile() {
        Abrir.Filtro2 filtro2 = new Abrir.Filtro2();
        File file = new File("example.xml");
        assertTrue(filtro2.accept(file));
    }

    @Test
    public void testFiltro2AcceptWithNonXmlFile() {
        Abrir.Filtro2 filtro2 = new Abrir.Filtro2();
        File file = new File("example.txt");
        assertFalse(filtro2.accept(file));
    }

    @Test
    public void testFiltro2GetDescription() {
        Abrir.Filtro2 filtro2 = new Abrir.Filtro2();
        assertEquals("XML JAW files", filtro2.getDescription());
    }

    @Test
    public void testFiltroAcceptWithJawFile() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        File file = new File("example.jaw");
        assertTrue(filtro.accept(file));
    }

    @Test
    public void testFiltroAcceptWithNonJawFile() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        File file = new File("example.txt");
        assertFalse(filtro.accept(file));
    }

    @Test
    public void testFiltroGetDescription() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }
}