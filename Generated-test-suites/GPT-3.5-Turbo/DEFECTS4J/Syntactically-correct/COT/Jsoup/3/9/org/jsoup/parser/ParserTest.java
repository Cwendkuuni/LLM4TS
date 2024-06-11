import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertEquals("Test Page", doc.title());
        Assert.assertEquals("Hello World!", doc.body().select("h1").first().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("Hello World!", doc.body().select("h1").first().text());
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<div>Hello World!</div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("Hello World!", doc.body().select("div").first().text());
    }

    // Additional Test Cases for Potential Edge Cases and Exception Handling Scenarios

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertEquals("", doc.title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        String html = null;
        String baseUri = "https://www.example.com";
        Parser.parse(html, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = null;
        Parser.parse(html, baseUri);
    }

    @Test
    public void testParseBodyFragmentWithNoBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "");
        Assert.assertEquals("Hello World!", doc.body().select("h1").first().text());
    }

    @Test
    public void testParseBodyFragmentWithEmptyHtml() {
        String bodyHtml = "";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("", doc.body().html());
    }

    @Test
    public void testParseBodyFragmentWithNullBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = null;
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("Hello World!", doc.body().select("h1").first().text());
    }
}

