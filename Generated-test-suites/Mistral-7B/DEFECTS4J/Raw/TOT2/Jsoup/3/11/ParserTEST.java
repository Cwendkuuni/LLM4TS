import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ParserTest {
    @Test
    public void testParseValidHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.body().getElementsByTag("h1").first().text());
    }

    @Test
    public void testParseEmptyHtml() {
        Document doc = Parser.parse("", "http://example.com");
        assertNotNull(doc);
        assertEquals("", doc.html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        String html = "<html<title";
        Parser.parse(html, "http://example.com");
    }
}