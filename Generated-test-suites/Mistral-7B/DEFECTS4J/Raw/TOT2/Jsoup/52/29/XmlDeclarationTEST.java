package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithValidNameAndProcessingInstructionFalse() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        String name = xmlDeclaration.name();
        String declaration = xmlDeclaration.getWholeDeclaration();
        assertEquals("#declaration", name);
        assertEquals("<xml version=\"baseUri\">\n", declaration);
    }

    @Test
    public void testXmlDeclarationWithValidNameAndProcessingInstructionTrue() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        String name = xmlDeclaration.name();
        String declaration = xmlDeclaration.getWholeDeclaration();
        assertEquals("#declaration", name);
        assertEquals("<?xml version=\"baseUri\"?>\n", declaration);
    }

    @Test
    public void testXmlDeclarationWithEmptyName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("", "baseUri", false);
        String name = xmlDeclaration.name();
        String declaration = xmlDeclaration.getWholeDeclaration();
        assertEquals("#declaration", name);
        assertEquals("<DECLARATION>\n", declaration);
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, "baseUri", false);
    }

    @Test
    public void testXmlDeclarationWithMultipleAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        String declaration = xmlDeclaration.getWholeDeclaration();
        assertEquals("<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"xml-1.0.dtd\" version=\"1.0\" encoding=\"UTF-8\">\n<xml version=\"1.0\" encoding=\"UTF-8\">\n", declaration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationWithInvalidName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("invalidName", "baseUri", false);
    }

    @Test
    public void testXmlDeclarationToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        String xmlString = xmlDeclaration.toString();
        assertEquals("<xml version=\"baseUri\"></xml>", xmlString);
    }
}