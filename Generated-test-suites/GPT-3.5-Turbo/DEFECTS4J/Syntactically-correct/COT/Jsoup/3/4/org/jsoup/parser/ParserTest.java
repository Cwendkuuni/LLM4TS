import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test HTML</title></head><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test HTML", doc.title());
        Assert.assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World!", doc.body().text());
    }
    
    @Test
    public void testParseBodyFragment_Relaed() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World!", doc.body().text());
    }
    
    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<!-- This is a comment -->", doc.childNode(0).outerHtml());
    }
    
    @Test
    public void testParseCdata() {
        String html = "<![CDATA[ This is CDATA ]]>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<![CDATA[ This is CDATA ]]>", doc.childNode(0).outerHtml());
    }

    // Add more test cases for the remaining methods...

}