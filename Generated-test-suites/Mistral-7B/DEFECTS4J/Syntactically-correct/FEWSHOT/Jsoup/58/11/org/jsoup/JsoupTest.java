package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());

        // Test with base URI
        Document docWithBaseUri = Jsoup.parse(html, "http://example.com");
        assertEquals("http://example.com/Hello%20World", docWithBaseUri.title());

        // Test with Parser
        Parser parser = Parser.xmlParser();
        Document docWithParser = Jsoup.parse(html, "", parser);
        assertEquals("Hello World", docWithParser.select("h1").text());
    }

    @Test
    public void testConnect() {
        try {
            Connection connection = Jsoup.connect("http://example.com");
            Document doc = connection.get();
            assertNotNull(doc);
            assertEquals("Example Domain", doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseFile() throws IOException {
        String filePath = "path/to/your/file.html";
        Document doc = Jsoup.parse(new File(filePath));
        assertNotNull(doc);
        assertEquals("File Title", doc.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertNotNull(doc);
        assertEquals("File Title", doc.title());
        inputStream.close();
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><body><script>alert('XSS');</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanBodyHtml = Jsoup.clean(bodyHtml, whitelist);
        assertNull(cleanBodyHtml.matches(".*<script\\b[^<]*.*>"));
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><body><script>alert('XSS');</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }
}