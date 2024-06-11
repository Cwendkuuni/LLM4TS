package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.Validate;

import java.io.IOException;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclaration() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());

        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationProcessingInstruction() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#processing-instruction", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml?>", xmlDeclaration.getWholeDeclaration());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationNullName() {
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        new XmlDeclaration(null, baseUri, isProcessingInstruction);
    }

    @Test
    public void testXmlDeclarationOuterHtml() throws IOException {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        Document document = new Document();
        Element element = new Element("html");
        element.append(xmlDeclaration);

        Appendable accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, document.outputSettings());
        xmlDeclaration.outerHtmlTail(accum, 0, document.outputSettings());

        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"></xml>", xmlDeclaration.toString());
        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"></xml>", accum.toString());

        attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);

        xmlDeclaration.outerHtmlHead(accum, 0, document.outputSettings());
        xmlDeclaration.outerHtmlTail(accum, 0, document.outputSettings());

        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"></xml>", xmlDeclaration.toString());
        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"></xml>", accum.toString());
    }

    @Test
    public void testXmlDeclarationProcessingInstructionOuterHtml() throws IOException {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        Document document = new Document();
        Element element = new Element("html");
        element.append(xmlDeclaration);

        Appendable accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, document.outputSettings());
        xmlDeclaration.outerHtmlTail(accum, 0, document.outputSettings());

        assertEquals("<?xml?>", xmlDeclaration.toString());
        assertEquals("<?xml?>", accum.toString());
    }

    @Test
    public void testXmlDeclarationNullBaseUri() {
        String name = "xml";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, null, isProcessingInstruction);
        assertNull(xmlDeclaration.baseUri());
    }
}