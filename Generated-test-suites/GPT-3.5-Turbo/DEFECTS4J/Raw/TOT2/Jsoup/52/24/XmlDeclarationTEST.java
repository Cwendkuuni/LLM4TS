import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithoutAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("testDeclaration", "http://example.com", false);
        assertEquals("testDeclaration", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        // Test this method by setting up appropriate inputs and checking the generated output
    }

    @Test
    public void testToString() {
        // Test this method by checking if the generated String matches the expected outerHtml
    }
}