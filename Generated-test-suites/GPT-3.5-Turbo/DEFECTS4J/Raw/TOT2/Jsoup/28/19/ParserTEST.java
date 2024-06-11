import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.*;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ParserTest {

    // Test case for parseInput method
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><body>Hello World</body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());
    }

    // Test case for getTreeBuilder method
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    // Test case for setTreeBuilder method
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTreeBuilder(new XmlTreeBuilder());
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    // Test case for isTrackErrors method
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    // Test case for getErrors method
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput("<html>", "http://example.com");
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
    }

    // Test case for static parse method
    @Test
    public void testStaticParse() {
        Document doc = Parser.parse("<html>Test</html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.text());
    }

    // Test case for static parseFragment method
    @Test
    public void testStaticParseFragment() {
        List<Node> nodes = Parser.parseFragment("<div>Test</div>", new Element("div"), "http://example.com");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("div", nodes.get(0).nodeName());
    }

    // Test case for static parseBodyFragment method
    @Test
    public void testStaticParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Test</p>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.text());
    }

    // Test case for static parseBodyFragmentRelaxed method
    @Test
    public void testStaticParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Test</p>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.text());
    }

    // Test case for htmlParser method
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    // Test case for xmlParser method
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
}