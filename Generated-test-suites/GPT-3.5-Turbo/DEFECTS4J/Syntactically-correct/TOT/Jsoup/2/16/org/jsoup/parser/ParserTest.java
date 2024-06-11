
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Welcome!</h1></body></html>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Welcome!</h1>";
        String baseUri = "https://example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(bodyHtml, document.body().html());
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(1, document.childNodes().size());
        Assert.assertEquals("<!-- This is a comment -->", document.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[<h1>Welcome!</h1>]]>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(1, document.childNodes().size());
        Assert.assertEquals("<![CDATA[<h1>Welcome!</h1>]]>", document.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version='1.0' encoding='UTF-8'?><h1>Welcome!</h1>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(2, document.childNodes().size());
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\">", document.childNodes().get(0).outerHtml());
        Assert.assertEquals("<h1>Welcome!</h1>", document.childNodes().get(1).outerHtml());
    }

    @Test
    public void testParseEndTag() {
        String html = "<h1>Welcome!</h1></body></html>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(3, document.childNodes().size());
        Assert.assertEquals("<h1>Welcome!</h1>", document.childNodes().get(0).outerHtml());
        Assert.assertEquals("</body>", document.childNodes().get(1).outerHtml());
        Assert.assertEquals("</html>", document.childNodes().get(2).outerHtml());
    }

    @Test
    public void testParseStartTag() {
        String html = "<h1>Welcome!</h1><p>Paragraph</p>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(2, document.childNodes().size());
        Assert.assertEquals("<h1>Welcome!</h1>", document.childNodes().get(0).outerHtml());
        Assert.assertEquals("<p>Paragraph</p>", document.childNodes().get(1).outerHtml());
    }

    @Test
    public void testParseTextNode() {
        String html = "<h1>Welcome!</h1>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(1, document.childNodes().size());
        Assert.assertEquals("Welcome!", document.childNodes().get(0).outerHtml());
    }

    @Test
    public void testEmptyString() {
        String html = "";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(0, document.childNodes().size());
    }
}

