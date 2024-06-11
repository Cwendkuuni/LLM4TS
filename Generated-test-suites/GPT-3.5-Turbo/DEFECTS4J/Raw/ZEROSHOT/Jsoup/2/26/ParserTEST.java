import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        
        Elements body = doc.select("body");
        assertEquals(1, body.size());
        
        Elements h1 = doc.select("h1");
        assertEquals(1, h1.size());
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        
        Elements body = doc.select("body");
        assertEquals(1, body.size());
        
        Elements h1 = doc.select("h1");
        assertEquals(1, h1.size());
        assertEquals("Hello World", h1.text());
    }
}