import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        
        assertNotNull(doc.head());
        assertEquals("head", doc.head().tagName());
        
        assertNotNull(doc.body());
        assertEquals("body", doc.body().tagName());
        
        assertNotNull(doc.title());
        assertEquals("title", doc.title().tagName());
        assertEquals("Test", doc.title());
        
        assertNotNull(doc.body().child(0));
        assertEquals("h1", doc.body().child(0).tagName());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("body", doc.tagName());
        
        assertNotNull(doc.child(0));
        assertEquals("h1", doc.child(0).tagName());
        assertEquals("Hello World", doc.child(0).text());
    }
}