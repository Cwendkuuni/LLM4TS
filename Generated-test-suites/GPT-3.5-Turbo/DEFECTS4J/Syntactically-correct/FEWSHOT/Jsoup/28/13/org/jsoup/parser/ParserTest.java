import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = parser.parseInput(html, baseUri);

        Element body = doc.body();

        assertEquals("html", doc.tagName());
        assertEquals("body", body.tagName());
        assertEquals(1, body.children().size());

        Element h1 = body.child(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello, World!", h1.text());

        assertFalse(parser.isTrackErrors());
        assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testGetSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);

        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        List<ParseError> errors = parser.getErrors();

        assertTrue(errors.isEmpty());

        parser.setTrackErrors(5);

        errors = parser.getErrors();

        assertTrue(errors.isEmpty());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        Element body = doc.body();

        assertEquals("html", doc.tagName());
        assertEquals("body", body.tagName());
        assertEquals(1, body.children().size());

        Element h1 = body.child(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello, World!", h1.text());
        
        assertFalse(Parser.isTrackErrors());
        assertEquals(0, Parser.getErrors().size());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertEquals(2, nodes.size());

        Element p1 = (Element) nodes.get(0);
        Element p2 = (Element) nodes.get(1);

        assertEquals("div", context.tagName());
        assertEquals("p", p1.tagName());
        assertEquals("Paragraph 1", p1.text());
        assertEquals("p", p2.tagName());
        assertEquals("Paragraph 2", p2.text());

        assertFalse(Parser.isTrackErrors());
        assertEquals(0, Parser.getErrors().size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Element body = doc.body();

        assertEquals("body", body.tagName());
        assertEquals(2, body.children().size());

        Element p1 = body.child(0);
        Element p2 = body.child(1);

        assertEquals("p", p1.tagName());
        assertEquals("Paragraph 1", p1.text());
        assertEquals("p", p2.tagName());
        assertEquals("Paragraph 2", p2.text());

        assertFalse(Parser.isTrackErrors());
        assertEquals(0, Parser.getErrors().size());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Element body = doc.body();

        assertEquals("body", body.tagName());
        assertEquals(2, body.children().size());

        Element p1 = body.child(0);
        Element p2 = body.child(1);

        assertEquals("p", p1.tagName());
        assertEquals("Paragraph 1", p1.text());
        assertEquals("p", p2.tagName());
        assertEquals("Paragraph 2", p2.text());

        assertFalse(Parser.isTrackErrors());
        assertEquals(0, Parser.getErrors().size());
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