import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Parser;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "https://www.example.com");

        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://www.example.com");

        Assert.assertEquals("div", doc.tagName()); // implicit parent is added
        Assert.assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World!</h1><p>Additional text</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "https://www.example.com");

        Assert.assertEquals("div", doc.tagName()); // implicit parent is added
        Assert.assertEquals("Additional text", doc.body().select("p").text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "https://www.example.com");

        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals(1, doc.select("comment").size());
        Assert.assertEquals(" This is a comment ", doc.select("comment").first().data());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><root><h1>Hello World!</h1></root>";
        Document doc = Parser.parse(html, "https://www.example.com");

        Assert.assertEquals("root", doc.tagName());
        Assert.assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><head><title>Test</title></head></html>";
        Document doc = Parser.parse(html, "https://www.example.com");

        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals(1, doc.head().children().size());
        Assert.assertEquals("Test", doc.head().select("title").text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "https://www.example.com");

        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "https://www.example.com");

        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><head><title>Test</title></head><body><![CDATA[Hello World!]]></body></html>";
        Document doc = Parser.parse(html, "https://www.example.com");

        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("Hello World!", doc.body().text());
    }

}