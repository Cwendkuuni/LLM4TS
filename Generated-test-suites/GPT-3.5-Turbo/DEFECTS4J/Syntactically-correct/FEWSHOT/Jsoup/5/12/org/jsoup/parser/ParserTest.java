import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><div id=\"content\">Hello World!</div></body></html>";
        String baseUri = "https://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.getElementById("content").text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div id=\"content\">Hello World!</div>";
        String baseUri = "https://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("Hello World!", doc.getElementById("content").text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div id=\"content\">Hello World!</div>";
        String baseUri = "https://example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertEquals("Hello World!", doc.getElementById("content").text());
    }
    
    @Test
    public void testParseWithInvalidHtml() {
        String html = "<html><head></head><body>";
        String baseUri = "https://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNull(doc);
    }
}