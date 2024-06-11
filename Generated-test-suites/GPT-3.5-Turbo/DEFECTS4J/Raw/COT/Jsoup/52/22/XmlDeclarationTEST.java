import org.junit.Test;

import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("name", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", true);
        String wholeDeclaration = declaration.getWholeDeclaration();
        assertEquals("xml version=\"baseUri\"", wholeDeclaration);
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            declaration.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("<!name>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            declaration.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("<!name>", declaration.toString());
    }
}