import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParseValidHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testParseHtmlWithBaseUri() {
        String html = "<img src=\"image.png\" alt=\"Image\">";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Element img = document.selectFirst("img");
        String imgUrl = img.attr("src");
        assertEquals("http://example.com/image.png", imgUrl);
    }

    @Test
    public void testParseHtmlWithCustomParser() {
        String html = "<xml><data>1</data></xml>";
        Parser parser = org.jsoup.parser.XmlParser.xmlParser();
        Document document = Jsoup.parse(html, "", parser);
        Element data = document.selectFirst("data");
        assertEquals("1", data.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        String html = "<html><body>Invalid HTML</body>"; // missing closing tag
        Document document = Jsoup.parse(html);
    }

    @Test
    public void testConnectAndParse() {
        String urlString = "https://example.com";
        Document document = Jsoup.connect(urlString).get();
        assertEquals("Example Domain", document.title());
    }

    @Test
    public void testParseFile() {
        String filePath = "test.html";
        Document document = null;
        try {
            document = Jsoup.parse(new File(filePath), "UTF-8");
        } catch (IOException e) {
            fail("Could not parse file: " + e.getMessage());
        }
        assertEquals("Test", document.title());
    }

    @Test
    public void testParseInputStream() {
        String filePath = "test.html";
        Document document = null;
        try (InputStream inputStream = new FileInputStream(new File(filePath))) {
            document = Jsoup.parse(inputStream, "UTF-8");
        } catch (FileNotFoundException | IOException e) {
            fail("Could not parse input stream: " + e.getMessage());
        }
        assertEquals("Test", document.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals("Test", document.body().text());
    }

    @Test
    public void testCleanHtml() {
        String html = "<html><body><script>alert('XSS')</script><p>Test</p></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanedHtml = Jsoup.clean(html, baseUri, whitelist);
        assertEquals("Test", cleanedHtml);
    }

    @Test
    public void testIsValidHtml() {
        String html = "<html><body><script>alert('XSS')</script><p>Test</p></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean isValid = Jsoup.isValid(html, whitelist);
        assertFalse(isValid);

        html = "<html><body><p>Test</p></body></html>";
        isValid = Jsoup.isValid(html, whitelist);
        assertTrue(isValid);
    }
}