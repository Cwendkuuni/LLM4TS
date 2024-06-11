import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("<h1>Hello</h1>", doc.body().html());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test</p></div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<div>\n <p>Test</p>\n</div>", doc.body().html());
    }
    
    @Test
    public void testParseBodyFragment_relaxed() {
        String bodyHtml = "<div><p>Test</div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<div>\n <p>Test</p>\n</div>", doc.body().html());
    }
    
    @Test
    public void testParse_comment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals(1, doc.childNodeSize());
        assertTrue(doc.childNode(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) doc.childNode(0)).getData());
    }
    
    @Test
    public void testParse_cdata() {
        String html = "<![CDATA[<h1>Hello</h1>]]>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals(1, doc.childNodeSize());
        assertTrue(doc.childNode(0) instanceof TextNode);
        assertEquals("<h1>Hello</h1>", ((TextNode) doc.childNode(0)).getWholeText());
    }
    
    @Test
    public void testParse_xmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals(1, doc.childNodeSize());
        assertTrue(doc.childNode(0) instanceof XmlDeclaration);
        assertEquals("1.0", ((XmlDeclaration) doc.childNode(0)).getVersion());
        assertEquals("UTF-8", ((XmlDeclaration) doc.childNode(0)).getCharset());
    }
    
    @Test
    public void testParse_endTag() {
        String html = "<div><p>Hello</p></div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<p>Hello</p>", doc.select("div").first().html());
    }
    
    @Test
    public void testParse_startTag() {
        String html = "<div><p>Hello</p></div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<div>\n <p>Hello</p>\n</div>", doc.body().html());
    }
    
    @Test
    public void testParse_attribute() {
        String html = "<a href=\"https://www.example.com\">Link</a>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.select("a").attr("href"));
        assertEquals("Link", doc.select("a").text());
    }
    
    @Test
    public void testParse_body() {
        String html = "<body><h1>Hello</h1><p>Test</p></body>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<h1>Hello</h1>\n<p>Test</p>", doc.body().html());
    }
    
    @Test
    public void testParse_data() {
        String html = "<script>var x = 5;</script>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("var x = 5;", doc.select("script").html());
    }
    
    @Test
    public void testParse_base() {
        String html = "<base href=\"https://www.example.com\">";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
    }
    
    @Test
    public void testParse_whitespace() {
        String html = "<div>\n <p>Test</p>\n</div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<div>\n <p>Test</p>\n</div>", doc.body().html());
    }
}