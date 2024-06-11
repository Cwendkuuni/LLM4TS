import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AbrirTest {
    
    private ProcessarEntidades form;
    private Abrir abrir;
    
    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        abrir = new Abrir(form, "");
    }
    
    @Test
    public void testAbrirConstructor() {
        assertNotNull(abrir);
        assertNotNull(abrir.xml);
        assertEquals("", abrir.xml);
        assertNotNull(abrir.form);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirFileConstructor() {
        String arquivo = "example.jaw.xml";
        abrir = new Abrir(form, arquivo);
        assertNotNull(abrir);
        assertNotNull(abrir.xml);
        assertEquals("", abrir.xml);
        assertNotNull(abrir.form);
        assertEquals(form, abrir.form);
        assertEquals("example.jaw.xml", abrir.form.getNomeArquivoSalvo());
        assertEquals(ABRIR_XML, abrir.tipoAbrir);
    }
    
    @Test
    public void testAbrirIntConstructor() {
        abrir = new Abrir(form, ABRIR);
        assertNotNull(abrir);
        assertNotNull(abrir.xml);
        assertEquals("", abrir.xml);
        assertNotNull(abrir.form);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirSerializado() {
        abrir.abrirSerializado("example.jaw");
        assertNotNull(abrir.form.getNomeArquivoSalvo());
        assertNotNull(abrir.form.getEntidades());
    }
    
    @Test
    public void testAbrirSerializadoWithInvalidFile() {
        abrir.abrirSerializado("invalidFile.txt");
        String expectedMessage = "Arquivo jaw corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!";
        assertEquals(expectedMessage, JOptionPane.getMessage());
    }
    
    @Test
    public void testAbrirXML() {
        abrir.abrirXML("example.jaw.xml");
        assertNotNull(abrir.form.getNomeArquivoSalvo());
        assertNotNull(abrir.form.getEntidades());
    }
    
    @Test
    public void testAbrirXMLWithInvalidFile() {
        abrir.abrirXML("invalidFile.txt");
        String expectedMessage = "Arquivo xml corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!";
        assertEquals(expectedMessage, JOptionPane.getMessage());
    }
    
    @Test
    public void testFiltro2Accept() {
        Abrir.Filtro2 filtro2 = abrir.new Filtro2();
        File validFile = new File("example.jaw.xml");
        File invalidFile = new File("invalidFile.txt");
        assertTrue(filtro2.accept(validFile));
        assertFalse(filtro2.accept(invalidFile));
    }
    
    @Test
    public void testFiltro2GetDescription() {
        Abrir.Filtro2 filtro2 = abrir.new Filtro2();
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltroAccept() {
        Abrir.Filtro filtro = abrir.new Filtro();
        File validFile = new File("example.jaw");
        File invalidFile = new File("invalidFile.txt");
        assertTrue(filtro.accept(validFile));
        assertFalse(filtro.accept(invalidFile));
    }
    
    @Test
    public void testFiltroGetDescription() {
        Abrir.Filtro filtro = abrir.new Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }
}