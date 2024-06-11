import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><body><p>Hello, Jsoup!</p></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><p>Hello, Jsoup!</p></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        
        assertNotNull(connection);
        assertEquals(url, connection.url().toString());
    }

    // Additional test cases for other methods can be added here

}