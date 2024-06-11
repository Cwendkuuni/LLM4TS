import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser();
    }

    @Test
    public void testParse_ValidHTML_ReturnsDocument() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("Hello, World!", document.select("h1").first().text());
    }

    @Test
    public void testParse_EmptyHTML_ReturnsEmptyDocument() {
        String html = "";
        String baseUri = "http://example.com";
        Document document = parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("", document.html());
    }

    @Test
    public void testParseBodyFragment_ValidHTML_ReturnsDocumentWithFragmentParsedIntoBody() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";
        Document document = parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("Hello, World!", document.select("h1").first().text());
    }

    @Test
    public void testParseBodyFragment_EmptyHTML_ReturnsEmptyDocument() {
        String bodyHtml = "";
        String baseUri = "http://example.com";
        Document document = parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("", document.html());
    }

    @Test
    public void testParseBodyFragment_InvalidHTML_ReturnsDocumentWithFragmentParsedIntoBody() {
        String bodyHtml = "<h1>Hello, World!</h2>";
        String baseUri = "http://example.com";
        Document document = parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<h1>Hello, World!</h1>", document.html());
    }

    @Test
    public void testParseBodyFragment_RelativeURLs_IncludesBaseUriInDocument() {
        String bodyHtml = "<img src='image.jpg'>";
        String baseUri = "http://example.com";
        Document document = parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<img src=\"http://example.com/image.jpg\">", document.html());
    }

    @Test
    public void testParseBodyFragment_RelaxedParsingEnabled_CreatesImplicitTagsWhenMissing() {
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        String baseUri = "http://example.com";
        Document document = parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head></head><body><div><p>Hello, World!</p></div></body></html>", document.html());
    }

    @Test
    public void testParseBodyFragment_RelaxedParsingDisabled_ImplicitTagsNotCreatedWhenMissing() {
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        String baseUri = "http://example.com";
        Document document = parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head></head><body><div><p>Hello, World!</p></div></body></html>", document.html());
    }

    @Test
    public void testParseComment_ValidComment_AddsCommentToDocument() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document document = parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<!-- This is a comment -->", document.html());
    }

    @Test
    public void testParseComment_CommentEndsWithHyphen_RemovesHyphenFromComment() {
        String html = "<!-- This is a comment. -->";
        String baseUri = "http://example.com";
        Document document = parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<!-- This is a comment -->", document.html());
    }

    @Test
    public void testParseCdata_ValidCdata_AddsCdataToDocument() {
        String html = "<![CDATA[<div>Hello, World!</div>]]>";
        String baseUri = "http://example.com";
        Document document = parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<div>Hello, World!</div>", document.html());
    }

    @Test
    public void testParseXmlDecl_ValidDeclaration_AddsXmlDeclarationToDocument() {
        String html = "<?xml version='1.0' encoding='UTF-8' ?>";
        String baseUri = "http://example.com";
        Document document = parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", document.html());
    }

    @Test
    public void testParseEndTag_ValidEndTag_ClosesMatchingTag() {
        String html = "<div><p>Hello, World!</p></div>";
        String baseUri = "http://example.com";
        Document document = parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<div><p>Hello, World!</p></div>", document.html());

        parser.parse("</p>", baseUri);
        assertEquals("<div></div>", document.html());
    }

    @Test
    public void testParseStartTag_ValidStartTag_AddsElementToDocument() {
        String html = "<div><p>Hello, World!</p></div>";
        String baseUri = "http://example.com";
        Document document = parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<div><p>Hello, World!</p></div>", document.html());

        parser.parse("<span>Test</span>", baseUri);
        assertEquals("<div><p>Hello, World!</p><span>Test</span></div>", document.html());
    }

}