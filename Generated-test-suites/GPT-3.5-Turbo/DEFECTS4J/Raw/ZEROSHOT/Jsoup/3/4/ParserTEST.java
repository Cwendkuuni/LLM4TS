import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("Hello World", doc.body().select("h1").text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals("body", doc.tagName());
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }
    
    // Add more test methods to cover all the methods in the Parser class
    
}