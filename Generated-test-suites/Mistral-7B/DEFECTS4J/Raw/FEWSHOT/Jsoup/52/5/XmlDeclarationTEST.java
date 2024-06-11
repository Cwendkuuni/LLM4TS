package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndProcessingInstructionFalse() {
        String baseUri = "baseUri";
        String name = "xml";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals("<?xml>\n", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndProcessingInstructionTrue() {
        String baseUri = "baseUri";
        String name = "xml";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals("<?xml>\n", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndAttributes() {
        String baseUri = "baseUri";
        String name = "xml";
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false, attributes);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\">\n", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNameOnlyAndProcessingInstructionFalse() {
        String name = "xml";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertNull(xmlDeclaration.baseUri());
        assertEquals("<?xml>\n", xmlDeclaration.getWholeDeclaration());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullName() {
        String baseUri = "baseUri";
        String name = null;
        new XmlDeclaration(name, baseUri, false);
    }

    @Test
    public void testXmlDeclarationToString() {
        String name = "xml";
        String baseUri = "baseUri";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
        assertEquals("<?xml>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationOuterHtmlHead() throws IOException {
        String name = "xml";
        String baseUri = "baseUri";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
        Appendable accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        assertEquals("<?xml>\n", accum.toString());
    }

    @Test
    public void testXmlDeclarationOuterHtmlTail() throws IOException {
        String name = "xml";
        String baseUri = "baseUri";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
        Appendable accum = new StringBuilder();
        xmlDeclaration.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testXmlDeclarationWithProcessingInstructionTrueAndAttributes() {
        String baseUri = "baseUri";
        String name = "processingInstruction";
        Attributes attributes = new Attributes();
        attributes.put("target", "foo");
        attributes.put("data", "data");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, true, attributes);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals("<?" + name + " " + attributes.toString() + ">\n", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithProcessingInstructionTrueAndNoAttributes() {
        String name = "processingInstruction";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, null, true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertNull(xmlDeclaration.baseUri());
        assertEquals("<?" + name + ">\n", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithDeclaration() {
        String name = "declaration";
        String baseUri = "baseUri";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals("<?" + name + ">\n", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithDeclarationAndAttributes() {
        String name = "declaration";
        String baseUri = "baseUri";
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false, attributes);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals("<?" + name + " version=\"1.0\" encoding=\"UTF-8\">\n", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithDeclarationAndProcessingInstructionFalse() {
        String name = "declaration";
        String baseUri = "baseUri";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals("<?" + name + ">\n", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithDeclarationAndProcessingInstructionTrue() {
        String name = "declaration";
        String baseUri = "baseUri";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals("<?" + name + "?" + ">\n", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithAttributesAndProcessingInstructionFalse() {
        String name = "declaration";
        String baseUri = "baseUri";
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false, attributes);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals("<?" + name + " version=\"1.0\" encoding=\"UTF-8\">\n", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithAttributesAndProcessingInstructionTrue() {
        String name = "declaration";
        String baseUri = "baseUri";
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, true, attributes);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals("<?" + name + " " + attributes.toString() + "?>\n", xmlDeclaration.getWholeDeclaration());
    }
}