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
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body><h1>Hello World";
        String baseUri = "https://www.example.com";
        
        try {
            Document doc = Parser.parse(html, baseUri);
            fail("Expected an exception");
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertTrue(doc.head().children().isEmpty());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testParseBodyFragmentInvalidHtml() {
        String bodyHtml = "<h1>Hello World";
        String baseUri = "https://www.example.com";

        try {
            Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
            fail("Expected an exception");
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><h1>Hello World</h1></div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertTrue(doc.head().children().isEmpty());
        assertEquals("Hello World", doc.body().child(0).child(0).text());
    }
}