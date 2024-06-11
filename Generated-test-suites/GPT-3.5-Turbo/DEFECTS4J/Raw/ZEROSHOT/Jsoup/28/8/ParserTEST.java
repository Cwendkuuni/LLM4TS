import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        Document doc = parser.parseInput(html, baseUri);
        
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals(1, doc.select("h1").size());
        Assert.assertEquals("Hello, World!", doc.selectFirst("h1").text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
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
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        Assert.assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        parser.setTrackErrors(10);
        
        Assert.assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        
        List<ParseError> errors = parser.getErrors();
        
        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());
    }
    
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals(1, doc.select("h1").size());
        Assert.assertEquals("Hello, World!", doc.selectFirst("h1").text());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello, World!</p></div>";
        Element context = new Element("div");
        String baseUri = "http://www.example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("div", nodes.get(0).nodeName());
        Assert.assertEquals("p", nodes.get(0).childNode(0).nodeName());
        Assert.assertEquals("Hello, World!", nodes.get(0).childNode(0).childNode(0).toString());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals(1, doc.select("h1").size());
        Assert.assertEquals("Hello, World!", doc.selectFirst("h1").text());
    }
    
    @Test
    public void testHTMLUnescape() {
        String html = "&lt;div&gt;Hello, World!&lt;/div&gt;";
        String result = Jsoup.parser().htmlUnescape(html);
        
        Assert.assertEquals("<div>Hello, World!</div>", result);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals(1, doc.select("h1").size());
        Assert.assertEquals("Hello, World!", doc.selectFirst("h1").text());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        Assert.assertNotNull(parser);
        Assert.assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        Assert.assertNotNull(parser);
        Assert.assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
}