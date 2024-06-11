import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private String html;
    private String baseUri;

    @Before
    public void setUp() {
        html = "<html>\n" +
                "<head>\n" +
                "<title>Test Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test Heading</h1>\n" +
                "<p>Test paragraph</p>\n" +
                "</body>\n" +
                "</html>";
        baseUri = "https://www.example.com";
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("html", doc.tagName());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>Test Heading 2</h2>\n" +
                "<p>Test paragraph 2</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("html", doc.tagName());
        assertNotNull(doc.body());
        assertEquals("head", doc.head().tagName());
        assertNotNull(doc.body().getElementsByTag("h2"));
        assertNotNull(doc.body().getElementsByTag("p"));
    }

    @Test
    public void testParseWithEmptyHtml() {
        Document doc = Parser.parse("", baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertTrue(doc.childNodes().isEmpty());
    }

    @Test
    public void testParseWithEmptyBaseUri() {
        Document doc = Parser.parse(html, "");
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
        assertEquals("html", doc.tagName());
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullHtml() {
        Parser.parse(null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullBaseUri() {
        Parser.parse(html, null);
    }
}