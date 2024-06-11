package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Attributes.fromMap;
import static org.jsoup.nodes.Elements.html;
import static org.jsoup.nodes.Elements.text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head><body><div id=\"test\" class=\"test\"><p>Hello World</p></div></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals("Test Title", doc.title());
        Element root = doc.body();
        Element div = root.selectFirst("#test");
        List<Element> pList = div.children();
        assertEquals(1, pList.size());
        Element p = pList.get(0);
        assertEquals("Hello World", p.text());
        assertEquals("test", div.classes().get(0));
        assertEquals("http://test.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div id=\"test\">Hello World</div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        Element root = doc.body();
        Element div = root.selectFirst("#test");
        assertEquals("Hello World", div.text());
        assertEquals("http://test.com", doc.baseUri());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element comment = doc.selectFirst("comment()");
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><script><![CDATA[alert('Hello World')]]></script></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element script = doc.selectFirst("script");
        DataNode cdata = (DataNode) script.child(0);
        assertEquals("alert('Hello World')", cdata.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\">";
        Document doc = Parser.parse(html, "http://test.com");
        XmlDeclaration xmlDecl = ((Element) doc.head()).child(0);
        Map<String, String> xmlns = xmlDecl.xmlns();
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", xmlns.get("xsi"));
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlns.get("xml"));
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><div><p>Hello World</p></div></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.body();
        Element div = root.selectFirst("div");
        Element p = div.selectFirst("p");
        assertEquals("Hello World", p.text());
        Element last = doc.body().last();
        assertEquals("div", last.tag().name());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><div id=\"test\" class=\"test\"><p>Hello World</p></div></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.body();
        Element div = root.selectFirst("#test");
        List<Element> pList = div.children();
        assertEquals(1, pList.size());
        Element p = pList.get(0);
        assertEquals("Hello World", p.text());
        assertEquals("div", div.tag().name());
        assertEquals("test", div.classes().get(0));
        Element head = root.selectFirst("head");
        assertEquals(1, head.children().size());
        assertEquals("title", head.children().get(0).tag().name());
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><body><div id=\"test\" class=\"test\" title=\"Test Title\"></div></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element div = doc.body().selectFirst("#test");
        List<Attribute> attributes = div.attributes();
        assertEquals(3, attributes.size());
        Attribute idAttribute = attributes.get(0);
        assertEquals("id", idAttribute.key());
        assertEquals("test", idAttribute.value());
        Attribute classAttribute = attributes.get(1);
        assertEquals("class", classAttribute.key());
        assertEquals("test", classAttribute.value());
        Attribute titleAttribute = attributes.get(2);
        assertEquals("title", titleAttribute.key());
        assertEquals("Test Title", titleAttribute.value());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><div>Hello World</div></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.body();
        Element div = root.selectFirst("div");
        assertEquals("Hello World", div.text());
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<html><body><img src=\"image.jpg\"></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.body();
        Element img = root.selectFirst("img");
        assertEquals(1, img.parent().children().size());
        assertEquals("img", img.parent().tag().name());
    }

    @Test(expected = AssertionError.class)
    public void testParseInvalidTagOrder() {
        String html = "<html><body><div><p></html></div></body></html>";
        Parser.parse(html, "http://test.com");
    }
}