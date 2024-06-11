import org.junit.Test;
import java.util.Vector;
import java.util.List;
import java.io.File;

public class SalvarTest {

    @Test
    public void testSalvarXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        
        // TODO: Insert assertions here
    }

    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR);
        
        // TODO: Insert assertions here
    }

    @Test
    public void testSalvarComo() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR_COMO);
        
        // TODO: Insert assertions here
    }
}