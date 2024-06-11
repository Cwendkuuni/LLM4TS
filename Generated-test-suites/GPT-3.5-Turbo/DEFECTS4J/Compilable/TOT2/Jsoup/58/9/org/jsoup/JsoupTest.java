package org.jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.select("p").text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parse(html, baseUri, Parser.htmlParser());
        
        assertNotNull(doc);
        assertEquals("Title", doc.select("h1").text());
    }

    @Test
    public void testConnect() {
        Connection conn = Jsoup.connect("https://example.com");
        
        assertNotNull(conn);
        assertEquals("https://example.com", conn.request().url().toExternalForm());
    }

    // Additional Test Cases for other public methods in Jsoup class can be added here
    
}