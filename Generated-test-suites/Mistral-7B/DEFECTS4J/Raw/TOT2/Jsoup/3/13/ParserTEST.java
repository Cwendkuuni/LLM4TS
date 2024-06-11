import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

public class ParserTest {
    @Test
    public void testParseValidHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.body();
        assertNotNull(root);
        assertEquals("Hello World", root.child(0).text());
        assertEquals("Test", doc.title().text());
    }
}