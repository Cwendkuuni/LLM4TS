package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.documentElementTag());
        assertEquals("Test Page", doc.title());
        assertEquals(1, doc.body().childNodeSize());
        assertEquals(1, doc.head().childNodeSize());

        Element h1 = doc.body().get(0).asHtml();
        assertEquals("h1", h1.tagName());
        assertEquals("Hello World", h1.text());

        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals(htmlTag, doc.documentElementTag());
        assertEquals("", doc.title());
        assertEquals(1, doc.body().childNodeSize());

        Element h1 = doc.body().get(0).asHtml();
        assertEquals("h1", h1.tagName());
        assertEquals("Hello World", h1.text());

        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.documentElementTag());
        assertEquals("", doc.title());
        assertEquals(0, doc.body().childNodeSize());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- this is a comment --></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(1, doc.body().childNodes().size());
        Element comment = doc.body().get(0).asComment();
        assertEquals("this is a comment", comment.data());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[this is cdata]]><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(1, doc.body().childNodes().size());
        Element cdata = doc.body().get(0).asText();
        assertEquals("this is cdata", cdata.text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>This is a text node</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element p = doc.body().get(0);
        assertEquals(1, p.childNodeSize());
        TextNode textNode = p.get(0).asText();
        assertEquals("This is a text node", textNode.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p><b>This is a <i>test</i></b></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element p = doc.body().get(0);
        assertEquals(3, p.childNodeSize());
        Element b = p.get(0).asHtml();
        assertEquals("b", b.tagName());
        Element i = b.get(0).asHtml();
        assertEquals("i", i.tagName());
        assertEquals("test", i.text());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p><b>This is a <i>test</i></b></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element p = doc.body().get(0);
        Element b = p.get(0).asHtml();
        assertEquals(1, b.childNodeSize());
        Element i = b.get(0).asHtml();
        assertEquals(1, i.childNodeSize());
        TextNode textNode = i.get(0).asText();
        assertEquals("test", textNode.text());

        assertEquals(1, p.childNodeSize());
    }

    @Test
    public void testParseAttributes() {
        String html = "<html><body><div id='myDiv' class='myClass' title='My Title'></div></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element div = doc.body().get(0).asHtml();
        assertEquals("div", div.tagName());
        Map<String, String> attributes = div.attributes();
        assertEquals(3, attributes.size());
        assertEquals("myDiv", attributes.get("id"));
        assertEquals("myClass", attributes.get("class"));
        assertEquals("My Title", attributes.get("title"));
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href='/mypath'></base></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("http://example.com/mypath", doc.baseUri());
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<html><body><img src='image.png'></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element img = doc.body().get(0).asHtml();
        assertEquals("img", img.tagName());
        assertEquals("image.png", img.absUrl("src"));

        Element div = new Element(Tag.valueOf("div"), "http://example.com");
        div.appendChild(img);
        Element parent = Parser.parseBodyFragment("<div><img src='image.png'></div>", "http://example.com").body();
        assertEquals(div, parent);
    }

    @Test
    public void testParseInvalidStartTag() {
        String html = "<html><body><div<script>alert('Hello')</script></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(1, doc.body().childNodeSize());
        Element div = doc.body().get(0).asHtml();
        assertEquals("div", div.tagName());
        assertEquals(0, div.childNodeSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullBaseUri() {
        Parser.parse("<html></html>", null);
    }
}