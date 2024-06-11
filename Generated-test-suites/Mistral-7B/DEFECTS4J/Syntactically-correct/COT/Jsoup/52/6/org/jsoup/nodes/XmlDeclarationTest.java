package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndIsProcessingInstructionFalse() {
        String name = "xml";
        String baseUri = "http://www.example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        String expectedName = "#declaration";
        String expectedDeclaration = "xml version=\"\" encoding=\"\"";

        assertEquals(expectedName, xmlDeclaration.nodeName());
        assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndIsProcessingInstructionTrue() {
        String name = "xml";
        String baseUri = "http://www.example.com";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        String expectedName = "#declaration";
        String expectedDeclaration = "<?xml>";

        assertEquals(expectedName, xmlDeclaration.nodeName());
        assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullName() {
        String baseUri = "http://www.example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, baseUri, isProcessingInstruction);

        // Null pointer exception should be thrown
    }

    @Test
    public void testGetWholeDeclarationWithVersion() {
        String name = "xml";
        String baseUri = "http://www.example.com";
        boolean isProcessingInstruction = false;
        String version = "1.0";

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("version", version);

        String expectedDeclaration = "xml version=\"1.0\" encoding=\"\"";

        assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithEncoding() {
        String name = "xml";
        String baseUri = "http://www.example.com";
        boolean isProcessingInstruction = false;
        String encoding = "UTF-8";

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("encoding", encoding);

        String expectedDeclaration = "xml version=\"\" encoding=\"UTF-8\"";

        assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithVersionAndEncoding() {
        String name = "xml";
        String baseUri = "http://www.example.com";
        boolean isProcessingInstruction = false;
        String version = "1.0";
        String encoding = "UTF-8";

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("version", version);
        xmlDeclaration.attributes().put("encoding", encoding);

        String expectedDeclaration = "xml version=\"1.0\" encoding=\"UTF-8\"";

        assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithEmptyName() {
        String baseUri = "http://www.example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration("", baseUri, isProcessingInstruction);

        String expectedName = "#declaration";
        String expectedDeclaration = "";

        assertEquals(expectedName, xmlDeclaration.nodeName());
        assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNoAttributes() {
        String name = "xml";
        String baseUri = "http://www.example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        String expectedName = "#declaration";
        String expectedDeclaration = "xml";

        assertEquals(expectedName, xmlDeclaration.nodeName());
        assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationToString() {
        String name = "xml";
        String baseUri = "http://www.example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        String expectedDeclaration = "<declaration xmlns:xml=\"http://www.w3.org/XML/1998/namespace\">xml</declaration>";

        String actualDeclaration = xmlDeclaration.toString();

        assertEquals(expectedDeclaration, actualDeclaration);
    }
}