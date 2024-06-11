
package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SalvarTest {
    @Test
    public void testConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 10);
        
        // Assert statements
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(new Vector<Entidade>(), salvar.entidade);
        
        // Cleanup
        form = null;
        salvar = null;
    }
    
    @Test
    public void testFiltro2_accept() {
        Salvar.Filtro2 filtro = new Salvar.Filtro2();
        File file = new File("test.jaw.xml");
        
        // Assert statement
        assertEquals(true, filtro.accept(file));
        
        // Cleanup
        filtro = null;
        file = null;
    }
    
    @Test
    public void testFiltro2_getDescription() {
        Salvar.Filtro2 filtro = new Salvar.Filtro2();
        
        // Assert statement
        assertEquals("XML JAW files", filtro.getDescription());
        
        // Cleanup
        filtro = null;
    }
    
    @Test
    public void testFiltro_accept() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        File file = new File("test.jaw");
        
        // Assert statement
        assertEquals(true, filtro.accept(file));
        
        // Cleanup
        filtro = null;
        file = null;
    }
    
    @Test
    public void testFiltro_getDescription() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        
        // Assert statement
        assertEquals("JAW files", filtro.getDescription());
        
        // Cleanup
        filtro = null;
    }
}
