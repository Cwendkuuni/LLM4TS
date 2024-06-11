package org.jsoup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><p>Hello world</p></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals(2, doc.childNodes().size());
        assertEquals("html", doc.tagName());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href='/test'></base></html><body><a href='about.html'></a></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("/test/about.html", doc.select("a[href]").get(0).attr("href"));
    }

    @Test
    public void testParseWithParser() {
        Parser parser = Jsoup.parser();
        String html = "<html><body><p>Hello world</p></body></html>";
        Document doc = parser.parseInput(html, "");
        assertNotNull(doc);
        assertEquals(2, doc.childNodes().size());
        assertEquals("html", doc.tagName());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("https://google.com");
        Document doc = connection.get();
        assertNotNull(doc);
    }

    @Test
    public void testParseFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, null);
        assertNotNull(doc);
    }

    @Test
    public void testParseFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(inputStream, null);
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello world</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("p", doc.body().tagName());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<p><a href='about.html'>Link to about page</a></p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "/base/");
        assertEquals("about.html", doc.select("a[href]").get(0).attr("href"));
    }

    @Test
    public void testParseUrl() throws IOException {
        Document doc = Jsoup.parse(new URL("https://google.com"), 5000);
        assertNotNull(doc);
    }

    @Test
    public void testClean() {
        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(html, "", whitelist);
        assertTrue(cleanHtml.isEmpty());
    }

    @Test
    public void testCleanWithBaseUri() {
        String html = "<html><body><img src='image.jpg'></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(html, "/base", whitelist);
        String expected = "<html><body><img src=\"image.jpg\"></body></html>";
        assertEquals(expected, cleanHtml);
    }

    @Test
    public void testIsValid() {
        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        assertFalse(Jsoup.isValid(html, whitelist));
    }

    @Test
    public void testIsValidWithWhitelist() {
        String html = "<html><body><p>Hello world <b>foo</b></p></body></html>";
        Whitelist whitelist = Whitelist.basic();
        assertTrue(Jsoup.isValid(html, whitelist));
    }
}