import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><div id=\"content\">Test Content</div></body></html>";
        String baseUri = "https://www.example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Test Content", document.getElementById("content").text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<div id=\"content\">Test Content</div>";
        String baseUri = "https://www.example.com";

        Document document = Parser.parseBodyFragment(html, baseUri);

        Assert.assertEquals("Test Content", document.getElementById("content").text());
    }

    @Test
    public void testParseWithEmptyHTML() {
        String html = "";
        String baseUri = "https://www.example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("", document.title());
        Assert.assertEquals("", document.body().html());
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullHTML() {
        String baseUri = "https://www.example.com";

        Document document = Parser.parse(null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><div id=\"content\">Test Content</div></body></html>";

        Document document = Parser.parse(html, null);
    }

    @Test
    public void testParseTextOnly() {
        String html = "Test Content";
        String baseUri = "https://www.example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("", document.title());
        Assert.assertEquals("Test Content", document.body().text());
    }

}