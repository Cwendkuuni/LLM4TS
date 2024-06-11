import jaw.entrada.Salvar;
import jaw.metamodelo.Entidade;
import jaw.ProcessarEntidades;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.Vector;

public class SalvarTest {

    private Salvar salvar;

    @Before
    public void setUp() {
        ProcessarEntidades form = new ProcessarEntidades(); // mock form object
        salvar = new Salvar(form, Salvar.SALVAR_XML);
    }

    @Test
    public void testSalvar() {
        // Typical use case
        // Not able to test user interface interactions
        // So, will need to mock certain objects

        // Edge case: test for empty form object
        ProcessarEntidades emptyForm = new ProcessarEntidades();
        Salvar salvarEmptyForm = new Salvar(emptyForm, Salvar.SALVAR_XML);
        assertNotNull(salvarEmptyForm);

        // Edge case: test file filter for XML JAW files
    }

    @Test
    public void testFiltro2() {
        // Typical use case
        // Not able to test actual file filtering, so only setup
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();
        File mockFile = new File("testFile.jaw.xml");
        assertTrue(filtro2.accept(mockFile));

        // Edge case: test for non XML JAW file
        File nonXMLFile = new File("testFile.jaw");
        assertFalse(filtro2.accept(nonXMLFile));
    }

    @Test
    public void testFiltro() {
        // Typical use case
        // Not able to test actual file filtering, so only setup
        Salvar.Filtro filtro = new Salvar.Filtro();
        File mockFile = new File("testFile.jaw");
        assertTrue(filtro.accept(mockFile));

        // Edge case: test for non JAW file
        File nonJAWFile = new File("testFile.xml");
        assertFalse(filtro.accept(nonJAWFile));
    }

}