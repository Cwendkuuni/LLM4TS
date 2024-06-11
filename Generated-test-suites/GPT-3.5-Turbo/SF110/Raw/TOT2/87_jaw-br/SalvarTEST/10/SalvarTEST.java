import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import jaw.entrada.Salvar;

public class SalvarTest {

    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertNotNull(salvar);
    }

    @Test
    public void testSalvarConstructorWithException() {
        ProcessarEntidades form = new ProcessarEntidades();
        assertThrows(Exception.class, () -> new Salvar(form, 100));
    }

    @Test
    public void testSalvarExportedFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR);
        assertTrue(salvar.arquivo.endsWith(".jaw"));
    }

    @Test
    public void testSalvarExportedXmlFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertTrue(salvar.arquivo.endsWith(".jaw.xml"));
    }

    @Test
    public void testFiltro2Accept() {
        Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
        assertTrue(filtro2.accept(new File("test.jaw.xml")));
        assertFalse(filtro2.accept(new File("test.pdf")));
    }

    @Test
    public void testFiltroDescription() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        assertEquals("XML JAW files", filtro.getDescription());
    }
}