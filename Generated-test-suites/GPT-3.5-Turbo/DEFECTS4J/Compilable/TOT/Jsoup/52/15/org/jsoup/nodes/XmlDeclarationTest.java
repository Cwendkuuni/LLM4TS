package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testGetWholeDeclarationXml() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        String expectedDeclaration = "xml version=\"1.0\" encoding=\"UTF-8\"";
        assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationOther() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("other", "http://example.com", false);
        assertEquals("other", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHeadXml() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\">";
        assertEquals(expectedOutput, xmlDeclaration.outerHtml());
    }

    @Test
    public void testOuterHtmlHeadOther() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("other", "http://example.com", false);
        String expectedOutput = "<!other>";
        assertEquals(expectedOutput, xmlDeclaration.outerHtml());
    }

}