
package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

    private ProcessarEntidades form;

    @Before
    public void setup() {
        form = new ProcessarEntidades();
    }

    @Test
    public void testConstructorWithArquivoEndingWithJawXML() {
        // setup
        String arquivo = "example.jaw.xml";

        // execute
        Abrir abrir = new Abrir(form, arquivo);

        // assert
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }

    @Test
    public void testConstructorWithArquivoNotEndingWithJawXML() {
        // setup
        String arquivo = "example.jaw";

        // execute
        Abrir abrir = new Abrir(form, arquivo);

        // assert
        assertNull(abrir.form);
    }

    @Test
    public void testConstructorWithTipoAbrirEqualsABRIR_XML() {
        // setup
        int tipoAbrir = 10;

        // execute
        Abrir abrir = new Abrir(form, tipoAbrir);

        // assert
        assertNull(abrir.form);
    }

    @Test
    public void testConstructorWithTipoAbrirEqualsABRIR() {
        // setup
        int tipoAbrir = 20;

        // execute
        Abrir abrir = new Abrir(form, tipoAbrir);

        // assert
        assertNull(abrir.form);
    }

    @Test
    public void testAbrirSerializado() {
        // setup
        String arquivo = "example.jaw";
        Abrir abrir = new Abrir(form, arquivo);

        // execute
        abrir.abrirSerializado(arquivo);

        // assert
        assertEquals(form.getNomeArquivoSalvo(), arquivo);
    }

    @Test
    public void testAbrirSerializadoNotJawFile() {
        // setup
        String arquivo = "example.jaw2";
        Abrir abrir = new Abrir(form, arquivo);

        // execute
        abrir.abrirSerializado(arquivo);

        // assert
        assertNull(form.getNomeArquivoSalvo());
    }

    @Test
    public void testAbrirXML() {
        // setup
        String arquivo = "example.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);

        // execute
        abrir.abrirXML(arquivo);

        // assert
        assertEquals(form.getNomeArquivoSalvo(), arquivo);
    }

    @Test
    public void testAbrirXMLNotXMLFile() {
        // setup
        String arquivo = "example.xml2";
        Abrir abrir = new Abrir(form, arquivo);

        // execute
        abrir.abrirXML(arquivo);

        // assert
        assertNull(form.getNomeArquivoSalvo());
    }

    @Test
    public void testAcceptInFiltro2WithJawFile() {
        // setup
        File file = new File("example.jaw");

        // execute
        boolean result = new Abrir.Filtro2().accept(file);

        // assert
        assertFalse(result);
    }

    @Test
    public void testAcceptInFiltro2WithJawXMLFile() {
        // setup
        File file = new File("example.jaw.xml");

        // execute
        boolean result = new Abrir.Filtro2().accept(file);

        // assert
        assertTrue(result);
    }

    @Test
    public void testGetDescriptionInFiltro2() {
        // execute
        String description = new Abrir.Filtro2().getDescription();

        // assert
        assertEquals("XML JAW files", description);
    }

    @Test
    public void testAcceptInFiltroWithJawFile() {
        // setup
        File file = new File("example.jaw");

        // execute
        boolean result = new Abrir.Filtro().accept(file);

        // assert
        assertTrue(result);
    }

    @Test
    public void testAcceptInFiltroWithJawXMLFile() {
        // setup
        File file = new File("example.jaw.xml");

        // execute
        boolean result = new Abrir.Filtro().accept(file);

        // assert
        assertFalse(result);
    }

    @Test
    public void testGetDescriptionInFiltro() {
        // execute
        String description = new Abrir.Filtro().getDescription();

        // assert
        assertEquals("JAW files", description);
    }
}
