package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head/><body><div><p>Hello, <world>!</p></div></body></html>";
        Document doc = Parser.parse(html, "https://example.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(3, doc.childNodes().size());
        assertEquals(headTag, doc.head().tagName());
        assertEquals(bodyTag, doc.body().tagName());

        assertEquals(1, doc.body().childNodes().size());
        assertEquals(divTag, doc.body().childNodes().get(0).tagName());
        assertEquals(1, doc.body().childNodes().get(0).childNodes().size());
        assertEquals(pTag, doc.body().childNodes().get(0).childNodes().get(0).tagName());
        assertEquals(2, doc.body().childNodes().get(0).childNodes().get(0).childNodes().size());
        assertEquals("Hello, ", doc.body().childNodes().get(0).childNodes().get(0).childNodes().get(0).text());
        assertEquals("world!", doc.body().childNodes().get(0).childNodes().get(0).childNodes().get(1).text());

        // test attributes
        Element div = doc.body().childNodes().get(0);
        Attribute attr = div.attributes().get("class");
        assertEquals("", attr.getValue());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello, <world>!</p></div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.tagName());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(divTag, doc.body().childNodes().get(0).tagName());
        assertEquals(1, doc.body().childNodes().get(0).childNodes().size());
        assertEquals(pTag, doc.body().childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("Hello, ", doc.body().childNodes().get(0).childNodes().get(0).childNodes().get(0).text());
        assertEquals("world!", doc.body().childNodes().get(0).childNodes().get(0).childNodes().get(1).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello, <world>!</p></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "https://example.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.tagName());
        assertEquals(1, doc.body().childNodes().size());
        Element div = doc.body().childNodes().get(0);
        assertEquals("div", div.tagName());
        assertEquals(1, div.childNodes().size());
        assertEquals("p", div.childNodes().get(0).tagName());
        assertEquals("Hello, ", div.childNodes().get(0).childNodes().get(0).text());
        Element world = div.childNodes().get(0).childNodes().get(1);
        assertEquals("world!", world.tagName());
        assertNull(world.childNodes());
    }

    @Test
    public void testParseEmpty() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).tagName());
        assertEquals(0, doc.childNodes().get(0).childNodes().size());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body><div<p>"; // missing closing tag
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(divTag, doc.body().childNodes().get(0).tagName());
        assertEquals(1, doc.body().childNodes().get(0).childNodes().size());
        assertTrue(doc.body().childNodes().get(0).childNodes().get(0).toString().contains("Error parsing source"));
    }

    @Test
    public void testParseScriptTag() {
        String html = "<html><body><script>function test() { return 1; }</script></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.body().childNodes().size());
        Element script = doc.body().childNodes().get(0);
        assertEquals("script", script.tagName());
        assertEquals(1, script.childNodes().size());
        DataNode dataNode = (DataNode) script.childNodes().get(0);
        assertEquals("function test() { return 1; }", dataNode.data());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(pTag, doc.body().childNodes().get(0).tagName());
        assertEquals("Hello World", doc.body().childNodes().get(0).childNodes().get(0).text());
        Element comment = doc.body().childNodes().get(0).getElementsByTag("comment").get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[ <script>function test() { return 1; }</script> ]]></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.body().childNodes().size());
        Element cdata = doc.body().childNodes().get(0);
        assertEquals("script", cdata.tagName());
        assertEquals("function test() { return 1; }", cdata.data());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:xlink=\"http://www.w3.org/1999/xlink\">";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.attributes().hasKey("xmlns:xlink"));
        Attribute xmlns = doc.attributes().get("xmlns:xlink");
        assertEquals("http://www.w3.org/1999/xlink", xmlns.getValue());
    }

    @Test
    public void testParseEmptyTag() {
        String html = "<html><body><img></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.body().childNodes().size());
        Element img = doc.body().childNodes().get(0);
        assertEquals("img", img.tagName());
        assertEquals(0, img.childNodes().size());
    }

    @Test
    public void testParseSelfClosingTag() {
        String html = "<html><body><img src=\"test.png\" /></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.body().childNodes().size());
        Element img = doc.body().childNodes().get(0);
        assertEquals("img", img.tagName());
        assertEquals(0, img.childNodes().size());
    }

    @Test
    public void testParseTextNodeWithAttributes() {
        String html = "<html><body><p class=\"highlight\" data-test=\"passed\">Hello World</p></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.body().childNodes().size());
        Element p = doc.body().childNodes().get(0);
        assertEquals("p", p.tagName());
        assertEquals(2, p.childNodes().size());
        TextNode textNode = (TextNode) p.childNodes().get(0);
        assertEquals("Hello World", textNode.text());
        assertEquals("highlight", p.classes().get(0).getValue());
        assertEquals("passed", p.attributes().get("data-test").getValue());
    }

    @Test
    public void testParseTextNodeWithSpecialChars() {
        String html = "<html><body>&lt;p&gt;Hello &amp; World&lt;/p&gt;</body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.body().childNodes().size());
        Element p = doc.body().childNodes().get(0);
        assertEquals("p", p.tagName());
        assertEquals(1, p.childNodes().size());
        TextNode textNode = (TextNode) p.childNodes().get(0);
        assertEquals("&lt;p&gt;Hello &amp; World&lt;/p&gt;", textNode.text());
        assertEquals("Hello & World", textNode.textAfterEscapes());
    }

    @Test
    public void testParseTextNodeWithEmptyAndSpecialChars() {
        String html = "<html><body><p>&lt;p&gt;&amp; &nbsp; Hello &amp; World &amp; &lt;/p&gt;</p></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.body().childNodes().size());
        Element p = doc.body().childNodes().get(0);
        assertEquals("p", p.tagName());
        assertEquals(1, p.childNodes().size());
        TextNode textNode = (TextNode) p.childNodes().get(0);
        assertEquals("&lt;p&gt;&amp; &nbsp; Hello &amp; World &amp; &lt;/p&gt;", textNode.text());
        assertEquals("&lt;p&gt;&nbsp;Hello & World", textNode.textAfterEscapes());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/base\"></head><body><a href=\"test.html\">Go to test.html</a></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.body().childNodes().size());
        Element a = doc.body().childNodes().get(0);
        assertEquals("a", a.tagName());
        assertEquals(1, a.childNodes().size());
        assertEquals("Go to test.html", a.text());
        assertEquals("/base/test.html", a.absUrl("href"));
    }
}