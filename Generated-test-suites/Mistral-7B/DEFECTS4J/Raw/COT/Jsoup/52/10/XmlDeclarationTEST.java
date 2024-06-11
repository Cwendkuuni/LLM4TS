package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;

import java.io.IOException;

/**
 * Test class for XmlDeclaration.
 */
public class XmlDeclarationTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertFalse(xmlDeclaration.isProcessingInstruction());
    }

    /**
     * Test getWholeDeclaration method with empty attributes.
     */
    @Test
    public void testGetWholeDeclarationEmptyAttributes() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        String expected = name;
        String actual = xmlDeclaration.getWholeDeclaration();
        assertEquals(expected, actual);
    }

    /**
     * Test getWholeDeclaration method with version attribute.
     */
    @Test
    public void testGetWholeDeclarationVersionAttribute() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
        String expected = name + " version=\"1.0\"";
        String actual = xmlDeclaration.getWholeDeclaration();
        assertEquals(expected, actual);
    }

    /**
     * Test getWholeDeclaration method with encoding attribute.
     */
    @Test
    public void testGetWholeDeclarationEncodingAttribute() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        Attributes attributes = new Attributes();
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
        String expected = name + " encoding=\"UTF-8\"";
        String actual = xmlDeclaration.getWholeDeclaration();
        assertEquals(expected, actual);
    }

    /**
     * Test getWholeDeclaration method with version and encoding attributes.
     */
    @Test
    public void testGetWholeDeclarationVersionAndEncodingAttributes() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
        String expected = name + " version=\"1.0\" encoding=\"UTF-8\"";
        String actual = xmlDeclaration.getWholeDeclaration();
        assertEquals(expected, actual);
    }

    /**
     * Test getWholeDeclaration method with processing instruction.
     */
    @Test
    public void testGetWholeDeclarationProcessingInstruction() {
        String name = "";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        String expected = "<?xml>";
        String actual = xmlDeclaration.getWholeDeclaration();
        assertEquals(expected, actual);
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        String expected = "<declaration xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" version=\"1.0\" encoding=\"UTF-8\"/>";
        String actual = xmlDeclaration.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test outerHtmlHead method with empty attributes.
     */
    @Test
    public void testOuterHtmlHeadEmptyAttributes() throws IOException {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        Appendable accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        xmlDeclaration.outerHtmlHead(accum, 0, out);
        String expected = "<declaration xmlns:xml=\"http://www.w3.org/XML/1998/namespace\">";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test outerHtmlHead method with version attribute.
     */
    @Test
    public void testOuterHtmlHeadVersionAttribute() throws IOException {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
        Appendable accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        xmlDeclaration.outerHtmlHead(accum, 0, out);
        String expected = "<declaration xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" version=\"1.0\">";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test outerHtmlHead method with encoding attribute.
     */
    @Test
    public void testOuterHtmlHeadEncodingAttribute() throws IOException {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        Attributes attributes = new Attributes();
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
        Appendable accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        xmlDeclaration.outerHtmlHead(accum, 0, out);
        String expected = "<declaration xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" encoding=\"UTF-8\">";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test outerHtmlHead method with version and encoding attributes.
     */
    @Test
    public void testOuterHtmlHeadVersionAndEncodingAttributes() throws IOException {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
        Appendable accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        xmlDeclaration.outerHtmlHead(accum, 0, out);
        String expected = "<declaration xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" version=\"1.0\" encoding=\"UTF-8\">";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test outerHtmlHead method with processing instruction.
     */
    @Test
    public void testOuterHtmlHeadProcessingInstruction() throws IOException {
        String name = "";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        Appendable accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        xmlDeclaration.outerHtmlHead(accum, 0, out);
        String expected = "<?xml>";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test outerHtmlTail method.
     */
    @Test
    public void testOuterHtmlTail() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        // outerHtmlTail method is empty
    }

    /**
     * Test constructor with null name.
     * Expected exception: IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullName() {
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        String name = null;
        new XmlDeclaration(name, baseUri, isProcessingInstruction);
    }

    /**
     * Test constructor with null baseUri.
     * Expected exception: IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullBaseUri() {
        String name = "xml";
        boolean isProcessingInstruction = false;
        String baseUri = null;
        new XmlDeclaration(name, baseUri, isProcessingInstruction);
    }

    /**
     * Test constructor with null isProcessingInstruction.
     * Expected exception: NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorNullIsProcessingInstruction() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = null;
        new XmlDeclaration(name, baseUri, isProcessingInstruction);
    }

    /**
     * Test constructor with empty attributes.
     */
    @Test
    public void testConstructorEmptyAttributes() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        Attributes attributes = new Attributes();
        new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
    }

    /**
     * Test constructor with null attributes.
     * Expected exception: NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorNullAttributes() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        Attributes attributes = null;
        new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
    }

    /**
     * Test constructor with invalid version attribute.
     * Expected exception: IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidVersionAttribute() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        Attributes attributes = new Attributes();
        attributes.put("version", "invalid");
        new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
    }

    /**
     * Test constructor with invalid encoding attribute.
     * Expected exception: IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidEncodingAttribute() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        Attributes attributes = new Attributes();
        attributes.put("encoding", "invalid");
        new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
    }

}