package org.junit.tests;

import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNameMethod() {
        String name = "xml";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, null, false);
        String actualName = xmlDeclaration.name();
        Assert.assertEquals(name, actualName);
    }

    @Test
    public void testGetWholeDeclarationNoAttributes() {
        String name = "xml";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, null, false);
        String actualDeclaration = xmlDeclaration.getWholeDeclaration();
        String expectedDeclaration = name;
        Assert.assertEquals(expectedDeclaration, actualDeclaration);
    }

    @Test
    public void testGetWholeDeclarationWithAttributes() {
        String name = "xml";
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, null, false);
        xmlDeclaration.setAttributes(attributes);
        String actualDeclaration = xmlDeclaration.getWholeDeclaration();
        String expectedDeclaration = String.format("<%s version=\"1.0\">", name);
        Assert.assertEquals(expectedDeclaration, actualDeclaration);
    }

    @Test(expected = NullPointerException.class)
    public void testGetWholeDeclarationWithNullName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
        xmlDeclaration.getWholeDeclaration();
    }

    @Test
    public void testConstructorWithValidArguments() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        String actualName = xmlDeclaration.name();
        Assert.assertEquals(name, actualName);
        String actualBaseUri = xmlDeclaration.baseUri();
        Assert.assertEquals(baseUri, actualBaseUri);
        boolean actualProcessingInstruction = xmlDeclaration.isProcessingInstruction();
        Assert.assertEquals(isProcessingInstruction, actualProcessingInstruction);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullName() {
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;
        new XmlDeclaration(null, baseUri, isProcessingInstruction);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBaseUri() {
        String name = "xml";
        boolean isProcessingInstruction = true;
        new XmlDeclaration(name, null, isProcessingInstruction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidProcessingInstruction() {
        String name = "xml";
        String baseUri = "baseUri";
        new XmlDeclaration(name, baseUri, 3);
    }

    @Test
    public void testOuterHtmlHeadWithValidArguments() throws IOException {
        Appendable accum = new StringBuilder();
        String name = "xml";
        boolean isProcessingInstruction = true;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, null, isProcessingInstruction);
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        String actualDeclaration = accum.toString();
        String expectedDeclaration = String.format("<%s%s>", isProcessingInstruction ? "!" : "?", xmlDeclaration.getWholeDeclaration());
        Assert.assertEquals(expectedDeclaration, actualDeclaration);
    }

    @Test
    public void testOuterHtmlTailWithValidArguments() throws IOException {
        Appendable accum = new StringBuilder();
        String name = "xml";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, null, isProcessingInstruction);
        xmlDeclaration.outerHtmlTail(accum, 0, null);
        String actualDeclaration = accum.toString();
        Assert.assertEquals("", actualDeclaration);
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlHeadWithNullAccum() throws IOException {
        boolean isProcessingInstruction = true;
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, isProcessingInstruction);
        xmlDeclaration.outerHtmlHead(null, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlTailWithNullAccum() throws IOException {
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, isProcessingInstruction);
        xmlDeclaration.outerHtmlTail(null, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlHeadWithNullOut() throws IOException {
        boolean isProcessingInstruction = true;
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, isProcessingInstruction);
        xmlDeclaration.outerHtmlHead(null, 0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOuterHtmlHeadWithNegativeDepth() throws IOException {
        boolean isProcessingInstruction = true;
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, isProcessingInstruction);
        xmlDeclaration.outerHtmlHead(null, -1, null);
    }
}