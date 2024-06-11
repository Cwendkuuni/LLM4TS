import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ParserTest {
  
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        Document doc = parser.parseInput("<html><body><h1>Test</h1></body></html>", "");

        // Check if the document is parsed correctly
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Test</h1>\n </body>\n</html>", doc.html());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        // Check if the tree builder is retrieved correctly
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        TreeBuilder newTreeBuilder = parser.getTreeBuilder();
        
        // Check if the tree builder is set correctly
        assertEquals(treeBuilder, newTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        
        // Check if error tracking is disabled by default
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        parser.setTrackErrors(5);
        
        // Check if error tracking is enabled correctly
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        List<ParseError> errors = parser.getErrors();
        
        // Check if the list of parse errors is retrieved correctly
        assertNotNull(errors);
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body><h1>Test</h1></body></html>", "");

        // Check if the document is parsed correctly
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Test</h1>\n </body>\n</html>", doc.html());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Test</p>", context, "");

        // Check if the fragment is parsed correctly
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("p", ((Element) nodes.get(0)).tagName());
        assertEquals("Test", ((Element) nodes.get(0)).text());
        assertEquals(context, ((Element) nodes.get(0)).parent());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Test</p>", "");

        // Check if the body fragment is parsed correctly
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("<p>Test</p>", body.html());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Test</p>", "");

        // Check if the body fragment is parsed correctly
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("<p>Test</p>", body.html());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        // Check if the HTML parser is created correctly
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        // Check if the XML parser is created correctly
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
    
}