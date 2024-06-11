import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParserTest {

    @Test
    public void testParseHtml() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals("Test Title", doc.title());
        assertEquals("Test Heading", doc.select("h1").text());
        assertEquals("http://test.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Test Heading</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");

        assertEquals("", doc.head().outerHtml());
        assertEquals("Test Heading", doc.select("h1").text());
        assertEquals("http://test.com", doc.baseUri());
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals("", doc.title().text());
        assertEquals("", doc.body().outerHtml());
        assertEquals("http://test.com", doc.baseUri());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body<p>Invalid HTML</p>";
        Document doc = Parser.parse(html, "http://test.com");

        assertNull(doc);
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- Test Comment --></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element comment = doc.select("html > comment()").first();
        assertEquals("Test Comment", comment.text());
    }

    @Test
    public void testParseXmlDeclaration() {
        String html = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\">";
        Document doc = Parser.parse(html, "http://test.com");

        XmlDeclaration decl = doc.select("html > xml-declaration()").first();
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", decl.xmlnsXsi());
        assertEquals("http://www.w3.org/XML/1998/namespace", decl.xmlnsXml());
        assertEquals(true, decl.isStandalone());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Test</p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element p = doc.select("p").first();
        assertEquals("Test", p.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1 id=\"title\">Test</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element h1 = doc.select("h1").first();
        assertEquals("Test", h1.text());
        assertEquals("title", h1.id());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body>Test</body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element p = doc.select("body > text()").first();
        assertEquals("Test", p.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[Test]]><body></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element cdata = doc.select("html > cdata()").first();
        assertEquals("Test", cdata.text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><body><a href=\"http://test.com\" title=\"Test Title\"><img src=\"image.jpg\" alt=\"Test Image\"></a></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element a = doc.select("a").first();
        assertEquals("http://test.com", a.absUrl("href"));
        assertEquals("Test Title", a.attr("title"));

        Element img = doc.select("img").first();
        assertEquals("image.jpg", img.absUrl("src"));
        assertEquals("Test Image", img.attr("alt"));
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/test/\">" +
                "</head><body><a href=\"about.html\">About</a></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals("/test/about.html", doc.select("a[href=\"about.html\"]").first().absUrl("href"));
    }

    @Test
    public void testParseEmptyBodyFragment() {
        String html = "<body></body>";
        Document doc = Parser.parseBodyFragment(html, "http://test.com");

        assertEquals("", doc.head().outerHtml());
        assertEquals("", doc.body().outerHtml());
        assertEquals("http://test.com", doc.baseUri());
    }

    @Test
    public void testParseInvalidBodyFragment() {
        String html = "<body><p>Invalid HTML</p>";
        Document doc = Parser.parseBodyFragment(html, "http://test.com");

        assertNull(doc);
    }
}