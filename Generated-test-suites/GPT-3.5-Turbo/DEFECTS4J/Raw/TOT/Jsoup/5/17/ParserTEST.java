import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><body><p>This is a paragraph</p></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("This is a paragraph", document.select("p").text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<p>This is a paragraph</p>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragment(html, baseUri);

        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("This is a paragraph", document.select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<p>This is a paragraph";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragmentRelaxed(html, baseUri);

        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("This is a paragraph", document.select("p").text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("<!-- This is a comment -->", document.select("body").html());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", document.select("body").html());
    }

    @Test
    public void testParseEndTag() {
        String html = "<p>This is a paragraph</p>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("", document.select("p").html());
    }

    @Test
    public void testParseStartTag() {
        String html = "<p>This is a paragraph</p>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("This is a paragraph", document.select("p").text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>This is a paragraph</p>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("This is a paragraph", document.select("p").text());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("<![CDATA[This is CDATA]]>", document.select("body").html());
    }

}