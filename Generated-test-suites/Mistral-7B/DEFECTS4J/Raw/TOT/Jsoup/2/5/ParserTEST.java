package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head>" +
                "<body><h1>Welcome to Jsoup</h1>" +
                "<p>This is a test.<a href=\"https://www.google.com\">Google</a></p>" +
                "<img src=\"images/logo.png\" alt=\"Logo\"/></body></html>";
        Document doc = Parser.parse(html, "http://test.com/");

        Element htmlNode = doc.elementAt(0);
        Assert.assertEquals(htmlTag, htmlNode.tag());

        Element headNode = htmlNode.child(0);
        Assert.assertEquals(headTag, headNode.tag());

        Element titleNode = headNode.child(0);
        Assert.assertEquals(titleTag, titleNode.tag());
        Assert.assertEquals("Test Page", titleNode.text());

        Element bodyNode = htmlNode.child(1);
        Assert.assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        Assert.assertEquals(Tag.H1, h1Node.tag());
        Assert.assertEquals("Welcome to Jsoup", h1Node.text());

        Element pNode = bodyNode.child(1);
        Assert.assertEquals(Tag.P, pNode.tag());
        Assert.assertEquals("This is a test.", pNode.text());

        Element aNode = pNode.child(0);
        Assert.assertEquals(Tag.A, aNode.tag());
        Assert.assertEquals("Google", aNode.text());
        Assert.assertEquals("https://www.google.com", aNode.absUrl("href"));

        Element imgNode = bodyNode.child(2);
        Assert.assertEquals(Tag.IMG, imgNode.tag());
        Assert.assertEquals(null, imgNode.text());
        Assert.assertEquals("images/logo.png", imgNode.absUrl("src"));
        Assert.assertEquals("Logo", imgNode.attr("alt"));

        // Test empty body fragment
        String bodyHtml = "<p>This is a test.</p>";
        Document bodyDoc = Parser.parseBodyFragment(bodyHtml, "http://test.com/");
        Element bodyNodeEmpty = bodyDoc.rootElement();
        Assert.assertEquals(bodyTag, bodyNodeEmpty.tag());
        Assert.assertNotNull(bodyNodeEmpty.child(0));
        Assert.assertEquals("This is a test.", bodyNodeEmpty.child(0).text());

        // Test malformed HTML
        String malformedHtml = "<html<title>Test Page</title>";
        try {
            Parser.parse(malformedHtml, "http://test.com/");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid baseUri
        String invalidBaseUri = "jsoup://test.com/";
        try {
            Parser.parse(html, invalidBaseUri);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid html
        String invalidHtml = "<html<title>Test Page</title>";
        try {
            Parser.parse(invalidHtml, "http://test.com/");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test base tag
        String baseHtml = "<html><head><base href=\"/test\"/><title>Test Page</title></head>" +
                "<body><h1>Welcome to Jsoup</h1>" +
                "<p>This is a test.<a href=\"/test2/test.html\">Test Link</a></p></body></html>";
        Document baseDoc = Parser.parse(baseHtml, "http://test.com/");
        Assert.assertEquals("/test", baseDoc.baseUri());
        Element h1NodeBase = baseDoc.select("h1").first();
        Assert.assertEquals("Welcome to Jsoup", h1NodeBase.text());
        Element aNodeBase = baseDoc.select("a").first();
        Assert.assertEquals("/test2/test.html", aNodeBase.absUrl("href"));

        // Test self-closing tag
        String selfClosingHtml = "<html><head><title>Test Page</title></head>" +
                "<body><img src=\"images/logo.png\" alt=\"Logo\"/></body></html>";
        Document selfClosingDoc = Parser.parse(selfClosingHtml, "http://test.com/");
        Element imgNodeSelfClosing = selfClosingDoc.select("img").first();
        Assert.assertTrue(imgNodeSelfClosing.hasAttr("src"));
        Assert.assertTrue(imgNodeSelfClosing.hasAttr("alt"));
        Assert.assertFalse(imgNodeSelfClosing.hasText());

        // Test parse comment
        String commentHtml = "<html><!-- This is a comment --><head><title>Test Page</title></head>" +
                "<body><h1>Welcome to Jsoup</h1></body></html>";
        Document commentDoc = Parser.parse(commentHtml, "http://test.com/");
        Element commentNode = commentDoc.select("html>*[position()=1]").first();
        Assert.assertEquals(Comment.class, commentNode.getClass());
        Assert.assertEquals("This is a comment", commentNode.text());

        // Test parse CDATA
        String cdataHtml = "<html><![CDATA[<script>function test() { alert('Hello World'); }]]>]<head><title>Test Page</title></head>" +
                "<body><h1>Welcome to Jsoup</h1>" +
                "<script>test();</script></body></html>";
        Document cdataDoc = Parser.parse(cdataHtml, "http://test.com/");
        Element scriptNode = cdataDoc.select("script").first();
        Assert.assertEquals("function test() { alert('Hello World'); }", scriptNode.text());

        // Test parse empty tag
        String emptyTagHtml = "<html><head><title>Test Page</title></head>" +
                "<body><input type=\"text\"/></body></html>";
        Document emptyTagDoc = Parser.parse(emptyTagHtml, "http://test.com/");
        Element inputNode = emptyTagDoc.select("input").first();
        Assert.assertEquals(Tag.INPUT, inputNode.tag());
        Assert.assertEquals("", inputNode.text());
        Assert.assertEquals(null, inputNode.attr("value"));
        Assert.assertFalse(inputNode.hasAttr("src"));

        // Test parse attribute with no value
        String attributeNoValueHtml = "<html><head><title>Test Page</title></head>" +
                "<body><input type=\"text\" class=\"input\"/></body></html>";
        Document attributeNoValueDoc = Parser.parse(attributeNoValueHtml, "http://test.com/");
        Element inputNodeNoValue = attributeNoValueDoc.select("input").first();
        Assert.assertEquals("input", inputNodeNoValue.attr("class"));
        Assert.assertEquals(null, inputNodeNoValue.attr("value"));

        // Test parse attribute with single quote and double quote
        String attributeQuotesHtml = "<html><head><title>Test Page</title></head>" +
                "<body><a href='http://test.com/test' id='testId'>Link</a></body></html>";
        Document attributeQuotesDoc = Parser.parse(attributeQuotesHtml, "http://test.com/");
        Element aNodeQuotes = attributeQuotesDoc.select("a").first();
        Assert.assertEquals("'http://test.com/test'", aNodeQuotes.attr("href"));
        Assert.assertEquals("testId", aNodeQuotes.attr("id"));

        // Test parse attribute with multiple values
        String attributeMultipleValuesHtml = "<html><head><title>Test Page</title></head>" +
                "<body><meta name=\"description\" content=\"Some description\" property=\"og:description\" />" +
                "<meta name=\"keywords\" content=\"Jsoup, Java, HTML parser\">" +
                "</body></html>";
        Document attributeMultipleValuesDoc = Parser.parse(attributeMultipleValuesHtml, "http://test.com/");
        Element metaNodeDescription = attributeMultipleValuesDoc.select("meta[name=\"description\"]").first();
        Assert.assertEquals("Some description", metaNodeDescription.attr("content"));
        Assert.assertEquals("og:description", metaNodeDescription.attr("property"));

        Element metaNodeKeywords = attributeMultipleValuesDoc.select("meta[name=\"keywords\"]").first();
        Assert.assertEquals("Jsoup, Java, HTML parser", metaNodeKeywords.attr("content"));
    }

    @Test
    public void testParseWithNamespaces() {
        String xhtmlHtml = "<html xmlns:x=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"><head><title>Test Page</title></head>" +
                "<body xml:lang=\"en\"><x:foo xmlns:x=\"http://example.com/\"><xml:foo/></x:foo></body></html>";
        Document doc = Parser.parse(xhtmlHtml, "http://test.com/");

        Element fooXmlns = doc.select("//*[local-name()='foo' and namespace-uri()='http://example.com/']").first();
        Element fooXml = doc.select("//*[local-name()='foo' and namespace-uri()='http://www.w3.org/XML/1998/namespace']").first();

        Assert.assertEquals(3, doc.childNodeSize());
        Assert.assertNotNull(fooXmlns);
        Assert.assertNotNull(fooXml);

        Assert.assertEquals(Tag.X, fooXmlns.tag());
        Assert.assertEquals("http://example.com/", fooXmlns.namespaceUri());
        Assert.assertEquals("http://example.com/foo", fooXmlns.tagName());

        Assert.assertEquals(Tag.XML, fooXml.tag());
        Assert.assertEquals("http://www.w3.org/XML/1998/namespace", fooXml.namespaceUri());
        Assert.assertEquals("xml:foo", fooXml.tagName());

        Assert.assertEquals("en", doc.rootElement().attr("xml:lang"));
        Assert.assertEquals("Test Page", doc.title().text());
    }
}