import org.jsoup.parser.HTMLParser;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;
import org.jsoup.nodes.Entities;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HTMLParser());
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        
        assertEquals("http://example.com", doc.baseUri());
        Element body = doc.body();
        Elements paragraphs = body.select("p");
        assertEquals(1, paragraphs.size());
        assertEquals("Hello World", paragraphs.first().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Element body = doc.body();
        Elements paragraphs = body.select("p");
        assertEquals(1, paragraphs.size());
        assertEquals("Hello World", paragraphs.first().text());
    }

    @Test
    public void testUnescapeEntities() {
        String escaped = "&lt;p&gt;Hello World&lt;/p&gt;";
        String unescaped = Parser.unescapeEntities(escaped, false);

        assertEquals("<p>Hello World</p>", unescaped);
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof HTMLParser);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof XMLParser);
    }
}