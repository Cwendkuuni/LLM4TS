import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.HtmlTreeBuilder;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><p>Hello, world!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, world!", doc.body().text());
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
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(2);
        assertTrue(parser.isTrackErrors());
        assertEquals(2, parser.getTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testParse() {
        String html = "<html><body><p>Hello, world!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, world!", doc.body().text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello, world!</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodes.size());
        assertEquals("p", nodes.get(0).nodeName());
        assertEquals("Hello, world!", nodes.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, world!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, world!", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, world!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, world!", doc.body().text());
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

    // Edge case for maximum errors
    @Test
    public void testSetTrackErrorsMax() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(Integer.MAX_VALUE);
        assertTrue(parser.isTrackErrors());
        assertEquals(Integer.MAX_VALUE, parser.getTrackErrors());
    }

}