import static org.junit.Assert.*;
import org.junit.Test;

public class SalvarTest {

    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        
        assertNotNull(salvar);
        assertEquals(0, salvar.entidade.size());
        assertEquals("", salvar.xml);
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
    }

    @Test
    public void testSalvarInvalidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR);
        
        assertNotNull(salvar);
        assertEquals(0, salvar.entidade.size());
        assertEquals("", salvar.xml);
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
    }

    @Test
    public void testSalvarEdgeCase() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR_COMO);
        
        assertNotNull(salvar);
        assertEquals(0, salvar.entidade.size());
        assertEquals("", salvar.xml);
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
    }

}