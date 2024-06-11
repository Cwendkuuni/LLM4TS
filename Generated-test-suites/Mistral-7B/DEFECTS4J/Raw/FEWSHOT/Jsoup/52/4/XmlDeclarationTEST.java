package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndUriAndFalseProcessingInstruction() {
        String baseUri = "http://example.com";
        String name = "xml";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("http://example.com", xmlDeclaration.baseUri());
        assertEquals("<?xml>\r\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndUriAndTrueProcessingInstruction() {
        String baseUri = "http://example.com";
        String name = "xml";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, true);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("http://example.com", xmlDeclaration.baseUri());
        assertEquals("<?xml>\r\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndAttributes() {
        String baseUri = "http://example.com";
        String name = "xml";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("http://example.com", xmlDeclaration.baseUri());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNullName() {
        String baseUri = "http://example.com";
        String name = null;

        try {
            new XmlDeclaration(name, baseUri, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testXmlDeclarationWithEmptyName() {
        String baseUri = "http://example.com";
        String name = "";

        try {
            new XmlDeclaration(name, baseUri, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testXmlDeclarationWithNullBaseUri() {
        String name = "xml";

        try {
            new XmlDeclaration(name, null, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testXmlDeclarationWithEmptyBaseUri() {
        String name = "xml";

        try {
            new XmlDeclaration(name, "", false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testXmlDeclarationWithNullNameAndUri() {
        try {
            new XmlDeclaration(null, null, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testXmlDeclarationWithNullProcessingInstruction() {
        String name = "xml";
        boolean isProcessingInstruction = null;

        try {
            new XmlDeclaration(name, "http://example.com", isProcessingInstruction);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetWholeDeclaration() {
        String name = "xml";
        String baseUri = "http://example.com";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationForDeclaration() {
        String name = "xml";
        String baseUri = "http://example.com";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);

        assertEquals(name, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationForProcessingInstruction() {
        String name = "xml-mapping";
        String baseUri = "http://example.com";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, true);

        assertEquals(name, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        String name = "xml";
        String baseUri = "http://example.com";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);

        Appendable accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, null);

        assertEquals("<?xml>\r\n", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        String name = "xml";
        String baseUri = "http://example.com";        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);

        Appendable accum = new StringBuilder();
        xmlDeclaration.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() throws IOException {
        String name = "xml";
        String baseUri = "http://example.com";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);

        assertEquals("<?xml>\r\n", xmlDeclaration.toString());
    }

    @Test
    public void testToStringForProcessingInstruction() throws IOException {
        String name = "xml-mapping";
        String baseUri = "http://example.com";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, true);

        assertEquals("<?xml-mapping>\r\n", xmlDeclaration.toString());
    }

    @Test
    public void testToStringWithAttributes() throws IOException {
        String name = "xml";
        String baseUri = "http://example.com";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n", xmlDeclaration.toString());
    }
}