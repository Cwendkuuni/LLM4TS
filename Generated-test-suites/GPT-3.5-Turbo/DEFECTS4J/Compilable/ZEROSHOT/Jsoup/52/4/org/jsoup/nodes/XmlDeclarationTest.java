package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeNameReturnsDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testNameReturnsNameOfDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclarationReturnsDeclarationString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHeadReturnsExpectedString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            xmlDeclaration.outerHtmlHead(stringBuilder, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!xml>", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTailDoesNothing() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            xmlDeclaration.outerHtmlTail(stringBuilder, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testToStringReturnsOuterHtml() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        assertEquals(xmlDeclaration.outerHtml(), xmlDeclaration.toString());
    }

}