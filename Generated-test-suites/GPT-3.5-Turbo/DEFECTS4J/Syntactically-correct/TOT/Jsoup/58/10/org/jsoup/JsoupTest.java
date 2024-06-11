import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        
        Assertions.assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.toString());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);

        Assertions.assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.toString());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Document document = Jsoup.parse(html);

        Assertions.assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.toString());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);

        Assertions.assertNotNull(connection);
    }

    @Test
    public void testParseFileWithCharsetNameAndBaseUri() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = Jsoup.parse(file, charsetName, baseUri);

            Assertions.assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseFileWithCharsetName() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        try {
            Document document = Jsoup.parse(file, charsetName);

            Assertions.assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = Jsoup.parse(inputStream, charsetName, baseUri);

            Assertions.assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInputStreamWithCharsetName() {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        try {
            Document document = Jsoup.parse(inputStream, charsetName);

            Assertions.assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);

        Assertions.assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.toString());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Test</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);

        Assertions.assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.toString());
    }

    @Test
    public void testParseURLWithTimeout() {
        String url = "http://example.com";
        int timeoutMillis = 5000;
        try {
            Document document = Jsoup.parse(new URL(url), timeoutMillis);

            Assertions.assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<script>alert('XSS')</script><h1>Test</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);

        Assertions.assertEquals("<html><head></head><body>&lt;script&gt;alert('XSS')&lt;/script&gt;<h1>Test</h1></body></html>", cleanedHtml);
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<script>alert('XSS')</script><h1>Test</h1>";
        Whitelist whitelist = Whitelist.basic();
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);

        Assertions.assertEquals("<html><head></head><body>&lt;script&gt;alert('XSS')&lt;/script&gt;<h1>Test</h1></body></html>", cleanedHtml);
    }

    @Test
    public void testCleanWithBaseUriWhitelistAndOutputSettings() {
        String bodyHtml = "<script>alert('XSS')</script><h1>Test</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);

        Assertions.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  &lt;script&gt;alert('XSS')&lt;/script&gt;\n" +
                "  <h1>Test</h1>\n" +
                " </body>\n" +
                "</html>", cleanedHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Test</h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);

        Assertions.assertTrue(isValid);
    }
}