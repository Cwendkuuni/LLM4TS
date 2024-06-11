import static org.junit.Assert.*;
import org.junit.Test;

public class SalvarTest {
    
    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(20, salvar.SALVAR);
    }
    
    @Test
    public void testSalvarConstructorWithException() {
        ProcessarEntidades form = null;
        int tipoSalvar = 30;
        
        try {
            Salvar salvar = new Salvar(form, tipoSalvar);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals("NullPointerException", e.getClass().getSimpleName());
        }
    }
    
    @Test
    public void testSalvarFileFilter2Accept() {
        Salvar.Filtro2 filtro = new Salvar().new Filtro2();
        
        assertTrue(filtro.accept(new File("test.jaw.xml")));
        assertFalse(filtro.accept(new File("test.jaw")));
    }
    
    @Test
    public void testSalvarFileFilterDescription() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        
        assertEquals("XML JAW files", filtro.getDescription());
    }
}