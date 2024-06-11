package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("baseUri", xmlDeclaration.baseUri());
        assertEquals("xml\n", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameProcessingInstructionAndBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("baseUri", xmlDeclaration.baseUri());
        assertEquals("<?xml>\n", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameProcessingInstructionEmptyAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertNull(xmlDeclaration.baseUri());
        assertEquals("<?xml>\n", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameDeclarationAndAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "version=\"1.0\" encoding=\"UTF-8\"", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
        assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\">\n", xmlDeclaration.getWholeDeclaration());
        assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\">\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameProcessingInstructionAndAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "version=\"1.0\" encoding=\"UTF-8\"", true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationWithNullName() {
        new XmlDeclaration(null, "baseUri", false);
    }
}