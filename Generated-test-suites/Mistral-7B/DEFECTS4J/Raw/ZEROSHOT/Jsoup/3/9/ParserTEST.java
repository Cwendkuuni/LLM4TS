package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    private static final String TEST_HTML = "<html><head><title>Test</title></head>"
            + "<body><h1>Test Heading</h1>"
            + "<p>Test Paragraph</p>"
            + "<p><strong>Test Bold Text</strong></p>"
            + "<p>Test Paragraph with <a href=\"http://example.com\">link</a></p>"
            + "<script type=\"text/javascript\">alert('JavaScript');</script>"
            + "<textarea>Test textarea</textarea></body></html>";

    private Parser parser;
    private Document doc;

    @Before
    public void setUp() {
        parser = new Parser(TEST_HTML, null, false);
        doc = parser.parse();
    }

    @Test
    public void testParserDocument() {
        assertNotNull(parser);
        assertNotNull(doc);
    }

    @Test
    public void testParserBodyFragment() {
        String bodyHtml = "<p>Test Paragraph</p>"
                + "<p><strong>Test Bold Text</strong></p>"
                + "<p>Test Paragraph with <a href=\"http://example.com\">link</a></p>";
        Document bodyDoc = Parser.parseBodyFragment(bodyHtml, null);
        assertNotNull(bodyDoc);
        assertEquals(bodyDoc.body().child(0).text(), "Test Paragraph");
        assertEquals(bodyDoc.body().child(1).child(0).text(), "Test Bold Text");
        assertEquals(bodyDoc.body().child(2).child(0).tagName(), "a");
        assertEquals(bodyDoc.body().child(2).child(0).attr("href"), "http://example.com");
    }

    @Test
    public void testParseComment() {
        parser = new Parser("<html><!-- Test comment --></html>", null, false);
        parser.parse();
        Element comment = doc.child(0).child(0);
        assertEquals(comment.text(), "-- Test comment --");
    }

    @Test
    public void testParseXmlDecl() {
        parser = new Parser("<html xmlns=\"http://www.w3.org/1999/xhtml\">", null, false);
        parser.parse();
        XmlDeclaration xmlDecl = doc.child(0);
        assertEquals(xmlDecl.xmlVersion(), "1.0");
        assertEquals(xmlDecl.xmlEncoding(), "UTF-8");
        assertNull(xmlDecl.xmlStandalone());
        assertEquals(xmlDecl.xmlMapping(), null);
    }

    @Test
    public void testParseEndTag() {
        parser = new Parser("<p><h1>Test Heading</h1></p>", null, false);
        parser.parse();
        Element h1 = doc.body().child(0).child(0);
        assertEquals(h1.tagName(), "h1");
        assertNull(h1.nextSibling());
    }

    @Test
    public void testParseStartTag() {
        parser = new Parser("<p><h1>Test Heading</h1></p>", null, false);
        parser.parse();
        Element h1 = doc.body().child(0).child(0);
        assertEquals(h1.tagName(), "h1");
        assertEquals(h1.child(0).text(), "Test Heading");
    }

    @Test
    public void testParseAttribute() {
        parser = new Parser("<img src=\"image.jpg\" alt=\"Test Image\" title=\"Test Title\" />", null, false);
        parser.parse();
        Element img = doc.body().child(0);
        assertEquals(img.tagName(), "img");
        assertEquals(img.attr("src"), "image.jpg");
        assertEquals(img.attr("alt"), "Test Image");
        assertEquals(img.attr("title"), "Test Title");
        assertNull(img.attr("class"));
    }

    @Test
    public void testParseTextNode() {
        parser = new Parser("<p>Test Paragraph</p>", null, false);
        parser.parse();
        Element p = doc.body().child(0);
        TextNode textNode = p.child(0);
        assertEquals(textNode.text(), "Test Paragraph");
    }

    @Test
    public void testParseCdata() {
        parser = new Parser("<script type=\"text/javascript\">alert('JavaScript');</script>", null, false);
        parser.parse();
        Element script = doc.body().child(0);
        TextNode cdataNode = script.child(0);
        assertEquals(cdataNode.text(), "alert('JavaScript');");
    }

    @Test
    public void testParseError() {
        String invalidHtml = "<html><p><script>alert('JavaScript');</p>";
        try {
            parser = new Parser(invalidHtml, null, false);
            parser.parse();
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(), "Error parsing start tag : Parse error at line 2 column 31: unexpected char: s");
            return;
        }
        fail("Expected an exception but no exception was thrown.");
    }

    @Test
    public void testParseBodyFragmentWithBaseTag() {
        String bodyHtmlWithBase = "<html><head><base href=\"base.html\"></head><body>"
                + "<p>Test Paragraph</p>"
                + "<a href=\"relative.html\">Relative link</a>"
                + "</body></html>";
        Document bodyDoc = Parser.parseBodyFragment(bodyHtmlWithBase, null);
        assertEquals(bodyDoc.baseUri(), "base.html");
        Element link = bodyDoc.body().child(0).child(1);
        assertEquals(link.absUrl("href"), "relative.html");
    }

    @Test
    public void testParseScriptTag() {
        String scriptHtml = "<script src=\"script.js\"></script>";
        Document doc = Parser.parseBodyFragment(scriptHtml, null);
        Element script = doc.body().child(0);
        assertEquals(script.tagName(), "script");
        assertNull(script.child(0));
        assertEquals(script.attr("src"), "script.js");
        assertEquals(script.absUrl("src"), "script.js");
    }

    @Test
    public void testParseTextareaTag() {
        String textareaHtml = "<textarea>Test textarea</textarea>";
        Document doc = Parser.parseBodyFragment(textareaHtml, null);
        Element textarea = doc.body().child(0);
        assertEquals(textarea.tagName(), "textarea");
        assertEquals(textarea.child(0).text(), "Test textarea");
    }

    @Test
    public void testParseImplicitParent() {
        String implicitParentHtml = "<img src=\"image.jpg\" title=\"Test Title\" />";
        Document doc = Parser.parseBodyFragment(implicitParentHtml, null);
        Element img = doc.body().child(0);
        assertEquals(img.tagName(), "img");
        Element parent = img.parent();
        assertEquals(parent.tagName(), "p");
        assertEquals(parent.child(0).tagName(), "img");
    }

    @Test
    public void testParseInvalidParent() {
        String implicitParentHtml = "<html><body><div><img src=\"image.jpg\" title=\"Test Title\" /></div></body></html>";
        Document doc = Parser.parseBodyFragment(implicitParentHtml, null);
        Element img = doc.body().child(0).child(1);
        Element parent = img.parent();
        assertEquals(parent.tagName(), "div");
        assertNull(parent.previousSibling());
        assertEquals(parent.nextSibling(), doc.body().child(1));
    }

    @After
    public void tearDown() {
        parser = null;
        doc = null;
    }
}