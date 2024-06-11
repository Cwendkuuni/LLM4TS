package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseSimpleHtml() {
        String html = "<html><head/><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "https://test.com/");
        Element root = doc.body();
        Element p = root.getChild(0);

        assertEquals(htmlTag, doc.childNodes().get(0));
        assertEquals(headTag, doc.childNodes().get(1));
        assertEquals(htmlTag, doc.tag());
        assertEquals(bodyTag, root.tag());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "https://test.com/");
        Element root = doc.body();

        assertEquals(htmlTag, doc.childNodes().get(0));
        assertEquals(htmlTag, doc.tag());
        assertEquals(bodyTag, root.tag());
        assertEquals(Element.EMPTY_ELEMENT_VECTOR, root.childNodes());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://test.com/");

        Element root = doc.body();
        Element p = root.getChild(0);

        assertEquals(htmlTag, doc.childNodes().get(0));
        assertEquals(bodyTag, root.tag());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!--This is a comment--><body></body></html>";
        Document doc = Parser.parse(html, "https://test.com/");
        Element comment = doc.body().getChild(0).getNextSibling();

        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[This is CDATA]]><body></body></html>";
        Document doc = Parser.parse(html, "https://test.com/");
        Element cdata = doc.body().getChild(0);

        assertEquals("This is CDATA", cdata.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"bar\">";
        Document doc = Parser.parse(html, "https://test.com/");
        XmlDeclaration xmlDecl = doc.childNodes().get(0).getChild(0);

        Validate.notNull(xmlDecl);
        assertEquals("https://test.com/", xmlDecl.baseURI());
        assertEquals("xmlns:foo=\"bar\"", xmlDecl.xmlDeclData());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "https://test.com/");

        Element p = doc.body().getChild(0);

        assertEquals(1, doc.body().childSize());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p title=\"test\" id=\"p1\">Hello World</p></body></html>";
        Document doc = Parser.parse(html, "https://test.com/");

        Element p = doc.body().getChild(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());
        assertEquals("test", p.attribute("title"));
        assertEquals("p1", p.attribute("id"));
    }

    @Test
    public void testParseAttributeWithSingleQuotes() {
        String html = "<p title='test'>Hello World</p>";
        Document doc = Parser.parse(html, "https://test.com/");

        Element p = doc.body().getChild(0);
        assertEquals("test", p.attribute("title"));
    }

    @Test
    public void testParseAttributeWithDoubleQuotes() {
        String html = "<p title=\"test\">Hello World</p>";
        Document doc = Parser.parse(html, "https://test.com/");

        Element p = doc.body().getChild(0);
        assertEquals("test", p.attribute("title"));
    }

    @Test
    public void testParseAttributeWithValueContainingSingleQuotes() {
        String html = "<p title='test \"value\"'>Hello World</p>";
        Document doc = Parser.parse(html, "https://test.com/");

        Element p = doc.body().getChild(0);
        assertEquals("test \"value\"", p.attribute("title"));
    }

    @Test
    public void testParseAttributeWithValueContainingDoubleQuotes() {
        String html = "<p title=\"test 'value'\">Hello World</p>";
        Document doc = Parser.parse(html, "https://test.com/");

        Element p = doc.body().getChild(0);
        assertEquals("test 'value'", p.attribute("title"));
    }

    @Test
    public void testParseAttributeWithMultipleValues() {
        String html = "<p class='test1 test2' title='test'></p>";
        Document doc = Parser.parse(html, "https://test.com/");

        Element p = doc.body().getChild(0);
        assertEquals(2, p.attributes().size());
        assertEquals("test1 test2", p.classes());
    }

    @Test
    public void testParseAttributeWithEmptyValue() {
        String html = "<p title=''></p>";
        Document doc = Parser.parse(html, "https://test.com/");

        Element p = doc.body().getChild(0);
        assertEquals(0, p.attributes().size());
    }

    @Test
    public void testParseEmptyTag() {
        String html = "<img src='image.png'/>";
        Document doc = Parser.parse(html, "https://test.com/");

        Element img = doc.body().getChild(0);
        assertEquals("img", img.tagName());
        assertEquals(Element.EMPTY_ELEMENT_VECTOR, img.childNodes());
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<p><span>Hello World</span></p>";
        Document doc = Parser.parse(html, "https://test.com/");

        Element p = doc.body().getChild(0);
        Element span = p.getChild(0);

        assertEquals("p", p.tagName());
        assertEquals("span", span.tagName());
    }

    @Test
    public void testParseInvalidTagOrder() {
        String html = "<body><p><html></html></p>";
        Document doc = Parser.parse(html, "https://test.com/");

        Element p = doc.body().getChild(0);
        Element htmlTag = p.getChild(0);

        assertEquals("body", doc.tag().tagName());
        assertEquals("html", htmlTag.tagName());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/new-folder/\"></head><body><a href='page.html'>Link</a></body></html>";
        Document doc = Parser.parse(html, "https://test.com/");

        assertEquals("/new-folder/page.html", doc.baseUri());
        assertEquals("/new-folder/page.html", doc.head().getChild(0).absUrl("href"));
    }

    @Test
    public void testParseXmlDeclWithEncoding() {
        String html = "<html xml:encoding=\"UTF-8\"><body></body></html>";
        Document doc = Parser.parse(html, "https://test.com/");

        XmlDeclaration xmlDecl = doc.childNodes().get(0).getChild(0);
        assertEquals("UTF-8", xmlDecl.encoding());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "https://test.com/");

        TextNode textNode = (TextNode) doc.body().getChild(0).getChild(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseTextNodeWithScriptAndStyles() {
        String html = "<html><head><style>body { color: red; }</style><script src=\"script.js\"></script><body></body></html>";
        Document doc = Parser.parse(html, "https://test.com/");

        TextNode textNode = (TextNode) doc.body().getChild(0);
        assertEquals("", textNode.text());

        Element style = doc.head().getChild(0);
        assertEquals("style", style.tagName());

        Element script = doc.head().getChild(1);
        assertEquals("script", script.tagName());
    }

    @Test
    public void testParseInvalidCharacter() {
        String html = "<html><body>&#65473;Hello World</body></html>";
        try {
            Parser.parse(html, "https://test.com/");
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}