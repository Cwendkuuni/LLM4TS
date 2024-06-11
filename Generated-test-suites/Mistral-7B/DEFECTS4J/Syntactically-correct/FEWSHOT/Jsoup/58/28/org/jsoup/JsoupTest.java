package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParse_String() {
        String html = "<html><head></head><body><p>Hello World!</p></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParse_String_BaseUri() {
        String html = "<html><head></head><body><img src=\"image.png\"></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertEquals("image.png", doc.images().first().attr("src"));
    }

    @Test
    public void testParse_String_Parser() {
        String html = "<html><head></head><body><p>Hello World!</p></body></html>";
        Parser parser = new Parser();
        Document doc = Jsoup.parse(html, "", parser);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testConnect() {
        Connection conn = Jsoup.connect("http://example.com");
        Document doc = conn.get();
        assertNotNull(doc);
    }

    @Test
    public void testParse_File() throws IOException {
        File file = new File("input.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParse_File_CharsetName_BaseUri() throws IOException {
        File file = new File("input.html");
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(file, "UTF-8", baseUri);
        assertEquals("image.png", doc.images().first().attr("src"));
    }

    @Test
    public void testParse_InputStream() throws IOException {
        File file = new File("input.html");
        InputStream in = new FileInputStream(file);
        Document doc = Jsoup.parse(in, "UTF-8");
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParse_InputStream_CharsetName_BaseUri() throws IOException {
        File file = new File("input.html");
        InputStream in = new FileInputStream(file);
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(in, "UTF-8", baseUri);
        assertEquals("image.png", doc.images().first().attr("src"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragment_String_BaseUri() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("image.png", doc.images().first().attr("src"));
    }

    @Test
    public void testParseBodyFragment_File() throws IOException {
        File file = new File("input.html");
        Document doc = Jsoup.parseBodyFragment(file);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragment_File_BaseUri() throws IOException {
        File file = new File("input.html");
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(file, baseUri);
        assertEquals("image.png", doc.images().first().attr("src"));
    }

    @Test
    public void testParse_Url() throws IOException {
        URL url = new URL("http://example.com");
        Document doc = Jsoup.parse(url, 5000);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testClean() throws IOException {
        String bodyHtml = "<html><head></head><body><script>alert('XSS')</script><p>Hello World!</p></body></html>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals(cleanHtml, doc.body().html());
    }

    @Test
    public void testClean_String_BaseUri() {
        String bodyHtml = "<html><head><script>alert('XSS')</script></head><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals(cleanHtml, doc.body().html());
    }

    @Test
    public void testClean_String_Whitelist() {
        String bodyHtml = "<html><head><script>alert('XSS')</script></head><body><p>Hello World!</p></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals(cleanHtml, bodyHtml);
    }

    @Test
    public void testClean_File() throws IOException {
        File file = new File("input.html");
        Document doc = Jsoup.parseBodyFragment(file);
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(file.getAbsolutePath(), whitelist);
        assertEquals(cleanHtml, doc.body().html());
    }

    @Test
    public void testClean_File_BaseUri() throws IOException {
        File file = new File("input.html");
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(file, baseUri);
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(file.getAbsolutePath(), whitelist, baseUri);
        assertEquals(cleanHtml, doc.body().html());
    }

    @Test
    public void testClean_File_Whitelist() throws IOException {
        File file = new File("input.html");
        Document doc = Jsoup.parseBodyFragment(file);
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(file.getAbsolutePath(), whitelist);
        assertEquals(cleanHtml, doc.body().html());
    }

    @Test
    public void testClean_File_BaseUri_Whitelist() throws IOException {
        File file = new File("input.html");
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(file, baseUri);
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(file.getAbsolutePath(), whitelist, baseUri);
        assertEquals(cleanHtml, doc.body().html());
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><head><script>alert('XSS')</script></head><body><p>Hello World!</p></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testIsValid_String_BaseUri() {
        String bodyHtml = "<html><head><script>alert('XSS')</script></head><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        assertFalse(Jsoup.isValid(bodyHtml, whitelist(), baseUri));
    }
}