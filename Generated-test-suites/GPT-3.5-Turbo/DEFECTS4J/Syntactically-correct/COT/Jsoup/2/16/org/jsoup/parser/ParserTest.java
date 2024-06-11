
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>Hello World</h1>\n" +
                " </body>\n" +
                "</html>", document.html());
        Assert.assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("<head></head>\n" +
                "<body>\n" +
                " <h1>Hello World</h1>\n" +
                "</body>", document.html());
        Assert.assertEquals("https://www.example.com", document.baseUri());
    }
}
