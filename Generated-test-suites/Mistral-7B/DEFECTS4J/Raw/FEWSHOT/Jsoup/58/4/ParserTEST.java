package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testCreateParserWithTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testParseDocumentWithEmptyInput() {
        Parser parser = Parser.htmlParser();
        Document document = parser.parseInput("", "");
        assertNotNull(document);
        assertTrue(document.body().childNodeSize() == 0);
    }

    @Test
    public void testParseDocumentWithSimpleHtml() {
        Parser parser = Parser.htmlParser();
        Document document = parser.parseInput("<html><head></head><body><p>Hello world</p></body></html>", "");
        assertNotNull(document);
        Element body = document.body();
        List<Node> children = body.children();
        assertEquals(1, children.size());
        assertEquals("Hello world", children.get(0).text());
    }

    @Test
    public void testParseDocumentWithErrors() {
        Parser parser = Parser.htmlParser();
        // malformed start tag
        List<ParseError> errors = parser.parseInput("<html<head></html>", "").getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing tag: Unexpected end tag : html", error.getMessage());
        // malformed end tag
        errors = parser.parseInput("<html><head><body></body></body>", "").getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        error = errors.get(0);
        assertEquals("Error parsing tag: Unexpected end tag : body", error.getMessage());
    }

    @Test
    public void testParseDocumentWithMaxErrors() {
        Parser parser = Parser.htmlParser().setTrackErrors(3);
        List<ParseError> errors = parser.parseInput("<html><body><a><b><c><d><e><f><g><h><i><j><k><l><m><n><o><p><q><r><s><t><u><v><w><x><y><z></html>", "").getErrors();
        assertNotNull(errors);
        assertEquals(16, errors.size());
    }

    @Test
    public void testParseDocumentWithTrackErrors() {
        Parser parser = Parser.htmlParser().setTrackErrors(1);
        List<ParseError> errors = parser.parseInput("<html><body><a><b><c><d><e><f><g><h><i><j><k><l><m><n><o><p><q><r><s><t><u><v><w><x><y><z></html>", "").getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing tag: Unexpected end tag : p", error.getMessage());
    }

    @Test
    public void testParseDocumentWithBaseUri() {
        Parser parser = Parser.htmlParser();
        Document document = parser.parseInput("<html><head><link rel=\"stylesheet\" href=\"styles.css\"></head><body><img src=\"image.png\"></body></html>", "");
        assertNotNull(document);
        Element img = document.select("img[src=\"image.png\"]").first();
        assertNotNull(img);
        String src = img.attr("abs:src");
        assertEquals("http://test.com/image.png", src);
    }

    @Test
    public void testParseDocumentWithBaseUriAndTrackErrors() {
        Parser parser = Parser.htmlParser().setTrackErrors(1);
        Document document = parser.parseInput("<html><head><link rel=\"stylesheet\" href=\"styles.css\"></head><body><img src=\"image.png\"></body></html>", "");
        assertNotNull(document);
        Element img = document.select("img[src=\"image.png\"]").first();
        assertNotNull(img);
        String src = img.attr("abs:src");
        assertEquals("http://test.com/image.png", src);
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testParseDocumentWithBaseUriAndMaxErrors() {
        Parser parser = Parser.htmlParser().setTrackErrors(3);
        Document document = parser.parseInput("<html><head><link rel=\"stylesheet\" href=\"styles.css\"></head><body><img src=\"image.png\"></body></html>", "");
        assertNotNull(document);
        Element img = document.select("img[src=\"image.png\"]").first();
        assertNotNull(img);
        String src = img.attr("abs:src");
        assertEquals("http://test.com/image.png", src);
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
        parser.setTrackErrors(0); // disable errors tracking
        String fragment = "<html><head><link rel=\"stylesheet\" href=\"styles.css\"></head><body><img src=\"image.png\"><img src=\"invalid.jpg\"></body></html>";
        errors = parser.parseInput(fragment, null).getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        error = errors.get(0);
        assertEquals("Error parsing tag: Unexpected end tag : img", error.getMessage());
    }

    @Test
    public void testParseFragmentWithEmptyInput() {
        Parser parser = Parser.htmlParser();
        List<Node> nodes = parser.parseFragment("", null, "");
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testParseFragmentWithSimpleHtml() {
        Parser parser = Parser.htmlParser();
        List<Node> nodes = parser.parseFragment("<p>Hello world</p>", null, "");
        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("p", element.tagName());
        assertEquals("Hello world", element.text());
    }

    @Test
    public void testParseFragmentWithErrors() {
        Parser parser = Parser.htmlParser();
        List<ParseError> errors = parser.parseFragment("<html<head>", null, "").getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing tag: Unexpected end tag : html", error.getMessage());
    }

    @Test
    public void testParseFragmentWithBaseUri() {
        Parser parser = Parser.htmlParser();
        List<Node> nodes = parser.parseFragment("<a href=\"image.png\"></a>", null, "");
        assertEquals(1, nodes.size());
        Element link = (Element) nodes.get(0);
        assertEquals("a", link.tagName());
        String src = link.attr("abs:src");
        assertEquals("http://test.com/image.png", src);
    }

    @Test
    public void testParseFragmentWithBaseUriAndTrackErrors() {
        Parser parser = Parser.htmlParser().setTrackErrors(1);
        List<Node> nodes = parser.parseFragment("<a href=\"image.png\"></a>", null, "");
        assertEquals(1, nodes.size());
        Element link = (Element) nodes.get(0);
        assertEquals("a", link.tagName());
        String src = link.attr("abs:src");
        assertEquals("http://test.com/image.png", src);
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testParseFragmentWithBaseUriAndMaxErrors() {
        Parser parser = Parser.htmlParser().setTrackErrors(3);
        List<Node> nodes = parser.parseFragment("<a href=\"image.png\"></a><img src=\"invalid.jpg\"></a>", null, "");
        assertEquals(2, nodes.size());
        Element link1 = (Element) nodes.get(0);
        assertEquals("a", link1.tagName());
        String src1 = link1.attr("abs:src");
        assertEquals("http://test.com/image.png", src1);
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing tag: Unexpected end tag : a", error.getMessage());
    }

    @Test
    public void testParseXmlFragmentWithEmptyInput() {
        Parser parser = Parser.xmlParser();
        List<Node> nodes = parser.parseXmlFragment("", "");
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testParseXmlFragmentWithSimpleXml() {
        Parser parser = Parser.xmlParser();
        List<Node> nodes = parser.parseXmlFragment("<root><child>Hello world</child></root>", "");
        assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        Element child = (Element) nodes.get(1);
        assertEquals("child", child.tagName());
        assertEquals("Hello world", child.text());
    }

    @Test
    public void testParseBodyFragment() {
        Parser parser = Parser.htmlParser();
        Document document = parser.parseBodyFragment("<p>Hello world</p>", "");
        assertEquals(1, document.childNodeSize());
        Element body = document.body();
        List<Node> children = body.children();
        assertEquals(1, children.size());
        assertEquals("Hello world", children.get(0).text());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;p&gt;Hello &amp; world&lt;/p&gt;";
        String unescaped = Parser.unescapeEntities(string, false);
        assertEquals("<p>Hello & world</p>", unescaped);
        string = "&lt;img src=\"&amp;image.png\" alt=\"&amp;amp;AMP; image\" /&gt;";
        unescaped = Parser.unescapeEntities(string, true);
        assertEquals("<img src=\"&amp;image.png\" alt=\"&amp;amp;AMP; image\" />", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<p>Hello world</p>", "");
        assertEquals(1, document.childNodeSize());
        Element body = document.body();
        List<Node> children = body.children();
        assertEquals(1, children.size());
        assertEquals("Hello world", children.get(0).text());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseBodyFragmentRelaxedDeprecated() {
        Parser parser = Parser.htmlParser();
        parser.parseBodyFragmentRelaxed("<p>Hello world</p>", "");
    }
}