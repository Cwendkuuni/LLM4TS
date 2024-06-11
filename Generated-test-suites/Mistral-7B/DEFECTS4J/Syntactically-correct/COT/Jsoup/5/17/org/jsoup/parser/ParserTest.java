import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Tag;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><div><p>Hello World</p></div></body></html>";
        Document document = Parser.parse(html, "http://example.com");
        Assert.assertEquals("http://example.com", document.baseUri());
        Element root = document.body();
        assertEquals(1, root.childSize());
        Element div = root.child(0);
        assertEquals(1, div.childSize());
        Element p = div.child(0);
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document document = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        Assert.assertEquals("http://example.com", document.baseUri());
        Element root = document;
        assertEquals(1, root.childSize());
        Element p = root.child(0);
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World</p>";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        Assert.assertEquals("http://example.com", document.baseUri());
        Element root = document;
        assertEquals(1, root.childSize());
        Element p = root.child(0);
        assertEquals("Hello World", p.text());
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
        String html = "<html><body><script src='javascript:alert(\"Hello World\")'></script></body></html>";
        try {
            Parser.parse(html, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseAttributes() {
        String html = "<div id='test' class='testClass' title='Test Title'><p>Hello World</p></div>";
        Document document = Parser.parse(html, "http://example.com");
        Element div = document.getElementById("test");
        assertEquals(3, div.attributeSize());
        Attribute idAttr = div.attribute("id");
        assertEquals("test", idAttr.getValue());
        Attribute classAttr = div.attribute("class");
        assertEquals("testClass", classAttr.getValue());
        Attribute titleAttr = div.attribute("title");
        assertEquals("Test Title", titleAttr.getValue());
    }

    @Test
    public void testParseSelfClosedEmptyTag() {
        String html = "<img src='image.jpg'/>";
        Document document = Parser.parse(html, "http://example.com");
        Element img = document.getElementsByTag("img").first();
        assertTrue(img.isSelfClosing());
    }

    @Test
    public void testParseSelfClosedTag() {
        String html = "<p><span>Hello World</span></p>";
        Document document = Parser.parse(html, "http://example.com");
        Element p = document.body();
        Element span = p.child(0);
        assertFalse(span.isSelfClosing());
    }

    @Test
    public void testParseEmptyTag() {
        String html = "<img src='image.jpg'/><input type='text' name='name'/>";
        Document document = Parser.parse(html, "http://example.com");
        Element img = document.getElementsByTag("img").first();
        assertTrue(img.isSelfClosing());
        Element input = document.getElementsByTag("input").first();
        assertFalse(input.isSelfClosing());
    }

    @Test
    public void testParseTagWithInvalidName() {
        String html = "<invalidTag/>";
        try {
            Parser.parse(html, "http://example.com");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseTagWithInvalidParent() {
        String html = "<body><html></html></body>";
        try {
            Parser.parse(html, "http://example.com");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseTagWithInvalidParentRelaxed() {
        String html = "<body><html></html></body>";
        Parser parser = new Parser(html, "http://example.com", true);
        Document document = parser.parse();
        Element body = document;
        Element htmlTag = body.getElementsByTag("html").first();
        assertEquals(1, htmlTag.childSize());
        assertEquals(1, htmlTag.child(0).childSize());
    }

    @Test
    public void testParseScriptTag() {
        String html = "<script type='text/javascript' src='script.js'></script>";
        Document document = Parser.parse(html, "http://example.com");
        Element script = document.getElementsByTag("script").first();
        assertEquals("text/javascript", script.attribute("type").getValue());
        assertEquals("script.js", script.absUrl("src"));
    }

    @Test
    public void testParseStyleTag() {
        String html = "<style type='text/css'>body { color: red; }</style>";
        Document document = Parser.parse(html, "http://example.com");
        Element style = document.getElementsByTag("style").first();
        assertEquals("text/css", style.attribute("type").getValue());
        assertEquals("body", style.attr("type").toString());
        assertEquals("color: red; ", style.text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Document document = Parser.parse(html, "http://example.com");
        Element comment = document.body().child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        Document document = Parser.parse(html, "http://example.com");
        Element cdata = document.body().child(0);
        assertEquals("This is CDATA", cdata.text());
    }

    @Test
    public void testParseXmlDeclaration() {
        String html = "<xml version='1.0' encoding='UTF-8' standalone='yes'/>";
        Document document = Parser.parse(html, "http://example.com");
        XmlDeclaration xmlDeclaration = document.getElementsByTag("xml").first();
        assertEquals("1.0", xmlDeclaration.version());
        assertEquals("UTF-8", xmlDeclaration.encoding());
        assertTrue(xmlDeclaration.isStandalone());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Hello World</p>";
        Document document = Parser.parse(html, "http://example.com");
        TextNode textNode = document.getElementsByText("Hello World").first();
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseTextNodeWithSpecialChars() {
        String html = "<p>&lt;Hello World&gt;</p>";
        Document document = Parser.parse(html, "http://example.com");
        TextNode textNode = document.getElementsByText("&lt;Hello World&gt;").first();
        assertEquals("<Hello World>", textNode.text());
    }

    @Test
    public void testParseTextNodeWithComment() {
        String html = "<p>Hello World <!-- This is a comment --></p>";
        Document document = Parser.parse(html, "http://example.com");
        TextNode textNode = document.getElementsByText("Hello World").first();
        assertEquals("Hello World", textNode.text());
        Comment comment = textNode.previousSibling();
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseTextNodeWithCdata() {
        String html = "<p><![CDATA[Hello World]]></p>";
        Document document = Parser.parse(html, "http://example.com");
        TextNode textNode = document.getElementsByText("Hello World").first();
        assertEquals("Hello World", textNode.text());
        Element cdata = textNode.parent();
        assertEquals(1, cdata.childSize());
    }

    @Test
    public void testParseTextNodeWithBaseTag() {
        String html = "<base href='/foo'><p>Hello World</p>";
        Document document = Parser.parse(html, "http://example.com");
        TextNode textNode = document.getElementsByText("Hello World").first();
        assertEquals("Hello World", textNode.text());
        Element base = document.getElementsByTag("base").first();
        assertEquals("/foo", base.absUrl("href"));
    }
}