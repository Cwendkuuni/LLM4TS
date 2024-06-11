package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndIsProcessingInstructionFalse() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
        assertNotNull(xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndIsProcessingInstructionTrue() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("<!xml version=\"\" encoding=\"\"?>", xmlDeclaration.getWholeDeclaration());
        assertNotNull(xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithEmptyNameAndBaseUriAndIsProcessingInstructionFalse() {
        String name = "";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertNull(xmlDeclaration.name());
        assertNull(xmlDeclaration.getWholeDeclaration());
        assertNull(xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndNoIsProcessingInstruction() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
        assertNotNull(xmlDeclaration.toString());
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
        assertEquals("<!xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
        assertNotNull(xmlDeclaration.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationWithNullName() {
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        new XmlDeclaration(null, baseUri, isProcessingInstruction);
    }

    @Test
    public void testXmlDeclarationWithEmptyStringAsName() {
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration("", baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertNull(xmlDeclaration.name());
        assertNull(xmlDeclaration.getWholeDeclaration());
        assertNull(xmlDeclaration.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullBaseUri() {
        String name = "xml";
        boolean isProcessingInstruction = false;
        new XmlDeclaration(name, null, isProcessingInstruction);
    }
}