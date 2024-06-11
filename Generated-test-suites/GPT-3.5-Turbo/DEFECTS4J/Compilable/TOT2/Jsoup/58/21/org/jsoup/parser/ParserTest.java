package org.jsoup.parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        Document document = parser.parseInput("<html></html>", "baseUri");
        assertEquals("Document should not be null", Document.class, document.getClass());
        assertEquals("Document should contain specified HTML", "<html></html>", document.outerHtml());
    }

    @Test
    public void testGetTreeBuilder() {
        assertEquals("TreeBuilder should be equal to the one set in the constructor", HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        parser.setTreeBuilder(new XmlTreeBuilder());
        assertEquals("TreeBuilder should be updated to the new one set", XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse("Error tracking should be disabled by default", parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(2);
        assertTrue("Error tracking should be enabled after setting a maximum number of errors", parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        parser.setTrackErrors(2);
        List<ParseError> errors = parser.getErrors();
        assertEquals("List of errors should be the same size as the maximum errors tracked", 0, errors.size());
    }

    @Test
    public void testStaticParse() {
        Document document = Parser.parse("<html></html>", "baseUri");
        assertEquals("Document should not be null", Document.class, document.getClass());
        assertEquals("Document should contain specified HTML", "<html></html>", document.outerHtml());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodeList = Parser.parseFragment("<div></div>", new Element("div"), "baseUri");
        assertEquals("List of nodes should not be null", 1, nodeList.size());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodeList = Parser.parseXmlFragment("<element></element>", "baseUri");
        assertEquals("List of nodes should not be null", 1, nodeList.size());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<div></div>", "baseUri");
        assertEquals("Body should contain parsed fragment", "<body><div></div></body>", document.body().outerHtml());
    }

    @Test
    public void testUnescapeEntities() {
        String unescapedString = Parser.unescapeEntities("&lt;div&gt;", true);
        assertEquals("String should be unescaped correctly", "<div>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<html></html>", "baseUri");
        assertEquals("Document should not be null", Document.class, document.getClass());
        assertEquals("Document should contain specified HTML", "<html></html>", document.outerHtml());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertEquals("Parser should use HtmlTreeBuilder", HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertEquals("Parser should use XmlTreeBuilder", XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
    }
}