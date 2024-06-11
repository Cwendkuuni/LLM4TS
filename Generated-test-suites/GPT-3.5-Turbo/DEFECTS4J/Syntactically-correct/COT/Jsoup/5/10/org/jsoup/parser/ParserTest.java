
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("Test Page", doc.title());
        Element body = doc.body();
        Assert.assertNotEquals(null, body);
        Assert.assertEquals("Hello World", body.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Element body = doc.body();
        Assert.assertNotEquals(null, body);
        Assert.assertEquals("Hello World", body.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div>Hello World</div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Element body = doc.body();
        Assert.assertNotEquals(null, body);
        Assert.assertEquals("Hello World", body.select("div").text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- Test comment -->";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<!-- Test comment -->", doc.childNode(0).outerHtml());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[<h1>Hello World</h1>]]>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Element body = doc.body();
        Assert.assertNotEquals(null, body);
        Assert.assertEquals("<h1>Hello World</h1>", body.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Element body = doc.body();
        Assert.assertNotEquals(null, body);
        Assert.assertEquals("Hello World", body.select("h1").text());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Element body = doc.body();
        Assert.assertNotEquals(null, body);
        Assert.assertEquals("Hello World", body.select("h1").text());
        
        // Test closing tags
        String endHtml = "</body></html>";
        Document endDoc = Parser.parse(endHtml, baseUri);
        
        Assert.assertEquals(0, endDoc.body().childNodeSize());
        Assert.assertEquals(null, endDoc.body());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Element h1 = doc.selectFirst("h1");
        
        Assert.assertNotEquals(null, h1);
        Assert.assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Element h1 = doc.selectFirst("h1");
        
        Assert.assertNotEquals(null, h1);
        Assert.assertEquals("Hello World", h1.text());
    }
}
