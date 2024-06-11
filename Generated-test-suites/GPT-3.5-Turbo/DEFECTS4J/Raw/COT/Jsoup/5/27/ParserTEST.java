
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import static org.junit.Assert.*;

public class ParserTest {
    
    // Test parse() method
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>This is a test</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html>", doc.toString());
        assertEquals("Test", doc.title());
        assertEquals("This is a test", doc.body().select("p").first().text());
    }
    
    // Test parseBodyFragment() method
    @Test
    public void testParseBodyFragment() {
        String html = "<p>This is a test</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<body><p>This is a test</p></body>", doc.toString());
        assertEquals("This is a test", doc.body().select("p").first().text());
    }
    
    // Test parseBodyFragmentRelaxed() method
    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<p>This is a test</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<body><p>This is a test</p></body>", doc.toString());
        assertEquals("This is a test", doc.body().select("p").first().text());
    }
}
