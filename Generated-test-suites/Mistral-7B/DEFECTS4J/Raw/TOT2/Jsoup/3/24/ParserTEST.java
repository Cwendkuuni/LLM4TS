import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test
    public void testParseValidHtml() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://www.example.com");
        Element root = doc.body();
        assertEquals("Test Heading", root.child(0).text());
    }

    // Add edge cases and exception handling test cases here
}