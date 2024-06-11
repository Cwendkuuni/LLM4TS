package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertTrue(doc.html().equals(html));
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href=\"/test\"/></head><body><a href=\"foo.html\">Go to foo</a></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com/test");
        String expected = "<html><head><base href=\"/test\">\n" +
                "<body>\n" +
                "<a href=\"foo.html\">Go to foo</a>\n" +
                "</body>\n" +
                "</html>";
        assertTrue(doc.html().equals(expected));
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser();
        Document doc = Jsoup.parse(html, "", parser);
        assertNotNull(doc);
        assertTrue(doc.html().equals(html));
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("https://example.com");
        Document doc = connection.get();
        assertNotNull(doc);
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<p><a href=\"foo.html\">Go to foo</a></p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "http://example.com");
        String expected = "<p><a href=\"foo.html\">Go to foo</a></p>";
        assertTrue(doc.html().equals(expected));
    }

    @Test
    public void testParseWithInvalidUrl() {
        assertThrows(IllegalArgumentException.class, () -> Jsoup.parse("file:/nonexistent.html"));
    }

    @Test
    public void testConnectWithInvalidUrl() {
        assertThrows(IllegalArgumentException.class, () -> Jsoup.connect("file:/nonexistent.html"));
    }
}