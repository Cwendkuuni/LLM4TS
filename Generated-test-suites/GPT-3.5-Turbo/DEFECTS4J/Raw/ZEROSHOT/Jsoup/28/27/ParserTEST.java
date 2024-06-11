import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

public class ParserTest {
    
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals("Hello, World!", doc.body().text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        TreeBuilder newTreeBuilder = new TreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        
        Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        
        Assert.assertFalse(parser.isTrackErrors());
        
        parser.setTrackErrors(10);
        Assert.assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        
        parser.setTrackErrors(10);
        Assert.assertEquals(10, parser.getErrors().size());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        
        parser.setTrackErrors(10);
        Assert.assertEquals(10, parser.getErrors().size());
    }
    
    @Test
    public void testParse() {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals("Hello, World!", doc.body().text());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello, World!</p>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";
        
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("Hello, World!", nodeList.get(0).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals("Hello, World!", doc.body().text());
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