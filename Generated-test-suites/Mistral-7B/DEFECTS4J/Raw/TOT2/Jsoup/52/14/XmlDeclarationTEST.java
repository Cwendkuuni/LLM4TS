import org.junit.Test;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testXmlDeclarationWithEmptyName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testXmlDeclarationWithNameAndProcessingInstructionFalse() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNameAndProcessingInstructionTrue() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationWithNullName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
    }

    @Test
    public void testXmlDeclarationWithNullBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        // Test other methods with null baseUri here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationWithInvalidBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "invalid:uri", false);
        // Test other methods with invalid baseUri here
    }

    @Test
    public void testXmlDeclarationWithVersionAndEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullVersion() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().put("version", null);
    }

    @Test
    public void testXmlDeclarationWithEmptyVersion() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().put("version", "");
        assertEquals("<?xml version=\"\"?>", xmlDeclaration.getWholeDeclaration());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().put("encoding", null);
    }

    @Test
    public void testXmlDeclarationWithEmptyEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().put("encoding", "");
        assertEquals("<?xml encoding=\"\"?>", xmlDeclaration.getWholeDeclaration());
    }

    @Test(expected = java.io.IOException.class)
    public void testXmlDeclarationWithIOException() throws IOException {
        Appendable appendable = new StringBuffer();
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.outerHtmlHead(appendable, 0, null);
    }

    // Test toString() and other methods similarly

    @Test
    public void testXmlDeclarationWithAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithEmptyAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("<?xml?>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("<declaration></declaration>", xmlDeclaration.toString());
    }
}