import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
    
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html>\n" +
                    " <head>\n" +
                    "  <title>Test</title>\n" +
                    " </head>\n" +
                    " <body>\n" +
                    "  <h1>Hello World</h1>\n" +
                    " </body>\n" +
                    "</html>", document.outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
    
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<body>\n" +
                        " <h1>Hello World</h1>\n" +
                        "</body>", document.body().outerHtml());
    }

    @Test
    public void testParse_withEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
    
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testParseBodyFragment_withEmptyBodyHtml() {
        String bodyHtml = "";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("", document.body().outerHtml());
    }

    @Test(expected = NullPointerException.class)
    public void testParse_withNullHtml() {
        String html = null;
        String baseUri = "http://example.com";
        
        Parser.parse(html, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragment_withNullBodyHtml() {
        String bodyHtml = null;
        String baseUri = "http://example.com";
        
        Parser.parseBodyFragment(bodyHtml, baseUri);
    }

}