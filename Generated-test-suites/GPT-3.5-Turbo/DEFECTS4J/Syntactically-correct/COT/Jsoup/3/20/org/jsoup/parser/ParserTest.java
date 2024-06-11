import org.jsoup.helper.W3CDom;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("<h1>Hello World</h1>", doc.body().html());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertEquals("<h1>Hello World</h1>", doc.body().html());
    }

    // Additional Test Case for parseBodyFragment
    @Test
    public void testParseBodyFragment_EmptyTag() {
        String bodyHtml = "<br/>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertEquals("<br>", doc.body().html());
    }

    // Additional Test Case for parseBodyFragment
    @Test
    public void testParseBodyFragment_WithRelaxedParsing() {
        String bodyHtml = "<h1>Hello World";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertEquals("<h1>Hello World</h1>", doc.body().html());
    }
    
    @Test
    public void testParse_Comment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("<!-- This is a comment -->", doc.body().html());
    }
    
    @Test
    public void testParse_Cdata() {
        String html = "<![CDATA[This is a CDATA]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("This is a CDATA", doc.body().html());
    }
    
    @Test
    public void testParse_XmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", doc.body().html());
    }
    
    @Test
    public void testParse_EndTag() {
        String html = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("<h1>Hello World</h1>", doc.body().html());
    }
    
    @Test
    public void testParse_StartTag() {
        String html = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("<h1>Hello World</h1>", doc.body().html());
    }
    
    // Additional Test Case for parseStartTag
    @Test
    public void testParse_StartTag_WithAttributes() {
        String html = "<a href=\"http://example.com\">Link</a>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("<a href=\"http://example.com\">Link</a>", doc.body().html());
    }
    
    // Additional Test Case for parseStartTag
    @Test
    public void testParse_StartTag_EmptyTag() {
        String html = "<br/>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("<br>", doc.body().html());
    }
    
    @Test
    public void testParse_TextNode() {
        String html = "Hello World";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("Hello World", doc.body().html());
    }
    
    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is a CDATA]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("This is a CDATA", doc.body().html());
    }
    
    @Test
    public void testPopStackToClose() {
        String html = "<div><p>Hello</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc.body().select("div"));
        Assert.assertNull(doc.body().select("p"));
    }
    
    @Test
    public void testLast() {
        String html = "<div><p>Hello</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("Hello", Parser.last().text());
    }
}