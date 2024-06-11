import static org.junit.Assert.*;
import org.junit.Test;

public class SalvarTest {

    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = Salvar.SALVAR;
        Salvar salvar = new Salvar(form, tipoSalvar);
    }
    
    @Test
    public void testSalvarXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = Salvar.SALVAR_XML;
        Salvar salvar = new Salvar(form, tipoSalvar);
    }
    
    @Test
    public void testSalvarComo() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = Salvar.SALVAR_COMO;
        Salvar salvar = new Salvar(form, tipoSalvar);
    }

    @Test
    public void testFiltro2Accept() {
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();
        assertTrue(filtro2.accept(new File("test.jaw.xml")));
        assertFalse(filtro2.accept(new File("test.xml")));
        assertFalse(filtro2.accept(new File("test.jaw")));
    }
    
    @Test
    public void testFiltro2GetDescription() {
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltroAccept() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        assertTrue(filtro.accept(new File("test.jaw")));
        assertFalse(filtro.accept(new File("test.xml")));
        assertFalse(filtro.accept(new File("test.jaw.xml")));
    }
    
    @Test
    public void testFiltroGetDescription() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }

}