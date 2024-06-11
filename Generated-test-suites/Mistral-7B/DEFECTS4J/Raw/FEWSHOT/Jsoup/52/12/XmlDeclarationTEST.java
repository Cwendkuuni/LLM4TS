package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndIsProcessingInstructionFalse() {
        String name = "xml";
        String baseUri = "/test.xml";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("/test.xml", xmlDeclaration.baseUri());
        assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndIsProcessingInstructionTrue() {
        String name = "xml";
        String baseUri = "/test.xml";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("/test.xml", xmlDeclaration.baseUri());
        assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndEmptyAttributes() {
        String name = "xml";
        String baseUri = "/test.xml";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("/test.xml", xmlDeclaration.baseUri());
        assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndVersionAttribute() {
        String name = "xml";
        String baseUri = "/test.xml";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("version", "1.0");

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("/test.xml", xmlDeclaration.baseUri());
        assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndEncodingAttribute() {
        String name = "xml";
        String baseUri = "/test.xml";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("/test.xml", xmlDeclaration.baseUri());
        assertEquals("<?xml encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml encoding=\"UTF-8\"?>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndVersionAndEncodingAttributes() {
        String name = "xml";
        String baseUri = "/test.xml";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("/test.xml", xmlDeclaration.baseUri());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullName() {
        String baseUri = "/test.xml";
        boolean isProcessingInstruction = false;

        new XmlDeclaration(null, baseUri, isProcessingInstruction);
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullBaseUri() {
        String name = "xml";
        boolean isProcessingInstruction = false;

        new XmlDeclaration(name, null, isProcessingInstruction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationWithEmptyName() {
        String baseUri = "/test.xml";
        boolean isProcessingInstruction = false;

        new XmlDeclaration("", baseUri, isProcessingInstruction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationWithEmptyStringAsNameAndBaseUri() {
        boolean isProcessingInstruction = false;

        new XmlDeclaration("", "", isProcessingInstruction);
    }

    @Test
    public void testXmlDeclarationWithNullAttributes() {
        String name = "xml";
        String baseUri = "/test.xml";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("/test.xml", xmlDeclaration.baseUri());
        assertNull(xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithEmptyAttributesMap() {
        String name = "xml";
        String baseUri = "/test.xml";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().clear();

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("/test.xml", xmlDeclaration.baseUri());
        assertNull(xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml>", xmlDeclaration.toString());
    }
}