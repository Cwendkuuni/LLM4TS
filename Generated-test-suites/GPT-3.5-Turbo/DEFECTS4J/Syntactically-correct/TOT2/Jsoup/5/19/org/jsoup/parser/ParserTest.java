import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Test Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Test Paragraph", doc.body().select("p").first().text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Body Fragment</p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Body Fragment", doc.body().select("p").first().text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<unclosedtag>Body Fragment</unclosedtag>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.body().children().size());
    }
    
    // Additional test cases for edge cases and exceptions can be added here
    
}