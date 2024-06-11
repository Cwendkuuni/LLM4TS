package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        assertTrue(doc.body().tagName().equals("body"));
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<img src=\"image.png\" alt=\"Image\">";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        String imgSrc = doc.select("img[src]").first().attr("src");
        assertTrue(imgSrc.startsWith(baseUri + "/image.png"));
    }

    @Test
    public void testParseWithParser() {
        String html = "<xml><tag>Hello</tag></xml>";
        Parser parser = org.jsoup.parser.XmlParser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        assertTrue(doc.select("tag").text().equals("Hello"));
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        Document doc = connection.get();
        assertTrue(doc.title().length() > 0);
    }

    @Test
    public void testParseFile() {
        String charsetName = "UTF-8";
        String filePath = "path/to/file.html";
        Document doc = null;
        try {
            doc = Jsoup.parse(new File(filePath), charsetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() {
        String charsetName = "UTF-8";
        String filePath = "path/to/file.html";
        File file = new File(filePath);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            Document doc = Jsoup.parse(inputStream, charsetName);
            assertNotNull(doc);
        } catch (FileNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertTrue(doc.body().text().equals("Hello World"));
    }

    @Test
    public void testParseUrl() {
        String urlString = "http://example.com";
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(urlString), 5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(doc);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script>alert('Hello')</script><p>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleTagNames("p");
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        assertTrue(cleanHtml.contains("Hello World"));
        assertFalse(cleanHtml.contains("script"));
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<input type=\"text\" name=\"email\" value=\"user@example.com\">";
        Whitelist whitelist = Whitelist.relaxed();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }
}