import org.jsoup.nodes.Document;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Check if the document is not null
        assertNotNull(doc);

        // Check if the document has correct title
        assertEquals("Test", doc.title());

        // Check if the document has a body
        assertNotNull(doc.body());

        // Check if the document body has the correct heading
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Check if the document is not null
        assertNotNull(doc);

        // Check if the document has a body
        assertNotNull(doc.body());

        // Check if the document body has the correct paragraph
        assertEquals("Hello World", doc.body().select("p").text());
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("", "", false);

        // Test comment parsing
        parser.parseComment();

        // Check if the last appended comment matches the expected data
        assertEquals("Test Comment", parser.last().childNode(0).outerHtml(), "<!--Test Comment-->");
    }

    // Test other methods in the Parser class

    // ...

}