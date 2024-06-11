import org.junit.Test;
import static org.junit.Assert.*;

public class SalvarTest {

    // Test constructors
    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertNotNull(salvar);
    }

    // Test the inner class Filtro2
    @Test
    public void testFiltro2() {
        Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
        File file1 = new File("testfile.jaw.xml");
        File file2 = new File("testfile.xml");
        assertFalse(filtro2.accept(file1));
        assertTrue(filtro2.accept(file2));
        assertEquals("XML JAW files", filtro2.getDescription());
    }

    // Test the inner class Filtro
    @Test
    public void testFiltro() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        File file1 = new File("testfile.jaw");
        File file2 = new File("testfile.xml");
        assertFalse(filtro.accept(file2));
        assertTrue(filtro.accept(file1));
        assertEquals("JAW files", filtro.getDescription());
    }
}