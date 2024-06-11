import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParseNormalHtml() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document document = Jsoup.parse(html);

        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseWithBaseUri() {
        String baseUri = "http://example.com";
        String html = "<img src=\"images/image.png\" alt=\"Image\">";
        Document document = Jsoup.parse(html, baseUri);

        Element img = document.select("img").first();
        String imgSrc = img.attr("src");
        Assert.assertEquals("http://example.com/images/image.png", imgSrc);
    }

    @Test
    public void testParseWithCustomParser() {
        Parser parser = Parser.xmlParser();
        String xmlHtml = "<xml><message>Hello World</message></xml>";
        Document document = Jsoup.parse(xmlHtml, "", parser);

        Element message = document.select("message").first();
        String messageText = message.text();
        Assert.assertEquals("Hello World", messageText);
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        Document document = Jsoup.parse(html);

        Assert.assertNull(document.title());
        Assert.assertEquals("", document.body().text());
    }

    @Test(expected = IOException.class)
    public void testParseIOException() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document document = null;

        try {
            document = Jsoup.parse(new File("non-existent-file.html"), "UTF-8");
        } catch (IOException e) {
            // expected
        } finally {
            Assert.assertNull(document);
        }
    }

    @Test
    public void testConnectToUrl() {
        String url = "https://www.google.com";
        Document document = Jsoup.connect(url).get();

        Assert.assertNotNull(document);
        Assert.assertEquals("Google", document.title());
    }

    @Test
    public void testParseFile() {
        String filename = "input.html";
        Document document = null;

        try {
            document = Jsoup.parse(new File(filename), "UTF-8");
        } catch (IOException e) {
            // handle exception
        }

        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.title());
    }

    @Test
    public void testParseInputStream() {
        String filename = "input.html";
        Document document = null;

        try (InputStream inputStream = Jsoup.class.getResourceAsStream(filename)) {
            document = Jsoup.parse(inputStream, "UTF-8");
        }

        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.title());
    }

    @Test
    public void testCleanWhitelist() {
        String bodyHtml = "<html><body><script src=\"malicious-script.js\"></script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);

        Assert.assertEquals("<html><body></body></html>", cleanHtml);
    }

    @Test
    public void testCleanWhitelistWithBaseUri() {
        String baseUri = "http://example.com";
        String bodyHtml = "<img src=\"images/image.png\" alt=\"Image\">";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);

        Assert.assertEquals("<html><body></body></html>", cleanHtml);
    }

    @Test
    public void testCleanWithInvalidInput() {
        String bodyHtml = "<script>alert('XSS')</script>";
        Whitelist whitelist = Whitelist.simpleText();

        try {
            Jsoup.clean(bodyHtml, "", whitelist);
            Assert.fail("Expected IOException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsValidBodyHtml() {
        String bodyHtml = "<p id=\"test\">Hello World</p>";
        Whitelist whitelist = Whitelist.simpleText();

        Assert.assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testIsInvalidBodyHtml() {
        String bodyHtml = "<script>alert('XSS')</script>";
        Whitelist whitelist = Whitelist.simpleText();

        Assert.assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }
}