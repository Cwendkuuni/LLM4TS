import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse_WithValidHtml_ReturnsDocumentObject() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testParse_WithNullHtml_ThrowsIllegalArgumentException() {
        String html = null;
        String baseUri = "http://example.com";
        
        try {
            Parser.parse(html, baseUri);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("html must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testParse_WithNullBaseUri_ThrowsIllegalArgumentException() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = null;
        
        try {
            Parser.parse(html, baseUri);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("baseUri must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testParseBodyFragment_WithValidHtml_ReturnsDocumentObject() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testParseBodyFragment_WithNullBodyHtml_ThrowsIllegalArgumentException() {
        String bodyHtml = null;
        String baseUri = "http://example.com";
        
        try {
            Parser.parseBodyFragment(bodyHtml, baseUri);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("bodyHtml must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testParseBodyFragment_WithNullBaseUri_ThrowsIllegalArgumentException() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = null;
        
        try {
            Parser.parseBodyFragment(bodyHtml, baseUri);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("baseUri must not be null", e.getMessage());
        }
    }
    
    // Additional test cases can be added for testing the other methods in the Parser class

}