import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, world!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello, world!</h1>\n </body>\n</html>", doc.toString());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a paragraph</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <p>This is a paragraph</p>\n </body>\n</html>", doc.toString());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>This is a paragraph</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <p>This is a paragraph</p>\n </body>\n</html>", doc.toString());
    }

    // Add more test methods to cover all the methods in the Parser class

}