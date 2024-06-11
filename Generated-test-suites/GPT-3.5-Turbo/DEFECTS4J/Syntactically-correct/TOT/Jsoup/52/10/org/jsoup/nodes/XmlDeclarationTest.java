import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlDeclarationTest {

    private static final String BASE_URI = "http://example.com";

    @Test
    public void testInitialization() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", BASE_URI, true);
        assertNotNull(xmlDeclaration);
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", BASE_URI, true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testGetWholeDeclarationForXml() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", BASE_URI, true);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
        xmlDeclaration.attributes.put("version", "1.0");
        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
        xmlDeclaration.attributes.put("unknown-attribute", "value");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationForNonXml() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("nonxml", BASE_URI, false);
        assertEquals("nonxml", xmlDeclaration.getWholeDeclaration());
        xmlDeclaration.attributes.put("version", "1.0");
        assertEquals("nonxml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        XmlDeclaration xmlDeclaration = Mockito.mock(XmlDeclaration.class, Mockito.CALLS_REAL_METHODS);
        Appendable accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<?xml version=\"1.0\"?>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", BASE_URI, true);
        Appendable accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        xmlDeclaration.outerHtmlTail(accum, 0, outputSettings);
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", BASE_URI, true);
        assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.toString());
    }
}