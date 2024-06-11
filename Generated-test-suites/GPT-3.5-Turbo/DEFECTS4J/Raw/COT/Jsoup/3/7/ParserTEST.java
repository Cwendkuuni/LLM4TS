import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    // 
    
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello world!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("Test", doc.title());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello world!</h1>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertEquals("Hello world!", doc.body().child(0).text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><h1>Hello world!</h1></div>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertEquals("Hello world!", doc.body().child(0).child(0).text());
    }
    
    // Add additional test cases for other methods
    
    // 
}