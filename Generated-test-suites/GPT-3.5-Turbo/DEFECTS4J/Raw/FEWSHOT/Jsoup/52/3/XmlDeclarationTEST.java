import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

    @Test
    public void testConstructor() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("http://example.com", xmlDeclaration.baseUri());
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertFalse(xmlDeclaration.isProcessingInstruction());

        XmlDeclaration xmlDeclaration2 = new XmlDeclaration("declaration", "http://test.com", true);
        assertEquals("declaration", xmlDeclaration2.name());
        assertEquals("http://test.com", xmlDeclaration2.baseUri());
        assertEquals("#declaration", xmlDeclaration2.nodeName());
        assertTrue(xmlDeclaration2.isProcessingInstruction());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertNull(xmlDeclaration.getWholeDeclaration());

        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());

        XmlDeclaration xmlDeclaration2 = new XmlDeclaration("declaration", "http://test.com", true);
        assertNull(xmlDeclaration2.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlHead(sb, 0, Document.OutputSettings.html());
        assertEquals("<xml>", sb.toString());

        XmlDeclaration xmlDeclaration2 = new XmlDeclaration("declaration", "http://test.com", true);
        sb.setLength(0);
        xmlDeclaration2.outerHtmlHead(sb, 0, Document.OutputSettings.html());
        assertEquals("<!declaration>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlTail(sb, 0, Document.OutputSettings.html());
        assertEquals("", sb.toString());

        XmlDeclaration xmlDeclaration2 = new XmlDeclaration("declaration", "http://test.com", true);
        sb.setLength(0);
        xmlDeclaration2.outerHtmlTail(sb, 0, Document.OutputSettings.html());
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("<xml>", xmlDeclaration.toString());

        XmlDeclaration xmlDeclaration2 = new XmlDeclaration("declaration", "http://test.com", true);
        assertEquals("<!declaration>", xmlDeclaration2.toString());
    }

}