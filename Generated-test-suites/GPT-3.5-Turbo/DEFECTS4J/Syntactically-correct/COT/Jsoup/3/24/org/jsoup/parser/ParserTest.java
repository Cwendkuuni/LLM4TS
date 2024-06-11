import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(2, doc.children().size());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Test</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        assertEquals("body", doc.body().tagName());
        assertEquals(1, doc.body().children().size());
        assertEquals("div", doc.body().child(0).tagName());
        assertEquals("Test", doc.body().child(0).text());
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<div>Test</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        assertEquals("body", doc.body().tagName());
        assertEquals(1, doc.body().children().size());
        assertEquals("div", doc.body().child(0).tagName());
        assertEquals("Test", doc.body().child(0).text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        assertEquals("<!-- This is a comment -->", doc.childNode(0).outerHtml());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        assertEquals("<![CDATA[This is CDATA]]>", doc.childNode(0).outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.childNode(0).outerHtml());
    }

    @Test
    public void testParseEndTag() {
        String html = "<p>This is a paragraph</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        assertEquals("p", doc.body().child(0).tagName());
        assertEquals("This is a paragraph", doc.body().child(0).text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div id=\"test\">This is a div</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        assertEquals("div", doc.body().child(0).tagName());
        assertEquals(Attributes.create("id", "test"), doc.body().child(0).attributes());
        assertEquals("This is a div", doc.body().child(0).text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<div id=\"test\" class=\"example\">This is a div</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        assertEquals("div", doc.body().child(0).tagName());
        assertEquals(Attributes.create("id", "test", "class", "example"), doc.body().child(0).attributes());
        assertEquals("This is a div", doc.body().child(0).text());
    }

    @Test
    public void testParseTextNode() {
        String html = "This is a text node";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        assertEquals("This is a text node", doc.childNode(0).outerHtml());
    }

}