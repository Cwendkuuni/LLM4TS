import org.junit.Assert;
import org.junit.Test;
import java.util.Vector;
import java.util.List;
import java.io.File;

public class SalvarTest {

    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        Assert.assertNotNull(salvar);
    }

    @Test
    public void testFiltro2_accept() {
        File file = new File("test.jaw.xml");
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();
        boolean result = filtro2.accept(file);
        Assert.assertTrue(result);
    }

    @Test
    public void testFiltro2_getDescription() {
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();
        String description = filtro2.getDescription();
        Assert.assertEquals("XML JAW files", description);
    }

    @Test
    public void testFiltro_accept() {
        File file = new File("test.jaw");
        Salvar.Filtro filtro = new Salvar.Filtro();
        boolean result = filtro.accept(file);
        Assert.assertTrue(result);
    }

    @Test
    public void testFiltro_getDescription() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        String description = filtro.getDescription();
        Assert.assertEquals("JAW files", description);
    }
}