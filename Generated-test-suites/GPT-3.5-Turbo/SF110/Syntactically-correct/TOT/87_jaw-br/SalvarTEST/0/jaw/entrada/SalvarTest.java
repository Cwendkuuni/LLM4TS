import org.junit.*;
import static org.junit.Assert.*;

public class SalvarTest {
    
    @Test
    public void testSalvarConstructor_SalvarXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(form.getEntidades(), salvar.entidade);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
    }
    
    @Test
    public void testSalvarConstructor_Salvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(form.getEntidades(), salvar.entidade);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
    }
    
    @Test
    public void testSalvarConstructor_SalvarComo() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 30;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(form.getEntidades(), salvar.entidade);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
    }
    
    @Test
    public void testSalvarConstructor_Exception() {
        ProcessarEntidades form = null;
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNull(salvar);
    }
    
    @Test
    public void testSalvarConstructor_FileNotSelected() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(form.getEntidades(), salvar.entidade);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
    }
    
    @Test
    public void testSalvarConstructor_NewProject() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Novo Projeto");
        int tipoSalvar = 20;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(form.getEntidades(), salvar.entidade);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
    }
    
    @Test
    public void testSalvarConstructor_SaveWithValidFormat() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Projeto");
        int tipoSalvar = 20;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(form.getEntidades(), salvar.entidade);
        assertEquals("Projeto.jaw", salvar.arquivo);
        assertEquals("", salvar.xml);
    }
    
    @Test
    public void testSalvarConstructor_SaveWithInvalidFormat() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Projeto");
        int tipoSalvar = 20;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(form.getEntidades(), salvar.entidade);
        assertEquals("Projeto", salvar.arquivo);
        assertEquals("", salvar.xml);
    }
    
    @Test
    public void testSalvarConstructor_SaveWithValidFormat_InXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Projeto");
        int tipoSalvar = 30;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(form.getEntidades(), salvar.entidade);
        assertEquals("Projeto.jaw", salvar.arquivo);
        assertEquals("", salvar.xml);
    }
    
    @Test
    public void testSalvarConstructor_SaveWithInvalidFormat_InXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Projeto.xml");
        int tipoSalvar = 30;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(form.getEntidades(), salvar.entidade);
        assertEquals("Projeto.xml", salvar.arquivo);
        assertEquals("", salvar.xml);
    }
    
    @Test
    public void testSalvarConstructor_SaveXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(form.getEntidades(), salvar.entidade);
        assertEquals("", salvar.arquivo);
        assertNotNull(salvar.xml);
    }
    
    @Test
    public void testSalvarConstructor_SaveXML_Exception() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(form.getEntidades(), salvar.entidade);
        assertEquals("", salvar.arquivo);
        assertNotNull(salvar.xml);
    }
    
    @Test
    public void testSalvarConstructor_SaveXML_FileNotSelected() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setName("");
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertEquals(form.getEntidades(), salvar.entidade);
        assertEquals("", salvar.arquivo);
        assertNotNull(salvar.xml);
    }
    
    @Test
    public void testFiltro2_Accept_ValidFile() {
        Salvar.Filtro2 filtro = new Salvar.Filtro2();
        File file = new File("projeto.jaw.xml");
        
        boolean result = filtro.accept(file);
        
        assertTrue(result);
    }
    
    @Test
    public void testFiltro2_Accept_InvalidFile() {
        Salvar.Filtro2 filtro = new Salvar.Filtro2();
        File file = new File("projeto.xml");
        
        boolean result = filtro.accept(file);
        
        assertFalse(result);
    }
    
    @Test
    public void testFiltro2_Accept_IsDirectory() {
        Salvar.Filtro2 filtro = new Salvar.Filtro2();
        File file = new File("directory");
        file.isDirectory(true);
        
        boolean result = filtro.accept(file);
        
        assertTrue(result);
    }
    
    @Test
    public void testFiltro2_GetDescription() {
        Salvar.Filtro2 filtro = new Salvar.Filtro2();
        
        String description = filtro.getDescription();
        
        assertEquals("XML JAW files", description);
    }
    
    @Test
    public void testFiltro_Accept_ValidFile() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        File file = new File("projeto.jaw");
        
        boolean result = filtro.accept(file);
        
        assertTrue(result);
    }
    
    @Test
    public void testFiltro_Accept_InvalidFile() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        File file = new File("projeto.txt");
        
        boolean result = filtro.accept(file);
        
        assertFalse(result);
    }
    
    @Test
    public void testFiltro_Accept_IsDirectory() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        File file = new File("directory");
        file.isDirectory(true);
        
        boolean result = filtro.accept(file);
        
        assertTrue(result);
    }
    
    @Test
    public void testFiltro_GetDescription() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        
        String description = filtro.getDescription();
        
        assertEquals("JAW files", description);
    }
}