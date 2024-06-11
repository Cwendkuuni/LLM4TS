import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jaw.metamodelo.Entidade;
import jaw.gui.ProcessarEntidades;
import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SalvarTest {

    private Salvar salvar;
    private ProcessarEntidades form;

    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        salvar = new Salvar(form, Salvar.SALVAR_XML);
    }

    @Test
    public void testSalvarXML() {
        assertNotNull(salvar);
        assertNotNull(salvar.form);
        assertEquals(Salvar.SALVAR_XML, salvar.tipoSalvar);
        assertNotNull(salvar.entidade);
        assertEquals(0, salvar.entidade.size());
        assertEquals("", salvar.xml);
    }

    @Test
    public void testFiltro2() {
        Salvar.Filtro2 filtro2 = salvar.new Filtro2();
        assertTrue(filtro2.accept(new File("test.jaw.xml")));
        assertFalse(filtro2.accept(new File("test.jaw")));
        assertFalse(filtro2.accept(new File("test.txt")));
        assertEquals("XML JAW files", filtro2.getDescription());
    }

    @Test
    public void testFiltro() {
        Salvar.Filtro filtro = salvar.new Filtro();
        assertTrue(filtro.accept(new File("test.jaw")));
        assertFalse(filtro.accept(new File("test.jaw.xml")));
        assertFalse(filtro.accept(new File("test.txt")));
        assertEquals("JAW files", filtro.getDescription());
    }

    @Test
    public void testSalvar() {
        form.setNomeArquivoSalvo("test.jaw");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        salvar = new Salvar(form, Salvar.SALVAR);
        assertNotNull(salvar);
        assertNotNull(salvar.form);
        assertEquals(Salvar.SALVAR, salvar.tipoSalvar);
        assertNotNull(salvar.entidade);
        assertEquals(0, salvar.entidade.size());
        assertEquals("test.jaw", salvar.arquivo);

        assertEquals("test.jaw", form.getNomeArquivoSalvo());
        assertEquals("test.jaw", salvar.arquivo);

        String expectedOutput = "test.jaw";
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }

    @Test
    public void testSalvarComo() {
        form.setNomeArquivoSalvo("New Project");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertNotNull(salvar);
        assertNotNull(salvar.form);
        assertEquals(Salvar.SALVAR_COMO, salvar.tipoSalvar);
        assertNotNull(salvar.entidade);
        assertEquals(0, salvar.entidade.size());
        assertEquals("New Project", salvar.arquivo);

        assertEquals("test.jaw", form.getNomeArquivoSalvo());
        assertEquals("test.jaw", salvar.arquivo);

        String expectedOutput = "test.jaw";
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }
}