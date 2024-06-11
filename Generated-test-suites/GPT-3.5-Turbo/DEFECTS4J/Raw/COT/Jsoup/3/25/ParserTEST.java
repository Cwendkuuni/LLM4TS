
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertEquals("Hello World", document.select("h1").text());
    }

    // Edge cases and exception handling scenarios

    @Test
    public void testUnknownTag() {
        String html = "<customtag>Hello World</customtag>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testEmptyTag() {
        String html = "<emptytag></emptytag>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("", document.body().text());
    }

    @Test
    public void testCDATA() {
        String html = "<![CDATA[<h1>Hello World</h1>]]>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("<h1>Hello World</h1>", document.body().text());
    }

    @Test
    public void testXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><h1>Hello World</h1>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testInvalidAttribute() {
        String html = "<h1 invalid=\"attribute\">Hello World</h1>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testAttributeWithNoValue() {
        String html = "<h1 class>Hello World</h1>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testAttributeWithSingleQuotes() {
        String html = "<h1 class='heading'>Hello World</h1>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testAttributeWithDoubleQuotes() {
        String html = "<h1 class=\"heading\">Hello World</h1>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testTextAfterTag() {
        String html = "<h1>Hello World</h1>Some text after tag";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Hello WorldSome text after tag", document.body().text());
    }

    @Test
    public void testBaseHref() {
        String html = "<base href=\"https://example.com\"><h1>Hello World</h1>";
        String baseUri = "https://example2.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("https://example.com", document.baseUri());
    }
}

