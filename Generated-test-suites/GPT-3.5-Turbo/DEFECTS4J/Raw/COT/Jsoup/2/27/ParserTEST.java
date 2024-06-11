import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser();
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertNotNull(doc.body());
        assertNotNull(doc.body().getElementsByTag("h1"));
        assertEquals("Hello World", doc.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertNull(doc.title());
        assertNotNull(doc.body());
        assertNotNull(doc.getElementsByTag("h1"));
        assertEquals("Hello World", doc.getElementsByTag("h1").text());
    }

    @Test
    public void testParseEmptyHTML() {
        String html = "";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertNull(doc.title());
        assertNotNull(doc.body());
        assertTrue(doc.body().children().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHTML() {
        String baseUri = "http://example.com";
        parser.parse(null, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        parser.parse(html, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullHTML() {
        String baseUri = "http://example.com";
        parser.parseBodyFragment(null, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullBaseUri() {
        String bodyHtml = "<h1>Hello World</h1>";
        parser.parseBodyFragment(bodyHtml, null);
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertNotNull(doc.childNodes().get(0));
        assertEquals("<!-- This is a comment -->", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseCDATA() {
        String html = "<![CDATA[This is some CDATA content]]>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertNotNull(doc.childNodes().get(0));
        assertEquals("<![CDATA[This is some CDATA content]]>", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertNotNull(doc.childNodes().get(0));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseEndTag() {
        String html = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertNotNull(doc.getElementsByTag("h1").get(0));
        assertEquals("<h1>Hello World</h1>", doc.getElementsByTag("h1").get(0).outerHtml());
    }

    @Test
    public void testParseStartTag() {
        String html = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertNotNull(doc.getElementsByTag("h1").get(0));
        assertEquals("<h1>Hello World</h1>", doc.getElementsByTag("h1").get(0).outerHtml());
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href=\"http://example.com\">Link</a>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("a").size());
        assertNotNull(doc.getElementsByTag("a").get(0));
        assertEquals("<a href=\"http://example.com\">Link</a>", doc.getElementsByTag("a").get(0).outerHtml());
    }

    @Test
    public void testParseTextNode() {
        String html = "Hello World";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertNotNull(doc.childNodes().get(0));
        assertEquals("Hello World", doc.childNodes().get(0).toString());
    }

    @Test
    public void testParseCdataNode() {
        String html = "<div><![CDATA[This is some CDATA content]]></div>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("div").size());
        assertNotNull(doc.getElementsByTag("div").get(0));
        assertEquals("<div>This is some CDATA content</div>", doc.getElementsByTag("div").get(0).outerHtml());
    }

    @Test
    public void testAddChildToParent() {
        String html = "<div><p>Hello World</p></div>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("div").size());
        assertNotNull(doc.getElementsByTag("div").get(0));
        assertEquals("<div><p>Hello World</p></div>", doc.getElementsByTag("div").get(0).outerHtml());
    }

    @Test
    public void testStackHasValidParent() {
        assertFalse(parser.stackHasValidParent(Tag.valueOf("body")));
        assertFalse(parser.stackHasValidParent(Tag.valueOf("html")));
        assertTrue(parser.stackHasValidParent(Tag.valueOf("head")));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Tag tag = Tag.valueOf("div");
        assertNull(parser.popStackToSuitableContainer(tag));
    }

    @Test
    public void testPopStackToClose() {
        Tag tag = Tag.valueOf("div");
        assertNull(parser.popStackToClose(tag));
    }

    @Test
    public void testLast() {
        parser.stack.add(Element.create(Tag.valueOf("html"), "", new Attributes()));
        assertNotNull(parser.last());
        assertEquals(Tag.valueOf("html"), parser.last().tag());
    }
}