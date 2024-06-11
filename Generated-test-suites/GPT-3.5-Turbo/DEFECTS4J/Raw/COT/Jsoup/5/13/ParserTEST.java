
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertEquals("Test", doc.title());
        assertEquals("Hello, World!", doc.body().text());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("Hello, World!", doc.body().text());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertEquals("Hello, World!", doc.body().text());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
}
