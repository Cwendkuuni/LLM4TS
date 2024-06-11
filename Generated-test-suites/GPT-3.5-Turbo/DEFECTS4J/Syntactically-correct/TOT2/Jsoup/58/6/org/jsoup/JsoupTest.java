import static org.junit.Assert.*;
import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head></head><body>Hello World</body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Jsoup.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseWithAlternateParser() {
        String html = "<html><head></head><body>Hello Jsoup</body></html>";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("Hello Jsoup", doc.body().text());
    }

    // Add more test cases for other methods

}