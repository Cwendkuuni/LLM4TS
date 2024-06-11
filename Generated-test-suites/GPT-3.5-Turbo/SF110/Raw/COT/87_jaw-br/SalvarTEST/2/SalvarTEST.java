
import static org.junit.Assert.*;
import org.junit.Test;

public class SalvarTest {

    @Test
    public void testSalvar_XML() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = Salvar.SALVAR_XML;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // test code here
    }
    
    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = Salvar.SALVAR;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // test code here
    }
    
    @Test
    public void testSalvar_Como() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = Salvar.SALVAR_COMO;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // test code here
    }
    
}
