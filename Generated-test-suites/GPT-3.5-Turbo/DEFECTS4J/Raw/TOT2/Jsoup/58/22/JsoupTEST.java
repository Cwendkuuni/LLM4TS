import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(html, baseUri);
        
        assertEquals("Test", doc.title());
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        
        Connection con = Jsoup.connect(url);
        
        assertNotNull(con);
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        
        assertNotNull(doc);
    }

    // More test cases for other methods can be added here

}