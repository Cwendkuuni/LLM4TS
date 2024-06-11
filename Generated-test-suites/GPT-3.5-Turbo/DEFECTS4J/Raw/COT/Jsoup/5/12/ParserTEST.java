
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.parser.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        String expectedTitle = "Test";
        String actualTitle = doc.title();
        assertEquals(expectedTitle, actualTitle);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        String expectedBodyHtml = "<body><p>Hello</p></body>";
        String actualBodyHtml = doc.body().html();
        assertEquals(expectedBodyHtml, actualBodyHtml);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        String expectedBodyHtml = "<body><p>Hello</p></body>";
        String actualBodyHtml = doc.body().html();
        assertEquals(expectedBodyHtml, actualBodyHtml);
    }

    @Test
    public void testParse_withEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        String expectedTitle = "";
        String actualTitle = doc.title();
        assertEquals(expectedTitle, actualTitle);
    }

    @Test(expected = NullPointerException.class)
    public void testParse_withNullHtml() {
        String html = null;
        String baseUri = "http://example.com";
        Parser.parse(html, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParse_withNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><p>Hello</p></body></html>";
        String baseUri = null;
        Parser.parse(html, baseUri);
    }

    @Test
    public void testParseBodyFragment_withEmptyHtml() {
        String bodyHtml = "";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        String expectedBodyHtml = "<body></body>";
        String actualBodyHtml = doc.body().html();
        assertEquals(expectedBodyHtml, actualBodyHtml);
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragment_withNullBodyHtml() {
        String bodyHtml = null;
        String baseUri = "http://example.com";
        Parser.parseBodyFragment(bodyHtml, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragment_withNullBaseUri() {
        String bodyHtml = "<p>Hello</p>";
        String baseUri = null;
        Parser.parseBodyFragment(bodyHtml, baseUri);
    }

    @Test
    public void testParseBodyFragmentRelaxed_withEmptyHtml() {
        String bodyHtml = "";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        String expectedBodyHtml = "<body></body>";
        String actualBodyHtml = doc.body().html();
        assertEquals(expectedBodyHtml, actualBodyHtml);
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentRelaxed_withNullBodyHtml() {
        String bodyHtml = null;
        String baseUri = "http://example.com";
        Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentRelaxed_withNullBaseUri() {
        String bodyHtml = "<p>Hello</p>";
        String baseUri = null;
        Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    }
}

