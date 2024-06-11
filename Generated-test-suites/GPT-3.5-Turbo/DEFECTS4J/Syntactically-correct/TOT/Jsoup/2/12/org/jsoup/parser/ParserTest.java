import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        // Typical Use Case: Valid HTML input
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertEquals("Title", doc.select("h1").text());
        Assert.assertEquals("Paragraph", doc.select("p").text());

        // Edge Case: Empty HTML input
        String emptyHtml = "";
        Document emptyDoc = Parser.parse(emptyHtml, baseUri);
        Assert.assertEquals("", emptyDoc.text());

        // Error Scenario: HTML input with invalid closing tag
        String invalidClosingTagHtml = "<html><body><h1>Title</h2><p>Paragraph</p></body></html>";
        Document invalidClosingTagDoc = Parser.parse(invalidClosingTagHtml, baseUri);
        Assert.assertNotEquals("Title", invalidClosingTagDoc.select("h1").text());

        // Error Scenario: HTML input with invalid tag name
        String invalidTagNameHtml = "<html><body><h1>Ti<tle</h1><p>Paragraph</p></body></html>";
        Document invalidTagNameDoc = Parser.parse(invalidTagNameHtml, baseUri);
        Assert.assertNotEquals("Title", invalidTagNameDoc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        // Typical Use Case: Valid body HTML fragment input
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("Title", doc.select("h1").text());
        Assert.assertEquals("Paragraph", doc.select("p").text());

        // Edge Case: Empty body HTML fragment input
        String emptyBodyHtml = "";
        Document emptyDoc = Parser.parseBodyFragment(emptyBodyHtml, baseUri);
        Assert.assertEquals("", emptyDoc.text());

        // Error Scenario: HTML fragment input with invalid closing tag
        String invalidClosingTagBodyHtml = "<h1>Title</h2><p>Paragraph</p>";
        Document invalidClosingTagDoc = Parser.parseBodyFragment(invalidClosingTagBodyHtml, baseUri);
        Assert.assertNotEquals("Title", invalidClosingTagDoc.select("h1").text());

        // Error Scenario: HTML fragment input with invalid tag name
        String invalidTagNameBodyHtml = "<h1>Ti<tle</h1><p>Paragraph</p>";
        Document invalidTagNameDoc = Parser.parseBodyFragment(invalidTagNameBodyHtml, baseUri);
        Assert.assertNotEquals("Title", invalidTagNameDoc.select("h1").text());
    }

}