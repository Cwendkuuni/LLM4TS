import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class ParserTest {
    
    @Before
    public void setUp() {
        // Initialize setup before each test case
    }
    
    @Test
    public void testParse() {
        String html = "<div><h1>Hello, world!</h1></div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertEquals("<div><h1>Hello, world!</h1></div>", doc.html());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, world!</h1>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("<body><h1>Hello, world!</h1></body>", doc.html());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, world!</h1>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertEquals("<body><h1>Hello, world!</h1></body>", doc.html());
    }
}