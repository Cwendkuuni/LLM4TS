import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser();
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("Test", document.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1><p>This is a test.</p>";
        String baseUri = "http://example.com";

        Document document = parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("Hello World!", document.selectFirst("h1").text());
        assertEquals("This is a test.", document.selectFirst("p").text());
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";

        Document document = parser.parse(html, baseUri);

        assertNotNull(document);
        assertNull(document.head());
        assertNull(document.body());
    }

    @Test
    public void testParseEmptyBodyFragment() {
        String bodyHtml = "";
        String baseUri = "http://example.com";

        Document document = parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertNull(document.head());
        assertNull(document.body());
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullHtml() {
        String baseUri = "http://example.com";

        parser.parse(null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentNullBodyHtml() {
        String baseUri = "http://example.com";

        parser.parseBodyFragment(null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";

        parser.parse(html, null);
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentNullBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1><p>This is a test.</p>";

        parser.parseBodyFragment(bodyHtml, null);
    }
}