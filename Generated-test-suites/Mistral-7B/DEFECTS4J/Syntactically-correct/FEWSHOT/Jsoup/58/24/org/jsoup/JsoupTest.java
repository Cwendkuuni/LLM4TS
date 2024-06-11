package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.TextNode;

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
        String html = "<html><body><a href=\"/foo\">Link</a></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com");
        assertNotNull(doc);
        Element link = doc.selectFirst("a");
        assertEquals("http://example.com/foo", link.attr("href"));
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        assertNotNull(doc);
        assertEquals(url, doc.baseUri());
    }

    @Test
    public void testParseFromFile() {
        String charsetName = "UTF-8";
        String filePath = "path/to/your/file.html";
        Document doc = null;
        try {
            doc = Jsoup.parse(new File(filePath), charsetName);
            assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseFromInputStream() {
        String charsetName = "UTF-8";
        String filePath = "path/to/your/file.html";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(filePath));
            Document doc = Jsoup.parse(inputStream, charsetName);
            assertNotNull(doc);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        assertNotNull(doc);
        assertEquals("<p>Hello World</p>", doc.html());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<a href=\"/foo\">Link</a>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(doc);
        Element link = doc.selectFirst("a");
        assertEquals("http://example.com/foo", link.attr("href"));
    }

    @Test
    public void testParseUrl() {
        String url = "http://example.com";
        Document doc = Jsoup.parse(url);
        assertNotNull(doc);
        assertEquals(url, doc.baseUri());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script>alert('XSS');</script><p>Hello World</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleTags();
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        String safeHtml = Jsoup.clean(bodyHtml, whitelist, baseUri);
        assertEquals("<p>Hello World</p>", safeHtml);
        assertNull(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<script>alert('XSS');</script><p>Hello World</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleTags();
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        String safeHtml = Jsoup.clean(bodyHtml, whitelist, baseUri, null);
        assertEquals("<p>Hello World</p>", safeHtml);
        assertNull(Jsoup.isValid(bodyHtml, whitelist));
    }
}