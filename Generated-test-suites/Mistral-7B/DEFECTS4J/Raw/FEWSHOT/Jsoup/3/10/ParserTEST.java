package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser.Tag;

public class ParserTest {

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertTrue(doc.body() == null);
    }

    @Test
    public void testParseHtmlWithBody() {
        String html = "<html><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(bodyTag, doc.body().tagName());
        assertTrue(doc.head() == null);
    }

    @Test
    public void testParseHtmlWithHeadBody() {
        String html = "<html><head></head><body><p>Test</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(headTag, doc.head().tagName());
        assertEquals(bodyTag, doc.body().tagName());
        Element p = doc.body().getElementsByTag("p").get(0);
        assertEquals("Test", p.text());
    }

    @Test
    public void testParseHtmlWithTextNodes() {
        String html = "<html><body>Test 1 <b>Test 2</b> Test 3</body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element body = doc.body();
        TextNode textNode1 = (TextNode) body.child(0);
        assertEquals("Test 1", textNode1.text());
        Element b = body.getElementsByTag("b").get(0);
        TextNode textNode2 = (TextNode) b.child(0);
        assertEquals("Test 2", textNode2.text());
        TextNode textNode3 = (TextNode) body.child(2);
        assertEquals(" Test 3", textNode3.text());
    }

    @Test
    public void testParseHtmlWithAttributes() {
        String html = "<html><body class='foo' id='bar'><p title='Hello'></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element body = doc.body();
        Attribute classAttr = body.attribute("class");
        assertEquals("foo", classAttr.getValue());
        Attribute idAttr = body.attribute("id");
        assertEquals("bar", idAttr.getValue());
        Element p = body.getElementsByTag("p").get(0);
        Attribute titleAttr = p.attribute("title");
        assertEquals("Hello", titleAttr.getValue());
    }

    @Test
    public void testParseHtmlWithComments() {
        String html = "<html><body><p>Test</p><!-- Comment --></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element comment = doc.body().getElementsByTag(Comment.class).get(0);
        assertEquals(" Comment ", comment.text());
    }

    @Test
    public void testParseHtmlWithDataNodes() {
        String html = "<html><body><script type='text/javascript'>function test() { alert('Hello'); }</script></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        DataNode scriptNode = (DataNode) doc.body().getElementsByTag(DataNode.class).get(0);
        assertEquals("function test() { alert('Hello'); }", scriptNode.text());
    }

    @Test
    public void testParseHtmlWithImplicitParents() {
        String html = "<html><body><img src='image.png'></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element img = doc.body().getElementsByTag("img").get(0);
        Element implicitParent = img.parent();
        assertEquals("div", implicitParent.tagName()); // implicit parent is div
        Element div = doc.body().getElementsByTag("div").get(0);
        Element explicitParent = img.parent(div.tagName());
        assertEquals(div, explicitParent); // explicit parent is the div element
    }

    @Test
    public void testParseHtmlWithBaseTag() {
        String html = "<html><head><base href='/base'><title>Test</title></head><body><a href='child.html'>Child</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("/base/child.html", doc.select("a[href]").text());
    }

    @Test
    public void testParseHtmlWithCdataSection() {
        String html = "<html><body><script><![CDATA[function test() { alert('Hello'); }]]></script></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        DataNode cdataNode = (DataNode) doc.body().getElementsByTag(DataNode.class).get(0);
        assertEquals("function test() { alert('Hello'); }", cdataNode.text());
    }
}