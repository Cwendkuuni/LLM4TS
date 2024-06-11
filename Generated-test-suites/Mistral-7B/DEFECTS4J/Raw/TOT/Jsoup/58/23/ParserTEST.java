package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInputHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, "http://example.com");
        Element title = doc.title();
        Assert.assertEquals("Test", title.text());
        Assert.assertNotNull(doc.body());
        Assert.assertEquals("Hello World", doc.body().text());

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseInputHtmlWithErrors() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello <script>World</script></h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, "http://example.com");
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
    }

    @Test
    public void testParseInputXml() {
        String xml = "<root><element>Hello World</element></root>";
        Parser parser = new Parser(new XmlTreeBuilder());
        Document doc = parser.parseInput(xml, "http://example.com");
        Element element = (Element) doc.getElementsByTagName("element").get(0);
        Assert.assertEquals("Hello World", element.text());

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseInputXmlWithErrors() {
        String xml = "<root><element><script>Hello World</script></element></root>";
        Parser parser = new Parser(new XmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(xml, "http://example.com");
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
    }

    @Test
    public void testParseFragment() {
        String html = "<h1>Hello World</h1>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<Node> nodes = Parser.parseFragment(html, null, "http://example.com");
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        Assert.assertEquals("Hello World", element.text());

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseFragmentWithErrors() {
        String html = "<h1>Hello <script>World</script></h1>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        List<Node> nodes = Parser.parseFragment(html, null, "http://example.com");
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");
        Assert.assertEquals(1, doc.numChildren());
        Element body = doc.body();
        Assert.assertEquals("Hello World", body.text());

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseBodyFragmentWithErrors() {
        String html = "<body><h1>Hello <script>World</script></h1></body>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = Parser.parseBodyFragment(html, "http://example.com");
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&lt;p&gt;<i>&amp;lt;script&gt;</i>&amp;gt;Hello &amp;quot;World&amp;quot; &amp;#38;Hello&amp;#39;";
        String unescaped = Parser.unescapeEntities(html, false);
        String expected = "<p><i><script></i>Hello \"World\" &lt;Hello&gt;</p>";
        Assert.assertEquals(expected, unescaped);
    }
}