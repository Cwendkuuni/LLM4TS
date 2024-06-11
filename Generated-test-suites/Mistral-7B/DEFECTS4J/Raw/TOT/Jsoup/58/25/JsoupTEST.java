package org.jsoup.test;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Xml;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParseSimpleHtml() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseHtmlWithBaseUri() {
        String html = "<img src=\"image.png\" alt=\"Image\">";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertEquals("image.png", doc.select("img[src]").attr("abs:src"));
    }

    @Test
    public void testParseHtmlWithCustomParser() {
        String xml = "<root><child/></root>";
        Parser parser = Jsoup.parser().xmlParser();
        Document doc = Jsoup.parse(xml, "", parser);
        assertEquals(Xml.Tag.ROOT, doc.tagName());
        assertEquals(1, doc.childNodes().size());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body><script>alert('Hello')</script><h1>Hello World</h1></body></html>";
        try {
            Document doc = Jsoup.parse(html);
            fail("Expected an IOException or HttpStatusException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testConnectAndParse() {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        assertEquals("Example Domain", doc.title());
    }

    @Test
    public void testParseFile() {
        String fileName = "test.html";
        String html = "<html><body><h1>File Test</h1></body></html>";
        File file = new File(fileName);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(html);
            fileWriter.close();

            Document doc = Jsoup.parse(file);
            assertEquals("File Test", doc.select("h1").text());

            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to write file for test");
        }
    }

    @Test
    public void testParseInputStream() {
        String html = "<html><body><h1>InputStream Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = Jsoup.parse(inputStream, null, Parser.htmlParser());
        assertEquals("InputStream Test", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Body Fragment Test</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals("Body Fragment Test", doc.select("h1").text());
    }

    @Test
    public void testCleanHtml() {
        String bodyHtml = "<html><body><script>alert('Hello')</script><h1>Clean Test</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleTextOnly();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("Clean Test", cleanHtml);
    }

    @Test
    public void testIsValidHtml() {
        String bodyHtml = "<html><body><script>alert('Hello')</script><h1>Valid Test</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleTextOnly();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }
}