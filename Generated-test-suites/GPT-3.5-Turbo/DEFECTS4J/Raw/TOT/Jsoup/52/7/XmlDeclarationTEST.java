import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("#declaration", declaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", declaration.name());
    }
    
    @Test
    public void testGetWholeDeclarationWithoutAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testGetWholeDeclarationWithInvalidAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("invalid", "value");
        assertEquals("xml", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder sb = new StringBuilder();
        try {
            declaration.outerHtmlHead(sb, 0, null);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
        assertEquals("<?xml>", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder sb = new StringBuilder();
        try {
            declaration.outerHtmlTail(sb, 0, null);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("<?xml>", declaration.toString());
    }
}