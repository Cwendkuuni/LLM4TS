package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {
    @Test
    public void testParse() {
        // Basic functional test
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.getRootElement();
        Element title = root.getElementsByTag("title").get(0);
        Element body = root.getElementsByTag("body").get(0);
        Element heading = body.getElementsByTag("h1").get(0);

        Assert.assertEquals("Test Title", title.text());
        Assert.assertEquals("Test Heading", heading.text());

        // Edge cases and exception handling tests
        String emptyHtml = "";
        try {
            doc = Parser.parse(emptyHtml, "http://example.com");
            Assert.fail(); // should throw an exception
        } catch (IllegalArgumentException e) {
            // expected
        }

        String invalidTagHtml = "<html><head><title>Test Title</title></head><body><invalidTag></body></html>";
        try {
            doc = Parser.parse(invalidTagHtml, "http://example.com");
            Assert.fail(); // should throw an exception
        } catch (IllegalArgumentException e) {
            // expected
        }

        String tagWithAttributesHtml = "<html><head><title attr='test'></title></head><body><a href='test.html' rel='external' target='_blank'>Test Link</a></body></html>";
        Document docWithAttributes = Parser.parse(tagWithAttributesHtml, "http://example.com");
        Element link = docWithAttributes.getElementsByTag("a").get(0);
        Assert.assertEquals("test.html", link.attr("href"));
        Assert.assertEquals("external", link.attr("rel"));
        Assert.assertEquals("_blank", link.attr("target"));
    }
}