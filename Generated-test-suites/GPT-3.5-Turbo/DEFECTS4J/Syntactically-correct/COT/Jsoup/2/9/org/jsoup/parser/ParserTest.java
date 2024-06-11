import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("Hello, World!", document.select("h1").text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String html = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("Paragraph 1", document.select("p").get(0).text());
        Assert.assertEquals("Paragraph 2", document.select("p").get(1).text());
    }
    
    @Test
    public void testParseWithEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
    }
    
    @Test
    public void testParseBodyFragmentWithEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
    }
    
    @Test(expected = NullPointerException.class)
    public void testParseWithNullHtml() {
        String html = null;
        String baseUri = "http://example.com";
        
        Parser.parse(html, baseUri);
    }
    
    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentWithNullHtml() {
        String html = null;
        String baseUri = "http://example.com";
        
        Parser.parseBodyFragment(html, baseUri);
    }
    
    @Test(expected = NullPointerException.class)
    public void testParseWithNullBaseUri() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = null;
        
        Parser.parse(html, baseUri);
    }
    
    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentWithNullBaseUri() {
        String html = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        String baseUri = null;
        
        Parser.parseBodyFragment(html, baseUri);
    }
    
    // Add test cases for potential edge cases and exception handling scenarios
    
}