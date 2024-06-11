import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class JsoupTest {
    @Test
    public void testParseValidHtml() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
    }

    @Test
    public void testParseHtmlWithBaseUri() {
        String html = "<img src=\"image.jpg\" />";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        String imgSrc = doc.select("img[src]").first().attr("src");
        String expectedImgSrc = baseUri + "/image.jpg";
        assert(imgSrc.equals(expectedImgSrc));
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body><script>alert('XSS')</script></body></html>";
        try {
            Document doc = Jsoup.parse(html);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}