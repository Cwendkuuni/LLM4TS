import org.junit.Test;
import org.jsoup.Jsoup;
import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Check parsed document structure
        Element htmlNode = doc.getElementsByTag("html").first();
        assertEquals(htmlTag, htmlNode.tag());
        Element headNode = htmlNode.getElementsByTag("head").first();
        assertEquals(headTag, headNode.tag());
        Element titleNode = headNode.getElementsByTag("title").first();
        assertEquals(titleTag, titleNode.tag());
        Element bodyNode = htmlNode.getElementsByTag("body").first();
        assertEquals(bodyTag, bodyNode.tag());
        Element h1Node = bodyNode.getElementsByTag("h1").first();
        assertEquals(Tag.H1, h1Node.tag());
        assertEquals("Hello World", h1Node.text());

        // Check base URI
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Check parsed document structure
        Element htmlNode = doc.getElementsByTag("html").first();
        assertEquals(htmlTag, htmlNode.tag());
        Element headNode = htmlNode.getElementsByTag("head").first();
        assertEquals(headTag, headNode.tag());
        Element bodyNode = htmlNode.getElementsByTag("body").first();
        assertEquals(bodyTag, bodyNode.tag());
        Element h1Node = bodyNode.getElementsByTag("h1").first();
        assertEquals(Tag.H1, h1Node.tag());
        assertEquals("Hello World", h1Node.text());

        // Check base URI
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html<tag>"; // missing closing tag
        String baseUri = "http://example.com";
        try {
            Document doc = Parser.parse(html, baseUri);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseMissingTag() {
        String html = "<html><body>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Element headNode = doc.getElementsByTag("head").first();
        assertEquals(0, headNode.children().size());
    }

    @Test
    public void testParseSelfClosedTag() {
        String html = "<html><body><img src='image.png'/></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Element imgNode = doc.getElementsByTag("img").first();
        assertEquals(Tag.IMG, imgNode.tag());
        assertEquals("image.png", imgNode.absUrl("src"));
    }

    @Test
    public void testParseInvalidTagName() {
        String html = "<html><body><x></x></body></html>";
        String baseUri = "http://example.com";
        try {
            Document doc = Parser.parse(html, baseUri);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}