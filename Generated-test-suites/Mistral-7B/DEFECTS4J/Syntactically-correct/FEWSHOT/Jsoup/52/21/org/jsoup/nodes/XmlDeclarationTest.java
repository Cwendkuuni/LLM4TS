package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
        Assert.assertEquals("xml", xmlDeclaration.name());
        Assert.assertEquals("http://www.example.com", xmlDeclaration.baseUri());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndProcessingInstructionFalse() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        String expectedDeclaration = "<declaration version=\"\" encoding=\"\">";
        Assert.assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndProcessingInstructionTrue() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", true);
        String expectedDeclaration = "<?xml version=\"\" encoding=\"\">";
        Assert.assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNameVersionAndBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", "1.0");
        String expectedDeclaration = "<declaration version=\"1.0\" encoding=\"\">";
        Assert.assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNameVersionEncodingAndBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", "1.0", "UTF-8");
        String expectedDeclaration = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
        Assert.assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNoNameAndBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, "http://www.example.com", false);
        Assert.assertNull(xmlDeclaration.name());
    }

    @Test
    public void testXmlDeclarationWithNameAndNoBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
        Assert.assertEquals("xml", xmlDeclaration.name());
        Assert.assertNull(xmlDeclaration.baseUri());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndNoProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        String expectedDeclaration = "<declaration version=\"\" encoding=\"\">";
        Assert.assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", "1.0", "UTF-8");
        String expectedDeclaration = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
        String xmlDeclarationString = xmlDeclaration.toString();
        Assert.assertEquals(expectedDeclaration, xmlDeclarationString);
    }
}