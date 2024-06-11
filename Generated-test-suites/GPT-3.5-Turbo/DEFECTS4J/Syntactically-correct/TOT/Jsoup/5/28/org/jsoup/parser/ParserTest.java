import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertEquals("html", document.tagName());
        Assert.assertEquals("http://example.com", document.baseUri());
        
        Assert.assertEquals("head", document.head().tagName());
        Assert.assertEquals("title", document.head().child(0).tagName());
        Assert.assertEquals("Test", document.head().child(0).text());
        
        Assert.assertEquals("body", document.body().tagName());
        Assert.assertEquals("h1", document.body().child(0).tagName());
        Assert.assertEquals("Hello World!", document.body().child(0).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertEquals("body", document.tagName());
        Assert.assertEquals("http://example.com", document.baseUri());
        
        Assert.assertEquals("h1", document.child(0).tagName());
        Assert.assertEquals("Hello World!", document.child(0).text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertEquals("body", document.tagName());
        Assert.assertEquals("http://example.com", document.baseUri());
        
        Assert.assertEquals("h1", document.child(0).tagName());
        Assert.assertEquals("Hello World!", document.child(0).text());
    }
}