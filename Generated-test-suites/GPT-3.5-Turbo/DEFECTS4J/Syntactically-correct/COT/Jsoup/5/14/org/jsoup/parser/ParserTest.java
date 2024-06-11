
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        assertNotNull(document);
        assertEquals("<html>\n <head>\n  <title>Test</title>\n </head>\n <body>\n  <h1>Hello, World!</h1>\n </body>\n</html>", document.outerHtml());
    }
    
    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(html, baseUri);
        
        assertNotNull(document);
        assertEquals("<body>\n <h1>Hello, World!</h1>\n</body>", document.outerHtml());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragmentRelaxed(html, baseUri);
        
        assertNotNull(document);
        assertEquals("<body>\n <h1>Hello, World!</h1>\n</body>", document.outerHtml());
    }
    
    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        assertNotNull(document);
        assertTrue(document.childNode(0).nodeName().equals("#comment"));
        assertEquals("<!-- This is a comment -->", document.outerHtml());
    }
    
    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        assertNotNull(document);
        assertTrue(document.childNode(0).nodeName().equals("#cdata-section"));
        assertEquals("<![CDATA[This is CDATA]]>", document.outerHtml());
    }

    // Add more test cases for other methods as needed
    
} 
