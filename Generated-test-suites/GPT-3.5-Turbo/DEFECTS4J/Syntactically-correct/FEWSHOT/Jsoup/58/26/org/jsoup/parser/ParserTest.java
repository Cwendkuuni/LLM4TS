
package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    private Parser parser;
  
    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }
  
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
        Assert.assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }
  
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
      
        TreeBuilder newTreeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(newTreeBuilder);
        Assert.assertEquals(XmlTreeBuilder.class, newTreeBuilder.getClass());
    }
  
    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        Assert.assertFalse(trackErrors);
    }
  
    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);
        boolean trackErrors = parser.isTrackErrors();
        Assert.assertTrue(trackErrors);
    }
  
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());
    }
  
    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(true, true);
        parser.settings(settings);
        ParseSettings newSettings = parser.settings();
        Assert.assertNotNull(newSettings);
        Assert.assertTrue(newSettings.isPreserveCase());
        Assert.assertTrue(newSettings.isEnableSafeMode());
    }
  
    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><div><h1>Title</h1></div></body></html>";
        String baseUri = "https://www.example.com";
        Document document = parser.parseInput(html, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("https://www.example.com", document.baseUri());
        Assert.assertEquals(1, document.getElementsByTag("title").size());
        Assert.assertEquals(1, document.getElementsByTag("h1").size());
    }
  
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><div><h1>Title</h1></div></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("https://www.example.com", document.baseUri());
        Assert.assertEquals(1, document.getElementsByTag("title").size());
        Assert.assertEquals(1, document.getElementsByTag("h1").size());
    }
  
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><h1>Title</h1></div>";
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        Element context = document.body();
        String baseUri = "https://www.example.com";
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("h1", nodeList.get(0).nodeName());
    }
  
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<node>Test</node>";
        String baseUri = "https://www.example.com";
        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);
        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("node", nodeList.get(0).nodeName());
    }
  
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Title</h1>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("https://www.example.com", document.baseUri());
        Assert.assertEquals(1, document.getElementsByTag("h1").size());
    }
  
    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;div&gt;Test&lt;/div&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, false);
        Assert.assertEquals("<div>Test</div>", unescapedString);
    }
  
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<html><head><title>Test</title></head><body><h1>Title</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("https://www.example.com", document.baseUri());
        Assert.assertEquals(1, document.getElementsByTag("title").size());
        Assert.assertEquals(1, document.getElementsByTag("h1").size());
    }
  
    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        Assert.assertNotNull(htmlParser);
        Assert.assertEquals(HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
    }
  
    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        Assert.assertNotNull(xmlParser);
        Assert.assertEquals(XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
    }
}
