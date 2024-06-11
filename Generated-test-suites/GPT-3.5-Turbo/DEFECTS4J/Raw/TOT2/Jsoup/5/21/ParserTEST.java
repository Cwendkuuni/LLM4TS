import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://www.example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>Fragment</h2><p>Test Fragment</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://www.example.com");
        assertNotNull(doc.body());
        assertEquals("Fragment", doc.body().child(0).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><a href=\"#\"></a></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://www.example.com");
        assertNotNull(doc.body());
        assertEquals("a", doc.body().child(0).child(0).tagName());
    }
}