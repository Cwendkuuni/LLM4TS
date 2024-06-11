import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
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
        assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>This is a paragraph</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("This is a paragraph", doc.body().select("p").text());
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertTrue(doc.body().children().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        String html = null;
        String baseUri = "http://example.com";
        Parser.parse(html, baseUri);
    }

    @Test
    public void testParseEmptyBodyFragment() {
        String bodyHtml = "";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("", doc.body().html());
    }

    @Test
    public void testParseEmptyBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseSpecialCharacters() {
        String html = "<html><head><title>&lt;&gt;&amp;&quot;&apos;</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("<>\"'", doc.title());
    }

    @Test
    public void testParseInvalidTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h2></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertTrue(doc.body().select("h1").isEmpty());
    }

    @Test
    public void testParseUnclosedTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertTrue(doc.body().select("h1").isEmpty());
    }
}