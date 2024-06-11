package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationCreation() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        assertNotNull(xmlDeclaration);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertTrue(xmlDeclaration.isProcessingInstruction() == isProcessingInstruction);
    }

    @Test
    public void testXmlDeclarationCreationWithProcessingInstruction() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        assertNotNull(xmlDeclaration);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertTrue(xmlDeclaration.isProcessingInstruction() == isProcessingInstruction);
    }

    @Test
    public void testXmlDeclarationWithEmptyName() {
        String name = "";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        assertNotNull(xmlDeclaration);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertNull(xmlDeclaration.name());
    }

    @Test
    public void testXmlDeclarationWithAttributes() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithoutAttributes() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationToString() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("<declaration xmlns:xml=\"http://www.w3.org/XML/1998/namespace\">" + xmlDeclaration.getWholeDeclaration() + "</declaration>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationProcessingInstructionToString() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("<?xml " + xmlDeclaration.getWholeDeclaration() + "?>", xmlDeclaration.toString());
    }
}