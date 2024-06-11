import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
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
        assertEquals("<p>Hello World!</p>", doc.body().html());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertNull(doc.title());
        assertEquals("<p>Hello World!</p>", doc.body().html());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("This is a comment", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("This is CDATA", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseEndTag() {
        String html = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(1, doc.body().childNodes().size());
        assertEquals("Hello World!", doc.body().childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div class=\"container\">Sample Text</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(1, doc.body().childNodes().size());
        assertEquals("<div class=\"container\">Sample Text</div>", doc.body().childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseTextNode() {
        String html = "Sample Text";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("Sample Text", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href=\"http://example.com\">Link</a>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(1, doc.body().childNodes().size());
        assertEquals("<a href=\"http://example.com\">Link</a>", doc.body().childNodes().get(0).outerHtml());
    }

    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "", false);
        Tag childTag = Tag.valueOf("div");
        assertTrue(parser.stackHasValidParent(childTag));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "", false);
        Tag tag = Tag.valueOf("div");
        assertNull(parser.popStackToSuitableContainer(tag));
    }

    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "", false);
        Tag tag = Tag.valueOf("div");
        assertNull(parser.popStackToClose(tag));
    }

    @Test
    public void testLast() {
        Parser parser = new Parser("", "", false); 
        assertNotNull(parser.last());
    }
}