
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.jsoup.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private static final String BASE_URI = "http://example.com";
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser();
    }

    @Test
    public void testParseMethod() {
        String html = "<div><p>Hello, world!</p></div>";
        Document doc = parser.parse(html, BASE_URI, false);
        assertEquals("http://example.com", doc.baseUri());
        assertTrue(doc.select("p").text().contains("Hello, world!"));
    }

    @Test
    public void testParseBodyFragmentMethod() {
        String bodyHtml = "<p>Hello, world!</p>";
        Document doc = parser.parseBodyFragment(bodyHtml, BASE_URI);
        assertEquals("http://example.com", doc.baseUri());
        assertTrue(doc.select("p").text().contains("Hello, world!"));
    }

    @Test
    public void testParseBodyFragmentMethodWithRelaxedParsing() {
        String bodyHtml = "<p>Hello, <tag>world</tag>!</p>";
        Document doc = parser.parseBodyFragment(bodyHtml, BASE_URI);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.getElementsByTag("tag").size());
    }
}
