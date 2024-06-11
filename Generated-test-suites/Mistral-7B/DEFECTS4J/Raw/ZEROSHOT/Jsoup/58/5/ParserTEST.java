package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.tree.ParseErrorList;
import org.jsoup.parser.tree.TreeBuilder;
import org.jsoup.parser.tokenizer.CharacterReader;
import org.jsoup.parser.tokenizer.Tokenizer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser.
 */
public class ParserTest {

    private static final String HTML_WITH_NO_ERRORS = "<html><body></body></html>";
    private static final String HTML_WITH_ONE_ERROR = "<html><body><div><script></script></div></body></html>";
    private static final String HTML_WITH_MULTIPLE_ERRORS = "<html><body><div><script></script></div><script></script></body></html>";
    private static final String XML_WITH_NO_ERRORS = "<xml></xml>";
    private static final String XML_WITH_ONE_ERROR = "<xml><script></script></xml>";
    private static final String XML_WITH_MULTIPLE_ERRORS = "<xml><script></script><script></script></xml>";
    private static final String INVALID_HTML = "<html><body><script><html></script></body></html>";

    @org.junit.Test
    public void testParse() {
        // Given
        Parser parser = Parser.htmlParser();

        // When parsing valid HTML
        Document document = parser.parseInput(HTML_WITH_NO_ERRORS, "");

        // Then
        assertNotNull(document);
        assertEquals(Document.HTML, document.documentType().name());
        assertEquals(1, document.numberOfElements());
        assertEquals(1, document.numberOfChildElements(document.body()));
        assertNull(parser.getErrors());

        // When parsing HTML with one error
        document = parser.parseInput(HTML_WITH_ONE_ERROR, "");

        // Then
        assertNotNull(document);
        assertEquals(Document.HTML, document.documentType().name());
        assertEquals(1, document.numberOfElements());
        assertEquals(1, document.numberOfChildElements(document.body()));
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());

        // When parsing HTML with multiple errors
        parser.setTrackErrors(2);
        document = parser.parseInput(HTML_WITH_MULTIPLE_ERRORS, "");

        // Then
        assertNotNull(document);
        assertEquals(Document.HTML, document.documentType().name());
        assertEquals(1, document.numberOfElements());
        assertEquals(1, document.numberOfChildElements(document.body()));
        assertTrue(parser.isTrackErrors());
        List<ParseError> errors = parser.getErrors();
        assertEquals(2, errors.size());
        ParseError error1 = errors.get(0);
        ParseError error2 = errors.get(1);
        assertEquals("Tag 'script' not closed", error1.getMessage());
        assertEquals("Tag 'html' not closed", error2.getMessage());

        // When parsing valid XML
        parser = Parser.xmlParser();
        Document xmlDocument = parser.parseInput(XML_WITH_NO_ERRORS, "");

        // Then
        assertNotNull(xmlDocument);
        assertEquals(Document.XML, xmlDocument.documentType().name());
        assertEquals(0, xmlDocument.numberOfElements());
        assertNull(parser.getErrors());

        // When parsing XML with one error
        xmlDocument = parser.parseInput(XML_WITH_ONE_ERROR, "");

