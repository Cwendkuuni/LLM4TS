import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        String html = "<html><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <p>\n   Hello World\n  </p>\n </body>\n</html>", doc.toString());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder initialTreeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(initialTreeBuilder);
        
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        parser.setTrackErrors(5);
        
        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.getErrors().size());
    }

    @Test
    public void testParse() {
        String html = "<html><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <p>\n   Hello World\n  </p>\n </body>\n</html>", doc.toString());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        String fragmentHtml = "<p>Hello World</p>";
        String baseUri = "http://example.com";
        
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
        Node node = nodeList.get(0);
        assertEquals("p", node.nodeName());
        assertEquals("Hello World", node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <p>\n   Hello World\n  </p>\n </body>\n</html>", doc.toString());
    }
}