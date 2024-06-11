import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Test typical case
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html></html>", "http://example.com");
        assertNotNull(doc);
        
        // Test edge case
        doc = parser.parseInput("", "http://example.com");
        assertNotNull(doc);
        
        // Test error scenario
        assertNull(parser.parseInput(null, "http://example.com"));
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTreeBuilder(new XmlTreeBuilder());
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNotNull(parser.getErrors());
    }

    @Test
    public void testStaticParse() {
        Document doc = Parser.parse("<html></html>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<span></span>", context, "http://example.com");
        assertEquals(1, nodes.size());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Hello</p>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Deprecated method, should still work
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Hello</p>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
}