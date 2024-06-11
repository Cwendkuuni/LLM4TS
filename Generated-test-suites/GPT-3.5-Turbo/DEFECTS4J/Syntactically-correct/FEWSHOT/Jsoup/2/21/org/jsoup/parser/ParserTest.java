import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.children().size());

        Element htmlTag = doc.child(0);
        assertEquals("html", htmlTag.tagName());

        Element headTag = htmlTag.child(0);
        assertEquals("head", headTag.tagName());

        Element titleTag = headTag.child(0);
        assertEquals("title", titleTag.tagName());
        assertEquals("Test Page", titleTag.text());

        Element bodyTag = htmlTag.child(1);
        assertEquals("body", bodyTag.tagName());

        Element h1Tag = bodyTag.child(0);
        assertEquals("h1", h1Tag.tagName());
        assertEquals("Hello World!", h1Tag.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1><p>This is a test</p>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.children().size());

        Element bodyTag = doc.child(0);
        assertEquals("body", bodyTag.tagName());

        Element h1Tag = bodyTag.child(0);
        assertEquals("h1", h1Tag.tagName());
        assertEquals("Hello World!", h1Tag.text());

        Element pTag = bodyTag.child(1);
        assertEquals("p", pTag.tagName());
        assertEquals("This is a test", pTag.text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.children().size());

        Comment comment = (Comment)doc.child(0);
        assertEquals(" This is a comment ", comment.getData());
        assertEquals("<!-- This is a comment -->", comment.outerHtml());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.children().size());

        TextNode cdata = (TextNode)doc.child(0);
        assertEquals("This is CDATA", cdata.getWholeText());
        assertEquals("<![CDATA[This is CDATA]]>", cdata.outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.children().size());

        XmlDeclaration xmlDecl = (XmlDeclaration)doc.child(0);
        assertEquals("xml", xmlDecl.tagName());
        assertEquals("1.0", xmlDecl.attr("version"));
        assertEquals("UTF-8", xmlDecl.attr("encoding"));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDecl.outerHtml());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div class=\"container\"><h1>Title</h1><p>Paragraph</p></div>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.children().size());

        Element divTag = doc.child(0);
        assertEquals("div", divTag.tagName());
        assertEquals("container", divTag.attr("class"));
        assertEquals("<div class=\"container\"><h1>Title</h1><p>Paragraph</p></div>", divTag.outerHtml());

        Element h1Tag = divTag.child(0);
        assertEquals("h1", h1Tag.tagName());
        assertEquals("Title", h1Tag.text());

        Element pTag = divTag.child(1);
        assertEquals("p", pTag.tagName());
        assertEquals("Paragraph", pTag.text());
    }

    @Test
    public void testParseEndTag() {
        String html = "<div><h1>Title</h1><p>Paragraph</p></div>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.children().size());

        Element divTag = doc.child(0);
        assertEquals("div", divTag.tagName());
        assertEquals("<div><h1>Title</h1><p>Paragraph</p></div>", divTag.outerHtml());

        Element h1Tag = divTag.child(0);
        assertEquals("h1", h1Tag.tagName());
        assertEquals("Title", h1Tag.text());

        Element pTag = divTag.child(1);
        assertEquals("p", pTag.tagName());
        assertEquals("Paragraph", pTag.text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>This is a test</p>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.children().size());

        Element pTag = doc.child(0);
        assertEquals("p", pTag.tagName());
        assertEquals("This is a test", pTag.text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src=\"image.jpg\" alt=\"Image\">";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.children().size());

        Element imgTag = doc.child(0);
        assertEquals("img", imgTag.tagName());
        assertEquals("image.jpg", imgTag.attr("src"));
        assertEquals("Image", imgTag.attr("alt"));
        assertEquals("<img src=\"image.jpg\" alt=\"Image\">", imgTag.outerHtml());
    }
}