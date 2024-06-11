package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Test class for XmlDeclaration.
 */
public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationConstructor() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertNotNull(xmlDeclaration);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertNull(xmlDeclaration.getWholeDeclaration());

        XmlDeclaration processingInstruction = new XmlDeclaration("xml", null, true);
        assertNotNull(processingInstruction);
        assertEquals("#declaration", processingInstruction.nodeName());
        assertEquals("xml", processingInstruction.name());
        assertNotNull(processingInstruction.getWholeDeclaration());
    }

    @Test
    public void testEmptyXmlDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertNull(xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");

        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false, attributes);
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test(expected = NullPointerException.class)
    public void testNullNameInXmlDeclaration() {
        new XmlDeclaration(null, null, false);
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUriInXmlDeclaration() {
        new XmlDeclaration("xml", null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyNameInXmlDeclaration() {
        new XmlDeclaration("", null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationWithInvalidName() {
        new XmlDeclaration("aXmL", null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationWithDuplicateAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("version", "1.1");

        new XmlDeclaration("xml", null, false, attributes);
    }

    @Test
    public void testXmlDeclarationToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("<xml/>", xmlDeclaration.toString());

        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");

        xmlDeclaration = new XmlDeclaration("xml", null, false, attributes);
        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"/>", xmlDeclaration.toString());
    }

    @Test(expected = IOException.class)
    public void testOuterHtmlHeadThrowsIOException() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Document document = null;
        Element element = new Element("root");
        element.appendChild(xmlDeclaration);
        document = new Document();
        document.setRootElement(element);
        xmlDeclaration.outerHtmlHead(null, 0, null);
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Document document = new Document();
        xmlDeclaration.outerHtmlHead(document.body(), 0, document.outputSettings());
        assertEquals("<xml/>", document.body().html());

        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        xmlDeclaration = new XmlDeclaration("xml", null, false, attributes);
        document = new Document();
        xmlDeclaration.outerHtmlHead(document.body(), 0, document.outputSettings());
        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"/>", document.body().html());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Document document = new Document();
        xmlDeclaration.outerHtml(document.body(), 0, document.outputSettings());
        assertEquals("<xml/>", document.body().html());

        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        xmlDeclaration = new XmlDeclaration("xml", null, false, attributes);
        document = new Document();
        xmlDeclaration.outerHtml(document.body(), 0, document.outputSettings());
        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"/>", document.body().html());
    }

    @Test
    public void testXmlDeclarationIsProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertFalse(xmlDeclaration.isProcessingInstruction());

        XmlDeclaration processingInstruction = new XmlDeclaration("xml", null, true);
        assertTrue(processingInstruction.isProcessingInstruction());
    }

}