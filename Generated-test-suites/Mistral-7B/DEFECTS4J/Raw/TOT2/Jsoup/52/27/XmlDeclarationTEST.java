import org.junit.Test;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndProcessingInstructionFalse() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("xml version=\"\" encoding=\"\"", xmlDeclaration.getWholeDeclaration());

        String expectedXml = "<declaration xml version=\"\" encoding=\"\">";
        StringBuilder accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        xmlDeclaration.outerHtmlTail(accum, 0, null);
        assertEquals(expectedXml, xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndProcessingInstructionTrue() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml version=\"\" encoding=\"\"?>", xmlDeclaration.getWholeDeclaration());

        String expectedXml = "<?xml version=\"\" encoding=\"\"?>";
        StringBuilder accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        xmlDeclaration.outerHtmlTail(accum, 0, null);
        assertEquals(expectedXml, xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameVersionAndEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "version=\"1.0\"", "encoding=\"UTF-8\"", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        StringBuilder accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        xmlDeclaration.outerHtmlTail(accum, 0, null);
        assertEquals(expectedXml, xmlDeclaration.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, "baseUri", false);
        throw new AssertionError("Expected NullPointerException, but instead got: " + xmlDeclaration);
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        throw new AssertionError("Expected NullPointerException, but instead got: " + xmlDeclaration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationWithEmptyName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("", "version=\"1.0\"", "encoding=\"UTF-8\"", false);
        throw new AssertionError("Expected IllegalArgumentException, but instead got: " + xmlDeclaration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationWithEmptyVersion() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", "encoding=\"UTF-8\"", false);
        throw new AssertionError("Expected IllegalArgumentException, but instead got: " + xmlDeclaration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationWithEmptyEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "version=\"1.0\"", "", false);
        throw new AssertionError("Expected IllegalArgumentException, but instead got: " + xmlDeclaration);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionThrowsWhenEncodingIsNotSupported() {
        Attributes attributes = new Attributes();
        attributes.put("encoding", "ASCIIII");
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);

        try {
            xmlDeclaration.outerHtmlHead(new StringBuilder(), 0, null);
        } catch (IOException e) {
            // Expected
        }
    }
}