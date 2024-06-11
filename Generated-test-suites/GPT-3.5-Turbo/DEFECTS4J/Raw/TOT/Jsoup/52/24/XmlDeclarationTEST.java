import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {
    private XmlDeclaration xmlDeclaration;

    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration_xmlWithAttributes() {
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        String expected = "xml version=\"1.0\" encoding=\"UTF-8\"";
        assertEquals(expected, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclaration_xmlNoAttributes() {
        String expected = "xml";
        assertEquals(expected, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        String expected = "<!" + xmlDeclaration.getWholeDeclaration() + ">";
        try {
            xmlDeclaration.outerHtmlHead(accum, depth, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlTail(accum, depth, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        String expected = xmlDeclaration.outerHtml();
        assertEquals(expected, xmlDeclaration.toString());
    }
}