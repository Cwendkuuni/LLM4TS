import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {
  
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = parser.parseInput(html, baseUri);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = new TreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        parser.setTrackErrors(5);
        Assert.assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
    }
    
    @Test
    public void testSettings() {
        Parser parser = new Parser(new TreeBuilder());
        ParseSettings settings = new ParseSettings();
        parser.settings(settings);
        Assert.assertEquals(settings, parser.settings());
    }
    
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Test</p></div>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("p", nodes.get(0).nodeName());
        Assert.assertEquals("Test", nodes.get(0).text());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><child>Test</child></root>";
        String baseUri = "https://www.example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("child", nodes.get(0).nodeName());
        Assert.assertEquals("Test", nodes.get(0).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test</p></div>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("Test", document.body().select("p").text());
    }
    
    @Test
    public void testUnescapeEntities() {
        String string = "&lt;div&gt;Test&lt;/div&gt;";
        boolean inAttribute = false;
        String unescapedString = Parser.unescapeEntities(string, inAttribute);
        Assert.assertEquals("<div>Test</div>", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Test</p></div>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertEquals("Test", document.body().select("p").text());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        Assert.assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        Assert.assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
}