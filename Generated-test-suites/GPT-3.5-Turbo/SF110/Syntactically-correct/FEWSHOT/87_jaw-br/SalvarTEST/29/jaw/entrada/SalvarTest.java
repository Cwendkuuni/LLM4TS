
package jaw.entrada;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import jaw.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import java.util.Vector;

public class SalvarTest {
    
    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 0;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        //Add more assertions
    }
    
    @Test
    public void testSalvarMethod() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Novo Projeto");
        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        //Add assertions
    }
    
    //Add more test methods for each method in the class
    
}
