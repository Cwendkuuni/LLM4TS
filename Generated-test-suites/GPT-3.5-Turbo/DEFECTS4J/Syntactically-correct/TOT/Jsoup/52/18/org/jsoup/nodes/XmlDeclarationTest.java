
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {
    
    private XmlDeclaration xmlDeclaration;
    
    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", true);
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }
    
    @Test
    public void testName() {
        assertEquals("xml", xmlDeclaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration_WithVersionAndEncoding() {
        String expected = "xml version=\"1.0\" encoding=\"UTF-8\"";
        xmlDeclaration.getAttributes().put("version", "1.0");
        xmlDeclaration.getAttributes().put("encoding", "UTF-8");
        
        assertEquals(expected, xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testGetWholeDeclaration_WithoutVersionAndEncoding() {
        String expected = "xml";
        
        assertEquals(expected, xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testToString() {
        String expected = "<!xml>";
        assertEquals(expected, xmlDeclaration.toString());
    }
}
