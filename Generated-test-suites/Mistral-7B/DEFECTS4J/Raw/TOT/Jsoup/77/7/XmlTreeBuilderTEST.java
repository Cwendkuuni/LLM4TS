package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXML() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(""), "baseUri");
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseSimpleXML() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root></root>"), "baseUri");
        Element root = doc.child(0);
        assertEquals("root", root.tagName());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseXMLWithTextNodes() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root><text>Hello World</text></root>"), "baseUri");
        Element root = doc.child(0);
        Element text = root.child(0);
        assertEquals("text", text.tagName());
        assertEquals("Hello World", text.text());
    }

    @Test
    public void testParseXMLWithElements() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root><child><grandChild></grandChild></child></root>"), "baseUri");
        Element root = doc.child(0);
        Element child = root.child(0);
        Element grandChild = child.child(0);
        assertEquals("child", child.tagName());
        assertEquals("grandChild", grandChild.tagName());
        assertEquals(0, grandChild.childNodes().size());
    }

    @Test
    public void testParseXMLWithComments() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root><!-- This is a comment --></root>"), "baseUri");
        Element root = doc.child(0);
        List<Node> comments = doc.getElementsByTag("!--");
        assertEquals(1, comments.size());
    }

    @Test
    public void testParseXMLWithSelfClosingTags() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root><img src=\"image.jpg\" /></root>"), "baseUri");
        Element img = doc.getElementsByTag("img").get(0);
        assertTrue(img.hasAttr("src"));
        assertEquals("image.jpg", img.attr("src"));
        assertEquals(0, img.childNodes().size());
    }

    @Test
    public void testParseXMLWithDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED > ]><root></root>"), "baseUri");
        DocumentType doctype = doc.doctype();
        assertEquals("root", doctype.nodeName());
        assertNotNull(doctype.getPublicIdentifier());
        assertNotNull(doctype.getSystemIdentifier());
    }

    @Test
    public void testParseXMLWithErrors() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        Document doc = null;
        try {
            doc = builder.parse(new StringReader("<root></root><!--"), "baseUri", errors, ParseSettings.defaultSettings());
            fail("Expected parse error");
        } catch (IOException e) {
            // expected
        }
        List<ParseError> parseErrors = errors.getErrors();
        assertEquals(1, parseErrors.size());
        assertEquals("Expecting: '>'\n", parseErrors.get(0).message());
    }
}