import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SalvarTest {
    
    private ProcessarEntidades form;
    private int tipoSalvar;
    private Salvar salvar;
    
    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        tipoSalvar = 0;
        salvar = new Salvar(form, tipoSalvar);
    }
    
    @Test
    public void testSalvar_Constructor() {
        assertNotNull(salvar);
    }
    
    @Test
    public void testSalvar_DefaultValues() {
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotNull(salvar.entidade);
        assertTrue(salvar.entidade.isEmpty());
    }
    
    @Test
    public void testSalvar_SALVAR_XML_Filter() {
        tipoSalvar = Salvar.SALVAR_XML;
        salvar = new Salvar(form, tipoSalvar);
        assertEquals("XML JAW files", salvar.escolhedor.getFileFilter().getDescription());
        assertEquals("jaw.xml", salvar.escolhedor.accept(new File("test.jaw.xml")));
    }
    
    @Test
    public void testSalvar_SALVAR_Filter() {
        tipoSalvar = Salvar.SALVAR;
        salvar = new Salvar(form, tipoSalvar);
        assertEquals("JAW files", salvar.escolhedor.getFileFilter().getDescription());
        assertEquals("jaw", salvar.escolhedor.accept(new File("test.jaw")));
    }
    
    @Test
    public void testSalvar_SALVAR_COMO_Filter() {
        tipoSalvar = Salvar.SALVAR_COMO;
        salvar = new Salvar(form, tipoSalvar);
        assertEquals("JAW files", salvar.escolhedor.getFileFilter().getDescription());
        assertEquals("jaw", salvar.escolhedor.accept(new File("test.jaw")));
    }
    
    @Test
    public void testSalvar_SelectFile_Null() {
        assertNull(salvar.escolhedor.getSelectedFile());
    }
    
    @Test
    public void testSalvar_SelectFile_NotNull() {
        tipoSalvar = Salvar.SALVAR_XML;
        salvar = new Salvar(form, tipoSalvar);
        assertNotNull(salvar.escolhedor.getSelectedFile());
    }
    
    @Test
    public void testSalvar_SelectFile_NullAndFormNameEmpty() {
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
    }
    
    @Test
    public void testSalvar_SelectFile_WithValidFile() {
        tipoSalvar = Salvar.SALVAR_COMO;
        salvar = new Salvar(form, tipoSalvar);
        assertEquals("test.jaw", salvar.arquivo);
    }
    
    @Test
    public void testSalvar_SetNomeArquivoSalvo() {
        tipoSalvar = Salvar.SALVAR;
        salvar = new Salvar(form, tipoSalvar);
        salvar.form.setNomeArquivoSalvo("test");
        salvar.vela = true;
        salvar.arquivo = null;
        salvar.vela = true;
        
        salvar.arquivo = salvar.form.getNomeArquivoSalvo();
        assertEquals("test.jaw", salvar.arquivo);
        assertFalse(salvar.vela);
    }
    
    @Test
    public void testSalvar_SetNomeArquivoSalvo_WithEmptyFormName() {
        tipoSalvar = Salvar.SALVAR;
        salvar = new Salvar(form, tipoSalvar);
        salvar.vela = true;
        salvar.arquivo = null;
        salvar.vela = true;
        
        assertEquals("", salvar.arquivo);
        assertFalse(salvar.vela);
    }
    
    @Test
    public void testSalvar_SetNomeArquivoSalvo_WithValidFile() {
        tipoSalvar = Salvar.SALVAR;
        salvar = new Salvar(form, tipoSalvar);
        salvar.form.setNomeArquivoSalvo("test");
        salvar.vela = true;
        salvar.arquivo = "test";
        
        assertEquals("test.jaw", salvar.arquivo);
        assertFalse(salvar.vela);
    }
    
    @Test
    public void testSalvar_SetNomeArquivoSalvo_WithValidFile_WithoutExtension() {
        tipoSalvar = Salvar.SALVAR;
        salvar = new Salvar(form, tipoSalvar);
        salvar.form.setNomeArquivoSalvo("test");
        salvar.vela = true;
        salvar.arquivo = "test.jaw";
        
        assertEquals("test.jaw", salvar.arquivo);
        assertFalse(salvar.vela);
    }
    
    @Test
    public void testSalvar_SetNomeArquivoSalvo_WithValidFile_WithExtension() {
        tipoSalvar = Salvar.SALVAR;
        salvar = new Salvar(form, tipoSalvar);
        salvar.form.setNomeArquivoSalvo("test.jaw");
        salvar.vela = true;
        salvar.arquivo = "test.jaw";
        
        assertEquals("test.jaw", salvar.arquivo);
        assertFalse(salvar.vela);
    }
    
    @Test
    public void testSalvar_SALVAR_AND_ComEntidades() {
        tipoSalvar = Salvar.SALVAR_COMO;
        salvar = new Salvar(form, tipoSalvar);
        salvar.arquivo = "test.jaw";
        
        String oldArquivo = salvar.arquivo;
        salvar.entidade.add(new Entidade());
        assertEquals(oldArquivo, salvar.arquivo);
    }
    
    @Test
    public void testSalvar_SALVAR_AND_SemComEntidades() {
        tipoSalvar = Salvar.SALVAR;
        salvar = new Salvar(form, tipoSalvar);
        salvar.arquivo = "test.jaw";
        
        String oldArquivo = salvar.arquivo;
        salvar.entidade.add(new Entidade());
        assertEquals(oldArquivo, salvar.arquivo);
    }
    
    @Test
    public void testSalvar_SALVAR_COMO_WithInvalidFileFormat_SerializationException() {
        tipoSalvar = Salvar.SALVAR_COMO;
        salvar = new Salvar(form, tipoSalvar);
        salvar.arquivo = "test.invalid";
        salvar.vela = true;
        
        assertFalse(salvar.vela);
    }
    
    @Test
    public void testSalvar_SALVAR_ComValidFileFormat_SerializationException() {
        tipoSalvar = Salvar.SALVAR;
        salvar = new Salvar(form, tipoSalvar);
        salvar.arquivo = "test.jaw";
        salvar.vela = true;
        
        assertFalse(salvar.vela);
    }
    
    @Test
    public void testSalvar_SALVAR_WithValidFileFormat_FileSavedSuccessfully() {
        tipoSalvar = Salvar.SALVAR;
        salvar = new Salvar(form, tipoSalvar);
        salvar.arquivo = "test.jaw";
        salvar.vela = true;
        
        assertEquals("Arquivo salvo com sucesso!", JOptionPane.getMessage());
    }
    
    @Test
    public void testSalvar_SALVAR_XML_WithValidFileFormat_FileExportedSuccessfully() {
        tipoSalvar = Salvar.SALVAR_XML;
        salvar = new Salvar(form, tipoSalvar);
        salvar.arquivo = "test.jaw.xml";
        
        assertEquals("Arquivo Exportado com sucesso!", JOptionPane.getMessage());
    }
    
    @Test
    public void testSalvar_SALVAR_XML_WithInvalidFileFormat_SerializationException() {
        tipoSalvar = Salvar.SALVAR_XML;
        salvar = new Salvar(form, tipoSalvar);
        salvar.arquivo = "test.invalid.xml";
        
        assertFalse(salvar.vela);
    }

}