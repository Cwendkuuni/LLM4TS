package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ParserTest {

    @Test
    public void testParseEmptyHtml() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.body();
        Assert.assertNull(root);

        Element htmlTag = doc.head();
        Assert.assertNotNull(htmlTag);
        Assert.assertEquals("html", htmlTag.tagName());
    }

    @Test
    public void testParseSimpleHtml() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element root = doc.body();
        Element p = root.getElementsByTag("p").get(0);
        Assert.assertEquals("Hello World", p.text());

        Element title = doc.head().getElementsByTag("title").get(0);
        Assert.assertEquals("Test", title.text());
    }

    @Test
    public void testParseFragment() {
        String html = "<html><body><p>Hello</p></body></html>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");

        Element root = doc;
        Element p = root.getElementsByTag("p").get(0);
        Assert.assertEquals("Hello", p.text());
    }

    @Test
    public void testParseError() {
        String html = "<html><body><script>alert('Hello')</script></body></html>";
        try {
            Document doc = Parser.parseBodyFragment(html, "http://example.com");
            Assert.fail();
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testParseAttributes() {
        String html = "<html><head><title id='title'>Test</title></head><body><img src='image.png' alt='Image'></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element title = doc.head().getElementsByTag("title").get(0);
        Attributes titleAttributes = title.attributes();
        Map<String, String> titleMap = new HashMap<>();
        titleMap.put("id", "title");
        Assert.assertEquals(titleMap, titleAttributes.asMap());

        Element img = doc.body().getElementsByTag("img").get(0);
        Attributes imgAttributes = img.attributes();
        Map<String, String> imgMap = new HashMap<>();
        imgMap.put("src", "image.png");
        imgMap.put("alt", "Image");
        Assert.assertEquals(imgMap, imgAttributes.asMap());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href='/foo'></head><body><a href='bar.html'>Go to bar.html</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element base = doc.getElementsByTag("base").get(0);
        Assert.assertEquals("/foo", base.absUrl("href"));
        Assert.assertEquals("http://example.com/foo/bar.html", doc.getElementsByTag("a").get(0).absUrl("href"));
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[Hello World]]></body></html>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");

        Element root = doc;
        TextNode textNode = (TextNode) root.child(0);
        Assert.assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element root = doc;
        Comment comment = (Comment) root.child(0);
        Assert.assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo='http://example.com'><head></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element root = doc;
        XmlDeclaration xmlDeclaration = (XmlDeclaration) root.child(0);
        Assert.assertEquals("xmlns:foo", xmlDeclaration.xmlnsAttribute().key());
        Assert.assertEquals("http://example.com", xmlDeclaration.xmlnsAttribute().value());
    }

    @Test
    public void testParseFragmentWithScript() {
        String html = "<html><body><script>document.write('Hello World')</script><script src='script.js'></script></body></html>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");

        Element root = doc;
        TextNode textNode = (TextNode) root.child(0);
        Assert.assertEquals("Hello World", textNode.text());

        Element script = root.getElementsByTag("script").get(1);
        Assert.assertEquals("script.js", script.absUrl("src"));
    }

    @Test
    public void testParseFragmentWithEmptyScript() {
        String html = "<html><body><script></script></body></html>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");

        Element root = doc;
        TextNode textNode = (TextNode) root.child(0);
        Assert.assertTrue("", textNode.text().isEmpty());
    }

    @Test
    public void testParseWithImplicitParents() {
        String html = "<html><body><ul><li>Item 1</li><li>Item 2</li></ul></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element root = doc.body();
        Element ul = root.getElementsByTag("ul").get(0);
        List<Element> liList = ul.getElementsByTag("li");
        for (Element li : liList) {
            Assert.assertEquals("li", li.tagName());
        }
    }
}