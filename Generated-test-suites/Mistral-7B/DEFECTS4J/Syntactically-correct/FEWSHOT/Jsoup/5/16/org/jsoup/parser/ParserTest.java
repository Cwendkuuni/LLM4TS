package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.helper.W3C.CDATA;
import static org.jsoup.helper.W3C.COMMENT;
import static org.jsoup.helper.W3C.XML_DECLARATION;

import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1><p>This is a <test>test</test>.</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());

        Element root = doc.body();
        assertEquals(2, root.childNodes().size());

        Element title = doc.head().title();
        assertEquals("Test", title.text());

        Element h1 = root.child(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello World", h1.text());

        Element p = root.child(1);
        assertEquals("p", p.tagName());
        assertEquals("This is a <test>test</test>.", p.text());

        // check that the <test> tag was parsed as expected
        Element test = p.child(0);
        assertEquals("test", test.tagName());
        assertEquals(0, test.childNodes().size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a <test>test</test>.</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals("html", doc.tagName());
        assertEquals(1, doc.childNodes().size());

        Element root = doc.body();
        assertEquals(1, root.childNodes().size());

        Element p = root.child(0);
        assertEquals("p", p.tagName());
        assertEquals("This is a <test>test</test>.", p.text());

        // check that the <test> tag was parsed as expected
        Element test = p.child(0);
        assertEquals("test", test.tagName());
        assertEquals(0, test.childNodes().size());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>This is a <test>test</test>.</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        assertEquals("html", doc.tagName());
        assertEquals(1, doc.childNodes().size());

        Element root = doc.body();
        assertEquals(1, root.childNodes().size());

        Element p = root.child(0);
        assertEquals("p", p.tagName());
        assertEquals("This is a <test>test</test>.", p.text());

        // check that the <test> tag was not wrapped in a container
        Element test = p.child(0);
        assertEquals("test", test.tagName());
        assertEquals(0, test.childNodes().size());
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDecl = "<?xml version='1.0' encoding='UTF-8'?>";
        Document doc = Parser.parse("<html>" + xmlDecl + "</html>", "http://example.com");

        XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.child(0);
        assertEquals("1.0", xmlDeclaration.version());
        assertEquals("UTF-8", xmlDeclaration.encoding());
        assertEquals(COMMENT, xmlDeclaration.tagName());
    }

    @Test
    public void testParseComment() {
        String comment = "<!-- This is a comment -->";
        Document doc = Parser.parse("<html>" + comment + "</html>", "http://example.com");

        Comment commentNode = (Comment) doc.child(0);
        assertEquals("This is a comment", commentNode.data());
        assertEquals(COMMENT, commentNode.tagName());
    }

    @Test
    public void testParseCdata() {
        String cdata = "<![CDATA[ This is some CDATA ]]>";
        Document doc = Parser.parse("<html>" + cdata + "</html>", "http://example.com");

        TextNode cdataNode = (TextNode) doc.child(0);
        assertEquals("This is some CDATA", cdataNode.text());
        assertEquals(CDATA, cdataNode.tagName());
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<img src='image.png'/>";
        Document doc = Parser.parse(html, "http://example.com");

        Element img = doc.body().child(0);
        assertTrue(img.tag().isSelfClosing());
        assertEquals(0, img.childNodes().size());
    }

    @Test
    public void testParseSelfClosingElement() {
        String html = "<input type='text'/>";
        Document doc = Parser.parse(html, "http://example.com");

        Element input = doc.body().child(0);
        assertTrue(input.tag().isSelfClosing());
        assertEquals(0, input.childNodes().size());
    }

    @Test
    public void testParseIgnoredAttribute() {
        String html = "<base target='_blank'/>";
        Document doc = Parser.parse(html, "http://example.com");

        Element base = doc.body().child(0);
        assertEquals("base", base.tagName());
        assertEquals(0, base.childNodes().size());

        // the 'target' attribute was ignored because it is not a valid attribute for the 'base' tag
        assertFalse(base.hasAttr("target"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href='http://example.com'>Link</a>";
        Document doc = Parser.parse(html, "http://example.com");

        Element a = doc.body().child(0);
        assertEquals("a", a.tagName());
        assertEquals("http://example.com", a.attr("href"));
    }

    @Test
    public void testParseAttributeWithQuotes() {
        String html = "<a href='http://example.com/path/to/file.html'>Link</a>";
        Document doc = Parser.parse(html, "http://example.com");

        Element a = doc.body().child(0);
        assertEquals("a", a.tagName());
        assertEquals("http://example.com/path/to/file.html", a.attr("href"));
    }

    @Test
    public void testParseAttributeWithSingleQuote() {
        String html = "<a href='http://example.com/path/to/file.html'>Link</a>";
        Document doc = Parser.parse(html, "http://example.com");

        Element a = doc.body().child(0);
        assertEquals("a", a.tagName());
        assertEquals("http://example.com/path/to/file.html", a.attr("href"));

        // single quotes are allowed in attribute values, as long as they are escaped
        assertEquals("'single' quote is allowed", a.attr("title"));
    }

    @Test
    public void testParseAttributeWithMultipleValues() {
        String html = "<a href='http://example.com' class='foo bar baz'>Link</a>";
        Document doc = Parser.parse(html, "http://example.com");

        Element a = doc.body().child(0);
        assertEquals("a", a.tagName());
        assertEquals("http://example.com", a.attr("href"));
        assertEquals("foo bar baz", a.attr("class"));
    }

    @Test
    public void testParseAttributeWithEmptyValue() {
        String html = "<a href='' class=''>Link</a>";
        Document doc = Parser.parse(html, "http://example.com");

        Element a = doc.body().child(0);
        assertEquals("a", a.tagName());
        assertEquals("", a.attr("href"));
        assertEquals("", a.attr("class"));
    }

    @Test
    public void testParseAttributeWithEmptyValueSurroundedByWhitespace() {
        String html = "<a href=' \t\t\t '' class=' \t\t\t \t'>Link</a>";
        Document doc = Parser.parse(html, "http://example.com");

        Element a = doc.body().child(0);
        assertEquals("a", a.tagName());
        assertEquals("", a.attr("href"));
        assertEquals("", a.attr("class"));
    }

    @Test
    public void testParseAttributeWithMultipleAttributes() {
        String html = "<a href='http://example.com' class='foo bar' id='myid'>Link</a>";
        Document doc = Parser.parse(html, "http://example.com");

        Element a = doc.body().child(0);
        assertEquals("a", a.tagName());
        assertEquals("http://example.com", a.attr("href"));
        assertEquals("foo bar", a.attr("class"));
        assertEquals("myid", a.attr("id"));
    }

    @Test
    public void testParseAttributeWithNestedTags() {
        String html = "<a href='<script>alert('hello');</script>'><b>Link</b></a>";
        Document doc = Parser.parse(html, "http://example.com");

        Element a = doc.body().child(0);
        assertEquals("a", a.tagName());
        assertEquals(XML_DECLARATION, a.child(0).tagName());
        assertEquals("script", a.child(1).tagName());
        assertEquals("b", a.child(2).tagName());
        assertEquals("Link", a.child(3).text());
    }

    @Test
    public void testParseAttributeWithInvalidValue() {
        String html = "<a href='<script>alert('hello');</script>'><b>Link</b></a>";
        try {
            Parser.parse(html, "http://example.com");
            fail("Expected: ParserException");
        } catch (ParserException e) {
            // expected
        }
    }
}