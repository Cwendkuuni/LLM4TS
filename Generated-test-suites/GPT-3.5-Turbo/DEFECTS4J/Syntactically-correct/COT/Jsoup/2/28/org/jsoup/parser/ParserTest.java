import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("", doc.text());
    }

    @Test
    public void testParseCDATA() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("This is CDATA", doc.text());
    }

    // Add additional test cases for other methods

}