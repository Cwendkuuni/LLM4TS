import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        
        Element htmlElement = doc.getElementsByTag("html").first();
        assertNotNull(htmlElement);
        assertEquals("html", htmlElement.tagName());
        
        Element headElement = doc.getElementsByTag("head").first();
        assertNotNull(headElement);
        assertEquals("head", headElement.tagName());
        
        Element titleElement = doc.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("title", titleElement.tagName());
        assertEquals("Test", titleElement.text());
        
        Element bodyElement = doc.getElementsByTag("body").first();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.tagName());
        
        Element h1Element = doc.getElementsByTag("h1").first();
        assertNotNull(h1Element);
        assertEquals("h1", h1Element.tagName());
        assertEquals("Hello, World!", h1Element.text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        
        Element bodyElement = doc.getElementsByTag("body").first();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.tagName());
        
        Element h1Element = doc.getElementsByTag("h1").first();
        assertNotNull(h1Element);
        assertEquals("h1", h1Element.tagName());
        assertEquals("Hello, World!", h1Element.text());
    }
    
    @Test
    public void testParseComment() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseComment();
        
        Document doc = parser.doc;
        assertNotNull(doc);
        
        Element lastElement = parser.last();
        assertNotNull(lastElement);
        assertTrue(lastElement instanceof Comment);
        
        Comment comment = (Comment)lastElement;
        assertEquals("", comment.getData());
        assertEquals("http://example.com", comment.baseUri());
    }
    
    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseXmlDecl();
        
        Document doc = parser.doc;
        assertNotNull(doc);
        
        Element lastElement = parser.last();
        assertNotNull(lastElement);
        assertTrue(lastElement instanceof XmlDeclaration);
        
        XmlDeclaration decl = (XmlDeclaration)lastElement;
        assertEquals("", decl.getData());
        assertEquals("http://example.com", decl.baseUri());
        assertFalse(decl.isProcessingInstruction());
    }
    
    // ... (add more test methods to comprehensively test all methods in the Parser class)

}