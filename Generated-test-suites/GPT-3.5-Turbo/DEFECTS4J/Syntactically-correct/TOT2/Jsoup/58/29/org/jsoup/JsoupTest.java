import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><p>Hello, Jsoup!</p></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals("Hello, Jsoup!", doc.body().text());
    }
    
    // Additional test cases for other methods can be added here
    
}