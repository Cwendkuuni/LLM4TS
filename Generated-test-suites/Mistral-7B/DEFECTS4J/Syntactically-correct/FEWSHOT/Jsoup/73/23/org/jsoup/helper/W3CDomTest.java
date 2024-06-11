package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.w3c.dom.Attribute;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.ParserConfigurationException;

public class W3CDomTest {

    @Test
    public void testFromJsoupEmptyDocument() {
        org.jsoup.nodes.Document input = org.jsoup.parser.Parser.parseEmpty();
        Document output = new W3CDom().fromJsoup(input);
        assertNotNull(output);
        assertEquals("<document/>", asString(output));
    }

    @Test
    public void testFromJsoupSingleElementDocument() {
        org.jsoup.nodes.Document input = org.jsoup.parser.Parser.parseBodyFragment("<p>Hello World</p>");
        Document output = new W3CDom().fromJsoup(input);
        assertNotNull(output);
        assertEquals("<p>Hello World</p>", asString(output));
    }

    @Test
    public void testFromJsoupMultipleElementDocument() {
        org.jsoup.nodes.Document input = org.jsoup.parser.Parser.parseBodyFragment("<html><body><p>Hello World</p><p>Foo Bar</p></body></html>");
        Document output = new W3CDom().fromJsoup(input);
        assertNotNull(output);
        assertEquals("<html><body><p>Hello World</p><p>Foo Bar</p></body></html>", asString(output));
    }

    @Test
    public void testConvertDocument() {
        org.jsoup.nodes.Document input = org.jsoup.parser.Parser.parseBodyFragment("<html><body><p>Hello World</p><p>Foo Bar</p></body></html>");
        Document doc = new Document();
        new W3CDom().convert(input, doc);
        assertNotNull(doc);
        assertEquals("<html><body><p>Hello World</p><p>Foo Bar</p></body></html>", asString(doc));
    }

    @Test
    public void testConvertDocumentWithLocation() {
        org.jsoup.nodes.Document input = org.jsoup.parser.Parser.parseBodyFragment("<html><body><p>Hello World</p><p>Foo Bar</p></body></html>");
        Document doc = new Document();
        new W3CDom().convert(input, doc);
        doc.setDocumentURI("http://example.com");
        assertEquals("http://example.com", doc.getDocumentURI());
    }

    @Test
    public void testW3CBuilder() {
        org.jsoup.nodes.Document input = org.jsoup.parser.Parser.parseBodyFragment("<html><body><p id='id1'>Hello World</p><p class='class1'>Foo Bar</p></body></html>");
        Document doc = new Document();
        new W3CDom().convert(input, doc);
        Element root = doc.getDocumentElement();
        assertEquals("html", root.getTagName());
        Element body = root.getElementsByTagName("body").item(0);
        assertEquals("body", body.getTagName());
        Element p1 = body.getElementsByTagName("p").item(0);
        assertEquals("p", p1.getTagName());
        assertEquals("Hello World", p1.getTextContent());
        assertEquals("class1", p1.getClassAttribute());
        Element p2 = body.getElementsByTagName("p").item(1);
        assertEquals("p", p2.getTagName());
        assertEquals("Foo Bar", p2.getTextContent());
    }

    @Test
    public void testW3CBuilderWithNamespaces() {
        org.jsoup.nodes.Document input = org.jsoup.parser.Parser.parseBodyFragment("<html xmlns:xhtml='http://www.w3.org/1999/xhtml'><body><p xmlns:foo='http://example.com' class='class1'>Hello World</p></body></html>");
        Document doc = new Document();
        new W3CDom().convert(input, doc);
        Element root = doc.getDocumentElement();
        assertEquals("html", root.getTagName());
        Element body = root.getElementsByTagName("body").item(0);
        assertEquals("body", body.getTagName());
        Element p = body.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "p").item(0);
        assertEquals("p", p.getTagName());
        assertEquals("class1", p.getClassAttribute());
        assertEquals("http://example.com", p.getNamespaceURI());
        assertEquals("foo", p.getPrefix());
    }

    @Test
    public void testAsString() {
        Document doc = new Document();
        Element p = doc.createElement("p");
        p.setTextContent("Hello World");
        doc.appendChild(p);
        String result = new W3CDom().asString(doc);
        assertEquals("<p>Hello World</p>", result);
    }

    private String asString(Document doc) {
        return new W3CDom().asString(doc);
    }
}