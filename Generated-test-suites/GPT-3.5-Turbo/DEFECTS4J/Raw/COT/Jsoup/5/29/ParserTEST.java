import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.hasText());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello</div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.hasText());
        Assert.assertEquals("Hello", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div>Hello</div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.hasText());
        Assert.assertEquals("Hello", doc.body().text());
    }

    @Test
    public void testParseWithComment() {
        String html = "<html><body><!-- This is a comment --><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.hasText());
        Assert.assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseWithCdata() {
        String html = "<html><body><![CDATA[<h1>Hello World</h1>]]></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.hasText());
        Assert.assertEquals("<h1>Hello World</h1>", doc.body().text());
    }

    @Test
    public void testParseWithXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.hasText());
        Assert.assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><h1>Hello World</h2></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.hasText());
        Assert.assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseStartTagWithAttributes() {
        String html = "<html><body><div class=\"container\"><h1>Hello World</h1></div></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.hasText());
        Assert.assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseStartTagWithEmptyElement() {
        String html = "<html><body><img src=\"image.png\"></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.hasText());
    }

    @Test
    public void testParseWithDataTag() {
        String html = "<html><body><script>alert('Hello World');</script></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.hasText());
        Assert.assertEquals("alert('Hello World');", doc.body().text());
    }

    @Test
    public void testParseWithBaseHref() {
        String html = "<html><head><base href=\"https://www.example.com/\"><title>Title</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.hasText());
        Assert.assertEquals("https://www.example.com/", doc.baseUri());
    }
}