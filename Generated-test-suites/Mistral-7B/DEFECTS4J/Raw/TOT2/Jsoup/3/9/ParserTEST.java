import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Welcome</h1></body></html>";
        Document doc = Parser.parse(html, null);
        assertEquals("Test Page", doc.title().text());
        assertEquals("Welcome", doc.body().getElementsByTag("h1").text());
    }

    // Add test cases for other methods here...
}