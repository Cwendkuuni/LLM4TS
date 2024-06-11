
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

public class ParserTest {
    
    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document result = parser.parseInput(html, baseUri);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("Test", result.select("h1").text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        TreeBuilder result = parser.getTreeBuilder();
        
        Assert.assertEquals(treeBuilder, result);
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder1 = new HtmlTreeBuilder();
        TreeBuilder treeBuilder2 = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder1);
        
        parser.setTreeBuilder(treeBuilder2);
        
        Assert.assertEquals(treeBuilder2, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        boolean result = parser.isTrackErrors();
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testSetTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        parser.setTrackErrors(5);
        
        Assert.assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        List<ParseError> errors = parser.getErrors();
        
        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());
    }
    
    @Test
    public void testSettings() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        ParseSettings settings = new ParseSettings(false, false);
        parser.settings(settings);
        
        Assert.assertEquals(settings, parser.settings());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document result = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("Test", result.select("h1").text());
    }
    
    @Test
    public void testParseFragment() {
        Element context = Jsoup.parse("<div></div>").select("div").first();
        String fragmentHtml = "<p>Test</p>";
        String baseUri = "http://example.com";
        
        List<Node> result = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("Test", result.get(0).outerHtml());
    }
    
    @Test
    public void testParseFragmentWithErrors() {
        Element context = Jsoup.parse("<div></div>").select("div").first();
        String fragmentHtml = "<p>Test</p>";
        String baseUri = "http://example.com";
        
        ParseErrorList errorList = ParseErrorList.tracking(5);
        List<Node> result = Parser.parseFragment(fragmentHtml, context, baseUri, errorList);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("Test", result.get(0).outerHtml());
        Assert.assertNotNull(errorList);
        Assert.assertEquals(0, errorList.size());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><child>Test</child></root>";
        String baseUri = "http://example.com";
        
        List<Node> result = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("root", result.get(0).nodeName());
        Assert.assertEquals("child", result.get(1).nodeName());
        Assert.assertEquals("Test", result.get(1).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        String baseUri = "http://example.com";
        
        Document result = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("Test", result.select("p").text());
    }
    
    @Test
    public void testUnescapeEntities() {
        String string = "&lt;html&gt;&lt;body&gt;&lt;p&gt;Test&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
        boolean inAttribute = true;
        
        String result = Parser.unescapeEntities(string, inAttribute);
        
        Assert.assertEquals("<html><body><p>Test</p></body></html>", result);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Test</p>";
        String baseUri = "http://example.com";
        
        Document result = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("Test", result.select("p").text());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        Assert.assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }
}
