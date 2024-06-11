import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World!", doc.select("h1").text());
        Assert.assertTrue(doc.head().children().isEmpty());
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("<p>Paragraph 1</p><p>Paragraph 2</p>", doc.body().html());
    }

    @Test
    public void testParseComment() {
        String html = "<!--This is a comment-->";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("<!--This is a comment-->", doc.html());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("<![CDATA[This is CDATA]]>", doc.html());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head></head><body></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n <head></head>\n <body></body>\n</html>", doc.html());
    }

    @Test
    public void testParseEndTag() {
        String html = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("<div>\n <p>Paragraph 1</p>\n <p>Paragraph 2</p>\n</div>", doc.body().html());
        Assert.assertTrue(doc.head().children().isEmpty());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("<div>\n <p>Paragraph 1</p>\n <p>Paragraph 2</p>\n</div>", doc.body().html());
        Assert.assertTrue(doc.head().children().isEmpty());
    }

    @Test
    public void testParseTextNode() {
        String html = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Paragraph 1\nParagraph 2", doc.body().text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<div class=\"container\" id=\"main\">Content</div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("container", doc.select("div").attr("class"));
        Assert.assertEquals("main", doc.select("div").attr("id"));
    }

    @Test
    public void testParseAttributeWithSpaces() {
        String html = "<a href=\"https://example.com\" target=\"_blank\">Link</a>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("https://example.com", doc.select("a").attr("href"));
        Assert.assertEquals("_blank", doc.select("a").attr("target"));
    }

    @Test
    public void testParseAttributeWithNoQuotes() {
        String html = "<input type=text value=value>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("text", doc.select("input").attr("type"));
        Assert.assertEquals("value", doc.select("input").attr("value"));
    }

    @Test
    public void testParseAttributeWithMissingKey() {
        String html = "<div  value=value>Content</div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("", doc.select("div").attr("value"));
    }

    @Test
    public void testParseAttributeWithUnknownChar() {
        String html = "<div x='y\"z'>Content</div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("y\"z", doc.select("div").attr("x"));
    }
}