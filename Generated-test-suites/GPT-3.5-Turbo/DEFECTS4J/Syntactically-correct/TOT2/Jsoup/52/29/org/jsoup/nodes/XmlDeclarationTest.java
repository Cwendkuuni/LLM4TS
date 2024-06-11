import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    // Edge cases and error scenarios to be tested:

    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        xmlDeclaration.attributes().put("version", "1.0");
        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        // Test the generating XML declaration head
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("<!xml>", xmlDeclaration.toString());
    }

}