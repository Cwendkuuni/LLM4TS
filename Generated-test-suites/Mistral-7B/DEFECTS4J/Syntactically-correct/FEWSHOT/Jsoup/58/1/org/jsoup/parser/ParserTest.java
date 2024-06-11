package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.TreeBuilder;

public class ParserTest {

    @Test
    public void testParserInstance() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNotNull(parser);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        settings.setOmitXmlDeclaration(true);
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        assertEquals("Hello World", doc.head().text());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder getTreeBuilder = parser.getTreeBuilder();
        assertSame(newTreeBuilder, getTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsAndGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = parser.getErrors();
        assertNull(errors);

        parser.setTrackErrors(1);
        String html = "<html><body><h1>Hello World</h1></body></html>";
        ParseErrorList newErrors = parser.parseInput(html, null).getErrors();
        assertEquals(1, newErrors.size());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = parser.getErrors();
        assertNull(errors);

        String html = "<html><body><h1>Hello World</h1></body></html>";
        errors = parser.parseInput(html, null).getErrors();
        assertEquals(0, errors.size());

        parser.setTrackErrors(1);
        errors = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", null).getErrors();
        assertEquals(1, errors.size());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(html, null);
        assertEquals("Hello World", doc.head().text());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testParseFragment() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Element context = Jsoup.parse("<html></html>").body();
        List<Node> nodes = Parser.parseFragment(html, context, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Hello World", nodes.get(0).text());
    }

    @Test
    public void testParseXmlFragment() {
        String xml = "<xml><element>Hello World</element></xml>";
        List<Node> nodes = Parser.parseXmlFragment(xml, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Hello World", nodes.get(0).text());
    }

    @Test
    public void testParseInputWithErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(1);
        Document doc = parser.parseInput("<html><body><h1>Hello <b>World</b></h1></body></html>", null, errors,
                parser.settings());
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(2, error.getLine());
        assertEquals(6, error.getColumn());
        assertEquals("Expecting '>', but found: 'b'", error.getMessage());
    }

    @Test
    public void testParseInputWithMultipleErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(2);
        Document doc = parser.parseInput("<html><body><h1>Hello <b>World</b></h1></body></html>", null, errors,
                parser.settings());
        assertEquals(2, errors.size());
        List<ParseError> parseErrors = new ArrayList<>();
        parseErrors.addAll(errors);
        parseErrors.remove(parseErrors.size() - 1);
        ParseError error1 = parseErrors.get(0);
        assertEquals(2, error1.getLine());
        assertEquals(6, error1.getColumn());
        assertEquals("Expecting '>', but found: 'b'", error1.getMessage());
        ParseError error2 = parseErrors.get(0);
        assertEquals(3, error2.getLine());
        assertEquals(12, error2.getColumn());
        assertEquals("Expecting '>', but found: '<', but expected a closing '>'", error2.getMessage());
    }

