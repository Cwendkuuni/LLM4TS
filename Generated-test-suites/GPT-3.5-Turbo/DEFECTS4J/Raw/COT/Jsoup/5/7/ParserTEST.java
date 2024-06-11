import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello world!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("<h1>Hello world!</h1>", doc.body().html());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello world!</div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("<div>Hello world!</div>", doc.body().html());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div>Hello world!";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("<div>Hello world!", doc.body().html());
    }

}