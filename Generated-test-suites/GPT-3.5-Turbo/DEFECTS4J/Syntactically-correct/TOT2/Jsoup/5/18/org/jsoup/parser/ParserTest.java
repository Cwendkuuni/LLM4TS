import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<p>Hello World</p>", doc.body().html());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<p>Hello World</p>", doc.body().html());
    }

    // Include additional test cases for edge cases and error scenarios here

}