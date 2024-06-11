
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_shouldReturnParsedDocument() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document expected = Jsoup.parse(html, baseUri);

        Document actual = Parser.parse(html, baseUri);

        assertEquals(expected, actual);
    }

    @Test
    public void testParse_shouldThrowExceptionWhenHtmlIsNull() {
        String html = null;
        String baseUri = "http://example.com";

        try {
            Parser.parse(html, baseUri);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("html must not be null", e.getMessage());
        }
    }

    @Test
    public void testParse_shouldThrowExceptionWhenBaseUriIsNull() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = null;

        try {
            Parser.parse(html, baseUri);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("baseUri must not be null", e.getMessage());
        }
    }

    @Test
    public void testParseBodyFragment_shouldReturnParsedDocument() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document expected = Jsoup.parseBodyFragment(bodyHtml, baseUri);

        Document actual = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertEquals(expected, actual);
    }

    @Test
    public void testParseBodyFragment_shouldThrowExceptionWhenBodyHtmlIsNull() {
        String bodyHtml = null;
        String baseUri = "http://example.com";

        try {
            Parser.parseBodyFragment(bodyHtml, baseUri);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("bodyHtml must not be null", e.getMessage());
        }
    }

    @Test
    public void testParseBodyFragment_shouldThrowExceptionWhenBaseUriIsNull() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = null;

        try {
            Parser.parseBodyFragment(bodyHtml, baseUri);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("baseUri must not be null", e.getMessage());
        }
    }

    @Test
    public void testParseBodyFragment_shouldHandleRelaxedParsing() {
        String bodyHtml = "<div><p>Lorem ipsum</p></div>";
        String baseUri = "http://example.com";
        Document expected = Jsoup.parseBodyFragment(bodyHtml, baseUri);

        Document actual = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertEquals(expected, actual);
    }
}

