import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test Title", doc.title());
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test Paragraph</p></div>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test Paragraph", doc.body().select("p").text());
    }

    @Test
    public void testParseWithRelaxedParsing() {
        String html = "<textarea>Text inside textarea</textarea>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Text inside textarea", doc.select("textarea").first().text());
    }

    @Test
    public void testParseWithNullHtml() {
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(null, baseUri);
        
        assertNull(doc);
    }

    @Test
    public void testParseBodyFragmentWithNullHtml() {
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(null, baseUri);
        
        assertNull(doc);
    }

    @Test
    public void testParseWithNullBaseUri() {
        String html = "<p>Test Paragraph</p>";
        
        Document doc = Parser.parse(html, null);
        
        assertNull(doc);
    }

    @Test
    public void testParseBodyFragmentWithNullBaseUri() {
        String bodyHtml = "<div><p>Test Paragraph</p></div>";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        
        assertNull(doc);
    }

}