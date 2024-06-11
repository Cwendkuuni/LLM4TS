
package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        //Test functionality of parseInput method
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Title", document.select("h1").text());
        Assert.assertEquals("Paragraph", document.select("p").text());
    }

    @Test
    public void testGetTreeBuilder() {
        //Test functionality of getTreeBuilder method
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        Assert.assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        //Test functionality of setTreeBuilder method
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        
        parser.setTreeBuilder(treeBuilder);
        TreeBuilder updatedTreeBuilder = parser.getTreeBuilder();
        
        Assert.assertEquals(treeBuilder, updatedTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        //Test functionality of isTrackErrors method
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());
        
        parser.setTrackErrors(5);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        //Test functionality of setTrackErrors method
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        parser.setTrackErrors(5);
        Assert.assertEquals(5, parser.isTrackErrors());
        
        parser.setTrackErrors(0);
        Assert.assertEquals(0, parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        //Test functionality of getErrors method
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testSettings() {
        //Test functionality of settings method
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(true, false);
        
        parser.settings(settings);
        ParseSettings updatedSettings = parser.settings();
        
        Assert.assertEquals(settings, updatedSettings);
    }

    @Test
    public void testParse() {
        //Test functionality of parse method
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Title", document.select("h1").text());
        Assert.assertEquals("Paragraph", document.select("p").text());
    }

    @Test
    public void testParseFragment() {
        //Test functionality of parseFragment method
        String fragmentHtml = "<div><span>Text</span></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("Text", nodeList.get(0).select("span").text());
    }

    @Test
    public void testParseXmlFragment() {
        //Test functionality of parseXmlFragment method
        String fragmentXml = "<root><child>Text</child></root>";
        String baseUri = "http://example.com";
        
        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("Text", nodeList.get(0).select("child").text());
    }

    @Test
    public void testParseBodyFragment() {
        //Test functionality of parseBodyFragment method
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Title", document.select("h1").text());
        Assert.assertEquals("Paragraph", document.select("p").text());
    }

    @Test
    public void testUnescapeEntities() {
        //Test functionality of unescapeEntities method
        String escapedString = "This is &lt;b&gt;escaped&lt;/b&gt; text.";
        String unescapedString = Parser.unescapeEntities(escapedString, false);
        
        Assert.assertEquals("This is <b>escaped</b> text.", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        //Test functionality of parseBodyFragmentRelaxed method
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Title", document.select("h1").text());
        Assert.assertEquals("Paragraph", document.select("p").text());
    }

    @Test
    public void testHtmlParser() {
        //Test functionality of htmlParser method
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        //Test functionality of xmlParser method
        Parser parser = Parser.xmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        Assert.assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }
}
