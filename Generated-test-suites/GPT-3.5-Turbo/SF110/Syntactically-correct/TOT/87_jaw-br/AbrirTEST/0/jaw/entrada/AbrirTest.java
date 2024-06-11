import static org.junit.Assert.*;
import org.junit.Test;

public class AbrirTest {

    @Test
    public void testAbrir() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "testFile.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        assertNotNull(abrir.xml);
        assertNotNull(abrir.form);
        assertEquals(10, abrir.ABRIR_XML);
        assertEquals(20, abrir.ABRIR);
    }

    @Test
    public void testAbrirSerializado() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "testFile.jaw";
        Abrir abrir = new Abrir(form, arquivo);
        assertNotNull(abrir.form);
        assertNotNull(abrir.form.getNomeArquivoSalvo());
        assertNotNull(abrir.form.getEntidades());
    }

    @Test
    public void testAbrirSerializadoWithInvalidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "invalidFile.foo";
        Abrir abrir = new Abrir(form, arquivo);
        assertNotNull(abrir.form);
        assertNull(abrir.form.getNomeArquivoSalvo());
        assertNull(abrir.form.getEntidades());
    }

    @Test
    public void testAbrirXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "testFile.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        assertNotNull(abrir.form);
        assertNotNull(abrir.form.getNomeArquivoSalvo());
        assertNotNull(abrir.form.getEntidades());
    }

    @Test
    public void testAbrirXMLWithInvalidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "invalidFile.foo";
        Abrir abrir = new Abrir(form, arquivo);
        assertNotNull(abrir.form);
        assertNull(abrir.form.getNomeArquivoSalvo());
        assertNull(abrir.form.getEntidades());
    }
}