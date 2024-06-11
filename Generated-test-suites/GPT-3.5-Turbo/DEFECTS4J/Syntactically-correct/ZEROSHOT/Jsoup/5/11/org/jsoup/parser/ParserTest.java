import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello, world!", doc.body().select("h1").text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, world!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertEquals("Hello, world!", doc.body().select("h1").text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, world!</h1><p>This is a paragraph.</p><h2>Welcome</h2>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertEquals("Hello, world!", doc.body().select("h1").text());
        Assert.assertEquals("This is a paragraph.", doc.body().select("p").text());
        Assert.assertEquals("Welcome", doc.body().select("h2").text());
    }
}