package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com");

        assertEquals("https://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.documentElementTag());
        assertEquals(htmlTag, doc.head().parentTag());
        assertEquals(headTag, doc.head().tag());
        assertEquals(titleTag, doc.head().child(0).tag());
        assertEquals("Test", doc.head().child(0).text());
        assertEquals(bodyTag, doc.body().tag());
        assertEquals(1, doc.body().childNodeSize());
        assertEquals(h1Tag, doc.body().child(0).tag());
        assertEquals("Hello World", doc.body().child(0).text());

        List<String> tags = Arrays.asList("html", "head", "title", "body", "h1");
        for (Element el = doc.head(); el != null; el = el.nextSibling()) {
            assertTrue(tags.contains(el.tag().name()));
        }
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com");

        assertEquals("https://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.documentElementTag());
        assertEquals(bodyTag, doc.body());
        assertEquals(1, doc.body().childNodeSize());
        assertEquals(h1Tag, doc.body().child(0).tag());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testParseWithBaseTag() {
        String html = "<html><head><base href=\"/test/\"><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com");

        assertEquals("https://example.com/test/", doc.baseUri());
    }

    @Test
    public void testParseWithImplicitTags() {
        String html = "<img src=\"image.jpg\" alt=\"Image\">";
        Document doc = Parser.parse(html, "https://example.com");

        Element imgElement = doc.getElementsByTag("img").first();
        assertEquals("img", imgElement.tag().name());

        Element imgParent = imgElement.parent();
        assertEquals("div", imgParent.tag().name()); // implicit parent tag

        Element imgGrandparent = imgParent.parent();
        assertEquals("body", imgGrandparent.tag().name());
    }

    @Test
    public void testParseWithEmptyBodyFragment() {
        String bodyHtml = "";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com");

        assertEquals("https://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.documentElementTag());
        assertEquals(bodyTag, doc.body());
        assertEquals(0, doc.body().childNodeSize());
    }

    @Test
    public void testParseWithEmptyHTML() {
        String html = "";
        Document doc = Parser.parse(html, "https://example.com");

        assertEquals("", doc.body().html());
        assertEquals("", doc.head().html());
    }

    @Test
    public void testParseWithXMLDeclaration() {
        String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com");

        XmlDeclaration xmlDeclaration = doc.head().child(0).child(0).castTo(XmlDeclaration.class);
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", xmlDeclaration.data());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", xmlDeclaration.publicId());
    }

    @Test
    public void testParseWithComment() {
        String html = "<!DOCTYPE html>\n" +
                "<html><!-- This is a comment --><head><title>Test</title></head><body><h1>Hello World</h1><!-- Another comment --></body></html>";
        Document doc = Parser.parse(html, "https://example.com");

        Comment comment1 = doc.head().child(0).child(0).castTo(Comment.class);
        assertEquals("This is a comment", comment1.data());
        assertEquals("https://example.com", comment1.absUrl(""));

        Comment comment2 = doc.body().child(1).castTo(Comment.class);
        assertEquals("Another comment", comment2.data());
        assertEquals("https://example.com", comment2.absUrl(""));
    }

    @Test
    public void testParseWithScriptAndDataTags() {
        String html = "<html><head><title>Test</title><script type=\"text/javascript\">console.log('Hello JavaScript');</script></head><body><textarea>Hello TextArea</textarea></body></html>";
        Document doc = Parser.parse(html, "https://example.com");

        Element scriptElement = doc.head().child(1);
        assertEquals("script", scriptElement.tag().name());
        assertEquals("text/javascript", scriptElement.attr("type"));

        Element textareaElement = doc.body().child(0);
        assertEquals("textarea", textareaElement.tag().name());
        assertEquals("Hello TextArea", textareaElement.text());

        String scriptContent = scriptElement.text();
        assertEquals("console.log('Hello JavaScript');", scriptContent);
    }

    @Test
    public void testParseWithRelativeURLs() {
        String html = "<html><head><title>Test</title><link rel=\"stylesheet\" href=\"styles.css\"></head><body><img src=\"image.jpg\"></body></html>";
        Document doc = Parser.parse(html, "https://example.com/index.html");

        Element linkElement = doc.head().child(0);
        assertEquals("link", linkElement.tag().name());
        assertEquals("stylesheet", linkElement.attr("rel"));
        assertEquals("styles.css", linkElement.absUrl("href"));

        Element imgElement = doc.body().child(0);
        assertEquals("img", imgElement.tag().name());
        assertEquals("image.jpg", imgElement.absUrl("src"));
    }

    @Test
    public void testParseWithInvalidHTML() {
        String html = "<html><body></html><div><div";
        try {
            Parser.parse(html, "https://example.com");
            fail("Expected ParserException");
        } catch (ParserException e) {
            String errorMessage = e.getMessage();
            assertTrue(errorMessage.contains("Invalid HTML"));
            String errorLine = StringUtils.substringBefore(errorMessage, "\n");
            assertTrue(errorLine.contains("<div><div"));
        }
    }

    @Test
    public void testParseWithEmptyAttributeValue() {
        String html = "<img src=\"image.jpg\" alt=\"\">";
        Document doc = Parser.parse(html, "https://example.com");

        Element imgElement = doc.getElementsByTag("img").first();
        assertEquals("img", imgElement.tag().name());
        assertEquals("", imgElement.attr("alt"));
    }

    @Test
    public void testParseWithInvalidAttributeValue() {
        String html = "<img src=\"image.jpg\" alt=\"<script>alert('XSS');\"></script>\">";
        try {
            Parser.parse(html, "https://example.com");
            fail("Expected ParserException");
        } catch (ParserException e) {
            String errorMessage = e.getMessage();
            assertTrue(errorMessage.contains("Invalid attribute value"));
        }
    }

    @Test
    public void testParseWithAttributeKeyWithoutValue() {
        String html = "<img src=\"image.jpg\" alt>";
        try {
            Parser.parse(html, "https://example.com");
            fail("Expected ParserException");
        } catch (ParserException e) {
            String errorMessage = e.getMessage();
            assertTrue(errorMessage.contains("Missing value for attribute 'alt'"));
        }
    }
}