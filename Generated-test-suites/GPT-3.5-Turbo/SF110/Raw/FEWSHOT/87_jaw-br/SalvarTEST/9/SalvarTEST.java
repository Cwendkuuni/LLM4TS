package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SalvarTest {
    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(new Vector<Entidade>(), salvar.entidade);
        assertNull(salvar.entidade);
    }
    
    @Test
    public void testFiltro2() {
        FileFilter filtro2 = new Filtro2();
        
        String fileName1 = "file.jaw.xml";
        assertEquals(true, filtro2.accept(new File(fileName1)));
        
        String fileName2 = "file.xml";
        assertEquals(true, filtro2.accept(new File(fileName2)));
        
        String fileName3 = "file.jaw";
        assertEquals(false, filtro2.accept(new File(fileName3)));
        
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltro() {
        FileFilter filtro = new Filtro();
        
        String fileName1 = "file.jaw";
        assertEquals(true, filtro.accept(new File(fileName1)));
        
        String fileName2 = "file.xml";
        assertEquals(false, filtro.accept(new File(fileName2)));
        
        assertEquals("JAW files", filtro.getDescription());
    }
    
}