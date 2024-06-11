package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><div><p>Test</p></div></body></html>";
        Document parsed = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, parsed.tagName());
        assertEquals(1, parsed.childNodes().size());
        assertEquals(headTag, parsed.head().tagName());
        assertEquals(1, parsed.head().childNodes().size());
        assertEquals(titleTag, parsed.head().childNodes().get(0).tagName());
        assertEquals("Test", parsed.head().childNodes().get(0).text());
        assertEquals(bodyTag, parsed.body().tagName());
        assertEquals(1, parsed.body().childNodes().size());
        assertEquals(divTag, parsed.body().childNodes().get(0).tagName());
        assertEquals(1, parsed.body().childNodes().get(0).childNodes().size());
        assertEquals(pTag, parsed.body().childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("Test", parsed.body().childNodes().get(0).childNodes().get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test</p></div>";
        Document parsed = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals(htmlTag, parsed.tagName());
        assertEquals(1, parsed.childNodes().size());
        assertEquals(bodyTag, parsed.tagName(BodyTag.class));
        assertEquals(1, parsed.body().childNodes().size());
        assertEquals(divTag, parsed.body().childNodes().get(0).tagName());
        assertEquals(1, parsed.body().childNodes().get(0).childNodes().size());
        assertEquals(pTag, parsed.body().childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("Test", parsed.body().childNodes().get(0).childNodes().get(0).text());
    }

    @Test
    public void testParseWithRelaxedMode() {
        String bodyHtml = "<div><p>Test</p></div>";
        Document parsed = Parser.parseBodyFragment(bodyHtml, "http://example.com", true);

        assertEquals(htmlTag, parsed.tagName());
        assertEquals(1, parsed.childNodes().size());
        assertEquals(bodyTag, parsed.tagName(BodyTag.class));
        assertEquals(1, parsed.body().childNodes().size());
        assertEquals(divTag, parsed.body().childNodes().get(0).tagName());
        assertNull(parsed.body().childNodes().get(0).tagName(Tag.class, "head"));
        assertEquals(1, parsed.body().childNodes().get(0).childNodes().size());
        assertEquals(pTag, parsed.body().childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("Test", parsed.body().childNodes().get(0).childNodes().get(0).text());
    }

    @Test
    public void testParseEmptyDocument() {
        String html = "";
        Document parsed = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, parsed.tagName());
        assertEquals(0, parsed.childNodes().size());
    }

    @Test
    public void testParseWithWhitespace() {
        String html = "<html>\t<head>\t<title>\tTest\t</title>\t</head>\t<body>\t<div>\t<p>\tTest\t</p>\t</div>\t</body>\t</html>";
        Document parsed = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, parsed.tagName());
        assertEquals(1, parsed.childNodes().size());
        assertEquals(headTag, parsed.head().tagName());
        assertEquals(1, parsed.head().childNodes().size());
        assertEquals(titleTag, parsed.head().childNodes().get(0).tagName());
        assertEquals("Test", parsed.head().childNodes().get(0).text());
        assertEquals(bodyTag, parsed.body().tagName());
        assertEquals(1, parsed.body().childNodes().size());
        assertEquals(divTag, parsed.body().childNodes().get(0).tagName());
        assertEquals(1, parsed.body().childNodes().get(0).childNodes().size());
        assertEquals(pTag, parsed.body().childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("Test", parsed.body().childNodes().get(0).childNodes().get(0).text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- Test Comment -->";
        Document parsed = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, parsed.tagName());
        assertEquals(1, parsed.childNodes().size());
        assertEquals(Comment.class, parsed.childNodes().get(0).getClass());
        assertEquals("Test Comment", parsed.childNodes().get(0).text());
    }

    @Test
    public void testParseXmlDeclaration() {
        String html = "<html xmlns:foo='bar' xml:lang='en-US'>";
        Document parsed = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, parsed.tagName());
        assertEquals(1, parsed.childNodes().size());
        assertEquals(
                new XmlDeclaration("xmlns:foo=\"bar\" xml:lang=\"en-US\"", "http://example.com", false),
                parsed.childNodes().get(0));
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><div><p>Test</p></div></body></html>";
        Document parsed = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, parsed.tagName());
        assertEquals(1, parsed.childNodes().size());
        assertEquals(bodyTag, parsed.tagName(BodyTag.class));
        assertEquals(1, parsed.body().childNodes().size());
        assertEquals(divTag, parsed.body().childNodes().get(0).tagName());
        assertEquals(1, parsed.body().childNodes().get(0).childNodes().size());
        assertEquals(pTag, parsed.body().childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("Test", parsed.body().childNodes().get(0).childNodes().get(0).text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title><body><div><p>Test</p></div></body></html>";
        Document parsed = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, parsed.tagName());
        assertEquals(1, parsed.childNodes().size());
        assertEquals(headTag, parsed.head().tagName());
        assertEquals(1, parsed.head().childNodes().size());
        assertEquals(titleTag, parsed.head().childNodes().get(0).tagName());
        assertEquals("Test", parsed.head().childNodes().get(0).text());
        assertEquals(bodyTag, parsed.body().tagName());
        assertEquals(1, parsed.body().childNodes().size());
        assertEquals(divTag, parsed.body().childNodes().get(0).tagName());
        assertEquals(1, parsed.body().childNodes().get(0).childNodes().size());
        assertEquals(pTag, parsed.body().childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("Test", parsed.body().childNodes().get(0).childNodes().get(0).text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><body><input type='text' id='test' value='Test'></body></html>";
        Document parsed = Parser.parse(html, "http://example.com");

        Element input = parsed.body().childNodes().get(0);
        List<Attribute> attributes = input.attributes();

        assertEquals(3, attributes.size());
        assertEquals("type", attributes.get(0).getKey());
        assertEquals("text", attributes.get(0).getValue());
        assertEquals("id", attributes.get(1).getKey());
        assertEquals("test", attributes.get(1).getValue());
        assertEquals("value", attributes.get(2).getKey());
        assertEquals("Test", attributes.get(2).getValue());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><div>Test</div></body></html>";
        Document parsed = Parser.parse(html, "http://example.com");

        Element div = parsed.body().childNodes().get(0);
        Node textNode = div.childNodes().get(0);

        assertEquals(TextNode.class, textNode.getClass());
        assertEquals("Test", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><script><![CDATA[function Test(){ alert('Hello World'); }]]></script></body></html>";
        Document parsed = Parser.parse(html, "http://example.com");

        Element script = parsed.body().childNodes().get(0);
        Node cdataNode = script.childNodes().get(0);

        assertEquals(DataNode.class, cdataNode.getClass());
        assertEquals("function Test(){ alert('Hello World'); }", cdataNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        String html = "<html><body><div<p>Test</p></div></body></html>";
        Parser.parse(html, "http://example.com"); // should throw an exception
    }

    @Test
    public void testParseInvalidHtmlWithErrorHandler() {
        String html = "<html><body><div<p>Test</p></div></body></html>";
        Document parsed = Parser.parse(html, "http://example.com", new Parser.ErrorHandler() {
            @Override
            public void handleError(String message, int line, int column) {
                System.out.println("Error at line " + line + ", column " + column + ": " + message);
            }
        });

        assertEquals(htmlTag, parsed.tagName());
        assertEquals(1, parsed.childNodes().size());
        assertEquals(bodyTag, parsed.tagName(BodyTag.class));
        assertEquals(1, parsed.body().childNodes().size());
        assertEquals(divTag, parsed.body().childNodes().get(0).tagName());
        assertNull(parsed.body().childNodes().get(0).childNodes());
    }
}