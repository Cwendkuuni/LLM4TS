import org.junit.Test;
import static org.junit.Assert.*;

public class SalvarTest {

    @Test
    public void testSalvar_SALVAR_XML_typicalCase() {
        // Arrange
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        // Act
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Assert
        assertNotNull(salvar);
        assertEquals(".jaw.xml", salvar.arquivo.substring(salvar.arquivo.length() - 8));
    }
    
    @Test
    public void testSalvar_SALVAR_XML_emptyForm() {
        // Arrange
        ProcessarEntidades form = new ProcessarEntidades();
        form.setName("");
        int tipoSalvar = 10;
        
        // Act
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Assert
        assertNull(salvar.arquivo);
    }
    
    @Test
    public void testSalvar_SALVAR_typicalCase() {
        // Arrange
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Novo Projeto");
        int tipoSalvar = 20;
        
        // Act
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Assert
        assertNotNull(salvar);
        assertEquals(".jaw", salvar.arquivo.substring(salvar.arquivo.length() - 4));
    }
    
    @Test
    public void testSalvar_SALVAR_existingFile() {
        // Arrange
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("existingFile");
        int tipoSalvar = 20;
        
        // Act
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Assert
        assertNotNull(salvar);
        assertEquals("existingFile", salvar.arquivo);
    }
    
    @Test
    public void testSalvar_SALVAR_noEntidadesSaved() {
        // Arrange
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        
        // Act
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Assert
        assertNotNull(salvar);
        assertEquals("Novo Projeto.jaw", salvar.arquivo);
    }
    
    @Test
    public void testSalvar_SALVAR_COMO_typicalCase() {
        // Arrange
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Novo Projeto");
        int tipoSalvar = 30;
        
        // Act
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Assert
        assertNotNull(salvar);
        assertEquals(".jaw", salvar.arquivo.substring(salvar.arquivo.length() - 4));
    }
    
    @Test
    public void testSalvar_SALVAR_COMO_existingFile() {
        // Arrange
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("existingFile");
        int tipoSalvar = 30;
        
        // Act
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Assert
        assertNotNull(salvar);
        assertEquals("existingFile", salvar.arquivo);
    }
    
    @Test
    public void testSalvar_SALVAR_COMO_noEntidadesSaved() {
        // Arrange
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 30;
        
        // Act
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Assert
        assertNotNull(salvar);
        assertEquals("Novo Projeto.jaw", salvar.arquivo);
    }
}