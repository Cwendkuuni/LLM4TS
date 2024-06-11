package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import java.io.IOException;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndBaseUri() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNameProcessingInstructionAndBaseUri() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNameVersionEncodingAndBaseUri() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\"></declaration>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationToString() throws IOException {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
        assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\"></declaration>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationOuterHtmlHead() throws IOException {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);

        Document document = new Document();
        Element element = document.createElement("html");
        element.appendChild(xmlDeclaration);

        Appendable accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, null);

        assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\"></declaration>", accum.toString());
    }

    @Test
    public void testXmlDeclarationOuterHtmlTail() throws IOException {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);

        Document document = new Document();
        Element element = document.createElement("html");
        element.appendChild(xmlDeclaration);

        Appendable accum = new StringBuilder();
        xmlDeclaration.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testNullName() {
        try {
            new XmlDeclaration(null, "", false);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            new XmlDeclaration(null, "http://example.com", false);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testNullBaseUri() {
        String name = "xml";
        try {
            new XmlDeclaration(name, null, false);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testNullAttributes() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        Attributes attributes = null;

        try {
            new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testEmptyAttributes() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        Attributes attributes = new Attributes();

        assertEquals("<declaration></declaration>", xmlDeclaration.getWholeDeclaration());
    }
}