import org.jsoup.nodes.Document;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        // Add assertions to check if the parsing is correct
        // For example:
        // assertEquals("Test", doc.title());
        // assertNotNull(doc.select("p"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        // Add assertions to check if the parsing is correct
        // For example:
        // assertNotNull(doc.select("p"));
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><span>Hello World!</span></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        // Add assertions to check if the parsing is correct
        // For example:
        // assertNotNull(doc.select("div"));
        // assertNotNull(doc.select("span"));
    }

    // Add more test cases for the remaining methods in the Parser class

}