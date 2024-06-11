import org.jsoup.nodes.Document;
import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello, world!", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, world!</h1>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello, world!", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, world!</h1>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello, world!", doc.select("h1").text());
    }

    @Test
    public void testParse_EmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("", doc.title());
        assertEquals("", doc.body().text());
    }

    @Test
    public void testParseBodyFragment_EmptyFragment() {
        String bodyHtml = "";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed_EmptyFragment() {
        String bodyHtml = "";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("", doc.body().text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_NullHtml() {
        String baseUri = "http://example.com";

        Parser.parse(null, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragment_NullFragment() {
        String baseUri = "http://example.com";

        Parser.parseBodyFragment(null, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentRelaxed_NullFragment() {
        String baseUri = "http://example.com";

        Parser.parseBodyFragmentRelaxed(null, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_NullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";

        Parser.parse(html, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragment_NullBaseUri() {
        String bodyHtml = "<h1>Hello, world!</h1>";

        Parser.parseBodyFragment(bodyHtml, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentRelaxed_NullBaseUri() {
        String bodyHtml = "<h1>Hello, world!</h1>";

        Parser.parseBodyFragmentRelaxed(bodyHtml, null);
    }

}