import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div>Hello World</div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        assertEquals("Hello World", doc.select("div").text());
    }
}