import org.junit.Test;
import static org.junit.Assert.*;
import jaw.entrada.Salvar;

public class SalvarTest {

    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotNull(salvar.entidade);
    }
    
    @Test
    public void testFiltro2Accept() {
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();
        
        File f1 = new File("testFile.jaw.xml");
        File f2 = new File("testFile.txt");
        
        assertTrue(filtro2.accept(f1));
        assertFalse(filtro2.accept(f2));
    }
    
    @Test
    public void testFiltro2GetDescription() {
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();
        
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltroAccept() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        
        File f1 = new File("testFile.jaw");
        File f2 = new File("testFile.txt");
        
        assertTrue(filtro.accept(f1));
        assertFalse(filtro.accept(f2));
    }
    
    @Test
    public void testFiltroGetDescription() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        
        assertEquals("JAW files", filtro.getDescription());
    }
}