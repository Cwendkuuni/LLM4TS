import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello</h1>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals(1, doc.body().children().size());
        assertEquals("h1", doc.body().child(0).tagName());
        assertEquals("Hello", doc.body().child(0).text());
    }

    // Add more test cases for other methods in Parser class

}