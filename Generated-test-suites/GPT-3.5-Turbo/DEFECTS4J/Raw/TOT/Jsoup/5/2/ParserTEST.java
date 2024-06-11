import org.junit.Test;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello World!", doc.getElementsByTag("h1").first().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Fragment</h1><p>This is a fragment of HTML</p>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Fragment", doc.getElementsByTag("h1").first().text());
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals("This is a fragment of HTML", doc.getElementsByTag("p").first().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><span>Missing closing tag</span>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(1, doc.getElementsByTag("div").size());
        assertEquals(1, doc.getElementsByTag("span").size());
        assertEquals("Missing closing tag", doc.getElementsByTag("span").first().text());
    }

    @Test
    public void testParseInvalidHTML() {
        String html = "<html><head><title>Test</head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello World!", doc.getElementsByTag("h1").first().text());
    }

}