import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;

public class SalvarTest {
    
    private ProcessarEntidades form;
    private Salvar salvar;
    
    @Before
    public void setUp() {
        form = new ProcessarEntidades();
    }
    
    @After
    public void tearDown() {
        form = null;
        salvar = null;
    }
    
    @Test
    public void testSalvarConstructor_SALVAR_XML() {
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertNotNull(salvar);
        assertTrue(salvar.vela);
        assertTrue(salvar.arquivo.endsWith(".jaw.xml"));
        assertEquals(0, salvar.entidade.size());
    }
    
    @Test
    public void testSalvarConstructor_SALVAR() {
        salvar = new Salvar(form, Salvar.SALVAR);
        assertNotNull(salvar);
        assertTrue(salvar.vela);
        assertTrue(salvar.arquivo.endsWith(".jaw"));
        assertEquals(0, salvar.entidade.size());
    }
    
    @Test
    public void testSalvarConstructor_SALVAR_COMO() {
        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertNotNull(salvar);
        assertTrue(salvar.vela);
        assertTrue(salvar.arquivo.endsWith(".jaw"));
        assertEquals(0, salvar.entidade.size());
    }
    
    @Test
    public void testSalvarConstructor_Exception() {
        // Simulate an exception in form.getEntidades()
        form.setThrowException(true);
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertNull(salvar);
    }
    
    @Test
    public void testSalvarConstructor_NoFileSelected_WithName() {
        // Simulate no file selected but form has a name
        form.setName("Test Name");
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertNotNull(salvar);
        assertFalse(salvar.vela);
        assertEquals("Test Name.jaw.xml", salvar.arquivo);
        assertEquals(0, salvar.entidade.size());
    }
    
    @Test
    public void testSalvarConstructor_NoFileSelected_NoName() {
        // Simulate no file selected and form has no name
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertNull(salvar);
    }
    
    @Test
    public void testSalvarConstructor_SALVAR_WithName() {
        // Simulate SALVAR with a name
        form.setNomeArquivoSalvo("Test File");
        salvar = new Salvar(form, Salvar.SALVAR);
        assertNotNull(salvar);
        assertFalse(salvar.vela);
        assertEquals("Test File.jaw", salvar.arquivo);
        assertEquals(0, salvar.entidade.size());
    }
    
    @Test
    public void testSalvarConstructor_SALVAR_WithError() {
        // Simulate an exception in Serializacao.salvar()
        form.setSerializacaoThrowException(true);
        salvar = new Salvar(form, Salvar.SALVAR);
        assertNotNull(salvar);
        assertFalse(salvar.vela);
        assertEquals("Test File.jaw", salvar.arquivo);
        assertEquals(0, salvar.entidade.size());
        // Verify error dialog is displayed
        assertEquals(1, form.getErrorMessageCount());
    }
    
    @Test
    public void testSalvarConstructor_SALVAR_COMO_WithNameAndSave() {
        // Simulate SALVAR_COMO with a name and save selected
        form.setNomeArquivoSalvo("Test File");
        form.setShouldSave(true);
        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertNotNull(salvar);
        assertTrue(salvar.vela);
        assertEquals("Test File.jaw", salvar.arquivo);
        assertEquals(0, salvar.entidade.size());
    }
    
    @Test
    public void testSalvarConstructor_SALVAR_COMO_WithNameAndNoSave() {
        // Simulate SALVAR_COMO with a name and no save selected
        form.setNomeArquivoSalvo("Test File");
        form.setShouldSave(false);
        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertNotNull(salvar);
        assertFalse(salvar.vela);
        assertEquals("Test File.jaw", salvar.arquivo);
        assertEquals(0, salvar.entidade.size());
    }
    
    @Test
    public void testSalvarConstructor_SALVAR_COMO_WithoutName() {
        // Simulate SALVAR_COMO without a name
        form.setNomeArquivoSalvo(null);
        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertNotNull(salvar);
        assertFalse(salvar.vela);
        assertTrue(salvar.arquivo.endsWith(".jaw"));
        assertEquals(0, salvar.entidade.size());
    }
    
    @Test
    public void testSalvarConstructor_SALVAR_COMO_WithError() {
        // Simulate an exception in Serializacao.salvar()
        form.setSerializacaoThrowException(true);
        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertNotNull(salvar);
        assertFalse(salvar.vela);
        assertTrue(salvar.arquivo.endsWith(".jaw"));
        assertEquals(0, salvar.entidade.size());
        // Verify error dialog is displayed
        assertEquals(1, form.getErrorMessageCount());
    }
    
    @Test
    public void testFiltro2Accept() {
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();
        assertTrue(filtro2.accept(new File("test.jaw.xml")));
        assertTrue(filtro2.accept(new File("dir/test.jaw.xml")));
        assertFalse(filtro2.accept(new File("test.invalid")));
        assertFalse(filtro2.accept(new File("dir/test.invalid")));
    }
    
    @Test
    public void testFiltro2GetDescription() {
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltroAccept() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        assertTrue(filtro.accept(new File("test.jaw")));
        assertTrue(filtro.accept(new File("dir/test.jaw")));
        assertFalse(filtro.accept(new File("test.invalid")));
        assertFalse(filtro.accept(new File("dir/test.invalid")));
    }
    
    @Test
    public void testFiltroGetDescription() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }
}