import org.jsoup.nodes.Document;
import org.jsoup.parser.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Document doc = parser.parseInput("<html><body>Hello World!</body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(new XmlTreeBuilder());
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertEquals(5, parser.getErrors().size());
    }

    @Test
    public void testStaticParse() {
        Document doc = Parser.parse("<html><body>Hello World!</body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Hello</p>", context, "http://example.com");
        assertEquals(1, nodes.size());
        assertEquals("p", nodes.get(0).nodeName());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Hello World!</p>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<html><body>Hello World!</body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}