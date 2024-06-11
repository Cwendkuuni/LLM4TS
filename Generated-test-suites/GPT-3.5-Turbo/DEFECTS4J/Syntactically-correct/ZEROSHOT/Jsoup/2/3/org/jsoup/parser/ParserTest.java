import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertEquals("Hello World", doc.body().select("p").text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("This is a comment", doc.childNodes().get(0).toString());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals(html, doc.childNodes().get(0).toString());
    }

    @Test
    public void testParseEndTag() {
        String html = "<div><p>Hello World</p></div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals(1, doc.body().children().size());
        Assert.assertEquals("Hello World", doc.body().select("p").text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div><p>Hello World</p></div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals(1, doc.body().children().size());
        Assert.assertEquals("Hello World", doc.body().select("p").text());
    }

    // Add more test methods for other methods in the Parser class

}