package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

public class ParserTest {

    private Parser htmlParser;
    private Parser xmlParser;
    private TreeBuilder htmlTreeBuilder;
    private TreeBuilder xmlTreeBuilder;

    @org.junit.Before
    public void setup() {
        htmlParser = Parser.htmlParser();
        xmlParser = Parser.xmlParser();
        htmlTreeBuilder = new HtmlTreeBuilder();
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @org.junit.Test
    public void testParseInput() {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div id=\"content\">\n" +
                "        <h1>Hello World</h1>\n" +
                "        <p>This is a test.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "https://example.com";

        Document doc = htmlParser.parseInput(html, baseUri);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());

        Element content = doc.getElementById("content");
        assertNotNull(content);
        assertEquals(2, content.childElementCount());
        assertEquals("Hello World", content.child(0).text());
        assertEquals("This is a test.", content.child(1).text());

        List<ParseError> errors = htmlParser.getErrors();
        assertNull(errors);
    }

    @org.junit.Test
    public void testParseInputWithErrors() {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"content\">\n" +
                "<h1><script>alert('Hello World')</script>Hello</h1>\n" +
                "<p>This is a test.</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "https://example.com";

        htmlParser.setTrackErrors(1);
        Document doc = htmlParser.parseInput(html, baseUri);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());

