import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().select("p").first().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().select("p").first().text());
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<div><p>Hello World!</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().select("p").first().text());
    }

    @Test
    public void testParseWithComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertEquals(1, doc.childNodes().size());
        assertEquals("<!-- This is a comment -->", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseWithCDATA() {
        String html = "<![CDATA[This is a CDATA section]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertEquals(1, doc.childNodes().size());
        assertEquals("<![CDATA[This is a CDATA section]]>", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseWithXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertEquals(1, doc.childNodes().size());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseWithEndTag() {
        String html = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertEquals(1, doc.body().children().size());
        assertEquals("p", doc.body().child(0).tagName());
        assertEquals("Hello World!", doc.body().child(0).text());
    }

    @Test
    public void testParseWithStartTag() {
        String html = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertEquals(1, doc.body().children().size());
        assertEquals("p", doc.body().child(0).tagName());
        assertEquals("Hello World!", doc.body().child(0).text());
    }

    @Test
    public void testParseWithAttribute() {
        String html = "<img src=\"image.jpg\" alt=\"Image\">";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertEquals(1, doc.body().children().size());
        assertEquals("img", doc.body().child(0).tagName());
        assertEquals("image.jpg", doc.body().child(0).attr("src"));
        assertEquals("Image", doc.body().child(0).attr("alt"));
    }

}