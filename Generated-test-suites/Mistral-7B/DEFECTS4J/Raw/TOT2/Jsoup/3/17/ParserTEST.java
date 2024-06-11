package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {
    @Test
    public void testParse() {
        // Given
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://test.com";

        // When
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        // Then
        Element htmlNode = doc.head();
        Assert.assertEquals("html", htmlNode.tagName());
        Assert.assertNotNull(htmlNode.children());

        Element titleNode = htmlNode.getFirstChild();
        Assert.assertEquals("head", titleNode.parentTag().tagName());
        Assert.assertEquals("title", titleNode.tagName());
        Assert.assertEquals("Test", titleNode.text());

        Element bodyNode = doc.body();
        Assert.assertEquals("body", bodyNode.tagName());
        Assert.assertNotNull(bodyNode.children());

        Element h1Node = bodyNode.getFirstChild();
        Assert.assertEquals("h1", h1Node.tagName());
        Assert.assertEquals("Hello World", h1Node.text());

        // Edge Cases
        html = "<html><head></head><body></body></html>";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
        Assert.assertTrue(doc.head().children().isEmpty());
        Assert.assertTrue(doc.body().children().isEmpty());

        html = "<html><head><title>Test</title></head><body><h1>Hello <b>World</b></h1></body></html>";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();
        h1Node = bodyNode.getFirstChild();
        Assert.assertEquals("h1", h1Node.tagName());
        Assert.assertEquals("Hello <b>World</b>", h1Node.text());

        // Error Scenarios
        try {
            parser = new Parser(null, baseUri, false);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            parser = new Parser("", baseUri, false);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            parser = new Parser("<html", baseUri, false);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}