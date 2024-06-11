import static org.junit.Assert.*;
import org.junit.Test;

public class AbrirTest {

    @Test
    public void testAbrirConstructorWithArquivo() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "test.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        assertTrue(arquivo.endsWith(".jaw.xml"));
    }
    
    @Test
    public void testAbrirConstructorWithTipoAbrirABRIR_XML() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoAbrir = Abrir.ABRIR_XML;
        Abrir abrir = new Abrir(form, tipoAbrir);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        JFileChooser escolhedor = new JFileChooser();
        String currentDirectory = escolhedor.getCurrentDirectory().getAbsolutePath();
        assertEquals(currentDirectory, Main.configuracao.getCaminhoDosProjetos());
        assertEquals(Filtro2.class, escolhedor.getFileFilter().getClass());
        
    }
    
     @Test
    public void testAbrirConstructorWithTipoAbrirABRIR() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoAbrir = Abrir.ABRIR;
        Abrir abrir = new Abrir(form, tipoAbrir);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        JFileChooser escolhedor = new JFileChooser();
        String currentDirectory = escolhedor.getCurrentDirectory().getAbsolutePath();
        assertEquals(currentDirectory, Main.configuracao.getCaminhoDosProjetos());
        assertEquals(Filtro.class, escolhedor.getFileFilter().getClass());
        
    }
    
    @Test
    public void testAbrirSerializadoWithInvalidFileExtension() {
        String arquivo = "test.jaw";
        Abrir abrir = new Abrir(new ProcessarEntidades(), arquivo);
        assertEquals("", abrir.xml);
        assertNull(abrir.form.getNomeArquivoSalvo());
        assertNull(abrir.form.getEntidades());
        assertEquals("Arquivo jaw corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!", JOptionPane.getMessage());
        assertEquals(e.toString(), Main.janelaPrincipal.getLog());
    }
    
    @Test
    public void testAbrirSerializadoWithValidFileExtension() {
        String arquivo = "test.jaw";
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo(arquivo);
        Abrir abrir = new Abrir(form, arquivo);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        assertEquals(form, abrir.form.getNomeArquivoSalvo());
        assertNotNull(abrir.form.getEntidades());
    }
    
    @Test
    public void testAbrirXMLWithInvalidFileExtension() {
        String arquivo = "test.xml";
        Abrir abrir = new Abrir(new ProcessarEntidades(), arquivo);
        assertEquals("", abrir.xml);
        assertNull(abrir.form.getNomeArquivoSalvo());
        assertNull(abrir.form.getEntidades());
        assertEquals("Arquivo xml corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!", JOptionPane.getMessage());
        assertEquals(e.toString(), Main.janelaPrincipal.getLog());
    }
    
    @Test
    public void testAbrirXMLWithValidFileExtension() {
        String arquivo = "test.xml";
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo(arquivo);
        Abrir abrir = new Abrir(form, arquivo);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        assertEquals(form, abrir.form.getNomeArquivoSalvo());
        assertNotNull(abrir.form.getEntidades());
    }

    private class Filtro2Test extends Filtro2 {

        @Test
        public void testAcceptWithValidFileExtension() {
            File file = new File("test.jaw.xml");
            assertTrue(super.accept(file));
        }
        
        @Test
        public void testAcceptWithInvalidFileExtension() {
            File file = new File("test.xml");
            assertFalse(super.accept(file));
        }
        
        @Test
        public void testGetDescription() {
            assertEquals("XML JAW files", super.getDescription());
        }
    }
    
    private class FiltroTest extends Filtro {

        @Test
        public void testAcceptWithValidFileExtension() {
            File file = new File("test.jaw");
            assertTrue(super.accept(file));
        }
        
        @Test
        public void testAcceptWithInvalidFileExtension() {
            File file = new File("test.xml");
            assertFalse(super.accept(file));
        }
        
        @Test
        public void testGetDescription() {
            assertEquals("JAW files", super.getDescription());
        }
    }
}