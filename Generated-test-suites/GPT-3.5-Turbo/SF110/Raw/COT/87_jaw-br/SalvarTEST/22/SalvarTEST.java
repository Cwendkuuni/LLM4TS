
import org.junit.Test;

public class SalvarTest {

    @Test
    public void testSalvarXML() {
        // Create a ProcessarEntidades object
        ProcessarEntidades form = new ProcessarEntidades();

        // Create a Salvar object with tipoSalvar = 10
        Salvar salvar = new Salvar(form, 10);
        // Assert that the xml field is not empty
        assertNotEquals("", salvar.xml);
        // Assert that the entidade list is not empty
        assertFalse(salvar.entidade.isEmpty());
    }

    @Test
    public void testSalvar() {
        // Create a ProcessarEntidades object
        ProcessarEntidades form = new ProcessarEntidades();

        // Create a Salvar object with tipoSalvar = 20
        Salvar salvar = new Salvar(form, 20);
        // Assert that the entidade list is not empty
        assertFalse(salvar.entidade.isEmpty());
        // Assert that the arquivo field ends with ".jaw"
        assertTrue(salvar.arquivo.endsWith(".jaw"));
    }

    @Test
    public void testSalvarComo() {
        // Create a ProcessarEntidades object
        ProcessarEntidades form = new ProcessarEntidades();

        // Create a Salvar object with tipoSalvar = 30
        Salvar salvar = new Salvar(form, 30);
        // Assert that the entidade list is not empty
        assertFalse(salvar.entidade.isEmpty());
        // Assert that the arquivo field ends with ".jaw"
        assertTrue(salvar.arquivo.endsWith(".jaw"));
    }
}
