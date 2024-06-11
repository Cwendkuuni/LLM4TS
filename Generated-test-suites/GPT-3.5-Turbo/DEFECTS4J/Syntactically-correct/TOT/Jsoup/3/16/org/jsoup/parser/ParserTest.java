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
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello world!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test Page", doc.title());
        assertEquals("Hello world!", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Test content</div>";
        String baseUri = "https://www.example.com";
        Document doc = parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Test content", doc.body().text());
    }

    @Test
    public void testParseBodyFragment_EmptyHtml() {
        String bodyHtml = "";
        String baseUri = "https://www.example.com";
        Document doc = parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("", doc.body().text());
    }

    @Test(expected = NullPointerException.class)
    public void testParse_NullHtml() {
        String baseUri = "https://www.example.com";
        parser.parse(null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParse_NullBaseUri() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello world!</h1></body></html>";
        parser.parse(html, null);
    }

    @Test
    public void testParseBodyFragment_RelativeUrl() {
        String bodyHtml = "<a href='page.html'>Link</a>";
        String baseUri = "https://www.example.com";
        Document doc = parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com/page.html", doc.select("a").attr("href"));
    }
}