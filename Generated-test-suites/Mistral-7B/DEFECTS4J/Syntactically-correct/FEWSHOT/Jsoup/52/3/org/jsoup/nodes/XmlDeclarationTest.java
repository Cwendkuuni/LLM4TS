package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<xml>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndVersion() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().put("version", "1.0");
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml version=\"1.0\"\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml encoding=\"UTF-8\"\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameEncodingAndVersion() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        xmlDeclaration.attributes().put("version", "1.0");
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml encoding=\"UTF-8\" version=\"1.0\"\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlProcessingInstructionDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml-pi", null, true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("<?xml-pi>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlProcessingInstructionDeclarationWithData() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml-pi", null, true);
        xmlDeclaration.attributes().put("data", "ignore");
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("<?xml-pi data=\"ignore\">\n", xmlDeclaration.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullName() {
        new XmlDeclaration(null, null, false);
    }
}