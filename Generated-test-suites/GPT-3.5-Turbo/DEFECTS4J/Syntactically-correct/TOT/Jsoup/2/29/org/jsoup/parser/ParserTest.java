import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Welcome to ParserTest</h1></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull("Document should not be null", doc);
        Assert.assertEquals("Document title should be 'Test Page'", "Test Page", doc.title());
        Assert.assertEquals("Body should contain one h1 element", 1, doc.body().getElementsByTag("h1").size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Welcome to ParserTest</h1>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull("Document should not be null", doc);
        Assert.assertEquals("Body should contain one h1 element", 1, doc.body().getElementsByTag("h1").size());
    }

    @Test
    public void testParseWithEmptyHtml() {
        String html = "";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull("Document should not be null", doc);
        Assert.assertTrue("Document should be empty", doc.children().isEmpty());
    }

    @Test
    public void testParseWithEmptyBodyFragment() {
        String bodyHtml = "";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull("Document should not be null", doc);
        Assert.assertTrue("Body should be empty", doc.body().children().isEmpty());
    }

    @Test
    public void testParseWithInvalidHtml() {
        String html = "<html><head><title>Test Page</title></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull("Document should not be null", doc);
        Assert.assertNotEquals("Document title should not be 'Test Page'", "Test Page", doc.title());
        Assert.assertEquals("Body should be empty", 0, doc.body().children().size());
    }

}