
import org.junit.Assert;
import org.junit.Test;

public class SalvarTest {
    
    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR);
        Assert.assertEquals("Novo Projeto.jaw", salvar.arquivo);
    }
    
    @Test
    public void testSalvar_XML() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        Assert.assertEquals("Novo Projeto.jaw.xml", salvar.arquivo);
    }
    
    @Test
    public void testSalvar_Como() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR_COMO);
        Assert.assertEquals("Novo Projeto.jaw", salvar.arquivo);
    }
    
    @Test
    public void testSalvar_Exception() {
        ProcessarEntidades form = new ProcessarEntidades();
        // Set up a scenario where form.getEntidades() throws an exception
        Salvar salvar = new Salvar(form, Salvar.SALVAR);
        // Verify that exception message is logged
        // TODO: Assert on the logged exception message
    }
    
    @Test
    public void testFiltro2_Accept() {
        Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
        File file = new File("file.jaw.xml");
        Assert.assertTrue(filtro2.accept(file));
    }
    
    @Test
    public void testFiltro2_Accept_Directory() {
        Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
        File directory = new File("directory");
        Assert.assertTrue(filtro2.accept(directory));
    }
    
    @Test
    public void testFiltro2_Accept_IncorrectExtension() {
        Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
        File file = new File("file.png");
        Assert.assertFalse(filtro2.accept(file));
    }
    
    @Test
    public void testFiltro2_GetDescription() {
        Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
        Assert.assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltro_Accept() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        File file = new File("file.jaw");
        Assert.assertTrue(filtro.accept(file));
    }
    
    @Test
    public void testFiltro_Accept_Directory() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        File directory = new File("directory");
        Assert.assertTrue(filtro.accept(directory));
    }
    
    @Test
    public void testFiltro_Accept_IncorrectExtension() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        File file = new File("file.csv");
        Assert.assertFalse(filtro.accept(file));
    }
    
    @Test
    public void testFiltro_GetDescription() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        Assert.assertEquals("JAW files", filtro.getDescription());
    }
}
