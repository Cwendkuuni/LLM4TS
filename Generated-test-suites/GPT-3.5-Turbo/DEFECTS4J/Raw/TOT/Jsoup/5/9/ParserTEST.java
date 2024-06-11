import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class ParserTest {

    @Test
    public void testParse() {
        // Typical use case
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertEquals("Test", doc.title());

        // Edge case - Empty HTML
        html = "";
        baseUri = "http://example.com";
        doc = Parser.parse(html, baseUri);
        assertEquals("", doc.toString());

        // Error scenario - Null HTML and baseUri
        html = null;
        baseUri = null;
        doc = Parser.parse(html, baseUri);
        assertNull(doc);

        // Error scenario - Null HTML
        html = null;
        baseUri = "http://example.com";
        doc = Parser.parse(html, baseUri);
        assertNull(doc);

        // Error scenario - Null baseUri
        html = "<html></html>";
        baseUri = null;
        doc = Parser.parse(html, baseUri);
        assertNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case
        String bodyHtml = "<div><p>Hello World</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("div", doc.body().child(0).tagName());

        // Edge case - Empty HTML
        bodyHtml = "";
        baseUri = "http://example.com";
        doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(0, doc.body().childNodeSize());

        // Error scenario - Null HTML and baseUri
        bodyHtml = null;
        baseUri = null;
        doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNull(doc);

        // Error scenario - Null HTML
        bodyHtml = null;
        baseUri = "http://example.com";
        doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNull(doc);

        // Error scenario - Null baseUri
        bodyHtml = "<div></div>";
        baseUri = null;
        doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNull(doc);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Typical use case
        String bodyHtml = "<div><p>Hello World</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertEquals("div", doc.body().child(0).tagName());

        // Edge case - Empty HTML
        bodyHtml = "";
        baseUri = "http://example.com";
        doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertEquals(0, doc.body().childNodeSize());

        // Error scenario - Null HTML and baseUri
        bodyHtml = null;
        baseUri = null;
        doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNull(doc);

        // Error scenario - Null HTML
        bodyHtml = null;
        baseUri = "http://example.com";
        doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNull(doc);

        // Error scenario - Null baseUri
        bodyHtml = "<div></div>";
        baseUri = null;
        doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNull(doc);
    }
}