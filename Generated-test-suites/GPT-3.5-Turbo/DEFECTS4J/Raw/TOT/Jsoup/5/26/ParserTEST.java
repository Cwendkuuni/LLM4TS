import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private String html;
    private String baseUri;

    @Before
    public void setUp() {
        html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        baseUri = "https://example.com/";
    }

    @Test
    public void testParse() {
        Document document = Parser.parse(html, baseUri);

        // Check if the document is parsed correctly
        assertNotNull(document);
        assertEquals(html, document.outerHtml());

        // Check if the document contains the expected elements
        assertEquals("Test Title", document.title());
        assertEquals("Test Heading", document.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test Heading</h1>";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Check if the document is parsed correctly
        assertNotNull(document);
        assertEquals("<body><h1>Test Heading</h1></body>", document.body().outerHtml());

        // Check if the document contains the expected elements
        assertEquals("Test Heading", document.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Test Heading</h1><p>Test Paragraph";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        // Check if the document is parsed correctly
        assertNotNull(document);
        assertEquals("<body><h1>Test Heading</h1><p>Test Paragraph</p></body>", document.body().outerHtml());

        // Check if the document contains the expected elements
        assertEquals("Test Heading", document.select("h1").text());
        assertEquals("Test Paragraph", document.select("p").text());
    }

    @Test
    public void testParseComment() {
        String htmlWithComment = "<!-- This is a comment -->";
        Document document = Parser.parse(htmlWithComment, baseUri);

        // Check if the comment is parsed correctly
        assertEquals(1, document.childNodes().size());
        assertEquals("<!-- This is a comment -->", document.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseCdata() {
        String htmlWithCdata = "<![CDATA[This is CDATA]]>";
        Document document = Parser.parse(htmlWithCdata, baseUri);

        // Check if the CDATA is parsed correctly
        assertEquals(1, document.childNodes().size());
        assertEquals("<![CDATA[This is CDATA]]>", document.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        String htmlWithXmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document document = Parser.parse(htmlWithXmlDecl, baseUri);

        // Check if the XML declaration is parsed correctly
        assertEquals(1, document.childNodes().size());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", document.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseTextNode() {
        String htmlWithText = "This is some text";
        Document document = Parser.parse(htmlWithText, baseUri);

        // Check if the text node is parsed correctly
        assertEquals(1, document.childNodes().size());
        assertEquals("This is some text", document.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseStartTag() {
        String htmlWithStartTag = "<h1>Test Heading</h1>";
        Document document = Parser.parse(htmlWithStartTag, baseUri);

        // Check if the start tag is parsed correctly
        assertEquals(1, document.select("h1").size());
        assertEquals("Test Heading", document.select("h1").text());
    }

    @Test
    public void testParseEndTag() {
        String htmlWithEndTag = "<h1>Test Heading</h1>";
        Document document = Parser.parse(htmlWithEndTag, baseUri);

        // Check if the end tag is parsed correctly
        assertEquals(1, document.select("h1").size());
        assertEquals("Test Heading", document.select("h1").text());
    }

    @Test
    public void testParseAttribute() {
        String htmlWithAttribute = "<h1 class=\"title\">Test Heading</h1>";
        Document document = Parser.parse(htmlWithAttribute, baseUri);

        // Check if the attribute is parsed correctly
        assertEquals(1, document.select("h1[class=title]").size());
        assertEquals("Test Heading", document.select("h1").text());
    }

    @Test
    public void testPopStackToClose() {
        String htmlWithNestedTags = "<div><h1>Test Heading</h1></div>";
        Document document = Parser.parse(htmlWithNestedTags, baseUri);

        // Check if the stack is correctly popped to close the tags
        assertEquals(1, document.select("div").size());
        assertEquals(1, document.select("h1").size());
        assertEquals("Test Heading", document.select("h1").text());
    }

    @Test
    public void testStackHasValidParent() {
        String htmlWithInvalidParent = "<body><h1>Test Heading</h1></body>";
        Document document = Parser.parse(htmlWithInvalidParent, baseUri);

        // Check if the tag has the valid parent
        assertEquals(0, document.select("h1").size());
    }
}