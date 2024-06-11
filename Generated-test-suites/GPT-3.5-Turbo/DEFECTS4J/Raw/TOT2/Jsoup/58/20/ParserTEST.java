import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        Document document = parser.parseInput("<html></html>", "http://example.com");
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testStaticParse() {
        Document document = Parser.parse("<html></html>", "http://example.com");
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = Parser.parseFragment("<div></div>", new Element("div"), "http://example.com");
        assertEquals(1, nodes.size());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<div></div>", "http://example.com");
        assertEquals(1, nodes.size());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<div></div>", "http://example.com");
        assertEquals(1, document.body().childNodeSize());
    }

    @Test
    public void testUnescapeEntities() {
        String unescapedString = Parser.unescapeEntities("&lt;div&gt;", true);
        assertEquals("<div>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<html></html>", "http://example.com");
        assertEquals("http://example.com", document.baseUri());
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