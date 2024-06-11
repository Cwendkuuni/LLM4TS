package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * ParserTest.java
 */
public class ParserTest {

    @Test
    public void testParse() {
        Parser parser = new Parser("", "", false);
        Document doc = parser.parse();
        assertNotNull(doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser parser = new Parser(null, "", false);
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        Parser parser = new Parser("", null, false);
        parser.parse();
    }

    @Test
    public void testParseBodyFragment() {
        Parser parser = new Parser("<body></body>", "", true);
        Document doc = parser.parse();
        assertEquals(1, doc.numberOfChildren());
        assertTrue(doc.body().hasText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullHtml() {
        Parser parser = new Parser(null, "", true);
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullBaseUri() {
        Parser parser = new Parser("<body></body>", null, true);
        parser.parse();
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Parser parser = new Parser("<div><!--comment--></div>", "", true);
        Document doc = parser.parse();
        assertEquals(1, doc.numberOfChildren());
        assertTrue(doc.body().hasText());
        assertEquals(1, doc.body().numberOfChildren());
        assertTrue(doc.body().child(0) instanceof Comment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentRelaxedNullHtml() {
        Parser parser = new Parser(null, "", true);
        parser.parseRelaxed();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentRelaxedNullBaseUri() {
        Parser parser = new Parser("<body></body>", null, true);
        parser.parseRelaxed();
    }

    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
        Document doc = parser.parse();
        Element html = doc;
        assertEquals(3, html.numberOfChildren());
        assertEquals(htmlTag, html.tag());
        Element head = html.child(0);
        assertEquals(headTag, head.tag());
        Element title = head.child(0);
        assertEquals(titleTag, title.tag());
        assertEquals("Test", title.text());
        Element body = html.child(1);
        assertEquals(bodyTag, body.tag());
        Element h1 = body.child(0);
        assertEquals(h1Tag, h1.tag());
        assertEquals("Test", h1.text());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseStartTagEmptyTag() {
        Parser parser = new Parser("<img />");
        parser.parse();
        fail();
    }

    @Test(expected = IllegalStateException.class)
    public void testParseStartTagEmptyTagRelaxed() {
        Parser parser = new Parser("<img />", "", true);
        parser.parse();
        fail();
    }

    @Test(expected = IllegalStateException.class)
    public void testParseStartTagEmptyTagWithText() {
        Parser parser = new Parser("<img>Test</img>");
        parser.parse();
        fail();
    }

    @Test(expected = IllegalStateException.class)
    public void testParseStartTagEmptyTagWithTextRelaxed() {
        Parser parser = new Parser("<img>Test</img>", "", true);
        parser.parse();
        fail();
    }

    @Test(expected = IllegalStateException.class)
    public void testParseEndTag() {
        Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
        parser.parse();
        Element html = doc;
        parser.parseEndTag("html");
        assertEquals(0, html.numberOfChildren());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseEndTagNonExistentTag() {
        Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
        parser.parse();
        Element html = doc;
        parser.parseEndTag("body");
        fail();
    }

    @Test(expected = IllegalStateException.class)
    public void testParseEndTagNonExistentTagRelaxed() {
        Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>", "", true);
        parser.parse();
        Element html = doc;
        parser.parseEndTag("body");
        fail();
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("<!--Test-->");
        Document doc = parser.parse();
        Element comment = doc.body().child(0);
        assertEquals("Test", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("<?xml version='1.0'?>\n<html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
        Document doc = parser.parse();
        XmlDeclaration xmlDeclaration = doc.head().child(0);
        assertEquals("1.0", xmlDeclaration.version());
    }

    @Test
    public void testParseEndTagComment() {
        Parser parser = new Parser("<!--\n<html><head><title>Test</title></head><body><h1>Test</h1></body>\n-->\n");
        Document doc = parser.parse();
        assertEquals(0, doc.numberOfChildren());
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("<![CDATA[Test]]>");
        Document doc = parser.parse();
        TextNode cdata = doc.child(0);
        assertEquals("Test", cdata.text());
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("<p>Test</p>");
        Document doc = parser.parse();
        TextNode textNode = doc.body().child(0);
        assertEquals("Test", textNode.text());
    }

    @Test
    public void testParseTextNodeWithSpecialChars() {
        Parser parser = new Parser("<p>&lt; &gt; &amp; &quot;</p>");
        Document doc = parser.parse();
        TextNode textNode = doc.body().child(0);
        assertEquals("&lt; &gt; &amp; &quot;", textNode.text());
    }

    @Test
    public void testParseTextNodeWithSpecialCharsEncoded() {
        Parser parser = new Parser("<p>&lt; &gt; &amp; &quot;</p>");
        Document doc = parser.parse();
        TextNode textNode = doc.body().child(0);
        assertEquals("< &gt; &amp; &quot;", textNode.htmlText());
    }

    @Test
    public void testParseBaseTag() {
        Parser parser = new Parser("<base href='https://example.com'><html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
        Document doc = parser.parse();
        assertEquals("https://example.com", doc.baseUri());
    }

    @Test
    public void testParseBaseTagWithoutHref() {
        Parser parser = new Parser("<base /><html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
        Document doc = parser.parse();
        assertEquals("", doc.baseUri());
    }

    @Test
    public void testParseBaseTagWithInvalidSyntax() {
        Parser parser = new Parser("<base foo='bar'><html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
        Document doc = parser.parse();
        assertEquals("", doc.baseUri());
    }

}