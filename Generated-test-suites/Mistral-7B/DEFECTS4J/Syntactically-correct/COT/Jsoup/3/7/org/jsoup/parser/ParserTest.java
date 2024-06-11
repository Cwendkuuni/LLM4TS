package org.jsoup.parser; // make sure the test file is in the same package as the Parser class

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ParserTest class for testing the Parser class functionality, edge cases and exceptions handling.
 */
public class ParserTest {

    @Test
    public void testParseHtmlDocument() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Welcome to the JSoup Parser Test</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element root = doc.body();
        Assert.assertEquals("Welcome to the JSoup Parser Test", root.text());
        Assert.assertEquals("Test", doc.title().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element root = doc.body();
        Assert.assertEquals("Hello", root.text());
        Assert.assertNull(doc.head());
    }

    @Test
    public void testParseRelaxedBodyFragment() {
        String bodyHtml = "<body><h1>Hello</h1><script src=\"test.js\"></script></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element root = doc.body();
        Assert.assertEquals("Hello", root.text());
        Assert.assertNotNull(root.child(1)); // script tag should not be ignored
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtml() {
        Parser.parse("", "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidBaseUri() {
        Parser.parse("<html><head></head><body></body></html>", null);
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment --><html><head><title>Test</title></head>" +
                "<body><h1>Welcome to the JSoup Parser Test</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Comment comment = doc.body().getElementsByTag("comment").first();
        Assert.assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><script><![CDATA[function hello() { console.log('Hello'); }]]></script></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element script = doc.body().getElementsByTag("script").first();
        DataNode cdataNode = (DataNode) script.child(0);
        String expectedScript = "function hello() { console.log('Hello'); }";
        Assert.assertEquals(expectedScript, cdataNode.text());
    }

    @Test
    public void testParseUnknownTag() {
        String html = "<html><head></head><body><unknownTag></unknownTag></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element unknownTag = doc.body().getElementsByTag("unknownTag").first();
        Assert.assertNotNull(unknownTag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyTag() {
        String html = "<html><head></head><body><img /></body></html>";
        Parser.parse(html, "http://example.com");
    }

    @Test
    public void testParseSelfClosingTag() {
        String html = "<html><head></head><body><img src=\"test.png\" alt=\"Test\" />" +
                "</body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element img = doc.body().getElementsByTag("img").first();
        Assert.assertTrue(img.tag().isEmpty());
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><head></head><body>" +
                "<p id=\"test\" class=\"test\" title=\"Test Title\" ><a href=\"http://example.com\">Test Link</a></p>" +
                "</body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element p = doc.body().getElementsByTag("p").first();
        Map<String, String> expectedAttributes = new HashMap<>();
        expectedAttributes.put("id", "test");
        expectedAttributes.put("class", "test");
        expectedAttributes.put("title", "Test Title");

        Map<String, String> actualAttributes = p.attributes();
        Assert.assertEquals(expectedAttributes, actualAttributes);
    }

    @Test
    public void testParseAttributeValuesQuotationMarks() {
        String html = "<html><head></head><body><p title=\"Test Title with 'Single Quotation Marks' and \"Double Quotation Marks\"\"></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element p = doc.body().getElementsByTag("p").first();
        Assert.assertEquals("Test Title with 'Single Quotation Marks' and \"Double Quotation Marks\"", p.title().text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/new-folder/\"></head>" +
                "<body><a href=\"test.html\">Link</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        String expectedLink = String.format("%s/new-folder/test.html", "http://example.com");
        Assert.assertEquals(expectedLink, doc.head().getElementsByTag("base").first().absUrl("href"));
        Assert.assertEquals(expectedLink, doc.getElementsByClass("link").first().absUrl("href"));
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<html><head></head><body><input type=\"text\" name=\"username\"></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element input = doc.body().getElementsByTag("input").first();
        Element parent = input.parent();

        Tag expectedParentTag = Tag.valueOf("form");
        Tag actualParentTag = parent.tag();

        Assert.assertEquals(expectedParentTag, actualParentTag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidTagNesting() {
        String html = "<html><head></head><body>" +
                "<p><ul><li>List item 1</li></ul></p></body></html>";
        Parser.parse(html, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidXmlDeclaration() {
        String html = "<html xml:version=\"1.0\" xml:encoding=\"UTF-8\" <!DOCTYPE html>";
        Parser.parse(html, "http://example.com");
    }

    @Test
    public void testParseInvalidXmlDeclarationWithDTD() {
        String html = "<html xml:version=\"1.0\" xml:encoding=\"UTF-8\" <!DOCTYPE html[ <!ENTITY % myEntity SYSTEM \"my.dtd\"> ]>";
        Document doc = Parser.parse(html, "http://example.com");

        Assert.assertNotNull(doc.head()); // no exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidEndTag() {
        String html = "<html><head></head><body><p><div><p></div></p></body></html>";
        Parser.parse(html, "http://example.com");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseEmptyStack() {
        Parser parser = new Parser("", "", false);
        parser.stack = new LinkedList<>();
        parser.parse();
    }
}