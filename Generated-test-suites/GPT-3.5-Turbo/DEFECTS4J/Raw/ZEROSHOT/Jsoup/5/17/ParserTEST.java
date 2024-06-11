import org.jsoup.nodes.*;
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
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = parser.parse(html, "http://example.com");

        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Document doc = parser.parse(html, "http://example.com");

        assertNotNull(doc);
        Comment comment = (Comment) doc.childNode(0);
        assertNotNull(comment);
        assertEquals(" This is a comment ", comment.getData());
    }

    @Test
    public void testParseXmlDecl() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document doc = parser.parse(xml, "http://example.com");

        assertNotNull(doc);
        XmlDeclaration xmlDecl = (XmlDeclaration) doc.childNode(0);
        assertNotNull(xmlDecl);
        assertEquals("1.0", xmlDecl.getAttr("version"));
        assertEquals("UTF-8", xmlDecl.getAttr("encoding"));
    }

    @Test
    public void testParseEndTag() {
        String html = "<p>This is a paragraph.</p>";
        Document doc = parser.parse(html, "http://example.com");

        assertNotNull(doc);
        Element p = doc.getElementsByTag("p").first();
        assertNotNull(p);
        assertEquals("This is a paragraph.", p.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<h1>Hello World</h1>";
        Document doc = parser.parse(html, "http://example.com");

        assertNotNull(doc);
        Element h1 = doc.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseTextNode() {
        String html = "This is a paragraph.";
        Document doc = parser.parse(html, "http://example.com");

        assertNotNull(doc);
        TextNode textNode = (TextNode) doc.childNode(0);
        assertNotNull(textNode);
        assertEquals("This is a paragraph.", textNode.getWholeText());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA content]]>";
        Document doc = parser.parse(html, "http://example.com");

        assertNotNull(doc);
        TextNode textNode = (TextNode) doc.childNode(0);
        assertNotNull(textNode);
        assertEquals("This is CDATA content", textNode.getWholeText());
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href=\"http://example.com\">Link</a>";
        Document doc = parser.parse(html, "http://example.com");

        assertNotNull(doc);
        Element a = doc.getElementsByTag("a").first();
        assertNotNull(a);
        assertEquals("http://example.com", a.attr("href"));
        assertEquals("Link", a.text());
    }

    @Test
    public void testStackHasValidParent() {
        parser.parse("<body><h1>Hello World</h1></body>", "http://example.com");

        assertTrue(parser.stackHasValidParent(Tag.valueOf("h1")));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        parser.parse("<body><h1>Hello World</h1></body>", "http://example.com");

        Element element = parser.popStackToSuitableContainer(Tag.valueOf("body"));

        assertNotNull(element);
        assertEquals(Tag.valueOf("body"), element.tag());
    }

    @Test
    public void testPopStackToClose() {
        parser.parse("<body><h1>Hello World</h1></body>", "http://example.com");

        Element element = parser.popStackToClose(Tag.valueOf("body"));

        assertNotNull(element);
        assertEquals(Tag.valueOf("body"), element.tag());
    }

    @Test
    public void testLast() {
        parser.parse("<body><h1>Hello World</h1></body>", "http://example.com");

        Element last = parser.last();

        assertNotNull(last);
        assertEquals(Tag.valueOf("body"), last.tag());
    }
}