import org.junit.Test;
import org.junit.Assert;
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

public class JsoupTest {

    @Test
    public void testParseString() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<img src=\"image.png\" />";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Assert.assertEquals("http://example.com/image.png", document.select("img[src]").attr("abs:src"));
    }

    @Test
    public void testParseStringWithParser() {
        String html = "<xml><data>Hello World</data></xml>";
        Parser parser = org.jsoup.parser.XmlParser.xmlParser();
        Document document = Jsoup.parse(html, "", parser);
        Assert.assertEquals("Hello World", document.select("data").text());
    }

    @Test
    public void testParseStringEmpty() {
        String html = "";
        Document document = Jsoup.parse(html);
        Assert.assertNotNull(document);
        Assert.assertEquals("", document.body().html());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Document document = Jsoup.connect(url).get();
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.title());
        Assert.assertEquals(200, document.statusCode());
    }

    @Test
    public void testParseFile() throws IOException {
        String fileName = "test.html";
        Document document = Jsoup.parse(new File(fileName));
        Assert.assertNotNull(document);
        Assert.assertEquals("Test Page", document.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String fileName = "test.html";
        InputStream inputStream = new FileInputStream(new File(fileName));
        Document document = Jsoup.parse(inputStream, "UTF-8");
        Assert.assertNotNull(document);
        Assert.assertEquals("Test Page", document.title());
        inputStream.close();
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello World</h1>";
        Document document = Jsoup.parseBodyFragment(html);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String html = "<img src=\"image.png\" />";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(html, baseUri);
        Assert.assertEquals("http://example.com/image.png", document.select("img[src]").attr("abs:src"));
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlString = "http://example.com";
        Document document = Jsoup.parse(new URL(urlString));
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.title());
        Assert.assertEquals(200, document.statusCode());
    }

    @Test
    public void testClean() {
        String html = "<script src=\"script.js\">";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        String safeHtml = Jsoup.clean(html, baseUri, whitelist);
        Assert.assertEquals("", safeHtml);

        html = "<div id=\"test\" class=\"test\">Test</div>";
        whitelist = Whitelist.simpleTagNames("div");
        safeHtml = Jsoup.clean(html, "", whitelist);
        Assert.assertEquals("<div>Test</div>", safeHtml);

        html = "<div id=\"test\" class=\"test\" data-test=\"test\">Test</div>";
        whitelist = new Whitelist("div", new String[]{"data-test"});
        safeHtml = Jsoup.clean(html, "", whitelist);
        Assert.assertEquals("<div data-test=\"test\">Test</div>", safeHtml);
    }

    @Test
    public void testIsValid() {
        String html = "<script src=\"script.js\">";
        Whitelist whitelist = Whitelist.simpleText();
        boolean isValid = Jsoup.isValid(html, whitelist);
        Assert.assertFalse(isValid);

        html = "<div id=\"test\" class=\"test\">Test<script>alert('Hello World')</script></div>";
        whitelist = Whitelist.simpleTagNames("div");
        isValid = Jsoup.isValid(html, whitelist);
        Assert.assertFalse(isValid);

        html = "<div id=\"test\" class=\"test\">Test<script>alert('Hello World')</script></div>";
        whitelist = new Whitelist("div", new String[]{"data-test"});
        isValid = Jsoup.isValid(html, whitelist);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String html = "<div id=\"test\" class=\"test\">Test<script>alert('Hello World')</script></div>";
        String baseUri = "http://example.com";
        Whitelist whitelist = new Whitelist("div", new String[]{"data-test"});
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String safeHtml = Jsoup.clean(html, baseUri, whitelist, outputSettings).body().html();
        Assert.assertEquals("<div data-test=\"\">Test</div>", safeHtml);
    }
}