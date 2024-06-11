import static org.junit.Assert.*;
import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlHead(sb, 0, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("<?xml", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlTail(sb, 0, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("<?xml", xmlDeclaration.toString());
    }

}