import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParse() {
        // Typical test case
        String html = "<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello, world!", doc.body().text());
    }
    
    @Test
    public void testParseBodyFragment() {
        // Typical test case
        String bodyHtml = "<p>Fragment</p>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals(1, doc.body().children().size());
        assertEquals("Fragment", doc.body().text());
    }
    
    @Test
    public void testParseComment() {
        // Edge case: empty comment
        String html = "<!--  -->";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof Comment);
    }
    
    @Test
    public void testParseXmlDecl() {
        // Edge case: processing instruction
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof XmlDeclaration);
    }
    
    @Test
    public void testParseTextNode() {
        // Typical test case
        String html = "<p>Hello, world!</p>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(1, doc.body().children().size());
        assertTrue(doc.body().childNode(0) instanceof TextNode);
    }
    
    // Additional test cases for other methods can be added here
    
}