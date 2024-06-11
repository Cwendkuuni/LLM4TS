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

    @After
    public void tearDown() {
        parser = null;
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Hello</title></head><body><h1>Heading</h1></body></html>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Hello", doc.title());
        assertEquals("Heading", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Content</div>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.body().children().size());
        assertEquals("Content", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<span>Text<span>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.body().children().size());
        assertEquals("Text", doc.body().text());
    }

    @Test
    public void testParseWithEmptyHtml() {
        String html = "";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("", doc.text());
    }

    @Test
    public void testParseBodyFragmentWithEmptyHtml() {
        String bodyHtml = "";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("", doc.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        String baseUri = "http://www.example.com";

        Parser.parse(null, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithNullHtml() {
        String baseUri = "http://www.example.com";

        Parser.parseBodyFragment(null, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        String html = "<html><head><title>Hello</title></head><body><h1>Heading</h1></body></html>";

        Parser.parse(html, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithNullBaseUri() {
        String bodyHtml = "<div>Content</div>";

        Parser.parseBodyFragment(bodyHtml, null);
    }
}