import static org.junit.Assert.*;
import org.junit.Test;

public class SalvarTest {

    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 10);
        assertNotNull(salvar);
    }

    @Test
    public void testSalvarTipoSalvar10() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 10);
        assertEquals(".jaw.xml", salvar.arquivo.substring(salvar.arquivo.length() - 8));
    }

    @Test
    public void testSalvarTipoSalvar20() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 20);
        assertEquals(".jaw", salvar.arquivo.substring(salvar.arquivo.length() - 4));
    }

    @Test
    public void testSalvarTipoSalvar30() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 30);
        assertEquals(".jaw", salvar.arquivo.substring(salvar.arquivo.length() - 4));
    }

    @Test(expected = NullPointerException.class)
    public void testSalvarNullPointerException() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 30);
        salvar.entidade = null;
    }

    @Test
    public void testSalvarInvalidFileFormatException() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 20);
        salvar.arquivo = "test.txt";
        salvar.salvar(form, salvar.arquivo);
    }

}