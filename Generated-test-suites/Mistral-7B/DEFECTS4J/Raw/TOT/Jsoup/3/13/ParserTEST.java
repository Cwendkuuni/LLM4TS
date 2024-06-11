package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseEmptyHtml() {
        String html = StringUtils.EMPTY;
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.body();
        Assert.assertNotNull(root);
        Assert.assertEquals(html.length(), root.text().length());
    }

    @Test
    public void testParseHtmlWithTextAndSimpleTags() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1><p>This is a paragraph.</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element root = doc;
        Element title = root.head().title();
        Element h1 = root.body().getElementsByTag("h1").first();
        Element p = root.body().getElementsByTag("p").first();

        Assert.assertEquals("Test Page", title.text());
        Assert.assertEquals("Hello World", h1.text());
        Assert.assertEquals("This is a paragraph.", p.text());
    }

    @Test
    public void testParseHtmlWithComplexTags() {
        String html = "<html><head><title>Test Page</title></head><body>" +
                "<div id=\"content\">" +
                "<ul><li>Item 1</li><li><a href=\"http://example.com\">Link</a></li></ul>" +
                "<script type=\"text/javascript\">alert('Hello, World!');</script>" +
                "</div>" +
                "</body></html>";

        Document doc = Parser.parse(html, "http://example.com");

        Element root = doc;
        Element title = root.head().title();
        Element div = root.body().getElementsByTag("div").first();
        Element ul = div.getElementsByTag("ul").first();
        Element li1 = ul.getElementsByTag("li").get(0);
        Element liLink = ul.getElementsByTag("li").get(1);
        Element a = liLink.getElementsByTag("a").first();

        Assert.assertEquals("Test Page", title.text());
        Assert.assertEquals("Item 1", li1.text());
        Assert.assertEquals("Link", a.text());
        Assert.assertEquals("http://example.com", a.absUrl("href"));
    }

    @Test
    public void testParseHtmlWithSelfClosingTags() {
        String html = "<img src=\"image.png\" alt=\"Image\"/>";
        Document doc = Parser.parse(html, "http://example.com");

        Element root = doc;
        Element img = root.body().getElementsByTag("img").first();

        Assert.assertEquals("image.png", img.absUrl("src"));
        Assert.assertEquals("Image", img.attr("alt"));
    }

    @Test
    public void testParseHtmlWithError() {
        String html = "<html><head><title>Test Page</title></head><body><script>throw new Error(\"Test\");</script>";

        try {
            Document doc = Parser.parse(html, "http://example.com");
            // This should not happen
            Assert.fail();
        } catch (RuntimeException e) {
            // Tests that the parse method throws an exception when it encounters a script error
        }
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><p>This is a paragraph.</p></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element root = doc;
        Element p = root.getElementsByTag("p").first();

        Assert.assertEquals("This is a paragraph.", p.text());
    }

    @Test
    public void testParseBodyFragmentWithError() {
        String bodyHtml = "<script>throw new Error(\"Test\");</script>";

        try {
            Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
            // This should not happen
            Assert.fail();
        } catch (RuntimeException e) {
            // Tests that the parseBodyFragment method throws an exception when it encounters a script error
        }
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[This is some CDATA.]]></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element root = doc;
        Element body = root.body();
        Element cdata = body.getElementsByTag("cdata").first();

        Assert.assertEquals("This is some CDATA.", cdata.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"http://example.com/foo\" xml:lang=\"en\">";
        Document doc = Parser.parse(html, "http://example.com");

        Element root = doc;
        XmlDeclaration xmlDeclaration = root.xml();

        Assert.assertEquals("http://example.com/foo", xmlDeclaration.xmlns("foo"));
        Assert.assertEquals("en", xmlDeclaration.xmlLang());
    }

    @Test
    public void testParseRelativeUrls() {
        String html = "<a href=\"images/image.png\">Image</a>";
        Document doc = Parser.parse(html, "http://example.com/index.html");

        Element root = doc;
        Element a = root.getElementsByTag("a").first();

        String href = a.absUrl("href");
        List<String> expected = Arrays.asList("/images/image.png", "http://example.com/images/image.png");
        Assert.assertTrue(expected.contains(href));
    }

    @Test
    public void testParseBaseHref() {
        String html = "<base href=\"/images/\">" +
                "<html><body><a href=\"image.png\">Image</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com/index.html");

        Element root = doc;
        Element base = root.head().getElementsByTag("base").first();
        Element a = root.body().getElementsByTag("a").first();

        String baseHref = base.absUrl("href");
        String href = a.absUrl("href");

        Assert.assertEquals("/images/image.png", href);
        Assert.assertEquals("/images/image.png", baseHref);
    }
}