package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert.*;

import java.util.*;

/**
 * Test for Parser class.
 */
public class ParserTest {

    private static final String HTML_1 = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
    private static final String HTML_2 = "<html><head></head><body><h1>Hello</h1></body></html>";
    private static final String HTML_3 = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
    private static final String HTML_4 = "<html><head></head><body><h1>Hello</h1></body></html>";
    private static final String HTML_5 = "<html><head><title>Test</title></head><body><script src='script.js'></script><h1>Hello</h1></body></html>";
    private static final String HTML_6 = "<html><head><base href='/test'><title>Test</title></head><body><h1>Hello</h1></body></html>";
    private static final String HTML_7 = "<html><head><title>Test</title></head><body>";
    private static final String HTML_8 = "<html><head><title>Test</title></head><body><h1>Hello</h1><script>alert('Hello');</script></body></html>";
    private static final String HTML_9 = "<html><head><title>Test</title></head><body><textarea>Hello</textarea></body></html>";
    private static final String HTML_10 = "<html><head><title>Test</title></head><body><h1></h1></body></html>";

    private static Document parseDocument(String html, String baseUri) {
        return Parser.parse(html, baseUri);
    }

    private Element getElement(Document doc, String tagName) {
        Element element = null;
        for (Element child : doc.body().children()) {
            if (child.tagName().equals(tagName)) {
                element = child;
                break;
            }
        }
        return element;
    }

    private void testParser_ParseHtml(String html, String baseUri) {
        Document doc = parseDocument(html, baseUri);
        assertNotNull(doc);
        assertTrue(doc.body().children().size() > 0);
    }

    @Before
    public void setUp() {
        Validate.notNull(Parser.class);
        Validate.notNull(Document.class);
        Validate.notNull(Element.class);
    }

    

    /**
     * Test Parser.parse(String, String) method with a valid HTML string.
     */
    @Test
    public void testParser_ParseValidHtml() {
        String baseUri = "http://example.com";
        testParser_ParseHtml(HTML_1, baseUri);
    }

    /**
     * Test Parser.parse(String, String) method with an empty HTML string.
     */
    @Test
    public void testParser_ParseEmptyHtml() {
        String baseUri = "http://example.com";
        testParser_ParseHtml(StringUtils.EMPTY, baseUri);
    }

    /**
     * Test Parser.parse(String, String) method with an invalid HTML string.
     */
    @Test(expected = IllegalStateException.class)
    public void testParser_ParseInvalidHtml() {
        String baseUri = "http://example.com";
        testParser_ParseHtml(HTML_10, baseUri);
    }

    /**
     * Test Parser.parseBodyFragment(String, String) method with a valid HTML string.
     */
    @Test
    public void testParser_ParseBodyFragmentValidHtml() {
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(HTML_2, baseUri);
        assertNotNull(doc);
        assertTrue(doc.body().children().size() > 0);
    }

    /**
     * Test Parser.parseBodyFragment(String, String) method with an empty HTML string.
     */
    @Test
    public void testParser_ParseBodyFragmentEmptyHtml() {
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(StringUtils.EMPTY, baseUri);
        assertNotNull(doc);
        assertTrue(doc.body().children().size() == 0);
    }

    /**
     * Test Parser.parseBodyFragment(String, String) method with an invalid HTML string.
     */
    @Test(expected = IllegalStateException.class)
    public void testParser_ParseBodyFragmentInvalidHtml() {
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(HTML_10, baseUri);
        fail("Expected an IllegalArgumentException");
    }

    /**
     * Test Parser.parse() method for handling comments.
     */
    @Test
    public void testParser_ParseComment() {
        String baseUri = "http://example.com";
        Document doc = Parser.parse(HTML_3, baseUri);
        Element comment = getElement(doc, "comment");
        assertNotNull(comment);
        assertEquals("Test", comment.text());
    }

    /**
     * Test Parser.parse() method for handling XML declarations.
     */
    @Test
    public void testParser_ParseXmlDeclaration() {
        String baseUri = "http://example.com";
        Document doc = Parser.parse(HTML_5, baseUri);
        Element xmlDeclaration = getElement(doc, "xml-declaration");
        assertNotNull(xmlDeclaration);
        // Check for valid XML declaration
        String xmlDeclarationText = xmlDeclaration.html();
        assertTrue(xmlDeclarationText.startsWith("<?xml"));
        assertTrue(xmlDeclarationText.contains(" version='1.0' encoding='UTF-8'"));
    }

    /**
     * Test Parser.parse() method for handling end tags.
     */
    @Test
    public void testParser_ParseEndTag() {
        String baseUri = "http://example.com";
        Document doc = Parser.parse(HTML_6, baseUri);
        Element base = getElement(doc, "base");
        assertNotNull(base);
        Element title = getElement(doc, "title");
        assertNotNull(title);
        assertNull(getElement(doc, "h1"));
    }

    /**
     * Test Parser.parse() method for handling start tags.
     */
    @Test
    public void testParser_ParseStartTag() {
        String baseUri = "http://example.com";
        Document doc = Parser.parse(HTML_8, baseUri);
        Element title = getElement(doc, "title");
        assertNotNull(title);
        Element h1 = getElement(doc, "h1");
        assertNotNull(h1);
        Element script = getElement(doc, "script");
        assertNotNull(script);
        Element textarea = getElement(doc, "textarea");
        assertNull(textarea);
    }

    /**
     * Test Parser.parse() method for handling attributes.
     */
    @Test
    public void testParser_ParseAttribute() {
        String baseUri = "http://example.com";
        Document doc = Parser.parse(HTML_9, baseUri);
        Element textarea = getElement(doc, "textarea");
        assertNotNull(textarea);
        assertEquals("Hello", textarea.text());

        Element img = new Element(Tag.valueOf("img"), baseUri);
        img.setAttribute("src", "image.jpg");
        Element imgElement = Parser.parseStartTag(img.html());
        assertEquals("img", imgElement.tagName());
        assertEquals("image.jpg", imgElement.attr("src"));
    }

    /**
     * Test Parser.parse() method for handling text nodes.
     */
    @Test
    public void testParser_ParseTextNode() {
        String baseUri = "http://example.com";
        Document doc = Parser.parse(HTML_1, baseUri);
        Element h1 = getElement(doc, "h1");
        assertNotNull(h1);
        Text textNode = (Text) h1.childNodes().get(0);
        assertEquals("Hello", textNode.text());
    }

    /**
     * Test Parser.parse() method for handling CDATA sections.
     */
    @Test
    public void testParser_ParseCdata() {
        String baseUri = "http://example.com";
        Document doc = Parser.parse(HTML_4, baseUri);
        Element script = getElement(doc, "script");
        assertNotNull(script);
        Text cdataTextNode = (Text) script.childNodes().get(0);
        assertEquals("alert('Hello');", cdataTextNode.text());
    }

    
}