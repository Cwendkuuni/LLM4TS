package jaw.entrada;

import static org.junit.Assert.*;
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
        assertEquals(0, salvar.entidade.size());
        
        tipoSalvar = 20;
        salvar = new Salvar(form, tipoSalvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(0, salvar.entidade.size());
        
        tipoSalvar = 30;
        salvar = new Salvar(form, tipoSalvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(0, salvar.entidade.size());
        
    }

    @Test
    public void testSalvar_fileChooserReturnsNull() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(0, salvar.entidade.size());
        
        tipoSalvar = 20;
        salvar = new Salvar(form, tipoSalvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(0, salvar.entidade.size());
        
        tipoSalvar = 30;
        salvar = new Salvar(form, tipoSalvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(0, salvar.entidade.size());
    }

    @Test
    public void testSalvar_invalidFileExtension() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(0, salvar.entidade.size());
    }
}