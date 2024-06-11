import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
    
    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://www.example.com", true);
        assertEquals("#declaration", declaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("xml", declaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("xml", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        StringBuilder sb = new StringBuilder();
        try {
            declaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<?xml>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        StringBuilder sb = new StringBuilder();
        try {
            declaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("<?xml>", declaration.toString());
    }
    
}