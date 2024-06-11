import org.junit.Test;
import org.jsoup.Jsoup;
import static org.junit.Assert.assertNotNull;

public class JsoupTest {

    @Test
    public void testParseString() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNull() {
        String html = null;
        Document doc = Jsoup.parse(html);
    }

    @Test
    public void testParseEmptyString() {
        String html = "";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href=\"http://example.com\"></base></head><body><a href=\"about.html\"></a></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com");
        assertNotNull(doc.select("a[href]").first());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        String html = "<html><head><base href=\"http://example.com\"></base></head><body><a href=\"about.html\"></a></body></html>";
        Document doc = Jsoup.parse(html, (String) null);
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Jsoup.parser();
        Document doc = Jsoup.parse(html, "", parser);
        assertNotNull(doc);
    }

    // Add more test cases for other methods as needed
}