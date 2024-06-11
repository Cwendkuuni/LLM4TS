import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Test Page", doc.title());
        
        Element body = doc.body();
        assertNotNull(body);
        
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Hello, World!", h1.text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        
        Element body = doc.body();
        assertNotNull(body);
        
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Hello, World!", h1.text());
    }
}