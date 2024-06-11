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
    public void testParseString() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.title());
    }

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<html><head><base href=\"/test\"></base></html><body><a href=\"about.html\"></a></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com/test");
        Element link = doc.selectFirst("a");
        Assert.assertNotNull(link);
        Assert.assertEquals("about.html", link.attr("href"));
        Assert.assertEquals("http://example.com/about.html", link.absUrl("href"));
    }

    @Test
    public void testParseStringWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = org.jsoup.parser.JsoupScanner.overridingParser();
        Document doc = Jsoup.parse(html, "", parser);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.title());
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("http://example.com");
        Document doc = connection.get();
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.location().toString());
    }

    @Test(expected = MalformedURLException.class)
    public void testConnectWithMalformedUrl() {
        Jsoup.connect("file:///path/to/nonexistentfile.html");
    }

    @Test(expected = IOException.class)
    public void testParseFileWithIOException() {
        File file = new File("path/to/nonexistentfile.html");
        try {
            Jsoup.parse(file, "UTF-8");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseFile() {
        File file = new File("path/to/existingfile.html");
        Document doc = null;
        try {
            doc = Jsoup.parse(file, "UTF-8");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        Assert.assertNotNull(doc);
        Assert.assertEquals("path/to/existingfile.html", doc.location().toString());
    }

    @Test
    public void testParseInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("/path/to/existingfile.html");
        Document doc = null;
        try {
            doc = Jsoup.parse(inputStream, "UTF-8");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        Assert.assertNotNull(doc);
        Assert.assertEquals("path/to/existingfile.html", doc.location().toString());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithIOException() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            Jsoup.parse(inputStream, "UTF-8");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.title());
    }

    @Test
    public void testParseWithCleaner() {
        String html = "<html><body><script src=\"evilscript.js\"></script><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Document doc = Jsoup.parse(html);
        String cleanHtml = Jsoup.clean(doc.body().html(), whitelist);
        Assert.assertFalse(cleanHtml.contains("script"));
        Assert.assertTrue(cleanHtml.contains("Hello World"));
    }

    @Test
    public void testParseWithCleanerAndOutputSettings() {
        String html = "<html><body><script src=\"evilscript.js\"></script><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Document doc = Jsoup.parseBodyFragment(html);
        String cleanHtml = Jsoup.clean(doc.body().html(), whitelist, Document.OutputSettings.prettyPrint());
        Assert.assertFalse(cleanHtml.contains("script"));
        Assert.assertTrue(cleanHtml.contains("Hello World"));
    }

    @Test
    public void testIsValid() {
        String html = "<html><body><script src=\"evilscript.js\"></script><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleTextOnly();
        boolean isValid = Jsoup.isValid(html, whitelist);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValidWithAllowedScriptTag() {
        String html = "<html><body><script src=\"goodscript.js\"></script><h1>Hello World</h1></body></html>";
        Whitelist whitelist = new Whitelist().addTag("script");
        boolean isValid = Jsoup.isValid(html, whitelist);
        Assert.assertTrue(isValid);
    }
}