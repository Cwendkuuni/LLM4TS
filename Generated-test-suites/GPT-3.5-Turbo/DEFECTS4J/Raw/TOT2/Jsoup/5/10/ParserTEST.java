import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().getElementById("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test fragment</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Test fragment", doc.body().select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<span>Unclosed tag";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Unclosed tag", doc.body().text());
    }

    @Test
    public void testParseInvalidHTML() {
        String html = "<div><p>Test</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNull(doc);
    }

    @Test
    public void testParseEmptyHTML() {
        String html = "";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(0, doc.getAllElements().size());
    }
}