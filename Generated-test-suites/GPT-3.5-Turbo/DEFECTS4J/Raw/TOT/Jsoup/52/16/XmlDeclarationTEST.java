import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
    
    XmlDeclaration xmlDeclaration; // The instance of the class under test
    
    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("declaration", "baseUri", true);
    }
    
    @After
    public void tearDown() {
        xmlDeclaration = null;
    }
    
    // Test case for nodeName()
    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }
    
    // Test case for name()
    @Test
    public void testName() {
        assertEquals("declaration", xmlDeclaration.name());
    }
    
    // Test case for getWholeDeclaration() when name is "xml" and attributes size is greater than 1
    @Test
    public void testGetWholeDeclarationWithXmlAndAttributes() {
        xmlDeclaration.setAttributes(new HashMap<String, String>() {{
            put("version", "1.0");
            put("encoding", "UTF-8");
        }});
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }
    
    // Test case for getWholeDeclaration() when name is not "xml" and attributes size is greater than 1
    @Test
    public void testGetWholeDeclarationWithNonXmlAndAttributes() {
        xmlDeclaration.setAttributes(new HashMap<String, String>() {{
            put("version", "1.0");
            put("encoding", "UTF-8");
        }});
        assertEquals("declaration", xmlDeclaration.getWholeDeclaration());
    }
    
    // Test case for getWholeDeclaration() when name is "xml" and attributes size is 1
    @Test
    public void testGetWholeDeclarationWithXmlAndSingleAttribute() {
        xmlDeclaration.setAttributes(new HashMap<String, String>() {{
            put("version", "1.0");
        }});
        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
    }
    
    // Test case for getWholeDeclaration() when name is not "xml" and attributes size is 1
    @Test
    public void testGetWholeDeclarationWithNonXmlAndSingleAttribute() {
        xmlDeclaration.setAttributes(new HashMap<String, String>() {{
            put("version", "1.0");
        }});
        assertEquals("declaration", xmlDeclaration.getWholeDeclaration());
    }
    
    // Test case for getWholeDeclaration() when name is "xml" and attributes is empty
    @Test
    public void testGetWholeDeclarationWithXmlAndEmptyAttributes() {
        xmlDeclaration.setAttributes(new HashMap<String, String>());
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }
    
    // Test case for getWholeDeclaration() when name is not "xml" and attributes is empty
    @Test
    public void testGetWholeDeclarationWithNonXmlAndEmptyAttributes() {
        xmlDeclaration.setAttributes(new HashMap<String, String>());
        assertEquals("declaration", xmlDeclaration.getWholeDeclaration());
    }
    
    // Test case for outerHtmlHead()
    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        out.indentAmount(4);
        try {
            xmlDeclaration.outerHtmlHead(sb, 0, out);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!declaration>", sb.toString());
    }
    
    // Test case for outerHtmlTail()
    @Test
    public void testOuterHtmlTail() {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        out.indentAmount(4);
        try {
            xmlDeclaration.outerHtmlTail(sb, 0, out);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Test case for toString()
    @Test
    public void testToString() {
        assertEquals("<!declaration>", xmlDeclaration.toString());
    }
}