import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private String html;
    private String baseUri;

    @Before
    public void setUp() {
        html = "<html><head><title>Test HTML</title></head><body><p>Test Paragraph</p></body></html>";
        baseUri = "http://example.com";
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html>\n" +
                " <head>\n" +
                "  <title>Test HTML</title>\n" +
                " </head>\n" +
                " <body>\n" +
                "  <p>Test Paragraph</p>\n" +
                " </body>\n" +
                "</html>", doc.outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test Paragraph</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<body>\n" +
                " <p>Test Paragraph</p>\n" +
                "</body>", doc.outerHtml());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<img src=\"test.jpg\">";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<body>\n" +
                " <img src=\"http://example.com/test.jpg\">\n" +
                "</body>", doc.outerHtml());
    }

    @Test
    public void testParse_withInvalidHtml() {
        String invalidHtml = "<html><head><title>Test HTML<title><head><body><p>Test Paragraph</p><body>";
        Document doc = Parser.parse(invalidHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html>\n" +
                " <body>\n" +
                "  <p>Test Paragraph</p>\n" +
                " </body>\n" +
                "</html>", doc.outerHtml());
    }

    @Test
    public void testParseBodyFragment_withInvalidHtml() {
        String invalidBodyHtml = "<p>Test Paragraph<p>";
        Document doc = Parser.parseBodyFragment(invalidBodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<body>\n" +
                " <p>Test Paragraph</p>\n" +
                "</body>", doc.outerHtml());
    }

    @Test
    public void testParseBodyFragmentRelaxed_withInvalidHtml() {
        String invalidBodyHtml = "<div><p>Test Paragraph</div>";
        Document doc = Parser.parseBodyFragmentRelaxed(invalidBodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<body>\n" +
                " <div>\n" +
                "  <p>Test Paragraph</p>\n" +
                " </div>\n" +
                "</body>", doc.outerHtml());
    }
}