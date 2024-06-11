import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("<html><body><h1>Hello, World!</h1></body></html>", "");
        
        Assert.assertNotNull(document);
        Assert.assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", document.outerHtml());
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        Assert.assertNotNull(treeBuilder);
        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
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
        Parser parser = new Parser(new HtmlTreeBuilder());
        boolean trackErrors = parser.isTrackErrors();
        
        Assert.assertFalse(trackErrors);
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        
        Assert.assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        
        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());
    }
    
    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(false);
        
        Parser newParser = parser.settings(settings);
        
        Assert.assertEquals(settings, newParser.settings());
    }
    
    @Test
    public void testParse() {
        Document document = Parser.parse("<html><body><h1>Hello, World!</h1></body></html>", "");
        
        Assert.assertNotNull(document);
        Assert.assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", document.outerHtml());
    }
    
    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Hello, World!</p>", context, "");
        
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("p", nodes.get(0).nodeName());
        Assert.assertEquals("Hello, World!", ((Element) nodes.get(0)).text());
    }
    
    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<foo>Hello, World!</foo>", "");
        
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("foo", nodes.get(0).nodeName());
        Assert.assertEquals("Hello, World!", ((Element) nodes.get(0)).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<h1>Hello, World!</h1>", "");
        
        Assert.assertNotNull(document);
        Assert.assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", document.outerHtml());
    }
    
    @Test
    public void testUnescapeEntities() {
        String unescapedString = Parser.unescapeEntities("Hello &amp; World!", true);
        
        Assert.assertEquals("Hello & World!", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<h1>Hello, World!</h1>", "");
        
        Assert.assertNotNull(document);
        Assert.assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", document.outerHtml());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}