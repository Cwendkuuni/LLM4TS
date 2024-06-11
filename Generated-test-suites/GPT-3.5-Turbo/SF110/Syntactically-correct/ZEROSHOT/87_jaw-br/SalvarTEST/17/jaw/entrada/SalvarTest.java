import static org.junit.Assert.*;
import org.junit.*;

public class SalvarTest {
    
    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Assertions
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotEquals(null, salvar.entidade);
        assertEquals(0, salvar.entidade.size());
        assertEquals(20, Salvar.SALVAR);
        
    }
    
    @Test
    public void testSalvar_XML() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Assertions
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertNotEquals("", salvar.xml);
        assertNotEquals(null, salvar.entidade);
        assertEquals(0, salvar.entidade.size());
        assertEquals(10, Salvar.SALVAR_XML);
        
    }
    
    @Test
    public void testSalvar_COMO() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 30;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Assertions
        assertTrue(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotEquals(null, salvar.entidade);
        assertEquals(0, salvar.entidade.size());
        assertEquals(30, Salvar.SALVAR_COMO);
        
    }
    
    @Test
    public void testFiltro2() {
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();
        
        // Assertions
        assertTrue(filtro2.accept(new File("test.jaw.xml")));
        assertFalse(filtro2.accept(new File("test.txt")));
        assertEquals("XML JAW files", filtro2.getDescription());
        
    }
    
    @Test
    public void testFiltro() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        
        // Assertions
        assertTrue(filtro.accept(new File("test.jaw")));
        assertFalse(filtro.accept(new File("test.txt")));
        assertEquals("JAW files", filtro.getDescription());
        
    }
    
}