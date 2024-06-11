import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", declaration.getWholeDeclaration());
    }

    // Edge case: XML declaration with version and encoding attributes
    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        declaration.attributes().put("version", "1.0");
        declaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("<xml>xml</xml>", declaration.toString());
    }
}