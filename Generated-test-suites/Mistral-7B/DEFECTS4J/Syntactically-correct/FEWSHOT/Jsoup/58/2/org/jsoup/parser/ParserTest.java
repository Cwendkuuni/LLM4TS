package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TreeBuilder;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(1);

        Document document = parser.parseInput(html, null);
        assertNotNull(document);
        assertEquals(1, document.childNodes().size());
        assertEquals("Hello World", document.childNodes().get(0).text());

        List<ParseError> parseErrors = parser.getErrors();
        assertSame(errors, parser.getErrors());
        assertEquals(0, parseErrors.size());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><h1>Hello <script>World</script></h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(2);

        Document document = parser.parseInput(html, null);
        assertNotNull(document);
        assertEquals(1, document.childNodes().size());
        assertEquals("Hello ", document.childNodes().get(0).text());

        List<ParseError> parseErrors = parser.getErrors();
        assertEquals(2, parseErrors.size());
        ParseError firstError = parseErrors.get(0);
        assertEquals("Tag 'script' not allowed in this context", firstError.getMessage());
        assertEquals(10, firstError.getLine());
        assertEquals(12, firstError.getColumn());
        ParseError secondError = parseErrors.get(1);
        assertEquals("Error parsing 'script' tag: Unexpected '(', expected '>'", secondError.getMessage());
        assertEquals(10, secondError.getLine());
        assertEquals(16, secondError.getColumn());
    }

    @Test
    public void testParseInputWithErrorsDisabled() {
        String html = "<html><body><h1>Hello <script>World</script></h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());

        Document document = parser.parseInput(html, null);
        assertNotNull(document);
        assertEquals(1, document.childNodes().size());
        assertEquals("Hello <script>World</script>", document.childNodes().get(0).text());

        List<ParseError> parseErrors = parser.getErrors();
        assertNull(parseErrors);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);

        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testParseStaticMethods() {
        String html = "<html><body><h1>Hello World</h1></body></html>";

        Document document = Parser.parse(html, null);
        assertNotNull(document);
        assertEquals(1, document.childNodes().size());
        assertEquals("Hello World", document.childNodes().get(0).text());

        Element context = Jsoup.parse("<div></div>").body();
        List<Node> nodes = Parser.parseFragment(html, context, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertSame(context.child(0), nodes.get(0));

        String xml = "<root><element>Hello World</element></root>";
        List<Node> xmlNodes = Parser.parseXmlFragment(xml, null);
        assertNotNull(xmlNodes);
        assertEquals(1, xmlNodes.size());
        assertEquals("Hello World", xmlNodes.get(0).text());

        Document bodyDocument = Parser.parseBodyFragment("<body>Hello World</body>", null);
        assertNotNull(bodyDocument);
        assertEquals("Hello World", bodyDocument.body().text());

        String unescapedString = Parser.unescapeEntities("&lt;Hello &amp; World&gt;", false);
        assertEquals("<Hello & World>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxedDeprecated() {
        String html = "<body>Hello World</body>";
        Document document = Parser.parseBodyFragmentRelaxed(html, null);
        assertNotNull(document);
        assertEquals("Hello World", document.body().text());
    }
}