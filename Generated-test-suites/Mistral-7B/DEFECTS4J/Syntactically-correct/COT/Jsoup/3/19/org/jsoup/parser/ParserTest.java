package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;

/**
 * ParserTest.java
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element root = doc.getRootElement();

        // Basic functionality test
        assertEquals(html, doc.html());
        assertEquals("Test", root.getElementsByTag("title").text());
        assertEquals("Hello World", root.getElementsByTag("h1").text());

        // Edge cases and exception handling tests
        String emptyHtml = "";
        try {
            Parser parserEmpty = new Parser(emptyHtml, "http://example.com");
            parserEmpty.parse();
            fail("Expected IllegalArgumentException for empty HTML");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidHtml = "<html><head><title>Test</title></head><body><h1>Hello<script>World</script></body></html>";
        try {
            Parser parserInvalid = new Parser(invalidHtml, "http://example.com");
            parserInvalid.parse();
            fail("Expected IllegalArgumentException for invalid HTML");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String htmlWithComments = "<html><!--comment--><head><title>Test</title></head><body><!--comment--><h1>Hello World</h1></body></html>";
        Parser parserWithComments = new Parser(htmlWithComments, "http://example.com");
        Document docWithComments = parserWithComments.parse();
        assertNotNull(docWithComments.getElementsByTag("comment"));

        String htmlWithScript = "<html><head><title>Test</title><script>document.write('Hello World')</script></head><body></body></html>";
        Parser parserWithScript = new Parser(htmlWithScript, "http://example.com");
        Document docWithScript = parserWithScript.parse();
        assertNotNull(docWithScript.getElementsByTag("script"));
        assertEquals("", docWithScript.getElementsByTag("script").text());

        String htmlWithCdata = "<html><head><title>Test</title><![CDATA[document.write('Hello World')]]></head><body></body></html>";
        Parser parserWithCdata = new Parser(htmlWithCdata, "http://example.com");
        Document docWithCdata = parserWithCdata.parse();
        assertNotNull(docWithCdata.getElementsByTag("script"));
        String scriptText = docWithCdata.getElementsByTag("script").text();
        assertEquals("document.write('Hello World')", scriptText);

        String invalidBaseUri = "<html><head><title>Test</title></head><body><script>location.href='http:/'</script></body></html>";
        Parser parserInvalidBaseUri = new Parser(invalidBaseUri, "");
        Document docInvalidBaseUri = parserInvalidBaseUri.parse();
        assertEquals("", docInvalidBaseUri.getBaseUri());

        String htmlWithBase = "<html><head><title>Test</title><base href='/test'></head><body></body></html>";
        Parser parserWithBase = new Parser(htmlWithBase, "http://example.com");
        Document docWithBase = parserWithBase.parse();
        assertEquals("/test", docWithBase.getBaseUri());

        String htmlWithEmptyBody = "<html><head><title>Test</title></head></html>";
        Parser parserWithEmptyBody = new Parser(htmlWithEmptyBody, "http://example.com");
        Document docWithEmptyBody = parserWithEmptyBody.parse();
        assertNotNull(docWithEmptyBody.getElementsByTag("html"));
        assertNotNull(docWithEmptyBody.getElementsByTag("head"));
        assertEquals(0, docWithEmptyBody.body().childNodes().size());

        String htmlWithSelfClosing = "<html><head><title>Test</title></head><body><p><img src='test.jpg' alt='Test' /></p></body></html>";
        Parser parserWithSelfClosing = new Parser(htmlWithSelfClosing, "http://example.com");
        Document docWithSelfClosing = parserWithSelfClosing.parse();
        Element img = docWithSelfClosing.getElementsByTag("img").get(0);
        assertTrue(img.isEmptyElement());

        String htmlWithImplicitParent = "<html><body><p><img src='test.jpg' alt='Test'></p></body></html>";
        Parser parserWithImplicitParent = new Parser(htmlWithImplicitParent, "http://example.com");
        Document docWithImplicitParent = parserWithImplicitParent.parse();
        Element imgParent = docWithImplicitParent.getElementsByTag("p").get(0);
        Element imgElement = imgParent.getElementsByTag("img").get(0);
        assertEquals("p", imgParent.tagName());
        assertEquals("img", imgElement.tagName());

        String htmlWithInvalidTag = "<html><head><title>Test</title></head><body><invalid></body></html>";
        try {
            Parser parserWithInvalidTag = new Parser(htmlWithInvalidTag, "http://example.com");
            parserWithInvalidTag.parse();
            fail("Expected IllegalArgumentException for invalid tag");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<html><body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        Element root = doc.getRootElement();

        // Basic functionality test
        assertEquals(bodyHtml.substring(htmlTag.startIndex(), bodyTag.endIndex()), doc.html());
        assertEquals("Hello World", root.getElementsByTag("h1").text());

        // Edge cases and exception handling tests
        String emptyBodyHtml = "";
        try {
            Document docEmpty = Parser.parseBodyFragment(emptyBodyHtml, "http://example.com");
            fail("Expected IllegalArgumentException for empty HTML");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidBodyHtml = "<html><body><h1>Hello<script>World</script></body>";
        try {
            Document docInvalid = Parser.parseBodyFragment(invalidBodyHtml, "http://example.com");
            fail("Expected IllegalArgumentException for invalid HTML");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String bodyHtmlWithComments = "<html><body><!--comment--><h1>Hello World</h1><!--comment--></body>";
        Document docWithComments = Parser.parseBodyFragment(bodyHtmlWithComments, "http://example.com");
        assertNotNull(docWithComments.getElementsByTag("comment"));

        String bodyHtmlWithScript = "<html><body><h1>Hello<script>World</script></h1></body>";
        Document docWithScript = Parser.parseBodyFragment(bodyHtmlWithScript, "http://example.com");
        assertNotNull(docWithScript.getElementsByTag("script"));
        assertEquals("", docWithScript.getElementsByTag("script").text());

        String bodyHtmlWithCdata = "<html><body><h1><![CDATA[document.write('Hello World')]]></h1></body>";
        Document docWithCdata = Parser.parseBodyFragment(bodyHtmlWithCdata, "http://example.com");
        assertNotNull(docWithCdata.getElementsByTag("script"));
        String scriptText = docWithCdata.getElementsByTag("script").text();
        assertEquals("document.write('Hello World')", scriptText);

        String bodyHtmlInvalidBaseUri = "<html><body><script>location.href='http:/'</script></body>";
        Document docInvalidBaseUri = Parser.parseBodyFragment(bodyHtmlInvalidBaseUri, "");
        assertEquals("", docInvalidBaseUri.getBaseUri());

        String bodyHtmlWithBase = "<html><body><base href='/test'><h1>Test</h1></body>";
        Document docWithBase = Parser.parseBodyFragment(bodyHtmlWithBase, "http://example.com");
        assertEquals("/test", docWithBase.getBaseUri());

        String bodyHtmlWithEmptyBody = "<html><body></body>";
        Document docWithEmptyBody = Parser.parseBodyFragment(bodyHtmlWithEmptyBody, "http://example.com");
        assertNotNull(docWithEmptyBody.getElementsByTag("html"));
        assertNotNull(docWithEmptyBody.getElementsByTag("body"));
        assertEquals(0, docWithEmptyBody.childElements().size());

        String bodyHtmlWithSelfClosing = "<html><body><p><img src='test.jpg' alt='Test' /></p></body>";
        Document docWithSelfClosing = Parser.parseBodyFragment(bodyHtmlWithSelfClosing, "http://example.com");
        Element img = docWithSelfClosing.getElementsByTag("img").get(0);
        assertTrue(img.isEmptyElement());

        String bodyHtmlWithImplicitParent = "<html><body><p><img src='test.jpg' alt='Test'></p></body>";
        Document docWithImplicitParent = Parser.parseBodyFragment(bodyHtmlWithImplicitParent, "http://example.com");
        Element imgParent = docWithImplicitParent.getElementsByTag("p").get(0);
        Element imgElement = imgParent.getElementsByTag("img").get(0);
        assertEquals("p", imgParent.tagName());
        assertEquals("img", imgElement.tagName());

        String bodyHtmlWithInvalidTag = "<html><body><invalid></body>";
        try {
            Document docWithInvalidTag = Parser.parseBodyFragment(bodyHtmlWithInvalidTag, "http://example.com");
            fail("Expected IllegalArgumentException for invalid tag");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseStartTag() {
        // Given
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        LinkedList<Element> stack = parser.stack;

        // When parseStartTag() is called
        parser.parseStartTag();

        // Then
        assertEquals(htmlTag, stack.getLast().tag());
    }

    @Test
    public void testParseEndTag() {
        // Given
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Element htmlElement = parser.stack.getLast();
        parser.stack.removeLast();
        parser.parseEndTag();

        // Then
        assertEquals("html", htmlElement.tagName());
        assertEquals(htmlTag, parser.stack.getLast().tag());
    }

    @Test
    public void testParseComment() {
        // Given
        String html = "<html><!--comment--><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");

        // When parseComment() is called
        parser.parseComment();

        // Then
        assertNotNull(parser.stack.getLast());
        assertEquals("comment", parser.stack.getLast().tagName());
    }

    @Test
    public void testParseXmlDecl() {
        // Given
        String html = "<html xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");

        // When parseXmlDecl() is called
        parser.parseXmlDecl();

        // Then
        assertNotNull(parser.stack.getLast());
        assertEquals("xml", parser.stack.getLast().tagName());
    }

    @Test
    public void testParseAttribute() {
        // Given
        String html = "<html><body><p id='test' class='test'><img src='test.jpg' alt='Test' title='Hello World'></p></body></html>";
        Parser parser = new Parser(html, "http://example.com");

        // When parseAttribute() is called
        Attribute attribute = parser.parseAttribute();

        // Then
        assertNotNull(attribute);
        assertEquals("id", attribute.getKey());
        assertEquals("test", attribute.getValue());
    }

    @Test
    public void testParseTextNode() {
        // Given
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Parser parser = new Parser(html, "http://example.com");

        // When parseTextNode() is called
        parser.parseTextNode();

        // Then
        assertNotNull(parser.stack.getLast());
        assertEquals("text", parser.stack.getLast().tagName());
    }

    @Test
    public void testParseCdata() {
        // Given
        String html = "<html><head><title>Test</title></head><body><![CDATA[document.write('Hello World')]]></body></html>";
        Parser parser = new Parser(html, "http://example.com");

        // When parseCdata() is called
        parser.parseCdata();

        // Then
        assertNotNull(parser.stack.getLast());
        assertEquals("script", parser.stack.getLast().tagName());
    }

    @Test
    public void testPopStackToClose() {
        // Given
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1><div><p><img src='test.jpg' alt='Test' /></p></div></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Element div = parser.stack.getLast();
        parser.stack.removeLast();
        Element imgParent = parser.popStackToClose(imgTag);

        // Then
        assertEquals("div", div.tagName());
        assertEquals("p", imgParent.tagName());
    }

    @Test
    public void testPopStackToSuitableContainer() {
        // Given
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1><p><h2>Hello H2</h2></p></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Element h1 = parser.stack.getLast();

        // When popStackToSuitableContainer() is called
        Element h2 = parser.popStackToSuitableContainer(h2Tag);

        // Then
        assertEquals("h1", h1.tagName());
        assertEquals("p", h2.tagName());
    }
}