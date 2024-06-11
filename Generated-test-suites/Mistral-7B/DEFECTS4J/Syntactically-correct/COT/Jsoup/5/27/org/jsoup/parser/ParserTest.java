package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XML;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><div><p>Hello World</p></div></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.getRootElement();
        Assert.assertEquals("html", root.tagName());

        Element title = root.getElementsByTag("title").first();
        Assert.assertEquals("Test", title.text());

        Element div = root.getElementsByTag("div").first();
        Element p = div.getElementsByTag("p").first();
        Assert.assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        Element root = doc.getRootElement();
        Assert.assertEquals("body", root.tagName());

        Element div = root.getElementsByTag("div").first();
        Element p = div.getElementsByTag("p").first();
        Assert.assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        Element root = doc.getRootElement();
        Assert.assertEquals("body", root.tagName());

        Element div = root.getElementsByTag("div").first();
        Element p = div.getElementsByTag("p").first();
        Assert.assertEquals("Hello World", p.text());

        // Test for unknown self-closed tag
        doc = Parser.parseBodyFragmentRelaxed("<div/><unknown></unknown>", "http://example.com");
        root = doc.getRootElement();
        Assert.assertEquals("body", root.tagName());
        Element unknown = root.getElementsByTag("unknown").first();
        Assert.assertNotNull(unknown);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><div><p>Hello World</p></div></body></html>";
        Parser.parse(html, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithNullBodyHtml() {
        Parser.parseBodyFragment(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithNullBaseUri() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        Parser.parseBodyFragment(bodyHtml, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentRelaxedWithNullBodyHtml() {
        Parser.parseBodyFragmentRelaxed(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentRelaxedWithNullBaseUri() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        Parser.parseBodyFragmentRelaxed(bodyHtml, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyString() {
        Parser.parse("", "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithEmptyString() {
        Parser.parseBodyFragment("", "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentRelaxedWithEmptyString() {
        Parser.parseBodyFragmentRelaxed("", "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyList() {
        Parser parser = new Parser("");
        LinkedList<Element> stack = parser.stack;
        Assert.assertTrue(stack.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithEmptyList() {
        Parser parser = new Parser("");
        Document doc = parser.parse();
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.getRootElement().tagName().isEmpty());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<declare xml version='1.0' encoding='UTF-8'?>" +
                "<html><head><title>Test</title></head><body><div><p>Hello World</p></div></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.getRootElement();
        XML xmlDecl = root.getElementsByTag("declare").first();
        Assert.assertNotNull(xmlDecl);
        Assert.assertEquals("1.0", xmlDecl.attribute("version").toString());
        Assert.assertEquals("UTF-8", xmlDecl.attribute("encoding").toString());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.getRootElement();
        Comment comment = root.getElementsByTag("comment").first();
        Assert.assertNotNull(comment);
        Assert.assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><head><title>Test</title></head></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.getRootElement();
        Element title = root.getElementsByTag("title").first();
        Assert.assertNotNull(title);
        Element head = root.getElementsByTag("head").first();
        Parser.parseEndTag(head.tagName(), stack -> {
            Element last = stack.getLast();
            Assert.assertEquals("head", last.tagName());
        });
        Assert.assertNull(title);
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title></head><body><div><p>Hello World</p></div></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.getRootElement();
        Element title = root.getElementsByTag("title").first();
        Assert.assertNotNull(title);
        Element div = root.getElementsByTag("div").first();
        Parser.parseStartTag(new Element(htmlTag, "http://example.com"), stack -> {
            Element last = stack.getLast();
            Assert.assertEquals("html", last.tagName());
        });
        Parser.parseStartTag(new Element(headTag, "http://example.com"), stack -> {
            Element last = stack.getLast();
            Assert.assertEquals("head", last.tagName());
        });
        Parser.parseStartTag(new Element(titleTag, "http://example.com"), stack -> {
            Element last = stack.getLast();
            Assert.assertEquals("title", last.tagName());
            Assert.assertEquals("Test", last.text());
        });
        Parser.parseStartTag(new Element(bodyTag, "http://example.com"), stack -> {
            Element last = stack.getLast();
            Assert.assertEquals("body", last.tagName());
        });
        Parser.parseStartTag(new Element(divTag, "http://example.com"), stack -> {
            Element last = stack.getLast();
            Assert.assertEquals("div", last.tagName());
        });
        Parser.parseStartTag(new Element(pTag, "http://example.com"), stack -> {
            Element last = stack.getLast();
            Assert.assertEquals("p", last.tagName());
        });
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href='http://example.com'>Link</a>";
        Document doc = Parser.parse(html, "http://example.com");
        Element link = doc.getElementsByTag("a").first();
        Attribute hrefAttr = link.attributes().get("href");
        Assert.assertEquals("http://example.com", hrefAttr.toString());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Hello World</p>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.getRootElement();
        Element p = root.getElementsByTag("p").first();
        TextNode textNode = (TextNode) p.childNodes().get(0);
        Assert.assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[alert('Hello World')]]></script>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.getRootElement();
        Element script = root.getElementsByTag("script").first();
        DataNode dataNode = (DataNode) script.childNodes().get(0);
        Assert.assertEquals("alert('Hello World')", dataNode.data());
    }

    @Test
    public void testParseWithUnknownTag() {
        String html = "<unknown></unknown>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.getRootElement();
        Element unknown = root.getElementsByTag("unknown").first();
        Assert.assertNotNull(unknown);
    }

    @Test
    public void testParseWithEmptyTag() {
        String html = "<img />";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.getRootElement();
        Element img = root.getElementsByTag("img").first();
        Assert.assertNotNull(img);
        Assert.assertEquals(0, img.childNodes().size());
    }

    @Test
    public void testParseWithSelfClosedTag() {
        String html = "<p>Hello World</p>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.getRootElement();
        Element p = root.getElementsByTag("p").first();
        Assert.assertEquals(1, p.childNodes().size());
    }

    @Test
    public void testParseWithImplicitParent() {
        String html = "<img src='image.png' alt='Image' />";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.getRootElement();
        Element img = root.getElementsByTag("img").first();
        Element div = new Element(Tag.valueOf("div"), "http://example.com");
        Element p = new Element(Tag.valueOf("p"), "http://example.com");
        div.appendChild(p);
        p.appendChild(img);
        Element parent = addChildToParent(div, false);
        Assert.assertEquals(3, parent.childNodes().size());
    }

    private Element addChildToParent(Element child, boolean isEmptyElement) {
        Element parent = popStackToSuitableContainer(child.tag());
        return parent.appendChild(child);
    }

    @Test
    public void testStackHasValidParent() {
        String html = "<html><head><title>Test</title></head><body><div><p>Hello World</p></div></body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        LinkedList<Element> stack = parser.stack;
        Assert.assertTrue(stackHasValidParent(Tag.valueOf("div"), stack.getLast()));
        Assert.assertTrue(stackHasValidParent(Tag.valueOf("p"), stack.getLast()));
        Assert.assertTrue(stackHasValidParent(Tag.valueOf("title"), stack.getLast()));
        Assert.assertTrue(stackHasValidParent(Tag.valueOf("head"), stack.getLast()));
        Assert.assertTrue(stackHasValidParent(Tag.valueOf("body"), stack.getLast()));
        Assert.assertTrue(stackHasValidParent(Tag.valueOf("html"), stack.getLast()));
        Assert.assertFalse(stackHasValidParent(Tag.valueOf("p"), stack.getLast(2)));
    }

    private boolean stackHasValidParent(Tag childTag, Element lastElement) {
        for (int i = lastElement.getParents().size() - 1; i >= 0; i--) {
            Element parent = lastElement.getParent(i);
            if (parent.tag().isValidAncestor(childTag)) {
                return true;
            }
        }
        return false;
    }
}