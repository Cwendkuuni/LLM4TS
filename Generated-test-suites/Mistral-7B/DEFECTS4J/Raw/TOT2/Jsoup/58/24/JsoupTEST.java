import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullHtml() {
        String baseUri = "";
        Document document = Jsoup.parse(null, baseUri);
        assertNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyHtml() {
        String baseUri = "";
        Document document = Jsoup.parse("", baseUri);
        assertNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithMalformedHtml() {
        String baseUri = "";
        String html = "<html<body>";
        Document document = Jsoup.parse(html, baseUri);
        assertNull(document);
    }

    @Test
    public void testParseWithScriptTags() {
        String html = "<html><body><script>alert('Hello')</script><h1>Hello World</h1></body></html>";
        String baseUri = "";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseWithIOException() throws IOException {
        String baseUri = "";
        String html = "IOException test";
        Document document = Jsoup.parse(html, baseUri);
        assertNull(document);
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
    }

    @Test
    public void testParseWithBaseUriEmpty() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
    }

}