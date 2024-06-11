import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("<h1>Hello World</h1>", doc.body().html());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("<h1>Hello World</h1>", doc.body().html());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<h1>Hello World</h2>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("<h1>Hello World</h2>", doc.body().html());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(" This is a comment ", doc.body().text());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[<h1>Hello World</h1>]]>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("<h1>Hello World</h1>", doc.body().html());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("<h1>Hello World</h1>", doc.body().html());
    }
}