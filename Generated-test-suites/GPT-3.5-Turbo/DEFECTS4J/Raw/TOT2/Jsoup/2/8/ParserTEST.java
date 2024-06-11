import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.Assert;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc.body());
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<div><p>Missing closing tag";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragmentEmptyHtml() {
        String bodyHtml = "";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc.body());
    }

    @Test
    public void testParseBodyFragmentInvalidHtml() {
        String bodyHtml = "<div><p>Missing closing tag";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc.body());
    }
}