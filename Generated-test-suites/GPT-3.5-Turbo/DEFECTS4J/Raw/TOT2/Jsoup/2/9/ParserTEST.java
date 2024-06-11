import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void testParseEdgeCases() {
        // Test parsing of CDATA section
        String html = "<![CDATA[This is some CDATA text]]>";
        Document document = Parser.parse(html, "http://example.com");
        assertEquals("This is some CDATA text", document.body().text());
        
        // Test parsing of XML declaration
        String xmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document xmlDocument = Parser.parse(xmlDecl, "http://example.com");
        assertNotNull(xmlDocument);
    }

    @Test
    public void testParseExceptions() {
        // Test for parsing exception when HTML is null
        try {
            Parser.parse(null, "http://example.com");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }

        // Test for parsing exception when baseUri is null
        try {
            Parser.parse("<h1>Hello</h1>", null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}