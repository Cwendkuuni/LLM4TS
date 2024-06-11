package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com/";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";

        String baseUri = "https://www.example.com/";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World</h2>";

        String baseUri = "https://www.example.com/";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parse(html, baseUri);

        Comment comment = (Comment) doc.childNodes().get(0);
        assertEquals(" This is a comment ", comment.getData());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parse(html, baseUri);

        XmlDeclaration decl = (XmlDeclaration) doc.childNodes().get(0);
        assertEquals("xml", decl.tagName());
        assertEquals("1.0", decl.attr("version"));
        assertEquals("UTF-8", decl.attr("encoding"));
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body>Hello World</body></html>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parse(html, baseUri);

        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parse(html, baseUri);

        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body>Hello World</body></html>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parse(html, baseUri);

        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[<h1>Hello World</h1>]]>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parse(html, baseUri);

        TextNode textNode = (TextNode) doc.childNodes().get(0);
        assertEquals("<h1>Hello World</h1>", textNode.text());
    }

    @Test
    public void testAddChildToParent() {
        String html = "<body><div><p>Hello World</p></div></body>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parse(html, baseUri);

        Element body = doc.body();
        Element div = body.select("div").first();
        assertEquals("<p>Hello World</p>", div.html());
    }

    @Test
    public void testStackHasValidParent() {
        String html1 = "<body><div></div></body>";
        String baseUri1 = "https://www.example.com/";

        Document doc1 = Parser.parse(html1, baseUri1);
        Element div1 = doc1.select("div").first();
        assertTrue(div1.tag().isValidParent(bodyTag));

        String html2 = "<head><div></div></head>";
        String baseUri2 = "https://www.example.com/";

        Document doc2 = Parser.parse(html2, baseUri2);
        Element div2 = doc2.select("div").first();
        assertFalse(div2.tag().isValidParent(headTag));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        String html = "<body><div><p>Hello World</p></div></body>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parse(html, baseUri);

        Element body = doc.body();
        Element div = body.select("div").first();

        Element parent = div.parent();
        assertEquals(parent, body);
    }

    @Test
    public void testPopStackToClose() {
        String html = "<html><head><title>Title</title></head><body><div><p>Hello World</p></div></body></html>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parse(html, baseUri);

        Element body = doc.body();
        assertEquals(2, body.childNodes().size());

        Element htmlElement = doc.select("html").first();
        assertEquals(1, htmlElement.childNodes().size());
    }

    @Test
    public void testLast() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parse(html, baseUri);

        Element h1 = doc.select("h1").first();
        assertEquals(h1, doc.body().childNodes().get(0));
    }

}