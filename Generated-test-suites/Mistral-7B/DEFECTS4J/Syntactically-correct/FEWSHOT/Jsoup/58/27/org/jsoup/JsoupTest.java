package org.jsoup;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.jsoup.helper.Jsoup.parse;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;

public class JsoupTest {

    @Test
    public void testParseHtml() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document doc = parse(html);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseHtmlWithBaseUri() {
        String html = "<html><head><base href=\"/test\"></head><body><a href=\"about.html\">About</a></body></html>";
        Document doc = parse(html, "http://example.com/test");
        assertEquals("http://example.com/test/about.html", doc.select("a[href]").attr("abs:href"));
    }

    @Test
    public void testParseHtmlWithWhitelist() {
        String html = "<html><head></head><body><div id=\"content\" class=\"entry\">Hello World</div></body></html>";
        Document doc = parse(html, new Whitelist(Tag.DIV, Tag.CLASS));
        Element content = doc.selectFirst("#content");
        assertEquals("Hello World", content.text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        assertEquals("http://example.com", doc.location().toString());
    }

    @Test
    public void testParseFile() throws IOException {
        String fileName = "test.html";
        File file = new File(fileName);
        Document doc = Jsoup.parse(file, "UTF-8");
        assertEquals("UTF-8", doc.charset());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String fileName = "test.html";
        File file = new File(fileName);
        InputStream inputStream = new FileInputStream(file);
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertEquals("UTF-8", doc.charset());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello World</div>";
        Document doc = parseBodyFragment(bodyHtml);
        assertEquals("Hello World", doc.body().html());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<div><a href=\"about.html\">About</a></div>";
        Document doc = parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals("http://example.com/about.html", doc.select("a[href]").attr("abs:href"));
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><head><script>alert('Hello World')</script></head><body>Hello World</body></html>";
        String cleanHtml = Jsoup.clean(bodyHtml, new Whitelist(Tag.BODY));
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<html><head><script src=\"script.js\"></script></head><body><a href=\"about.html\">About</a></body></html>";
        String cleanHtml = Jsoup.clean(bodyHtml, new Whitelist(Tag.A), "http://example.com");
        assertEquals("About", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<html><head></head><body><p><script>alert('Hello World')</script>Hello World</p></body></html>";
        String cleanHtml = Jsoup.clean(bodyHtml, new Whitelist(Tag.P), new Document.OutputSettings().prettyPrint(true).escapeMode(Document.EscapeMode.HTML_QUIRKS));
        assertEquals("<p>Hello World</p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<p><script>alert('Hello World')</script>Hello World</p>";
        boolean isValid = Jsoup.isValid(bodyHtml, new Whitelist(Tag.P));
        assertFalse(isValid);
    }

    private Document parseBodyFragment(String bodyHtml) {
        return parseBodyFragment(bodyHtml, "");
    }

    private Document parseBodyFragment(String bodyHtml, String baseUri) {
        Parser parser = new Parser.MalformedHtmlParser();
        return parser.parseBodyFragment(bodyHtml, baseUri);
    }
}