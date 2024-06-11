import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.junit.After;
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
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://www.example.com";
        Document document = parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello World!</p></div>";
        String baseUri = "http://www.example.com";
        Document document = parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertNotNull(document.body());
        assertEquals(1, document.body().children().size());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<span>Some text</span>";
        String baseUri = "http://www.example.com";
        Document document = parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertNotNull(document.body());
        assertEquals(1, document.body().children().size());
    }

    @Test
    public void testParseInvalidHtml() {
        String invalidHtml = "<hello>this is invalid<html>";
        String baseUri = "http://www.example.com";
        Document document = parser.parse(invalidHtml, baseUri);

        assertNull(document);
    }

    @After
    public void tearDown() {
        parser = null;
    }
}