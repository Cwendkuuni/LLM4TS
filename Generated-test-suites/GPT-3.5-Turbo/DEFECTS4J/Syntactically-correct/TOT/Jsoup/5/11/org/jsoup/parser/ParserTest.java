package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);

        // Check if the document is not null
        Assert.assertNotNull(document);

        // Check if the title tag is correctly parsed
        Element title = document.head().getElementsByTag("title").first();
        Assert.assertNotNull(title);
        Assert.assertEquals("Test", title.text());

        // Check if the h1 tag is correctly parsed
        Element h1 = document.body().getElementsByTag("h1").first();
        Assert.assertNotNull(h1);
        Assert.assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>This is a paragraph.</p></div>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Check if the document is not null
        Assert.assertNotNull(document);

        // Check if the p tag is correctly parsed
        Element p = document.body().getElementsByTag("p").first();
        Assert.assertNotNull(p);
        Assert.assertEquals("This is a paragraph.", p.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><img src=\"image.jpg\"></div>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        // Check if the document is not null
        Assert.assertNotNull(document);

        // Check if the img tag is correctly parsed
        Element img = document.body().getElementsByTag("img").first();
        Assert.assertNotNull(img);
        Assert.assertEquals("image.jpg", img.attr("src"));
    }

    @Test
    public void testParseComment() {
        String html = "<p><!-- This is a comment --></p>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);

        // Check if the document is not null
        Assert.assertNotNull(document);

        // Check if the comment is correctly parsed
        Element p = document.body().getElementsByTag("p").first();
        Assert.assertNotNull(p);
        Assert.assertEquals(1, p.childNodes().size());

        TextNode comment = (TextNode) p.childNodes().get(0);
        Assert.assertEquals(" This is a comment ", comment.getWholeText());
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(xmlDecl, baseUri);

        // Check if the document is not null
        Assert.assertNotNull(document);

        // Check if the XML declaration is correctly parsed
        Assert.assertEquals(xmlDecl, document.toString());
    }

    @Test
    public void testParseEndTag() {
        String html = "<div><p>This is a paragraph.</p></div>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);

        // Check if the document is not null
        Assert.assertNotNull(document);

        // Check if the closing tag is correctly parsed
        Element p = document.body().getElementsByTag("p").first();
        Assert.assertNotNull(p);

        Element div = p.parent();
        Assert.assertNotNull(div);
        Assert.assertEquals("div", div.tagName());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div><p>This is a paragraph.</p></div>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);

        // Check if the document is not null
        Assert.assertNotNull(document);

        // Check if the opening tag is correctly parsed
        Element div = document.body().getElementsByTag("div").first();
        Assert.assertNotNull(div);

        Element p = div.getElementsByTag("p").first();
        Assert.assertNotNull(p);
        Assert.assertEquals("This is a paragraph.", p.text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>This is a <strong>bold</strong> text</p>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);

        // Check if the document is not null
        Assert.assertNotNull(document);

        // Check if the text node is correctly parsed
        Element p = document.body().getElementsByTag("p").first();
        Assert.assertNotNull(p);
        Assert.assertEquals(3, p.childNodes().size());

        TextNode textNode1 = (TextNode) p.childNodes().get(0);
        Assert.assertEquals("This is a ", textNode1.getWholeText());

        Element strong = (Element) p.childNodes().get(1);
        Assert.assertEquals("strong", strong.tagName());

        TextNode textNode2 = (TextNode) p.childNodes().get(2);
        Assert.assertEquals(" text", textNode2.getWholeText());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is a CDATA section]]>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);

        // Check if the document is not null
        Assert.assertNotNull(document);

        // Check if the CDATA section is correctly parsed
        Assert.assertEquals(html, document.toString());
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href=\"http://example.com\">Link</a>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);

        // Check if the document is not null
        Assert.assertNotNull(document);

        // Check if the attribute is correctly parsed
        Element a = document.body().getElementsByTag("a").first();
        Assert.assertNotNull(a);
        Assert.assertEquals("http://example.com", a.attr("href"));
        Assert.assertEquals("Link", a.text());
    }
}