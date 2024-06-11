import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import jaw.entrada.Abrir;

public class AbrirTest {
    private ProcessarEntidades form;
    private Abrir abrir;        
    
    // Set up the necessary objects for testing
    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        abrir = new Abrir(form, "testFile.jaw.xml");
    }
    
    // Test abrirSerializado() for valid .jaw file
    // Typical use case
    @Test
    public void testAbrirSerializado_ValidJawFile() {
        abrir.abrirSerializado("validJawFile.jaw");
        
        // Assert that the form object's nomeArquivoSalvo variable is set to "validJawFile.jaw"
        assertEquals("validJawFile.jaw", form.getNomeArquivoSalvo());
        
        // Assert that the serializado object is called correctly and sets the entidades variable in the form object
        assertNotNull(form.getEntidades());
    }
    
    // Test abrirSerializado() for invalid .jaw file
    // Error scenario
    @Test
    public void testAbrirSerializado_InvalidJawFile() {
        abrir.abrirSerializado("invalidJawFile.jaw");
        
        // Assert that a JOptionPane dialog is displayed with the correct error message
        assertEquals("Arquivo jaw corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!", JOptionPane.getMessage());
        
        // Assert that the form object's nomeArquivoSalvo variable is not set
        assertNull(form.getNomeArquivoSalvo());
        
        // Assert that the form object's entidades variable is not set
        assertNull(form.getEntidades());
    }
    
    // Test abrirXML() for valid .jaw.xml file
    // Typical use case
    @Test
    public void testAbrirXML_ValidXmlFile() {
        abrir.abrirXML("validXmlFile.jaw.xml");
        
        // Assert that the form object's nomeArquivoSalvo variable is set to "validXmlFile.jaw.xml"
        assertEquals("validXmlFile.jaw.xml", form.getNomeArquivoSalvo());
        
        // Assert that the xml2Entidades() method in SerializacaoXML is called correctly and sets the entidades variable in the form object
        assertNotNull(form.getEntidades());
    }
    
    // Test abrirXML() for invalid .jaw.xml file
    // Error scenario
    @Test
    public void testAbrirXML_InvalidXmlFile() {
        abrir.abrirXML("invalidXmlFile.jaw.xml");
        
        // Assert that a JOptionPane dialog is displayed with the correct error message
        assertEquals("Arquivo xml corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!", JOptionPane.getMessage());
        
        // Assert that the form object's nomeArquivoSalvo variable is not set
        assertNull(form.getNomeArquivoSalvo());
        
        // Assert that the form object's entidades variable is not set
        assertNull(form.getEntidades());
    }
}