    @Test
    public void testParseInputNoErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.noTracking();
        Document doc = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", null, errors,
                parser.settings());
        assertNull(errors);

        parser.setTrackErrors(1);
        errors = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", null).getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseInputWithValidation() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html></html>", null);
        assertEquals(0, doc.bodyChildren().size());

        parser.settings().setValidating(true);
        doc = parser.parseInput("<html></html>", null);
        assertEquals(0, doc.bodyChildren().size());

        parser.settings().setValidating(false);
        doc = parser.parseInput("<html></html>", null);
        assertEquals(0, doc.bodyChildren().size());

        parser.settings().setValidating(true);
        String invalidHtml = "<html><body><h1>Hello <b>World</b></h1></body></html>";
        doc = parser.parseInput(invalidHtml, null);
        List<ParseError> errors = doc.getErrors();
        assertEquals(1, errors.size());
        assertEquals(2, errors.get(0).getLine());
        assertEquals(6, errors.get(0).getColumn());
        assertEquals("Expecting '>', but found: 'b'", errors.get(0).getMessage());
    }

    @Test
    public void testParseInputWithEncoding() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><body><h1>Hello &auml; World</h1></body></html>", "UTF-8");
        assertEquals("Hello Ä World", doc.text());
    }

    @Test
    public void testParseInputWithBaseUri() {
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><body><img src=\"image.jpg\"></body></html>", baseUri);
        assertEquals("image.jpg", doc.baseUri());
    }

    @Test
    public void testParseInputWithErrorTracking() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(1);
        Document doc = parser.parseInput("<html><body><h1>Hello <b>World</b></h1></body></html>", null, errors,
                parser.settings());
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(2, error.getLine());
        assertEquals(6, error.getColumn());
        assertEquals("Expecting '>', but found: 'b'", error.getMessage());
    }

    @Test
    public void testParseInputWithMultipleErrorsAndErrorTracking() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(2);
        Document doc = parser.parseInput("<html><body><h1>Hello <b>World</b></h1></body></html>", null, errors,
                parser.settings());
        assertEquals(2, errors.size());
        List<ParseError> parseErrors = new ArrayList<>();
        parseErrors.addAll(errors);
        parseErrors.remove(parseErrors.size() - 1);
        ParseError error1 = parseErrors.get(0);
        assertEquals(2, error1.getLine());
        assertEquals(6, error1.getColumn());
        assertEquals("Expecting '>', but found: 'b'", error1.getMessage());
        ParseError error2 = parseErrors.get(0);
        assertEquals(3, error2.getLine());
        assertEquals(12, error2.getColumn());
        assertEquals("Expecting '>', but found: '<', but expected a closing '>'", error2.getMessage());
    }

    @Test
    public void testParseInputWithErrorTrackingAndValidation() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(1);
        Document doc = parser.parseInput("<html></html>", null, errors, parser.settings());
        assertNull(doc.bodyChildren());
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.getLine());
        assertEquals(1, error.getColumn());
        assertEquals("No element found", error.getMessage());
    }

    @Test
    public void testParseInputWithErrorTrackingAndValidationAndEncoding() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(1);
        Document doc = parser.parseInput("<html></html>", null, errors, parser.settings());
        assertNull(doc.bodyChildren());
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.getLine());
        assertEquals(1, error.getColumn());
        assertEquals("No element found", error.getMessage());
        assertEquals("", doc.text());
    }

    @Test
    public void testParseInputWithErrorTrackingAndValidationAndEncodingAndBaseUri() {
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(1);
        Document doc = parser.parseInput("<html></html>", null, errors, parser.settings(), baseUri);
        assertNull(doc.bodyChildren());
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.getLine());
        assertEquals(1, error.getColumn());
        assertEquals("No element found", error.getMessage());
        assertEquals("", doc.text());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseInputWithErrorTrackingAndValidationAndEncodingAndBaseUriAndValidationSchema() {
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(1);
        Document doc = parser.parseInput("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"></html>",
                null, errors, parser.settings(), baseUri, "http://example.com/xsd/");
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.getLine());
        assertEquals(1, error.getColumn());
        assertEquals("No start tag found", error.getMessage());
        assertEquals("", doc.text());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseInputWithErrorTrackingAndValidationAndEncodingAndBaseUriAndValidationSchemaAndDoctype() {
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(1);
        Document doc = parser.parseInput("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>",
                null, errors, parser.settings(), baseUri, "http://example.com/xsd/");
        assertNotNull(doc.head());
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(21, error.getLine());
        assertEquals(1, error.getColumn());
        assertEquals("Unexpected character 0x3D (=) found", error.getMessage());
        assertEquals("Test Page", doc.title().text());
        assertEquals("Hello World", doc.body().child(0).text());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseInputWithErrorTrackingAndValidationAndEncodingAndBaseUriAndValidationSchemaAndDoctypeAndEncodingDeclaration() {
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = ParseErrorList.tracking(1);
        Document doc = parser.parseInput("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xml:lang=\"en\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>",
                null, errors, parser.settings(), baseUri, "http://example.com/xsd/");
        assertNotNull(doc.head());
        assertEquals(0, errors.size());
        assertEquals("en", doc.xmlLang());
        assertEquals("Test Page", doc.title().text());
        assertEquals("Hello World", doc.body().child(0).text());
        assertEquals(baseUri, doc.baseUri());
    }
}