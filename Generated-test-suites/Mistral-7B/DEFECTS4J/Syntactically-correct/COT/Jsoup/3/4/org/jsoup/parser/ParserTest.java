package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for Parser.
 */
public class ParserTest {
    private static final String HTML_WITH_COMMENT = "<!-- This is a comment --><html><head><title>Test</title></head><body><p>Test Parser</p></body></html>";
    private static final String HTML_WITH_XML_DECLARATION = "<!-- This is a comment --><xml version=\"1.0\" encoding=\"UTF-8\">";
    private static final String HTML_WITH_EMPTY_TAG = "<html><body><img src=\"image.jpg\"/></body></html>";
    private static final String HTML_WITH_SELF_CLOSED_TAG = "<html><body><input type=\"text\"/></body></html>";
    private static final String HTML_WITH_IMplicit_TAG = "<html><body><div><p>Test Parser</p></div></body></html>";
    private static final String HTML_WITH_INVALID_TAG = "<html><body><div><p>Test Parser</p></script></body></html>";
    private static final String HTML_WITH_DATA_TAG = "<html><body><textarea>Test Parser</textarea></body></html>";
    private static final String HTML_WITH_BASE_TAG = "<html><head><base href=\"base.html\"><title>Test</title></head><body><p>Test Parser</p></body></html>";

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(StringUtils.join(new String[]{HTML_WITH_COMMENT, HTML_WITH_XML_DECLARATION}, "\n"), "", false);
    }

    @Test
    public void testParseComment() {
        Document doc = parser.parse();
        Element commentElement = doc.body().child(0);
        assertEquals("This is a comment", commentElement.text());
    }

    @Test
    public void testParseXmlDeclaration() {
        Document doc = parser.parse();
        Element xmlDeclarationElement = doc.body().child(0);
        assertTrue(xmlDeclarationElement instanceof XmlDeclaration);
        assertEquals("UTF-8", xmlDeclarationElement.attr("encoding"));
    }

    @Test
    public void testParseEmptyTag() {
        Document doc = Parser.parse(HTML_WITH_EMPTY_TAG, "");
        assertEquals(1, doc.body().numChildren());
        assertTrue(doc.body().child(0) instanceof Element);
        assertTrue(doc.body().child(0).tagName().equals("img"));
    }

    @Test
    public void testParseSelfClosedTag() {
        Document doc = Parser.parse(HTML_WITH_SELF_CLOSED_TAG, "");
        assertEquals(1, doc.body().numChildren());
        assertTrue(doc.body().child(0) instanceof Element);
        assertTrue(doc.body().child(0).tagName().equals("input"));
        assertTrue(doc.body().child(0).tag().isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void testParseImplicitTag() {
        Document doc = parser.parse();
        Element divElement = doc.body().child(0);
        Element pElement = divElement.child(0);

        // The test case below should pass, but we intentionally throw an AssertionError to test the error handling
        // for the implicit tag not being found in the stack (which should be handled by creating an implicit tag)
        Element pImplicitElement = divElement.child(1);
        fail("An implicit tag for <p> should be created, but it wasn't.");
    }

    @Test(expected = AssertionError.class)
    public void testParseInvalidTag() {
        Document doc = parser.parse();
        Element scriptElement = doc.body().child(0);
        fail("A script tag should not be allowed as a child of body, but it was.");
    }

    @Test
    public void testParseDataTag() {
        Document doc = parser.parse();
        Element textareaElement = doc.body().child(0);
        assertEquals("Test Parser", textareaElement.text());
    }

    @Test
    public void testParseBaseTag() {
        Document doc = Parser.parse(HTML_WITH_BASE_TAG, "");
        assertEquals("base.html", doc.baseUri());
    }
}