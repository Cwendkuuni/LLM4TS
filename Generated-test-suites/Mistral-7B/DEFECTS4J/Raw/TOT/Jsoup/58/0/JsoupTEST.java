import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsoupTest {

    @Test
    public void testParseSimpleHtml() throws IOException {
        // Test parsing a simple HTML string
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseHtmlWithBaseUri() throws IOException {
        // Test parsing HTML with a base URI
        String html = "<html><base href=\"http://example.com\"/><body><a href=\"about.html\">About</a></body></html>";
        Document doc = Jsoup.parse(html, "http://test.com");
        Element link = doc.selectFirst("a");
        assertEquals("http://example.com/about.html", link.absUrl(""));
    }

    @Test
    public void testParseHtmlWithCustomParser() throws IOException {
        // Test parsing HTML with a custom parser
        String html = "<xml><data>Hello World</data></xml>";
        Parser parser = Jsoup.parser().xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        assertEquals("Hello World", doc.select("data").text());
    }

    @Test
    public void testParseFile() throws IOException {
        // Test parsing a file
        File inputFile = new File("input.html");
        Document doc = Jsoup.parse(inputFile, "UTF-8");
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testConnectAndParse() throws IOException {
        // Test connecting to a URL and parsing HTML
        Document doc = Jsoup.connect("https://www.google.com").get();
        assertEquals("Google", doc.title());
    }

    @Test(expected = IOException.class)
    public void testParseInvalidHtml() throws IOException {
        // Test parsing invalid HTML
        String html = "<html><body><script>alert('Hello World');</script>";
        Document doc = Jsoup.parse(html);
        // This test should throw an IOException because of the script tag
    }

    @Test
    public void testCleanHtml() {
        // Test cleaning untrusted HTML
        String html = "<html><body><script>alert('Hello World');</script><div id=\"content\">Hello</div></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanedHtml = Jsoup.clean(html, "", whitelist);
        assertEquals("<div id=\"content\">Hello</div>", cleanedHtml);
    }

    @Test
    public void testIsValidHtml() {
        // Test checking if untrusted HTML is valid based on a whitelist
        String html = "<html><body><h1>Hello</h1><script>alert('World');</script></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        boolean isValid = Jsoup.isValid(html, whitelist);
        assertFalse(isValid);
    }
}