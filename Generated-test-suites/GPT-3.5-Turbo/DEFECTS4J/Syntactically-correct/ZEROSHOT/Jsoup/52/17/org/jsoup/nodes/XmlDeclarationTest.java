import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("testDeclaration", "http://example.com", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("testDeclaration", "http://example.com", false);
        assertEquals("testDeclaration", xmlDeclaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("testDeclaration", "http://example.com", false);
        assertEquals("testDeclaration", xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("testDeclaration", "http://example.com", false);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
        assertEquals("<?testDeclaration>", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("testDeclaration", "http://example.com", false);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlTail(sb, 0, outputSettings);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("testDeclaration", "http://example.com", false);
        assertEquals("<?testDeclaration>", xmlDeclaration.toString());
    }
    
}