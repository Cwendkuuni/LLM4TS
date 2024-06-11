import static org.junit.Assert.*;
import org.junit.Test;

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
        String html = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }
    
    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection con = Jsoup.connect(url);
        assertNotNull(con);
    }
    
    // Add more test cases for other methods as needed
    
}