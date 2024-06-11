package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

public class ParserTest {

    private Document doc;
    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html>\n" +
                      "<head>\n" +
                      "<title>Test Title</title>\n" +
                      "</head>\n" +
                      "<body>\n" +
                      "<p id=\"p1\">Test content</p>\n" +
                      "<img src=\"test.png\" alt=\"Test Image\">\n" +
                      "<script src=\"script.js\"></script>\n" +
                      "<textarea readonly=\"readonly\">Test textarea</textarea>\n" +
                      "</body>\n" +
                      "</html>";

        parser = new Parser(html, null, false);
        doc = parser.parse();
    }

    @After
    public void tearDown() {
        parser = null;
        doc = null;
    }

    @Test
    public void testParse() {
        assertSame(org.jsoup.nodes.Document.class, doc.getClass());
        assertFalse(doc.body().text().isEmpty());
        assertTrue(doc.head() != null);
        assertEquals(1, doc.numHeadElements());
        assertEquals(1, doc.numBodyElements());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p id=\"p2\">Test fragment</p>";
        Document fragment = Parser.parseBodyFragment(bodyHtml, null);

        assertSame(org.jsoup.nodes.Document.class, fragment.getClass());
        assertEquals(1, fragment.numBodyElements());
        assertNull(fragment.head());
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("<!-- Test comment -->", null, false);
        Document doc = parser.parse();

        assertFalse(doc.body().text().isEmpty());
        Element comment = doc.body().child(0);

        assertTrue(comment instanceof Comment);
        assertEquals("Test comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", null, false);
        Document doc = parser.parse();

        assertTrue(doc.xmlDeclaration() != null);
    }

    @Test(expected = IllegalStateException.class)
    public void testParseInvalidXmlDecl() {
        Parser parser = new Parser("<!DOCTYPE html SYSTEM \"about:legacy-compat\" bla bla>", null, false);
        parser.parse();
    }

    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("<html><p>Test content</p></html>", null, false);
        Document doc = parser.parse();

        assertEquals(2, doc.numBodyElements());
        Element p = doc.body().child(0);
        assertEquals("p", p.tagName());
        assertFalse(p.text().isEmpty());

        parser = new Parser("<html><p>Test content</p></html><p>Test 2</p>", null, false);
        doc = parser.parse();

        assertEquals(3, doc.numBodyElements());
        p = doc.body().child(1);
        assertEquals("p", p.tagName());
        assertFalse(p.text().isEmpty());
    }

    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("<html><body><p>Test content</p></body></html>", null, false);
        Document doc = parser.parse();

        Element body = doc.body();
        Element p = body.child(0);

        assertEquals("p", p.tagName());
        assertFalse(p.text().isEmpty());
        assertSame(body, p.parent());

        // Test with empty tag
        parser = new Parser("<html><body><img src=\"test.png\"></body></html>", null, false);
        doc = parser.parse();

        Element img = doc.body().child(0);
        assertTrue(img.tagName().isEmpty());
        assertFalse(img.text().isEmpty());
        assertSame(doc.body(), img.parent());
    }

    @Test
    public void testParseAttribute() {
        Parser parser = new Parser("<p id=\"p1\" class=\"test\" title=\"Test title\"></p>", null, false);
        Element p = parser.parse().body().child(0);

        Map<String, Attribute> attributes = p.attributes();
        List<Map.Entry<String, Attribute>> entries = new ArrayList<>(attributes.entrySet());

        assertEquals(3, entries.size());
        Map.Entry<String, Attribute> idEntry = entries.get(0);
        assertEquals("id", idEntry.getKey());
        assertEquals("p1", idEntry.getValue().value());

        Map.Entry<String, Attribute> classEntry = entries.get(1);
        assertEquals("class", classEntry.getKey());
        assertEquals("test", classEntry.getValue().value());

        Map.Entry<String, Attribute> titleEntry = entries.get(2);
        assertEquals("title", titleEntry.getKey());
        assertEquals("Test title", titleEntry.getValue().value());
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("<p>Test content</p>", null, false);
        Document doc = parser.parse();
        Element p = doc.body().child(0);

        TextNode textNode = (TextNode) p.child(0);
        assertEquals("Test content", textNode.text());
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("<script><![CDATA[function test() { alert('Hello World'); }]]></script>", null, false);
        Document doc = parser.parse();
        Element script = doc.body().child(0);

        DataNode dataNode = (DataNode) script.child(0);
        assertEquals("<![CDATA[function test() { alert('Hello World'); }]]>", dataNode.data());
    }

    @Test
    public void testParseInvalidTagName() {
        Parser parser = new Parser("<html><invalid><p>Test content</p></invalid></html>", null, false);

        try {
            parser.parse();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testParseInvalidEndTagName() {
        Parser parser = new Parser("<html><body><div></div></invalid>", null, false);

        try {
            parser.parse();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testParseImplicitParent() {
        Parser parser = new Parser("<textarea readonly=\"readonly\">Test textarea</textarea>", null, false);
        Document doc = parser.parse();
        Element textarea = doc.body().child(0);

        assertTrue(textarea.parent() instanceof Element);
        Element implicitParent = (Element) textarea.parent();
        assertEquals("div", implicitParent.tagName());
        assertEquals(1, implicitParent.children().size());
        assertEquals(textarea, implicitParent.child(0));
    }

    @Test
    public void testParseBaseTag() {
        Parser parser = new Parser("<base href=\"/test\">", null, false);
        Document doc = parser.parse();
        assertEquals("/test", doc.baseUri());
    }
}