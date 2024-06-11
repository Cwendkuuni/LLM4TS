import static org.junit.Assert.*;
import org.junit.Test;

public class SalvarTest {

    @Test
    public void testSalvarConstructor_SALVAR_XML() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(0, salvar.entidade.size());
    }
    
    @Test
    public void testSalvarConstructor_SALVAR() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(0, salvar.entidade.size());
    }
    
    @Test
    public void testSalvarConstructor_SALVAR_COMO() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 30;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(0, salvar.entidade.size());
    }

    @Test
    public void testSalvarConstructor_Exception() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertEquals(exception.toString(), Main.janelaPrincipal.log());
    }

    // Add more test methods to test other functionalities of the class

}