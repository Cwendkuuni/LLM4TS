import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Test", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Test</h1>";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragment(html, baseUri);

        Assert.assertEquals("Test", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithComment() {
        String html = "<html><!--This is a comment--><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Test", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithXmlDecl() {
        String html = "<html><?xml version=\"1.0\"?><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Test", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithEndTag() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Test", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithStartTag() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Test", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithTextNode() {
        String html = "<html><body>Test</body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Test", document.getElementsByTag("body").text());
    }

    @Test
    public void testParseWithCdata() {
        String html = "<html><body><![CDATA[<h1>Test</h1>]]></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("<h1>Test</h1>", document.getElementsByTag("body").html());
    }

    @Test
    public void testParseWithAttribute() {
        String html = "<html><body class=\"test\"><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("test", document.getElementsByTag("body").attr("class"));
        Assert.assertEquals("Test", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithEmptyElement() {
        String html = "<html><body><img src=\"test.jpg\"></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("test.jpg", document.getElementsByTag("img").attr("src"));
    }

    @Test
    public void testParseWithExplicitParent() {
        String html = "<body><h1>Test</h1></body>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Test", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithImplicitParent() {
        String html = "<div><h1>Test</h1></div>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("Test", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithBaseHref() {
        String html = "<html><head><base href=\"http://example.com/\"></head><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("http://example.com/", document.baseUri());
    }
}