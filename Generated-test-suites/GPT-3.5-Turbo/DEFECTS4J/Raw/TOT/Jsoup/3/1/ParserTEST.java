import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Check if the document is parsed correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Check if the document is parsed correctly as a body fragment
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("body", doc.tagName());
        Assert.assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<h1>Hello <span>World!</span></h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Check if the document with relaxed parsing is parsed correctly as a body fragment
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("body", doc.tagName());
        Assert.assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseWithComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Check if the comment is parsed correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<!-- This is a comment -->", doc.body().html());
    }

    @Test
    public void testParseWithCDATA() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Check if the CDATA is parsed correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("This is CDATA", doc.body().text());
    }

    @Test
    public void testParseWithXmlDeclaration() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Check if the XML declaration is parsed correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.body().html());
    }

    @Test
    public void testParseWithEndTag() {
        String html = "<h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Check if the end tag is handled correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("<h1>Hello World!</h1>", doc.body().html());
    }

    @Test
    public void testParseWithStartTag() {
        String html = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Check if the start tag is handled correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("<h1>Hello World!</h1>", doc.body().html());
    }

    @Test
    public void testParseWithAttribute() {
        String html = "<div id=\"test\">Hello World!</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Check if the attribute is parsed correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<div id=\"test\">Hello World!</div>", doc.body().html());
        Assert.assertEquals("test", doc.select("div").attr("id"));
    }

    @Test
    public void testParseWithTextNode() {
        String html = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Check if the text node is parsed correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseWithCdata() {
        String html = "<h1><![CDATA[Hello World!]]></h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Check if the CDATA is parsed correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseWithNestedTags() {
        String html = "<div><h1>Hello <span>World!</span></h1></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Check if the nested tags are parsed correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<div><h1>Hello <span>World!</span></h1></div>", doc.body().html());
        Assert.assertEquals("Hello World!", doc.body().text());
    }

}