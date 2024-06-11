import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class SalvarTest {

    @Test
    public void testSalvarXMLFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("TestFile");
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        
        assertEquals("TestFile.jaw.xml", salvar.arquivo);
        assertNotNull(salvar.xml);
        assertFalse(salvar.entidade.isEmpty());
    }

    @Test
    public void testSalvarJAWFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("TestFile");
        Salvar salvar = new Salvar(form, Salvar.SALVAR);
        
        assertEquals("TestFile.jaw", salvar.arquivo);
        assertNotNull(salvar.entidade);
    }

    @Test
    public void testSalvarComError() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo(null);
        Salvar salvar = new Salvar(form, 0);
        
        assertTrue(salvar.vela);
    }

    @Test(expected = IOException.class)
    public void testSalvarWithIOException() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("TestFile");
        Salvar salvar = new Salvar(form, 30);
        
        // Trying to save a file without the required extension should throw an IOException
    }

}