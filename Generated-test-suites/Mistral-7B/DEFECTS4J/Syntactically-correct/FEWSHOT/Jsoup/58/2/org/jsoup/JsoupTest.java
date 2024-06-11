package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href=\"/test\">" + "</head><body>" + "<a href=\"foo.html\">Go to foo.html</a>" + "</body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("foo.html", doc.select("a[href]").attr("href"));
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("https://jsoup.org");
        Document doc = connection.get();
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertNotNull(doc);
        assertEquals("Test Page", doc.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("test.html");
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertNotNull(doc);
        assertEquals("Test Page", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<p><a href=\"foo.html\">Go to foo.html</a></p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "/test");
        assertNotNull(doc);
        assertEquals("foo.html", doc.select("a[href]").attr("href"));
    }

    @Test
    public void testParseWithTimeout() throws IOException {
        Connection connection = Jsoup.connect("https://jsoup.org");
        Document doc = connection.timeout(500).get();
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testClean() throws IOException {
        String bodyHtml = "<script src=\"evil.js\">foo</script><p>Hello World</p>";
        Whitelist whitelist = Whitelist.relaxed();
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
        doc.select("script").remove();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("<p>Hello World</p>", cleanHtml);
    }

    @Test
    public void testCleanWithBaseUri() throws IOException {
        String bodyHtml = "<img src=\"foo.jpg\" alt=\"image\">";
        Whitelist whitelist = Whitelist.simpleTagNames("p");
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "/test");
        doc.select("img").remove();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist, "/test");
        assertEquals("<p></p>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() throws IOException {
        String bodyHtml = "<p><img src=\"foo.jpg\" alt=\"image\">Hello World</p>";
        Whitelist whitelist = Whitelist.simpleTagNames("p");
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
        doc.select("img").remove();
        Document cleanDoc = Jsoup.parseBodyFragment(bodyHtml, "");
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist, cleanDoc.outputSettings(new Document.OutputSettings().prettyPrint(true)));
        assertEquals("<p>Hello World</p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<p><script>alert('Hello World');</script></p>";
        Whitelist whitelist = Whitelist.simpleTagNames("p");
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }
}