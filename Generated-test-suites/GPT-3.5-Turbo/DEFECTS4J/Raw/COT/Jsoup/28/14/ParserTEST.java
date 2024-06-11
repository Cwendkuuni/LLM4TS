
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.parser.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><head></head><body><h1>Hello World!</h1></body></html>", "");
        assertEquals("Hello World!", doc.body().select("h1").text());
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
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
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
        parser.setTrackErrors(5);
        assertNotNull(parser.getErrors());
    }
    
    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><head></head><body><h1>Hello World!</h1></body></html>", "");
        assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseFragment() {
        Element element = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>This is a paragraph</p>", element, "");
        assertEquals(1, nodes.size());
        assertEquals("p", nodes.get(0).nodeName());
        assertEquals("This is a paragraph", nodes.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>This is a paragraph</p>", "");
        assertEquals("<p>This is a paragraph</p>", doc.body().html());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>This is a paragraph</p>", "");
        assertEquals("<p>This is a paragraph</p>", doc.body().html());
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
