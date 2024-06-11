
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

    // Test case for Parser.parse(String html, String baseUri)
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        Elements headings = body.getElementsByTag("h1");
        assertEquals(1, headings.size());
        assertEquals("Hello World", headings.get(0).text());
    }

    // Test case for Parser.parseBodyFragment(String bodyHtml, String baseUri)
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        Elements headings = body.getElementsByTag("h1");
        assertEquals(1, headings.size());
        assertEquals("Hello World", headings.get(0).text());
    }

    // Test case for Parser.parseBodyFragmentRelaxed(String bodyHtml, String baseUri)
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World</h2>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        Elements headings = body.getElementsByTag("h1");
        assertEquals(1, headings.size());
        assertEquals("Hello World", headings.get(0).text());
    }

    // Additional test cases for edge cases and exceptions
    // TODO: Add additional test cases here

}

