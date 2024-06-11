import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        Element body = doc.body();
        Element h1 = body.getElementsByTag("h1").first();
        
        Assert.assertEquals("Hello, World!", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello, World!</h1>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragment(html, baseUri);
        
        Element h1 = doc.getElementsByTag("h1").first();
        
        Assert.assertEquals("Hello, World!", h1.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<h1>Hello, World!</h1>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(html, baseUri);
        
        Element h1 = doc.getElementsByTag("h1").first();
        
        Assert.assertEquals("Hello, World!", h1.text());
    }

    // Add more test cases to cover edge cases and error scenarios

}