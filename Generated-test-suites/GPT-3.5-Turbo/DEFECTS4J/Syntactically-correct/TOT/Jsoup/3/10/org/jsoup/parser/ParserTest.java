import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<p>This is a paragraph</p>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragment(html, baseUri);

        Assert.assertEquals("This is a paragraph", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsingEnabled() {
        String html = "<p>This is a paragraph";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragment(html, baseUri);

        Assert.assertEquals("This is a paragraph", doc.select("p").text());
    }

    @Test
    public void testParseWithEmptyHtml() {
        String html = "";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("", doc.html());
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullHtml() {
        String baseUri = "https://www.example.com";

        Parser.parse(null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";

        Parser.parse(html, null);
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentWithNullHtml() {
        String baseUri = "https://www.example.com";

        Parser.parseBodyFragment(null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentWithNullBaseUri() {
        String html = "<p>This is a paragraph</p>";

        Parser.parseBodyFragment(html, null);
    }

    @Test
    public void testParseWithComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("", doc.html());
    }

    @Test
    public void testParseBodyFragmentWithCData() {
        String html = "<![CDATA[<p>This is a paragraph</p>]]>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragment(html, baseUri);

        Assert.assertEquals("<p>This is a paragraph</p>", doc.html());
    }

    @Test
    public void testParseWithXmlDeclaration() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("Hello World", doc.select("h1").text());
    }
}