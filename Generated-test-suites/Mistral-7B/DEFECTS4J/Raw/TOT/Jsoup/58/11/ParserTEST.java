import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;
    private ParseSettings settings;

    @Before
    public void setup() {
        treeBuilder = new HtmlTreeBuilder();
        settings = treeBuilder.defaultSettings();
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInputHtml() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        Element h1 = doc.selectFirst("h1");
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseInputHtmlWithTrackingErrors() {
        Parser parserWithErrors = new Parser(new HtmlTreeBuilder());
        parserWithErrors.setTrackErrors(1);

        String html = "<html><body><script>alert('Hi')</script></body></html>";
        String baseUri = "http://example.com";
        Document doc = parserWithErrors.parseInput(html, baseUri);

        List<ParseError> errors = parserWithErrors.getErrors();
        assertEquals(1, errors.size());
        assertTrue(errors.get(0) instanceof ScriptError);
    }

    @Test
    public void testParseInputHtmlWithNoTrackingErrors() {
        String html = "<html><body></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertTrue(doc.body() == null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullHtml() {
        String html = null;
        String baseUri = "http://example.com";
        parser.parseInput(html, baseUri);
    }

    @Test
    public void testParseInputEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        assertTrue(doc.body() == null);
    }

    @Test
    public void testParseInputHtmlWithInvalidTag() {
        String html = "<tag></tag>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        assertTrue(doc instanceof org.jsoup.nodes.Document);
        assertEquals(0, doc.body().childNodes().size());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTreeBuilderNull() {
        Parser parser = new Parser(treeBuilder);
        parser.setTreeBuilder(null);
    }

    @Test
    public void testGetErrorsEmpty() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();

        assertTrue(errors.isEmpty());
    }

    @Test
    public void testGetErrorsWithErrors() {
        Parser parserWithErrors = new Parser(new HtmlTreeBuilder());
        parserWithErrors.setTrackErrors(1);

        String html = "<html><body><script>alert('Hi')</script></body></html>";
        String baseUri = "http://example.com";
        Document doc = parserWithErrors.parseInput(html, baseUri);

        List<ParseError> errors = parserWithErrors.getErrors();
        assertEquals(1, errors.size());
        assertTrue(errors.get(0) instanceof ScriptError);
    }

    @Test
    public void testSettings() {
        ParseSettings settings = parser.settings();
        assertNotNull(settings);
    }
}