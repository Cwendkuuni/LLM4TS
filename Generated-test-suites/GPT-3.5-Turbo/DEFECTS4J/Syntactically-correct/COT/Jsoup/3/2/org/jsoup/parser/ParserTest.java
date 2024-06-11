import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    // Test case for parse(String html, String baseUri) method
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World", doc.body().select("h1").text());
    }

    // Test case for parseBodyFragment(String bodyHtml, String baseUri) method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        Assert.assertEquals("Hello World", doc.body().select("h1").text());
    }
}