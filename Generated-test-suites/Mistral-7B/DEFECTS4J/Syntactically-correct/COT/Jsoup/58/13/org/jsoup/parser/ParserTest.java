package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.jsoup.parser.Parser.parse;
import static org.jsoup.parser.Parser.parseBodyFragment;
import static org.jsoup.parser.Parser.parseFragment;
import static org.jsoup.parser.Parser.unescapeEntities;

public class ParserTest {

    @Test
    public void testParserConstructor() {
        Parser parser = new Parser(null);
        Assert.assertNotNull(parser.getTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Document doc = new Parser(new HtmlTreeBuilder()).parseInput(html, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.head().title().text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(null);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(null);
        Assert.assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(null);
        parser.setTrackErrors(1);
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());

        parser.setTrackErrors(0);
        errors = parser.getErrors();
        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testParseStaticFunctions() {
        String html = "<html><body><h1>Test</h1></body></html>";

        Document doc = Parser.parse(html, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.head().title().text());

        Element body = new Element("body");
        List<Node> nodes = Parser.parseFragment(html, body, null);
        for (Node node : nodes) {
            body.appendChild(node);
        }
        Assert.assertEquals("Test", body.child(0).text());

        Document doc2 = Parser.parseBodyFragment(html, null);
        Assert.assertEquals("Test", doc2.head().title().text());

        String xml = "<xml><tag>Test</tag></xml>";
        List<Node> nodesXml = Parser.parseXmlFragment(xml, null);
        Assert.assertEquals(1, nodesXml.size());
        Assert.assertEquals("Test", nodesXml.get(0).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentWithNullContext() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Parser.parseFragment(html, null, null, ParseErrorList.noTracking(), null.defaultSettings());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&amp;Hello &lt;World&gt;! &quot; &apos; &nbsp; &eacute; &auml;";
        String result = Parser.unescapeEntities(html, false);
        Assert.assertEquals("&amp;Hello &lt;World&gt;! &quot; &apos; &nbsp; é ä", result);

        String result2 = Parser.unescapeEntities(html, true);
        Assert.assertEquals("Hello <World>! \" ' \u00A0 é ä", result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentRelaxedWithNullBaseUri() {
        Parser.parseBodyFragmentRelaxed(null, null);
    }
}