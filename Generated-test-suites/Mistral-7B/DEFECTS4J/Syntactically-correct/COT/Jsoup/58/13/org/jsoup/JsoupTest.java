package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><img src=\"image.jpg\"></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element img = doc.select("img").first();
        Assert.assertNotNull(img);
        Assert.assertEquals("image.jpg", img.attr("src"));
        URL url = new URL(img.absUrl("image.jpg"));
        Assert.assertEquals("http://example.com/image.jpg", url.toString());
    }

    @Test
    public void testParseWithCustomParser() {
        String html = "<xml><data>Test</data></xml>";
        Parser parser = org.jsoup.parser.XmlParser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        Assert.assertNotNull(doc);
        Element data = doc.select("data").first();
        Assert.assertNotNull(data);
        Assert.assertEquals("Test", data.text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        Assert.assertNotNull(doc);
        Assert.assertNotEquals("", doc.title());
    }

    @Test
    public void testParseFile() {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/directory";
        File file = new File("/path/to/file.html");
        Document doc = null;
        try {
            doc = Jsoup.parse(file, charsetName, baseUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(doc);
        Assert.assertNotEquals("", doc.title());
    }

    @Test
    public void testParseInputStream() {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/directory";
        File file = new File("/path/to/file.html");
        try (InputStream inputStream = new FileInputStream(file)) {
            Document doc = Jsoup.parse(inputStream, charsetName, baseUri);
            Assert.assertNotNull(doc);
            Assert.assertNotEquals("", doc.title());
        } catch (IOException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.select("h1").text());
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
        Assert.assertNotNull(doc);
        Assert.assertNotEquals("", doc.title());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script src=\"script.js\"></script><h1 id=\"title\">Test</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Document doc = Jsoup.parseBodyFragment(cleanHtml);
        Element title = doc.select("h1").first();
        Assert.assertNotNull(title);
        Assert.assertEquals("Test", title.text());
        Assert.assertFalse(doc.body().select("script").size() > 0);
    }

    @Test(expected = MalformedURLException.class)
    public void testParseUrlWithMalformedUrl() {
        String urlString = "file:///path/to/file.html";
        Document doc = Jsoup.parse(new URL(urlString), 5000);
    }

    @Test(expected = HttpStatusException.class)
    public void testParseUrlWithHttpError() {
        String urlString = "http://example.com/error";
        Document doc = Jsoup.parse(new URL(urlString), 5000);
    }

    @Test(expected = UnsupportedMimeTypeException.class)
    public void testParseUrlWithUnsupportedMimeType() {
        String urlString = "http://example.com/image.svg";
        Document doc = Jsoup.parse(new URL(urlString), 5000);
    }

    @Test(expected = SocketTimeoutException.class)
    public void testParseUrlWithSocketTimeout() {
        String urlString = "http://example.com";
        Document doc = Jsoup.parse(new URL(urlString), 10);
    }

    @Test(expected = IOException.class)
    public void testParseUrlWithIOException() {
        String urlString = "http://example.com";
        Document doc = Jsoup.parse(new URL(urlString), 5000);
        doc.close(); // intentionally close the connection to trigger an IOException when re-opening
        Document doc2 = Jsoup.parse(new URL(urlString), 5000);
    }
}