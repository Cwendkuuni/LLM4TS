import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

    private ProcessarEntidades form;
    private Salvar salvar;

    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        salvar = new Salvar(form, Salvar.SALVAR_XML);
    }

    @Test
    public void testSalvarConstructor() {
        assertNotNull(salvar);
    }

    @Test
    public void testSalvarEntidades2XML() {
        assertNotNull(salvar.xml);
    }

    @Test
    public void testSalvarNomeArquivoSalvo() {
        assertNotNull(salvar.arquivo);
    }

    @Test
    public void testSalvarEstilos() {
        assertNotNull(salvar.entidade);
    }

    @Test
    public void testSalvarNomeArquivoNulo() {
        form.setNomeArquivoSalvo(null);
        Salvar novoSalvar = new Salvar(form, Salvar.SALVAR);
        assertEquals("", novoSalvar.arquivo);
    }

    @Test(expected = NullPointerException.class)
    public void testSalvarEntidadesNulas() {
        form.setEntidades(null);
        new Salvar(form, Salvar.SALVAR_XML);
    }

}