package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>";
        String baseUri = "http://www.example.com";

        Document document = Parser.parse(html, baseUri);

        Element body = document.body();
        Assert.assertNotNull(body);

        Element title = document.selectFirst("title");
        Assert.assertNotNull(title);
        Assert.assertEquals("Test Title", title.text());

        Element heading = document.selectFirst("h1");
        Assert.assertNotNull(heading);
        Assert.assertEquals("Test Heading", heading.text());

        Element paragraph = document.selectFirst("p");
        Assert.assertNotNull(paragraph);
        Assert.assertEquals("Test Paragraph", paragraph.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test Heading</h1><p>Test Paragraph</p>";
        String baseUri = "http://www.example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Element body = document.body();
        Assert.assertNotNull(body);

        Element heading = document.selectFirst("h1");
        Assert.assertNotNull(heading);
        Assert.assertEquals("Test Heading", heading.text());

        Element paragraph = document.selectFirst("p");
        Assert.assertNotNull(paragraph);
        Assert.assertEquals("Test Paragraph", paragraph.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Test Heading</h1><p>Test Paragraph";
        String baseUri = "http://www.example.com";

        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Element body = document.body();
        Assert.assertNotNull(body);

        Element heading = document.selectFirst("h1");
        Assert.assertNotNull(heading);
        Assert.assertEquals("Test Heading", heading.text());

        Element paragraph = document.selectFirst("p");
        Assert.assertNotNull(paragraph);
        Assert.assertEquals("Test Paragraph", paragraph.text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- Test Comment -->";
        String baseUri = "http://www.example.com";

        Document document = Parser.parse(html, baseUri);

        Node comment = document.childNodes().get(0);
        Assert.assertEquals("<!-- Test Comment -->", comment.outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://www.example.com";

        Document document = Parser.parse(html, baseUri);

        Node xmlDecl = document.childNodes().get(0);
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDecl.outerHtml());
    }

    @Test
    public void testParseEndTag() {
        String html = "<p>Test Paragraph</p>";
        String baseUri = "http://www.example.com";

        Document document = Parser.parse(html, baseUri);

        Element paragraph = document.selectFirst("p");
        Assert.assertNotNull(paragraph);
        Assert.assertEquals("Test Paragraph", paragraph.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<h1>Test Heading</h1>";
        String baseUri = "http://www.example.com";

        Document document = Parser.parse(html, baseUri);

        Element heading = document.selectFirst("h1");
        Assert.assertNotNull(heading);
        Assert.assertEquals("Test Heading", heading.text());
    }

    @Test
    public void testParseTextNode() {
        String html = "This is a test.";
        String baseUri = "http://www.example.com";

        Document document = Parser.parse(html, baseUri);

        Node textNode = document.childNodes().get(0);
        Assert.assertEquals("This is a test.", textNode.outerHtml());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[<p>Test Paragraph</p>]]>";
        String baseUri = "http://www.example.com";

        Document document = Parser.parse(html, baseUri);

        Node cdataNode = document.childNodes().get(0);
        Assert.assertEquals("<![CDATA[<p>Test Paragraph</p>]]>", cdataNode.outerHtml());
    }

    @Test
    public void testParseAttribute() {
        String html = "<div id=\"testDiv\" class=\"testClass\"></div>";
        String baseUri = "http://www.example.com";

        Document document = Parser.parse(html, baseUri);

        Element div = document.selectFirst("div");
        Assert.assertNotNull(div);
        Assert.assertEquals("testDiv", div.attr("id"));
        Assert.assertEquals("testClass", div.attr("class"));
    }

    @Test
    public void testStackHasValidParent() {
        Tag childTag = Tag.valueOf("div");

        Parser parser = new Parser("<html><body><div></div></body></html>", "http://www.example.com", false);
        parser.parseStartTag();

        boolean result = parser.stackHasValidParent(childTag);

        Assert.assertTrue(result);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Tag tag = Tag.valueOf("div");

        Parser parser = new Parser("<html><body><span></span></body></html>", "http://www.example.com", false);
        parser.parseStartTag();

        Element parent = parser.popStackToSuitableContainer(tag);

        Assert.assertNotNull(parent);
        Assert.assertEquals("body", parent.tagName());
    }

    @Test
    public void testPopStackToClose() {
        Tag tag = Tag.valueOf("div");

        Parser parser = new Parser("<html><body><span></span><div></div></body></html>", "http://www.example.com", false);
        parser.parseStartTag();

        Element element = parser.popStackToClose(tag);

        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.tagName());
    }

    @Test
    public void testLast() {
        Parser parser = new Parser("<html><body><span></span></body></html>", "http://www.example.com", false);
        parser.parseStartTag();

        Element element = parser.last();

        Assert.assertNotNull(element);
        Assert.assertEquals("span", element.tagName());
    }
}