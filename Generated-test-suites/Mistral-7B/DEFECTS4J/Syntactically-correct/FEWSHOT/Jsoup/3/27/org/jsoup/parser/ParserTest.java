package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.jsoup.nodes.Attributes.fromMap;
import static org.jsoup.nodes.Tag.*;

import org.jsoup.nodes.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(htmlTag, htmlNode.tag());

        Element headNode = htmlNode.child(0);
        assertEquals(headTag, headNode.tag());
        assertTrue(headNode.childElements().isEmpty());

        Element bodyNode = htmlNode.child(1);
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        assertEquals(h1, h1Node.tag());
        assertEquals("Hello World", h1Node.text());

        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element htmlNode = doc;
        assertEquals(htmlTag, htmlNode.tag());

        Element bodyNode = htmlNode.child(0);
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        assertEquals(h1, h1Node.tag());
        assertEquals("Hello World", h1Node.text());

        assertEquals("http://example.com", doc.baseUri());
        assertTrue(doc.head().childElements().isEmpty());
    }

    @Test
    public void testParseComments() {
        String html = "<!-- This is a single line comment -->\n" +
                "<!--\n" +
                "This is a multi-line comment\n" +
                "-->";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(htmlTag, htmlNode.tag());

        Element commentNode1 = htmlNode.child(0);
        assertEquals(Comment.class, commentNode1.getClass());
        assertEquals("This is a single line comment", commentNode1.data());

        Element commentNode2 = htmlNode.child(1);
        assertEquals(Comment.class, commentNode2.getClass());
        assertEquals("This is a multi-line comment\n", commentNode2.data());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n" +
                "<html>\n" +
                "<head><title>Page Title</title></head>\n" +
                "<body>\n" +
                "</body>\n" +
                "</html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element xmlDeclNode = doc.head().child(0);
        assertEquals(XmlDeclaration.class, xmlDeclNode.getClass());
        assertEquals("about:legacy-compat", xmlDeclNode.absUrl("systemId"));
        assertFalse(xmlDeclNode.hasAttr("id"));

        Element htmlNode = doc.body();
        assertEquals(htmlTag, htmlNode.tag());
    }

    @Test
    public void testParseEndTag() {
        String html = "<div><p>This is a paragraph.</p></div>";
        Document doc = Parser.parse(html, "http://example.com");

        Element divNode = doc.body();
        assertEquals(bodyTag, divNode.tag());

        Element pNode = divNode.child(0);
        assertEquals(p, pNode.tag());
        assertEquals("This is a paragraph.", pNode.text());

        Element divClosingTag = divNode.child(1);
        assertEquals(div, divClosingTag.tag());
        assertTrue(divClosingTag.text().isEmpty());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div id='myDiv' class='myClass'><p>This is a paragraph.</p></div>";
        Document doc = Parser.parse(html, "http://example.com");

        Element divNode = doc.body();
        assertEquals(bodyTag, divNode.tag());

        Element divStartTag = divNode.child(0);
        assertEquals(div, divStartTag.tag());
        assertEquals("myDiv", divStartTag.attr("id"));
        assertEquals("myClass", divStartTag.attr("class"));
        assertTrue(divStartTag.childElements().isEmpty());

        Element pNode = divStartTag.child(1);
        assertEquals(p, pNode.tag());
        assertEquals("This is a paragraph.", pNode.text());
    }

    @Test
    public void testParseAttributes() {
        String html = "<input type='text' id='userName' name='username' value='joe'>";
        Document doc = Parser.parse(html, "http://example.com");

        Element inputNode = doc.body().child(0);
        assertEquals(input, inputNode.tag());

        Map<String, String> attributes = inputNode.attributes();
        assertEquals("text", attributes.get("type"));
        assertEquals("userName", attributes.get("id"));
        assertEquals("username", attributes.get("name"));
        assertEquals("joe", attributes.get("value"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>This is a paragraph.</p>";
        Document doc = Parser.parse(html, "http://example.com");

        Element pNode = doc.body().child(0);
        assertEquals(p, pNode.tag());

        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("This is a paragraph.", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[function myFunction() { alert('Hello World!'); }]]></script>";
        Document doc = Parser.parse(html, "http://example.com");

        Element scriptNode = doc.body().child(0);
        assertEquals(script, scriptNode.tag());

        TextNode cdataNode = (TextNode) scriptNode.child(0);
        assertEquals("function myFunction() { alert('Hello World!'); }", cdataNode.text());
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<img src='image.png'>";
        Document doc = Parser.parse(html, "http://example.com");

        Element bodyNode = doc.body();
        assertTrue(bodyNode.childElements().isEmpty());

        Element imgNode = bodyNode.child(0);
        assertEquals(img, imgNode.tag());

        assertEquals("http://example.com/image.png", imgNode.absUrl("src"));

        Element divNode = new Element(div, "http://example.com");
        divNode.appendChild(imgNode);

        Document docWithDiv = new Document("http://example.com");
        docWithDiv.setBaseUri("http://example.com");
        docWithDiv.appendChild(divNode);

        assertEquals(div, divNode.tag());
        assertEquals(1, divNode.childSize());
        assertEquals(img, divNode.child(0).tag());
    }
}