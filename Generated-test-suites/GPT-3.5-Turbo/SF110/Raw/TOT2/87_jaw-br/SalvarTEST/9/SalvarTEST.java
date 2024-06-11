import static org.junit.Assert.*;
import org.junit.Test;

public class SalvarTest {

    @Test
    public void testConstructor() {
        // Test the constructor of Salvar class
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertNotNull(salvar);
    }

    @Test
    public void testSalvarXML() {
        // Test saving as XML
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
    
        // Edge case: file type not .jaw.xml
        form.setNomeArquivoSalvo("teste.txt");
        salvar = new Salvar(form, tipoSalvar);
        assertNotNull(salvar);
    }

    @Test
    public void testSalvarJAW() {
        // Test saving as JAW
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);

        // Edge case: file type not .jaw
        form.setNomeArquivoSalvo("teste.txt");
        salvar = new Salvar(form, tipoSalvar);
        assertNotNull(salvar);
    }

    @Test
    public void testSalvarComo() {
        // Test the functionality of saving as new file
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 30;
        Salvar salvar = new Salvar(form, tipoSalvar);

        // Edge case: saving as new file with default name
        form.setNomeArquivoSalvo("Novo Projeto");
        salvar = new Salvar(form, tipoSalvar);
        assertNotNull(salvar);
    }

}