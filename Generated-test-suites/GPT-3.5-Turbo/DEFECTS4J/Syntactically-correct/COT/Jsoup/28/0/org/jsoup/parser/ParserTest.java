import org.junit.Test;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import java.util.List;
import static org.junit.Assert.*;

public class ParserTest {
    
    // Method: parseInput(String, String)
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        Document doc = parser.parseInput("<html><head></head><body><p>Hello, world!</p></body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <p>Hello, world!</p>\n </body>\n</html>", doc.toString());
    }

    // Method: getTreeBuilder()
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
        assertEquals(TreeBuilder.class, treeBuilder.getClass());
    }

    // Method: setTreeBuilder(TreeBuilder)
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder newTreeBuilder = new TreeBuilder();
        Parser updatedParser = parser.setTreeBuilder(newTreeBuilder);
        assertNotNull(updatedParser);
        assertEquals(newTreeBuilder, updatedParser.getTreeBuilder());
    }

    // Method: isTrackErrors()
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    // Method: setTrackErrors(int)
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    // Method: getErrors()
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        assertEquals(ParseErrorList.noTracking(), parser.getErrors());
        parser.setTrackErrors(5);
        assertNotNull(parser.getErrors());
    }

    // Method: parse(String, String)
    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><head></head><body><p>Hello, world!</p></body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <p>Hello, world!</p>\n </body>\n</html>", doc.toString());
    }

    // Method: parseFragment(String, Element, String)
    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Hello, world!</p>", context, "http://example.com");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("<p>Hello, world!</p>", nodes.get(0).toString());
    }

    // Method: parseBodyFragment(String, String)
    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Hello, world!</p>", "http://example.com");
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <p>Hello, world!</p>\n </body>\n</html>", doc.toString());
    }

    // Method: parseBodyFragmentRelaxed(String, String)
    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Hello, world!</p>", "http://example.com");
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <p>Hello, world!</p>\n </body>\n</html>", doc.toString());
    }

    // Method: htmlParser()
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
        assertEquals(Parser.class, parser.getClass());
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    // Method: xmlParser()
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
        assertEquals(Parser.class, parser.getClass());
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

}