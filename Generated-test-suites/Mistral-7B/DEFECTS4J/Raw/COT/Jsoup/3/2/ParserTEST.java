package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Test class for Parser.
 */
public class ParserTest {

    private static final String HTML_WITH_COMMENT = "<html><!-- This is a comment --><body></body></html>";
    private static final String HTML_WITH_CDATA = "<html><![CDATA[ <p>This is a paragraph</p> ]]></html>";
    private static final String HTML_WITH_XML_DECL = "<html xmlns:xml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">";
    private static final String HTML_WITH_SCRIPT = "<html><head><script type=\"text/javascript\">function test(){}</script></head><body></body></html>";
    private static final String HTML_WITH_IMG = "<html><head></head><body><img src=\"image.jpg\"></body></html>";
    private static final String HTML_WITH_BASE = "<html><head><base href=\"base.html\"></base></head><body><div></div></body></html>";
    private static final String HTML_WITH_TEXTAREA = "<html><head></head><body><textarea></textarea></body></html>";
    private static final String HTML_WITH_EMPTY_TAG = "<html><body><div></div></body></html>";

    private Parser parser;

    @Before
    public void setUp() {
        String html = HTML_WITH_COMMENT; // Change this to test other cases
        String baseUri = "http://example.com";
        parser = new Parser(html, baseUri, false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment(HTML_WITH_COMMENT, baseUri);
        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(baseUri, doc.baseUri());
        assertTrue(doc.body().childNodeSize() > 0);
    }

    @Test
    public void testParseComment() {
        parser.parse(); // Parse the HTML to populate the stack
        Comment comment = parser.last().child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        Document doc = parser.parse();
        XmlDeclaration xmlDecl = doc.head().child(0);
        assertEquals("xmlns:xml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"", xmlDecl.data());
    }

    @Test
    public void testParseEndTag() {
        parser.parse(); // Parse the HTML to populate the stack
        parser.popStackToClose(bodyTag);
        assertTrue(parser.stack.size() == 1);
        assertEquals(htmlTag, parser.stack.getLast().tag());
    }

    @Test(expected = AssertionError.class)
    public void testParseEndTagInvalidTag() {
        parser.parse(); // Parse the HTML to populate the stack
        parser.popStackToClose(textareaTag); // textareaTag is not a valid parent for bodyTag
    }

    @Test
    public void testParseStartTag() {
        parser.parse(); // Parse the HTML to populate the stack
        Element div = parser.last().child(0);
        assertEquals("div", div.tagName());
        assertTrue(div.childNodeSize() == 0);
    }

    @Test
    public void testParseAttribute() {
        parser.parse(); // Parse the HTML to populate the stack
        Element img = parser.last().child(0);
        Attribute src = img.attribute("src");
        assertEquals("image.jpg", src.value());
    }

    @Test
    public void testParseTextNode() {
        parser.parse(); // Parse the HTML to populate the stack
        TextNode textNode = (TextNode) parser.last().child(0);
        assertEquals("", textNode.text());
    }

    @Test
    public void testParseCdata() {
        Document doc = parser.parse();
        TextNode textNode = (TextNode) doc.body().child(0);
        assertEquals("<p>This is a paragraph</p>", textNode.text());
    }

    @Test
    public void testParseBase() {
        Document doc = parser.parse();
        Element base = doc.head().child(0);
        assertEquals("base.html", base.absUrl("href"));
    }

    @Test
    public void testParseTextarea() {
        Document doc = parser.parse();
        TextNode textNode = (TextNode) doc.body().child(0);
        assertEquals("", textNode.text());
        assertTrue(doc.body().child(1) instanceof Element);
        Element textarea = (Element) doc.body().child(1);
        assertEquals("textarea", textarea.tagName());
    }

    @Test
    public void testParseEmptyTag() {
        Document doc = parser.parse();
        Element div = doc.body().child(0);
        assertTrue(div.tagName().equals(bodyTag) || div.tagName().equals(htmlTag));
        assertTrue(div.childNodeSize() == 0);
    }
}