import org.junit.Test;
import static org.junit.Assert.*;

public class SalvarTest {
    
    @Test
    public void testConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
    }
}