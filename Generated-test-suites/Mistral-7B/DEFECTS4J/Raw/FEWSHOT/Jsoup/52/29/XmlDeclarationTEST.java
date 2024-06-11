package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndUriAndProcessingInstructionFalse() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());

        assertNull(xmlDeclaration.outerHtmlTail(null, 0, null));
        assertEquals("<declaration xml=\"http://example.com\">\n", xmlDeclaration.outerHtmlHead(null, 0, null));
        assertEquals("<declaration xml=\"http://example.com\">\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndUriAndProcessingInstructionTrue() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("<?xml xml=\"http://example.com\">\n", xmlDeclaration.getWholeDeclaration());

        assertNull(xmlDeclaration.outerHtmlTail(null, 0, null));
        assertEquals("<?xml xml=\"http://example.com\">\n", xmlDeclaration.outerHtmlHead(null, 0, null));
        assertEquals("<?xml xml=\"http://example.com\">\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndAttributes() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("<declaration xml=\"http://example.com\" version=\"1.0\" encoding=\"UTF-8\"/>\n", xmlDeclaration.getWholeDeclaration());

        assertNull(xmlDeclaration.outerHtmlTail(null, 0, null));
        assertEquals("<declaration xml=\"http://example.com\" version=\"1.0\" encoding=\"UTF-8\"/>\n", xmlDeclaration.outerHtmlHead(null, 0, null));
        assertEquals("<declaration xml=\"http://example.com\" version=\"1.0\" encoding=\"UTF-8\"/>\n", xmlDeclaration.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullName() {
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        new XmlDeclaration(null, baseUri, isProcessingInstruction);
    }

    @Test
    public void testXmlDeclarationWithEmptyName() {
        String name = "";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("", xmlDeclaration.name());
        assertEquals("<declaration xml=\"http://example.com\"/>\n", xmlDeclaration.getWholeDeclaration());

        assertNull(xmlDeclaration.outerHtmlTail(null, 0, null));
        assertEquals("<declaration xml=\"http://example.com\"/>\n", xmlDeclaration.outerHtmlHead(null, 0, null));
        assertEquals("<declaration xml=\"http://example.com\"/>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndNullUri() {
        String name = "xml";
        String baseUri = null;
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("<declaration/>\n", xmlDeclaration.getWholeDeclaration());

        assertNull(xmlDeclaration.outerHtmlTail(null, 0, null));
        assertEquals("<declaration/>\n", xmlDeclaration.outerHtmlHead(null, 0, null));
        assertEquals("<declaration/>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNullNameAndUri() {
        String name = null;
        String baseUri = null;
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertNull(xmlDeclaration.name());
        assertNull(xmlDeclaration.getWholeDeclaration());

        assertNull(xmlDeclaration.outerHtmlTail(null, 0, null));
        assertNull(xmlDeclaration.outerHtmlHead(null, 0, null));
        assertNull(xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndUriAndProcessingInstructionAndAttributes() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("<?xml xml=\"http://example.com\" version=\"1.0\" encoding=\"UTF-8\"/>\n", xmlDeclaration.getWholeDeclaration());

        assertNull(xmlDeclaration.outerHtmlTail(null, 0, null));
        assertEquals("<?xml xml=\"http://example.com\" version=\"1.0\" encoding=\"UTF-8\"/>\n", xmlDeclaration.outerHtmlHead(null, 0, null));
        assertEquals("<?xml xml=\"http://example.com\" version=\"1.0\" encoding=\"UTF-8\"/>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNullAttributes() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("<declaration xml=\"http://example.com\"/>\n", xmlDeclaration.getWholeDeclaration());

        assertNull(xmlDeclaration.outerHtmlTail(null, 0, null));
        assertEquals("<declaration xml=\"http://example.com\"/>\n", xmlDeclaration.outerHtmlHead(null, 0, null));
        assertEquals("<declaration xml=\"http://example.com\"/>\n", xmlDeclaration.toString());
    }
}