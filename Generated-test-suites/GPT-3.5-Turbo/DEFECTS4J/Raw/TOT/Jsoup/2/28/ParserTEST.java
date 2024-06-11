
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    private String test_html = "<html>\n" +
            "<head>\n" +
            "    <title>Test HTML</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <h1>Welcome to the Test Page</h1>\n" +
            "    <p>This is a paragraph.</p>\n" +
            "    <a href=\"https://www.example.com\">Click here</a>\n" +
            "</body>\n" +
            "</html>";

    @Test
    public void testParse() {
        Document doc = Parser.parse(test_html, "https://www.example.com");
        
        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals(1, doc.getElementsByTag("head").size());
        Assert.assertEquals(1, doc.getElementsByTag("body").size());
        Assert.assertEquals("Test HTML", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Welcome to the Test Page</h1>\n" +
            "<p>This is a paragraph.</p>\n" +
            "<a href=\"https://www.example.com\">Click here</a>";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://www.example.com");
        
        Assert.assertEquals(1, doc.getElementsByTag("h1").size());
        Assert.assertEquals(1, doc.getElementsByTag("p").size());
        Assert.assertEquals(1, doc.getElementsByTag("a").size());
    }
    
    @Test
    public void testInvalidHtml() {
        String invalidHtml = "<html>\n" +
            "<head>\n" +
            "    <title>Test HTML</title>\n" +
            "</body>\n" +
            "</html>";
        
        Document doc = Parser.parse(invalidHtml, "https://www.example.com");
        
        Assert.assertNull(doc);
    }
    
    @Test
    public void testEmptyHtml() {
         Document doc = Parser.parse("", "https://www.example.com");
        
        Assert.assertNull(doc);
    }
}
