package org.jsoup.nodes.test;

import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for XmlDeclaration.
 */
public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndIsProcessingInstructionFalse() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        String expectedName = "#declaration";
        String expectedDeclaration = "xml version=\"\" encoding=\"\"";
        String expectedToString = "<declaration xml version=\"\" encoding=\"\">";

        Assert.assertEquals(expectedName, xmlDeclaration.nodeName());
        Assert.assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
        Assert.assertEquals(expectedToString, xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndIsProcessingInstructionTrue() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        String expectedName = "#declaration";
        String expectedDeclaration = "<?xml version=\"\" encoding=\"\"?>";
        String expectedToString = "<?xml version=\"\" encoding=\"\">";

        Assert.assertEquals(expectedName, xmlDeclaration.nodeName());
        Assert.assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
        Assert.assertEquals(expectedToString, xmlDeclaration.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullName() {
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, baseUri, isProcessingInstruction);
    }

    @Test
    public void testXmlDeclarationWithMultipleAttributes() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        Map<String, String> attributes = new HashMap<>();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");

        String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String expectedToString = "<declaration xml version=\"1.0\" encoding=\"UTF-8\">";

        xmlDeclaration.attributes = attributes;

        Assert.assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
        Assert.assertEquals(expectedToString, xmlDeclaration.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationWithEmptyName() {
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration("", baseUri, isProcessingInstruction);
    }

    @Test
    public void testXmlDeclarationToStringWithNullDocumentOutputSettings() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        String expectedToString = "<declaration xml version=\"\" encoding=\"\">";

        Assert.assertEquals(expectedToString, xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationToStringWithOutputSettings() throws IOException {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        String expectedToString = "<declaration xml version=\"\" encoding=\"\">";

        Appendable accum = new StringBuilder();
        xmlDeclaration.toString(accum, 0, null);

        Assert.assertEquals(expectedToString, accum.toString());
    }
}