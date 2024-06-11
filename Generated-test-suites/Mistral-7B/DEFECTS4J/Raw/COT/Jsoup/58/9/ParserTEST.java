package org.jsoup.parser;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import org.junit.Test;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    @Test
    public void testParser() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><body></body></html>", "");
        assertNotNull(doc);
    }

    @Test
    public void testParseInputWithBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><head><base href=\"http://test.com\"></base></head><body></body></html>", "");
        assertNotNull(doc);
        assertNotNull(doc.baseUri());
        assertEquals("http://test.com", doc.baseUri().toString());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(treeBuilder, parser.treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotSame(treeBuilder, parser.treeBuilder);
        assertSame(newTreeBuilder, treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrorsWhenNoErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetErrorsWhenErrorsPresent() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = Arrays.asList(new ParseError());
        parser.setTreeBuilder(new TreeBuilder() {
            @Override
            public List<ParseError> errors() {
                return errors;
            }
        });
        List<ParseError> errorsList = parser.getErrors();
        // UnsupportedOperationException should be thrown since getErrors() is not supported by the TreeBuilder used.
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        assertNotNull(settings);
    }

    @Test
    public void testStaticParse() {
        Document doc = Parser.parse("<html><body></body></html>", "");
        assertNotNull(doc);
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = Parser.parseFragment("<div class=\"test\">Test</div>", null, "");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertInstanceOf(Element.class, node);
        Element element = (Element) node;
        assertEquals("div", element.tagName());
        assertEquals("test", element.className());
        assertEquals("Test", element.text());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<xml><tag>Test</tag></xml>", "");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertInstanceOf(Element.class, node);
        Element element = (Element) node;
        assertEquals("tag", element.tagName());
        assertEquals("Test", element.text());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<body><div class=\"test\">Test</div></body>", "");
        assertNotNull(doc);
        Element body = doc.body();
        List<Node> nodes = body.childNodes();
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertInstanceOf(Element.class, node);
        Element element = (Element) node;
        assertEquals("div", element.tagName());
        assertEquals("test", element.className());
        assertEquals("Test", element.text());
    }

    @Test
    public void testStaticParseWithMaxErrors() {
        List<ParseError> errors = Arrays.asList(new ParseError(), new ParseError());
        Parser parser = new Parser(new HtmlTreeBuilder()) {
            @Override
            public List<ParseError> errors() {
                return errors;
            }
        };
        Document doc = Parser.parse("<html><body><script>throw new Error(\"Test\")</script></body></html>", "");
        assertNotNull(doc);
        List<ParseError> parseErrors = parser.getErrors();
        assertEquals(2, parseErrors.size());
        assertEquals("Test", parseErrors.get(0).getDetailMessage());
        assertEquals("Test", parseErrors.get(1).getDetailMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput("", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput("<html></html>", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput("<html><base href=\"javascript:alert('Test')\"></base></html>", "");
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullContext() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseFragment(null, null, "");
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&nbsp; Test &amp; &lt; &gt; &quot; &apos; &eacute; &egrave; &iexcl; &iquest;";
        String unescaped = Parser.unescapeEntities(html, false);
        assertEquals(" Test < > \" ' é ë ï ¿", unescaped);
    }

    @Test
    public void testUnescapeEntitiesInAttribute() {
        String html = "<a href=\"mailto:test&amp;_test@example.com\" target=\"_blank\">Test</a>";
        String unescaped = Parser.unescapeEntities(html, true);
        assertEquals("<a href=\"mailto:test_test@example.com\" target=\"_blank\">Test</a>", unescaped);
    }

    @Test(expected = NullPointerException.class)
    public void testUnescapeEntitiesWithNullString() {
        Parser.unescapeEntities(null, false);
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertInstanceOf(HtmlTreeBuilder.class, parser.treeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertInstanceOf(XmlTreeBuilder.class, parser.treeBuilder);
    }
}