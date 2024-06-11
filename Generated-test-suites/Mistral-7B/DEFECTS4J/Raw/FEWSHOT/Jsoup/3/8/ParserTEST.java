package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        assertEquals("Test", doc.title().text());
        assertEquals("Hello", doc.body().children().get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello</h1></body>";
        Parser parser = new Parser(bodyHtml, null, true);
        Document doc = parser.parse();
        assertEquals("Hello", doc.body().children().get(0).text());
        assertEquals(0, doc.head().childrenSize());
    }

    @Test
    public void testParseEmpty() {
        String html = "";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        assertNull(doc);
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html<title";
        Parser parser = new Parser(html, null, false);
        assertNull(parser.parse());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        Element comment = doc.body().child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[This is CDATA]]></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        Element cdata = doc.body().child(0);
        assertEquals("This is CDATA", cdata.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"bar\">";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        XmlDeclaration decl = doc.xml();
        assertEquals("xmlns:foo=\"bar\"", decl.xmlDecl().data());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello</p></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        assertEquals(1, doc.body().childrenSize());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1 id=\"title\">Hello</h1></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        Element title = doc.body().child(0);
        assertEquals("h1", title.tagName());
        assertEquals("Hello", title.text());
        assertEquals("title", title.id());
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src=\"image.jpg\" alt=\"Image\">";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        Element img = doc.body().child(0);
        List<Attribute> attributes = img.attributes();
        assertEquals(2, attributes.size());
        Attribute src = attributes.get(0);
        assertEquals("src", src.key());
        assertEquals("image.jpg", src.value());
        Attribute alt = attributes.get(1);
        assertEquals("alt", alt.key());
        assertEquals("Image", alt.value());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        TextNode text = (TextNode) doc.body().child(0).child(0);
        assertEquals("Hello World", text.text());
    }
}