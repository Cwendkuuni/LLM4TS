import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello, world!</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertEquals("Test Page", doc.title());
        assertTrue(doc.hasText());
        assertEquals("Hello, world!", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello, world!</p></div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertTrue(doc.hasText());
        assertEquals("Hello, world!", doc.body().select("p").text());
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<div><p>Hello, world!</p></div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertTrue(doc.hasText());
        assertEquals("Hello, world!", doc.body().select("p").text());
    }

    @Test
    public void testParseWithComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertFalse(doc.hasText());
    }

    @Test
    public void testParseWithCdata() {
        String html = "<![CDATA[<p>Hello, world!</p>]]>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertTrue(doc.hasText());
        assertEquals("<p>Hello, world!</p>", doc.body().html());
    }

    @Test
    public void testParseWithXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertFalse(doc.hasText());
    }

    @Test
    public void testParseWithEndTag() {
        String html = "<p>Hello, world!</p>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertTrue(doc.hasText());
        assertEquals("Hello, world!", doc.body().select("p").text());
    }

    @Test
    public void testParseWithStartTag() {
        String html = "<div><p>Hello, world!</p></div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertTrue(doc.hasText());
        assertEquals("Hello, world!", doc.body().select("p").text());
    }

    @Test
    public void testParseWithAttribute() {
        String html = "<p class=\"highlight\">Hello, world!</p>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertTrue(doc.hasText());
        assertTrue(doc.body().select("p").hasClass("highlight"));
        assertEquals("Hello, world!", doc.body().select("p").text());
    }

    @Test
    public void testParseWithTextNode() {
        String html = "Hello, world!";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertTrue(doc.hasText());
        assertEquals("Hello, world!", doc.body().text());
    }

    @Test
    public void testParseWithBaseTag() {
        String html = "<base href=\"https://example.com\">";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertTrue(doc.hasText());
        assertEquals("https://example.com", doc.baseUri());
    }
}