        List<ParseError> errors = htmlParser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected tag 'script'", error.getMessage());
        assertEquals(10, error.getLine());
        assertEquals(38, error.getColumn());
    }

    @org.junit.Test
    public void testParseInputWithMaxErrorsReached() {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"content\">\n" +
                "<h1><script>alert('Hello World')</script>Hello</h1>\n" +
                "<p>This is a test.</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "https://example.com";

        htmlParser.setTrackErrors(2);
        Document doc = htmlParser.parseInput(html, baseUri);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());

        List<ParseError> errors = htmlParser.getErrors();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        ParseError error1 = errors.get(0);
        assertEquals("Unexpected tag 'script'", error1.getMessage());
        assertEquals(10, error1.getLine());
        assertEquals(38, error1.getColumn());
        ParseError error2 = errors.get(1);
        assertEquals("Unexpected tag 'script'", error2.getMessage());
        assertEquals(10, error2.getLine());
        assertEquals(38, error2.getColumn());

        htmlParser.setTrackErrors(0);
        doc = htmlParser.parseInput(html, baseUri);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());

        errors = htmlParser.getErrors();
        assertNull(errors);
    }

    @org.junit.Test
    public void testParseInputWithCustomSettings() {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"content\">\n" +
                "<h1>Hello <a href=\"javascript:void(0)\">World</a></h1>\n" +
                "<p>This is a test.</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "https://example.com";

        ParseSettings settings = new ParseSettings();
        settings.setOmitScriptAndStyle();
        htmlParser = new Parser(htmlTreeBuilder);
        htmlParser.settings(settings);

        Document doc = htmlParser.parseInput(html, baseUri);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());

        Element content = doc.getElementById("content");
        assertNotNull(content);
        assertEquals(1, content.childElementCount());
        Element h1 = content.child(0);
        assertNotNull(h1);
        assertEquals(1, h1.childElementCount());
        TextNode textNode = (TextNode) h1.child(0);
        assertEquals("Hello ", textNode.text());
        assertEquals("World", h1.text());

        List<ParseError> errors = htmlParser.getErrors();
        assertNull(errors);
    }

    @org.junit.Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello World</h1>";
        Element context = new Element("div");

        List<Node> nodes = xmlParser.parseFragment(fragmentHtml, context, null);
        assertEquals(1, nodes.size());
        assertEquals("Hello World", nodes.get(0).text());

        nodes = htmlParser.parseFragment(fragmentHtml, context, null);
        assertEquals(1, nodes.size());
        assertEquals("Hello World", nodes.get(0).text());

        nodes = htmlParser.parseFragment(fragmentHtml, context, "https://example.com");
        assertEquals(1, nodes.size());
        assertEquals("Hello World", nodes.get(0).text());
        assertEquals("https://example.com", context.baseUri());
    }

    @org.junit.Test
    public void testParseXmlFragment() {
        String fragmentXml = "<book><title>XML Primer</title></book>";

        List<Node> nodes = xmlParser.parseXmlFragment(fragmentXml, null);
        assertEquals(1, nodes.size());
        assertEquals("XML Primer", nodes.get(0).text());
        assertTrue(nodes.get(0) instanceof Element);

        Element bookElement = (Element) nodes.get(0);
        assertEquals("book", bookElement.tagName());
        assertEquals(1, bookElement.childElementCount());
        assertEquals("title", bookElement.child(0).tagName());
    }

    @org.junit.Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body>\n" +
                "<div id=\"content\">\n" +
                "<h1>Hello World</h1>\n" +
                "<p>This is a test.</p>\n" +
                "</div>\n" +
                "</body>";

        Document doc = htmlParser.parseBodyFragment(bodyHtml, "https://example.com");
        assertEquals("<html lang=\"undefined\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"content\">\n" +
                "<h1>Hello World</h1>\n" +
                "<p>This is a test.</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>", doc.html());
        assertEquals("https://example.com", doc.baseUri());

        Element content = doc.getElementById("content");
        assertNotNull(content);
        assertEquals(2, content.childElementCount());
        assertEquals("Hello World", content.child(0).text());
        assertEquals("This is a test.", content.child(1).text());

        List<ParseError> errors = htmlParser.getErrors();
        assertNull(errors);
    }

    @org.junit.Test
    public void testUnescapeEntities() {
        String string = "&lt;hello&gt; world &amp; &quot; &apos; &eacute; &nbsp;";
        String unescapedString = Parser.unescapeEntities(string, false);
        assertEquals("<hello> world & & \" ' é ", unescapedString);

        string = "&lt;hello&gt; world &amp; &quot; &apos; &eacute; &nbsp;";
        unescapedString = Parser.unescapeEntities(string, true);
        assertEquals("<hello> world &amp; &quot; &apos; é ", unescapedString);
    }

    @org.junit.Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body>\n" +
                "<div id=\"content\">\n" +
                "<h1>Hello World</h1>\n" +
                "<p>This is a test.</p>\n" +
                "</div>\n" +
                "</body>";

        Document doc = htmlParser.parseBodyFragmentRelaxed(bodyHtml, "https://example.com");
        assertEquals("<html lang=\"undefined\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"content\">\n" +
                "<h1>Hello World</h1>\n" +
                "<p>This is a test.</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>", doc.html());
        assertEquals("https://example.com", doc.baseUri());

        Element content = doc.getElementById("content");
        assertNotNull(content);
        assertEquals(2, content.childElementCount());
        assertEquals("Hello World", content.child(0).text());
        assertEquals("This is a test.", content.child(1).text());

        List<ParseError> errors = htmlParser.getErrors();
        assertNull(errors);
    }

    @org.junit.Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertTrue(parser instanceof Parser);
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @org.junit.Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertTrue(parser instanceof Parser);
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }

    @org.junit.Test
    public void testParseInputNullHtml() {
        String baseUri = "https://example.com";

        List<ParseError> errors = htmlParser.parseInput("", baseUri);
        assertNull(errors);
        Document doc = htmlParser.parseInput("", baseUri);
        assertEquals("", doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @org.junit.Test
    public void testParseInputNullBaseUri() {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"content\">\n" +
                "<h1>Hello World</h1>\n" +
                "<p>This is a test.</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        List<ParseError> errors = htmlParser.parseInput(html, null);
        assertNull(errors);
        Document doc = htmlParser.parseInput(html, null);
        assertEquals(html, doc.html());
        assertNull(doc.baseUri());
    }
}