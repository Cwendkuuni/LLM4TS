import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class XmlDeclarationTest {

    // Test case for nodeName() method
    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("xmlDeclaration", "http://example.com", false);
        String expected = "#declaration";
        String actual = declaration.nodeName();
        assertEquals(expected, actual);
    }

    // Test case for name() method
    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("xmlDeclaration", "http://example.com", false);
        String expected = "xmlDeclaration";
        String actual = declaration.name();
        assertEquals(expected, actual);
    }

    // Test case for getWholeDeclaration() method with attributes present
    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");

        String expected = "xml version=\"1.0\" encoding=\"UTF-8\"";
        String actual = declaration.getWholeDeclaration();
        assertEquals(expected, actual);
    }

    // Test case for getWholeDeclaration() method without attributes
    @Test
    public void testGetWholeDeclarationWithoutAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xmlDeclaration", "http://example.com", false);
        String expected = "xmlDeclaration";
        String actual = declaration.getWholeDeclaration();
        assertEquals(expected, actual);
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xmlDeclaration", "http://example.com", false);
        String expected = "<?xmlDeclaration>";
        String actual = declaration.toString();
        assertEquals(expected, actual);
    }

    // Test case for outerHtmlHead() method
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xmlDeclaration", "http://example.com", false);
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(false);

        try {
            declaration.outerHtmlHead(accum, 0, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expected = "<?xmlDeclaration>";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    // Test case for outerHtmlTail() method
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xmlDeclaration", "http://example.com", false);
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(false);

        declaration.outerHtmlTail(accum, 0, out);

        String expected = "";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }
}