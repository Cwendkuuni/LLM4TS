package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class ParserTest {

    private static final String HTML_WITH_COMMENT = "<html><!-- This is a comment --></html>";
    private static final String HTML_WITH_CDATA = "<html><![CDATA[This is CDATA section]]></html>";
    private static final String HTML_WITH_XML_DECL = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">";
    private static final String BODY_FRAGMENT = "<body><p>Body fragment</p></body>";
    private static final String HTML_EMPTY = "<html></html>";
    private static final String HTML_WITH_EMPTY_TAG = "<html><img/></html>";
    private static final String HTML_WITH_MIXED_CONTENT = "<html><a href=\"link.html\" title=\"Title\"><img src=\"image.png\" alt=\"Alt\"/><p>Paragraph</p></a></html>";
    private static final String HTML_WITH_BASE_TAG = "<html><base href=\"base.html\"><body><a href=\"link.html\">Link</a></body></html>";
    private static final String INVALID_HTML = "<html><html/><body></html>"; // invalid HTML
    private static final String HTML_WITH_UNCLOSED_TAG = "<html><p>Open tag<div"; // unclosed tag

    private Document doc;
    private Parser parser;

    @Before
    public void setUp() {
        doc = null;
        parser = null;
    }

    @Test
    public void testParse() {
        String html = HTML_WITH_COMMENT;
        doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.body().childSize());
        assertEquals(1, doc.head().childSize());
        assertEquals(1, doc.head().child(0).childSize()); // comment

        assertEquals(htmlTag, parser.last().tag());

        // check comment
        Comment comment = (Comment) doc.head().child(0);
        assertEquals("This is a comment", comment.data());
        assertEquals("http://example.com", comment.baseUri());

        // check CDATA
        html = HTML_WITH_CDATA;
        doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.body().childSize());
        assertEquals(1, doc.head().childSize());
        assertEquals(1, doc.body().child(0).childSize()); // CDATA

        // check XML declaration
        html = HTML_WITH_XML_DECL;
        doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.head().childSize());
        assertEquals(1, doc.head().child(0).childSize()); // XML declaration

        // check body fragment
        doc = null;
        parser = new Parser(BODY_FRAGMENT, "http://example.com", true);
        doc = parser.parse();
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.body().childSize());
        assertEquals("Body fragment", doc.body().text());

        // check empty HTML
        doc = Parser.parse(HTML_EMPTY, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(0, doc.body().childSize());

        // check empty tag
        doc = Parser.parse(HTML_WITH_EMPTY_TAG, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.body().childSize());
        assertEquals(0, doc.body().child(0).childSize());

        // check mixed content
        doc = Parser.parse(HTML_WITH_MIXED_CONTENT, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.body().childSize());
        assertEquals(1, doc.body().child(0).childSize()); // anchor tag
        assertEquals("Link", doc.body().child(0).child(0).tagName());
        assertEquals("link.html", doc.body().child(0).child(0).attr("href"));
        assertEquals("Title", doc.body().child(0).child(0).attr("title"));
        assertEquals(1, doc.body().child(0).child(0).childSize()); // image
        assertEquals("image.png", doc.body().child(0).child(0).child(0).attr("src"));
        assertEquals("Alt", doc.body().child(0).child(0).child(0).attr("alt"));
        assertEquals(1, doc.body().child(0).childSize() + 2); // paragraph
        assertEquals("Paragraph", doc.body().child(0).child(1).text());

        // check base tag
        doc = Parser.parse(HTML_WITH_BASE_TAG, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.body().childSize());
        assertEquals("link.html", doc.body().child(0).attr("href"));

        // check invalid HTML
        try {
            doc = Parser.parse(INVALID_HTML, "http://example.com");
            fail("Expected IllegalArgumentException for invalid HTML");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // check unclosed tag
        try {
            doc = Parser.parse(HTML_WITH_UNCLOSED_TAG, "http://example.com");
            fail("Expected IllegalArgumentException for unclosed tag");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // check parsing of empty tag with attributes
        String htmlWithEmptyTagAttributes = "<html><img src=\"image.png\" height=\"100\" width=\"100\"/></html>";
        doc = Parser.parse(htmlWithEmptyTagAttributes, "http://example.com");
        Element img = doc.body().child(0);
        assertEquals("img", img.tagName());
        assertEquals("image.png", img.attr("src"));
        assertEquals("100", img.attr("height"));
        assertEquals("100", img.attr("width"));
        assertEquals(1, img.childSize());
        assertEquals(1, img.child(0).childSize());
        assertEquals("", img.child(0).tagName());
        assertEquals("", img.child(0).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        Parser parser = new Parser(null, "http://example.com", false);
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        Parser parser = new Parser("<html></html>", null, false);
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyBaseUri() {
        Parser parser = new Parser("<html></html>", "", false);
        parser.parse();
    }
}