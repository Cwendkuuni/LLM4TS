import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        parser = new Parser(html, baseUri, false);
    }
    
    @Test
    public void testParse() {
        Document document = parser.parse();
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("Test", document.title());
        assertNotNull(document.head());
        assertEquals(1, document.head().childNodeSize());
        assertNotNull(document.body());
        assertEquals(1, document.body().childNodeSize());
        assertEquals("h1", document.body().child(0).nodeName());
        assertEquals("Hello World", document.body().text());
    }
    
    @Test
    public void testParse_withEmptyHTML() {
        String html = "";
        String baseUri = "https://www.example.com";
        
        parser = new Parser(html, baseUri, false);
        
        Document document = parser.parse();
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("", document.title());
        assertNotNull(document.head());
        assertEquals(0, document.head().childNodeSize());
        assertNotNull(document.body());
        assertEquals(0, document.body().childNodeSize());
    }
    
    @Test
    public void testParse_withInvalidHTML() {
        String html = "<html><head><title>Test</title><body><h1>Hello World</h1></body>";
        String baseUri = "https://www.example.com";
        
        parser = new Parser(html, baseUri, false);
        
        Document document = parser.parse();
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("Test", document.title());
        assertNotNull(document.head());
        assertEquals(1, document.head().childNodeSize());
        assertNotNull(document.body());
        assertEquals(1, document.body().childNodeSize());
        assertEquals("h1", document.body().child(0).nodeName());
        assertEquals("Hello World", document.body().text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertNull(document.title());
        assertNotNull(document.head());
        assertEquals(0, document.head().childNodeSize());
        assertNotNull(document.body());
        assertEquals(1, document.body().childNodeSize());
        assertEquals("h1", document.body().child(0).nodeName());
        assertEquals("Hello World", document.body().text());
    }
    
    @Test
    public void testParseBodyFragment_withEmptyHTML() {
        String bodyHtml = "";
        String baseUri = "https://www.example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertNull(document.title());
        assertNotNull(document.head());
        assertEquals(0, document.head().childNodeSize());
        assertNotNull(document.body());
        assertEquals(0, document.body().childNodeSize());
    }
    
    @Test
    public void testParseBodyFragment_withInvalidHTML() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertNull(document.title());
        assertNotNull(document.head());
        assertEquals(0, document.head().childNodeSize());
        assertNotNull(document.body());
        assertEquals(1, document.body().childNodeSize());
        assertEquals("h1", document.body().child(0).nodeName());
        assertEquals("Hello World", document.body().text());
    }
}