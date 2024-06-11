import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;

public class JsoupTest {

    @Test
    public void testParseStringString() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
        assertEquals("Hello World!", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseStringStringParser() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        assertNotNull(document);
        assertEquals("Hello World!", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseString() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
        assertEquals("Hello World!", document.getElementsByTag("h1").text());
    }

    @Test
    public void testConnect() {
        String url = "https://www.example.com";
        Connection connection = Jsoup.connect(url);
        assertNotNull(connection);
        assertEquals(url, connection.request().url().toString());
    }

    @Test
    public void testParseFileStringString() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        try {
            Document document = Jsoup.parse(file, charsetName, baseUri);
            assertNotNull(document);
            assertEquals("Hello World!", document.getElementsByTag("h1").text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseFileString() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        try {
            Document document = Jsoup.parse(file, charsetName);
            assertNotNull(document);
            assertEquals("Hello World!", document.getElementsByTag("h1").text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInputStreamStringString() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        try {
            Document document = Jsoup.parse(inputStream, charsetName, baseUri);
            assertNotNull(document);
            assertEquals("Hello World!", document.getElementsByTag("h1").text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInputStreamStringStringParser() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document document = Jsoup.parse(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
            assertEquals("Hello World!", document.getElementsByTag("h1").text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseBodyFragmentStringString() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals("Hello World!", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseBodyFragmentString() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(document);
        assertEquals("Hello World!", document.getElementsByTag("h1").text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals("<h1>Hello World!</h1>", cleanHtml);
    }

    @Test
    public void testCleanStringWhitelist() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("<h1>Hello World!</h1>", cleanHtml);
    }

    @Test
    public void testCleanStringStringWhitelistOutputSettings() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals("<h1>Hello World!</h1>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }
}