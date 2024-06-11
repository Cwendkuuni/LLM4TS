import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private String html;
    private String baseUri;

    @Before
    public void setUp() {
        html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        baseUri = "https://example.com";
    }

    @After
    public void tearDown() {
        // Clean up after each test
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Fragment</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Fragment", doc.body().select("h1").text());
    }

    @Test
    public void testParseEmptyHtml() {
        String emptyHtml = "";
        Document doc = Parser.parse(emptyHtml, baseUri);
        assertNotNull(doc);
        assertEquals(0, doc.childNodeSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        String nullHtml = null;
        Parser.parse(nullHtml, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        String nullBaseUri = null;
        Parser.parse(html, nullBaseUri);
    }

    // Add more test cases for each method...

}