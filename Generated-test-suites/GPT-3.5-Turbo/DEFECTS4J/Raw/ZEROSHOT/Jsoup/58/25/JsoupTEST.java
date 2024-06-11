import org.junit.Test;

import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.select("h1").text());
    }
    
    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        assertNotNull(doc);
        assertEquals("Test", doc.select("h1").text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Test", doc.select("h1").text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        assertNotNull(connection);
        assertEquals(url, connection.url());
    }

    @Test
    public void testParseFileWithCharset() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = Jsoup.parse(file, charsetName, baseUri);
            assertNotNull(doc);
            assertEquals("Test", doc.select("h1").text());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testParseFileWithoutCharset() {
        File file = new File("test.html");
        String charsetName = null;
        try {
            Document doc = Jsoup.parse(file, charsetName);
            assertNotNull(doc);
            assertEquals("Test", doc.select("h1").text());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testParseInputStreamWithCharset() {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = Jsoup.parse(in, charsetName, baseUri);
            assertNotNull(doc);
            assertEquals("Test", doc.select("h1").text());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testParseInputStreamWithoutCharset() {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = null;
        String baseUri = "http://example.com";
        try {
            Document doc = Jsoup.parse(in, charsetName, baseUri);
            assertNotNull(doc);
            assertEquals("Test", doc.select("h1").text());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Test</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertEquals("Test", doc.select("h1").text());
    }

    @Test
    public void testParseURL() {
        String url = "http://example.com";
        int timeoutMillis = 5000;
        try {
            Document doc = Jsoup.parse(new URL(url), timeoutMillis);
            assertNotNull(doc);
            assertEquals("Test", doc.select("h1").text());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertNotNull(cleanHtml);
        assertFalse(cleanHtml.contains("<script>"));
        assertEquals("Test", Jsoup.parse(cleanHtml).select("h1").text());
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<h1>Test</h1>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertNotNull(cleanHtml);
        assertFalse(cleanHtml.contains("<script>"));
        assertEquals("Test", Jsoup.parse(cleanHtml).select("h1").text());
    }

    @Test
    public void testCleanWithBaseUriAndWhitelistAndOutputSettings() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertNotNull(cleanHtml);
        assertFalse(cleanHtml.contains("<script>"));
        assertEquals("Test", Jsoup.parse(cleanHtml).select("h1").text());
        assertFalse(cleanHtml.contains("<br />"));
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Test</h1>";
        Whitelist whitelist = Whitelist.basic();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));

        String invalidHtml = "<h1>Test<script>alert('XSS')</script></h1>";
        assertFalse(Jsoup.isValid(invalidHtml, whitelist));
    }
}