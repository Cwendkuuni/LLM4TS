import org.junit.Test;
import static org.junit.Assert.*;

public class SalvarTest {

    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertNotNull(salvar);
    }

    @Test
    public void testFiltro2Accept() {
        Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
        File file = new File("test.jaw.xml");
        assertTrue(filtro2.accept(file));
    }

    @Test
    public void testFiltro2GetDescription() {
        Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
        assertEquals("XML JAW files", filtro2.getDescription());
    }

    @Test
    public void testFiltroAccept() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        File file = new File("test.jaw");
        assertTrue(filtro.accept(file));
    }

    @Test
    public void testFiltroGetDescription() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }
}