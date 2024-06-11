import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {
    private Document parsedDocument;
    private Document parsedBodyFragment;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        parsedDocument = Parser.parse(html, "http://example.com");
        parsedBodyFragment = Parser.parseBodyFragment(bodyHtml, "http://example.com");
    }

    @Test
    public void testParseDocument() {
        assertNotNull(parsedDocument);
        assertNotNull(parsedDocument.title());
        assertEquals("Test", parsedDocument.title().text());
        assertEquals(1, parsedDocument.body().childSize());
        assertEquals("Hello World", parsedDocument.body().get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        assertNotNull(parsedBodyFragment);
        assertNotNull(parsedBodyFragment.title());
        assertNull(parsedBodyFragment.title().text());
        assertEquals(1, parsedBodyFragment.body().childSize());
        assertEquals("Hello World", parsedBodyFragment.body().get(0).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtml() {
        Parser.parse("", "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidBaseUri() {
        Parser.parse("<html><head></head>", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtmlWithComment() {
        String html = "<html><!-- This is a comment --><head></head>";
        Parser.parse(html, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidXmlDecl() {
        String html = "<html <?xml version=\"1.0\" encoding=\"UTF-8\"?><head></head>";
        Parser.parse(html, "http://example.com");
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p><p></p></body></html>";
        Document document = Parser.parse(html, "http://example.com");
        assertEquals(1, document.body().childSize());
        assertEquals(1, document.body().get(0).childSize());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1>Hello</h1><p>World</p></body></html>";
        Document document = Parser.parse(html, "http://example.com");
        assertEquals(2, document.body().childSize());
        assertEquals("Hello", document.body().get(0).tagName());
        assertEquals("World", document.body().get(1).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidStartTag() {
        String html = "<html><body><h1<p>Hello</p></body></html>";
        Parser.parse(html, "http://example.com");
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document document = Parser.parse(html, "http://example.com");
        assertEquals("Hello World", document.body().get(0).text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[Hello World]]><p>Test</p></body></html>";
        Document document = Parser.parse(html, "http://example.com");
        assertEquals("Hello World", document.body().get(0).text().toString());
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><body><p id=\"test\" class=\"test\"></p></body></html>";
        Document document = Parser.parse(html, "http://example.com");
        String id = document.body().get(0).attr("id");
        String className = document.body().get(0).attr("class");
        assertEquals("test", id);
        assertEquals("test", className);
    }
}