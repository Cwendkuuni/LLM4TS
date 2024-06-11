import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", doc.html());
        assertEquals("https://example.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("", doc.head().html());
        assertEquals("<h1>Hello World</h1>", doc.body().html());
        assertEquals("https://example.com", doc.baseUri());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNodes().get(0) instanceof Comment);
        Comment comment = (Comment) doc.childNodes().get(0);
        assertEquals(" This is a comment ", comment.getData());
        assertEquals("https://example.com", comment.baseUri());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNodes().get(0) instanceof XmlDeclaration);
        XmlDeclaration decl = (XmlDeclaration) doc.childNodes().get(0);
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\" ", decl.getData());
        assertEquals("https://example.com", decl.baseUri());
        assertFalse(decl.isProcessingInstruction());
    }

    @Test
    public void testParseEndTag() {
        String html = "<h1>Hello World</h1>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("<h1>Hello World</h1>", doc.body().html());
        doc.body().childNodes().get(0).remove();

        assertEquals("<h1></h1>", doc.body().html());

        String newHtml = "</h1>";
        doc = Parser.parse(newHtml, baseUri);

        assertNotNull(doc);
        assertEquals(0, doc.body().childNodes().size());
    }

    @Test
    public void testParseStartTag() {
        String html = "<h1>Hello World</h1>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("<h1>Hello World</h1>", doc.body().html());
        doc.body().childNodes().get(0).remove();

        assertEquals("<h1></h1>", doc.body().html());

        String newHtml = "<h2>Hi</h2>";
        doc = Parser.parse(newHtml, baseUri);

        assertNotNull(doc);
        assertEquals("<h2>Hi</h2>", doc.body().html());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>This is a paragraph</p>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("<p>This is a paragraph</p>", doc.body().html());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNodes().get(0) instanceof TextNode);
        TextNode textNode = (TextNode) doc.childNodes().get(0);
        assertEquals("This is CDATA", textNode.getWholeText());
        assertEquals("https://example.com", textNode.baseUri());
    }

    @Test
    public void testParseAttribute() {
        String html = "<div id='myDiv'class='myClass' data-data='123'>Hello</div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("<div id=\"myDiv\" class=\"myClass\" data-data=\"123\">Hello</div>", doc.body().html());
    }

    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "https://example.com", false);
        Tag childTag = Tag.valueOf("div");
        Element parent = parser.popStackToSuitableContainer(childTag);

        assertNull(parent);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "https://example.com", false);
        Tag childTag = Tag.valueOf("div");
        Element parent = parser.popStackToSuitableContainer(childTag);

        assertNull(parent);
    }

    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "https://example.com", false);
        Tag tag = Tag.valueOf("div");
        Element elToClose = parser.popStackToClose(tag);

        assertNull(elToClose);
    }

    @Test
    public void testLast() {
        Parser parser = new Parser("", "https://example.com", false);
        Element last = parser.last();

        assertNotNull(last);
    }
}