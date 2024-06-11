import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        Element body = doc.body();
        
        assertNotNull(body);
        assertEquals("Test", body.select("h1").text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Element body = doc.body();
        
        assertNotNull(body);
        assertEquals("Test", body.select("h1").text());
    }
}