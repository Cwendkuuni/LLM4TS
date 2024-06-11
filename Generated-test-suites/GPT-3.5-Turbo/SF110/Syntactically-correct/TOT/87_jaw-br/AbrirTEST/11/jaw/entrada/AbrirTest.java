import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.*;
import java.io.FileReader;
import java.io.Reader;

public class AbrirTest {
    
    private ProcessarEntidades form;
    private Abrir abrir;
    
    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        abrir = new Abrir(form, 10);
    }
    
    @Test
    public void testConstructorWithFile() {
        String arquivo = "test.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        assertEquals(arquivo, abrir.xml);
        assertEquals(form, abrir.form);
        assertEquals(ABRIR_XML, abrir.ABRIR_XML);
        assertEquals(ABRIR, abrir.ABRIR);
    }
    
    @Test
    public void testConstructorWithType() {
        int tipoAbrir = 10;
        Abrir abrir = new Abrir(form, tipoAbrir);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        assertEquals(ABRIR_XML, abrir.ABRIR_XML);
        assertEquals(ABRIR, abrir.ABRIR);
    }
    
    @Test
    public void testAbrirSerializadoWithValidFile() {
        String arquivo = "test.jaw";
        abrir.abrirSerializado(arquivo);
        assertEquals(arquivo, form.getNomeArquivoSalvo());
        assertNotNull(form.getEntidades());
    }
    
    @Test
    public void testAbrirSerializadoWithInvalidFile() {
        String arquivo = "invalid.jaw";
        abrir.abrirSerializado(arquivo);
        assertEquals("", form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
        assertEquals("Arquivo jaw corrompido ou incompatível com aplicação!", JOptionPane.getMessage());
    }
    
    @Test
    public void testAbrirXMLWithValidFile() {
        String arquivo = "test.jaw.xml";
        abrir.abrirXML(arquivo);
        assertEquals(arquivo, form.getNomeArquivoSalvo());
        assertNotNull(form.getEntidades());
    }
    
    @Test
    public void testAbrirXMLWithInvalidFile() {
        String arquivo = "invalid.jaw.xml";
        abrir.abrirXML(arquivo);
        assertEquals("", form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
        assertEquals("Arquivo xml corrompido ou incompatível com aplicação!", JOptionPane.getMessage());
    }
    
    @Test
    public void testFiltro2AcceptForAcceptedFile() {
        File file = new File("test.jaw.xml");
        assertTrue(abrir.new Filtro2().accept(file));
    }
    
    @Test
    public void testFiltro2AcceptForRejectedFile() {
        File file = new File("test.xml");
        assertFalse(abrir.new Filtro2().accept(file));
    }
    
    @Test
    public void testFiltro2GetDescription() {
        assertEquals("XML JAW files", abrir.new Filtro2().getDescription());
    }
    
    @Test
    public void testFiltroAcceptForAcceptedFile() {
        File file = new File("test.jaw");
        assertTrue(abrir.new Filtro().accept(file));
    }
    
    @Test
    public void testFiltroAcceptForRejectedFile() {
        File file = new File("test.xml");
        assertFalse(abrir.new Filtro().accept(file));
    }
    
    @Test
    public void testFiltroGetDescription() {
        assertEquals("JAW files", abrir.new Filtro().getDescription());
    }
}