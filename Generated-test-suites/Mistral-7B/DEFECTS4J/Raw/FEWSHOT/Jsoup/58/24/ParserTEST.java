package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.nodes.DocType.html;
import static org.jsoup.nodes.Elements.doc;
import static org.jsoup.nodes.Elements.html;
import static org.jsoup.nodes.Elements.head;
import static org.jsoup.nodes.Elements.body;
import static org.jsoup.nodes.Elements.p;
import static org.jsoup.nodes.Attributes.id;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        String html = "<html><head></head><body><p id='test'>Test</p></body></html>";
        Document doc = parser.parseInput(html, null);

        assertEquals(htmlDoc(), doc.htmlDocument().documentElement());
        assertEquals(1, doc.body().childNodeSize());
        assertEquals("Test", doc.getElementById("test").text());

        // Test with errors tracking
        ParseErrorList errors = ParseErrorList.tracking(1);
        Document docWithError = parser.parseInput("<html><head></head><body><p id='test'>Test<script>alert('XSS')</script></p></body></html>", null);
        List<ParseError> parseErrors = parser.getErrors();
        assertEquals(1, parseErrors.size());
        assertEquals("Error parsing start tag : script", parseErrors.get(0).getMessage());
        assertEquals(htmlDoc(), docWithError.htmlDocument().documentElement());
        assertEquals(1, docWithError.body().childNodeSize());
        assertEquals("Test", docWithError.getElementById("test").text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(new HtmlTreeBuilder(), parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTreeBuilder(new XmlTreeBuilder());
        assertEquals(new XmlTreeBuilder(), parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        parser.setTrackErrors(2);
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());

        ParseErrorList errorList = ParseErrorList.tracking(1);
        parser.parseInput("<html><head></head><body><p id='test'>Test<script>alert('XSS')</script></p></body></html>", null);
        errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        assertEquals(new ParseSettings(), settings);

        parser.settings(new ParseSettings(true, false));
        ParseSettings newSettings = parser.settings();
        assertTrue(newSettings.isOmitXmlDeclaration());
        assertFalse(newSettings.isTrimEmptyText());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><head></head><body><p id='test'>Test</p></body></html>", null);
        assertEquals(htmlDoc(), doc.htmlDocument().documentElement());
        assertEquals(1, doc.body().childNodeSize());
        assertEquals("Test", doc.getElementById("test").text());
    }

    @Test
    public void testParseFragment() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Element context = doc().head();
        List<Node> nodes = parser.parseFragment("<p>Test</p>", context, null);
        assertEquals(1, nodes.size());
        assertEquals(p(), nodes.get(0));
        assertEquals("Test", nodes.get(0).text());
    }

    @Test
    public void testParseFragmentWithErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Element context = doc().head();
        ParseErrorList errors = ParseErrorList.tracking(1);
        List<Node> nodes = parser.parseFragment("<p>Test<script>alert('XSS')</script></p>", context, null, errors);
        assertEquals(1, nodes.size());
        assertEquals(p(), nodes.get(0));
        assertEquals("Test", nodes.get(0).text());
        List<ParseError> parseErrors = parser.getErrors();
        assertEquals(1, parseErrors.size());
        assertEquals("Error parsing start tag : script", parseErrors.get(0).getMessage());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<xml><book><title>Animal Farm</title></book></xml>", null);
        assertEquals(2, nodes.size());
        Element book = (Element) nodes.get(0);
        assertEquals("book", book.tagName());
        Element title = (Element) nodes.get(1);
        assertEquals("title", title.tagName());
        assertEquals("Animal Farm", title.text());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = parserBodyFragment("<p>Test</p>", null);
        assertEquals(1, doc.body().childNodeSize());
        assertEquals("Test", doc.body().child(0).text());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;p&gt;Test&amp; &nbsp; &eacute; &copy; &lt;script&gt;alert('XSS')&lt;/script&gt;";
        String unescapedString = Parser.unescapeEntities(string, false);
        assertEquals("<p>Test & &nbsp; é &copy; <script>alert('XSS')</script>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Test</p>", null);
        assertEquals(1, doc.body().childNodeSize());
        assertEquals("Test", doc.body().child(0).text());
    }

    private static Document htmlDoc() {
        return doc(html(), head(), body());
    }

    private static Document doc() {
        return new Document(html(), null, null);
    }
}