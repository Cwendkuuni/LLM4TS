import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.List;

public class SalvarTest {
    
    // Test case for constructor
    @Test
    public void testConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotNull(salvar.entidade);
        assertEquals(0, salvar.entidade.size());
    }
    
    // Test case for tipoSalvar = 10
    @Test
    public void testSalvarXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        
        // The test case should not throw any exception and should export the file successfully
        assertTrue(new File(salvar.arquivo).exists());
    }
    
    // Test case for tipoSalvar = 20 or tipoSalvar = 30
    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        
        // The test case should not throw any exception and should save the file successfully
        assertTrue(new File(salvar.arquivo).exists());
    }
    
    // Test case for tipoSalvar = 10 and arquivo does not end with ".jaw.xml"
    @Test
    public void testSalvarXMLWrongExtension() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        
        // The arquivo should be updated to end with ".jaw.xml"
        assertTrue(salvar.arquivo.endsWith(".jaw.xml"));
    }
    
    // Test case for tipoSalvar = 20 and arquivo does not end with ".jaw"
    @Test
    public void testSalvarWrongExtension() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        
        // The arquivo should be updated to end with ".jaw"
        assertTrue(salvar.arquivo.endsWith(".jaw"));
    }
    
    // Test case for tipoSalvar = 20 and form.getNomeArquivoSalvo() equals "Novo Projeto"
    @Test
    public void testSalvarNovoProjeto() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Novo Projeto");
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        
        // The arquivo should be updated to end with ".jaw" as form.getNomeArquivoSalvo() is "Novo Projeto"
        assertTrue(salvar.arquivo.endsWith(".jaw"));
    }
    
    // Test case for tipoSalvar = 10 and escolhedor.getSelectedFile() is null and form.getName() is ""
    @Test
    public void testSalvarXMLNoFileSelectedAndNoName() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        // Set escolhedor.getSelectedFile() to null and form.getName() to empty string
        form.setEscolhedorSelectedFile(null);
        form.setName("");
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        
        // Since escolhedor.getSelectedFile() is null and form.getName() is "", no arquivo should be saved
        assertEquals("", salvar.arquivo);
    }
    
    // Test case for tipoSalvar = 20 and escolhedor.getSelectedFile() is null and form.getName() is ""
    @Test
    public void testSalvarNoFileSelectedAndNoName() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        
        // Set escolhedor.getSelectedFile() to null and form.getName() to empty string
        form.setEscolhedorSelectedFile(null);
        form.setName("");
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        
        // Since escolhedor.getSelectedFile() is null and form.getName() is "", no arquivo should be saved
        assertEquals("", salvar.arquivo);
    }
    
    // Test case for tipoSalvar = 20 and !this.arquivo.endsWith(".jaw") and this.vela is true
    @Test
    public void testSalvarWrongExtensionWithVela() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        
        // Since !this.arquivo.endsWith(".jaw") and this.vela is true, form.getNomeArquivoSalvo() should be updated to end with ".jaw" and this.vela should be false
        assertTrue(form.getNomeArquivoSalvo().endsWith(".jaw"));
        assertEquals(false, salvar.vela);
    }
    
    // Test case for tipoSalvar = 20 and !this.arquivo.endsWith(".jaw") and this.vela is false
    @Test
    public void testSalvarWrongExtensionWithoutVela() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("test.jaw");
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        
        // Since !this.arquivo.endsWith(".jaw") and this.vela is false, this.arquivo should remain unchanged and this.vela should remain false
        assertEquals("test.jaw", salvar.arquivo);
        assertEquals(false, salvar.vela);
    }
    
    // Test case for tipoSalvar = 20 and saving file throws an exception
    @Test
    public void testSalvarException() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        
        // Set an empty list of entities to save
        form.setEntidades(new Vector<Entidade>());
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        
        // The file saving operation should throw an exception
        assertFalse(new File(salvar.arquivo).exists());
    }
    
    // Test case for tipoSalvar = 10 and arquivo does not end with ".jaw.xml"
    // Also, test case for saving XML file throws an exception
    @Test
    public void testSalvarXMLWrongExtensionWithException() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        // Set an empty list of entities to save
        form.setEntidades(new Vector<Entidade>());
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        
        // The arquivo should be updated to end with ".jaw.xml"
        assertTrue(salvar.arquivo.endsWith(".jaw.xml"));
        // The XML file saving operation should throw an exception
        assertFalse(new File(salvar.arquivo).exists());
    }
    
    // Test case for tipoSalvar = 10 and arquivo does not end with ".jaw.xml"
    // Also, test case for typeSalvar = 10, escolhedor.getSelectedFile() is null and form.getName() is ""
    // Also, test case for saving XML file throws an exception
    @Test
    public void testSalvarXMLWrongExtensionNoFileSelectedAndNoNameWithException() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        // Set escolhedor.getSelectedFile() to null and form.getName() to empty string
        form.setEscolhedorSelectedFile(null);
        form.setName("");
        
        // Set an empty list of entities to save
        form.setEntidades(new Vector<Entidade>());
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        
        // The arquivo should be updated to end with ".jaw.xml"
        assertTrue(salvar.arquivo.endsWith(".jaw.xml"));
        // Since escolhedor.getSelectedFile() is null and form.getName() is "", no arquivo should be saved
        assertEquals("", salvar.arquivo);
    }
    
    // Test case for tipoSalvar = 20 and arquivo ends with ".jaw"
    @Test
    public void testSalvarWithExtension() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("test.jaw");
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        
        // The arquivo should remain unchanged
        assertEquals("test.jaw", salvar.arquivo);
    }
    
    // Test case for tipoSalvar = 10 and arquivo ends with ".jaw.xml"
    @Test
    public void testSalvarXMLWithExtension() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("test.jaw.xml");
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        
        // The arquivo should remain unchanged
        assertEquals("test.jaw.xml", salvar.arquivo);
    }
}