        // Then
        assertNotNull(xmlDocument);
        assertEquals(Document.XML, xmlDocument.documentType().name());
        assertEquals(0, xmlDocument.numberOfElements());
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());

        // When parsing XML with multiple errors
        parser.setTrackErrors(2);
        xmlDocument = parser.parseInput(XML_WITH_MULTIPLE_ERRORS, "");

        // Then
        assertNotNull(xmlDocument);
        assertEquals(Document.XML, xmlDocument.documentType().name());
        assertEquals(0, xmlDocument.numberOfElements());
        assertTrue(parser.isTrackErrors());
        errors = parser.getErrors();
        assertEquals(2, errors.size());
        error1 = errors.get(0);
        error2 = errors.get(1);
        assertEquals("Tag 'script' not closed", error1.getMessage());
        assertEquals("Tag 'script' not closed", error2.getMessage());

        // When parsing invalid HTML
        parser = Parser.htmlParser();
        document = parser.parseInput(INVALID_HTML, "");

        // Then
        assertNotNull(document);
        assertEquals(Document.HTML, document.documentType().name());
        assertEquals(0, document.numberOfElements());
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());
    }

    @org.junit.Test
    public void testParseFragment() {
        // Given
        Parser parser = Parser.htmlParser();

        // When parsing valid HTML fragment
        List<Node> nodes = Parser.parseFragment("<div></div>", null, "");

        // Then
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertSame(Element.class, nodes.get(0).getClass());
        Element element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        assertNull(parser.getErrors());

        // When parsing HTML fragment with one error
        nodes = Parser.parseFragment("<div><script></script></div>", null, "");

        // Then
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertSame(Element.class, nodes.get(0).getClass());
        element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());

        // When parsing HTML fragment with multiple errors
        parser.setTrackErrors(2);
        nodes = Parser.parseFragment("<div><script></script><script></script></div>", null, "");

        // Then
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertSame(Element.class, nodes.get(0).getClass());
        element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        assertTrue(parser.isTrackErrors());
        List<ParseError> errors = parser.getErrors();
        assertEquals(2, errors.size());
        ParseError error1 = errors.get(0);
        ParseError error2 = errors.get(1);
        assertEquals("Tag 'script' not closed", error1.getMessage());
        assertEquals("Tag 'script' not closed", error2.getMessage());

        // When parsing valid XML fragment
        parser = Parser.xmlParser();
        nodes = Parser.parseFragment("<div></div>", null, "");

        // Then
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertSame(Element.class, nodes.get(0).getClass());
        element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        assertNull(parser.getErrors());

        // When parsing XML fragment with one error
        nodes = Parser.parseFragment("<div><script></script></div>", null, "");

        // Then
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertSame(Element.class, nodes.get(0).getClass());
        element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());

        // When parsing XML fragment with multiple errors
        parser.setTrackErrors(2);
        nodes = Parser.parseFragment("<div><script></script><script></script></div>", null, "");

        // Then
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertSame(Element.class, nodes.get(0).getClass());
        element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        assertTrue(parser.isTrackErrors());
        errors = parser.getErrors();
        assertEquals(2, errors.size());
        error1 = errors.get(0);
        error2 = errors.get(1);
        assertEquals("Tag 'script' not closed", error1.getMessage());
        assertEquals("Tag 'script' not closed", error2.getMessage());
    }

    @org.junit.Test
    public void testParseXmlFragment() {
        // Given
        Parser parser = Parser.xmlParser();

        // When parsing valid XML fragment
        List<Node> nodes = Parser.parseXmlFragment("<div></div>", "");

        // Then
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertSame(Element.class, nodes.get(0).getClass());
        Element element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        assertNull(parser.getErrors());

        // When parsing XML fragment with one error
        nodes = Parser.parseXmlFragment("<div><script></script></div>", "");

        // Then
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertSame(Element.class, nodes.get(0).getClass());
        element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());

        // When parsing XML fragment with multiple errors
        parser.setTrackErrors(2);
        nodes = Parser.parseXmlFragment("<div><script></script><script></script></div>", "");

        // Then
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertSame(Element.class, nodes.get(0).getClass());
        element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        assertTrue(parser.isTrackErrors());
        List<ParseError> errors = parser.getErrors();
        assertEquals(2, errors.size());
        ParseError error1 = errors.get(0);
        ParseError error2 = errors.get(1);
        assertEquals("Tag 'script' not closed", error1.getMessage());
        assertEquals("Tag 'script' not closed", error2.getMessage());
    }

    @org.junit.Test
    public void testParseBodyFragment() {
        // Given
        Parser parser = Parser.htmlParser();

        // When parsing valid HTML body fragment
        Document document = Parser.parseBodyFragment("<html><body><div></div></body></html>", "");

        // Then
        assertNotNull(document);
        assertEquals(Document.HTML, document.documentType().name());
        assertEquals(1, document.numberOfElements());
        assertEquals(1, document.numberOfChildElements(document.body()));
        assertNull(parser.getErrors());

        // When parsing HTML body fragment with one error
        document = Parser.parseBodyFragment("<html><body><div><script></script></div></body></html>", "");

        // Then
        assertNotNull(document);
        assertEquals(Document.HTML, document.documentType().name());
        assertEquals(1, document.numberOfElements());
        assertEquals(1, document.numberOfChildElements(document.body()));
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());

        // When parsing HTML body fragment with multiple errors
        parser.setTrackErrors(2);
        document = Parser.parseBodyFragment("<html><body><div><script></script></div><script></script></body></html>", "");

        // Then
        assertNotNull(document);
        assertEquals(Document.HTML, document.documentType().name());
        assertEquals(1, document.numberOfElements());
        assertEquals(1, document.numberOfChildElements(document.body()));
        assertTrue(parser.isTrackErrors());
        List<ParseError> errors = parser.getErrors();
        assertEquals(2, errors.size());
        ParseError error1 = errors.get(0);
        ParseError error2 = errors.get(1);
        assertEquals("Tag 'script' not closed", error1.getMessage());
        assertEquals("Tag 'script' not closed", error2.getMessage());

        // When parsing invalid HTML body fragment
        document = Parser.parseBodyFragment("<html><body><script><html></script></body></html>", "");

        // Then
        assertNotNull(document);
        assertEquals(Document.HTML, document.documentType().name());
        assertEquals(1, document.numberOfElements());
        assertEquals(1, document.numberOfChildElements(document.body()));
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());
    }

    @org.junit.Test
    public void testUnescapeEntities() {
        // Given

        // When unescaping valid entities
        String input = "&lt;html&gt;&amp;lt;body&gt;&amp;nbsp; &amp;copy; 2023 &amp;#x26; &amp;#xA9;";
        String output = Parser.unescapeEntities(input, false);

        // Then
        assertEquals("<html><body>       &copy;  © 2023 &amp;</output>", output);

        // When unescaping entities in strict mode
        output = Parser.unescapeEntities(input, true);

        // Then
        assertEquals("<html><body>&lt;</body></html>&amp;copy; &copy; 2023 &amp;#x26; &amp;#xA9;", output);
    }
}