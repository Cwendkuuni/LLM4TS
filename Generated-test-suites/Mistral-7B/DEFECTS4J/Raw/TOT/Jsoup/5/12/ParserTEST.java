package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ParserTest {

    @Test
    public void testParseSimpleHtml() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element htmlNode = doc.rootElement();
        Assert.assertEquals(htmlTag, htmlNode.tag());

        Element headNode = htmlNode.child(0);
        Assert.assertEquals(headTag, headNode.tag());

        Element titleNode = headNode.child(0);
        Assert.assertEquals(titleTag, titleNode.tag());
        Assert.assertEquals("Test", titleNode.text());

        Element bodyNode = htmlNode.child(1);
        Assert.assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        Assert.assertEquals(Tag.H1, h1Node.tag());
        Assert.assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() throws IOException {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");

        Element htmlNode = doc.rootElement();
        Assert.assertEquals(htmlTag, htmlNode.tag());

        Element bodyNode = htmlNode.child(0);
        Assert.assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        Assert.assertEquals(Tag.H1, h1Node.tag());
        Assert.assertEquals("Hello World", h1Node.text());

        // test empty head
        Assert.assertNull(doc.head());
    }

    @Test
    public void testParseBodyFragmentRelaxed() throws IOException {
        String bodyHtml = "<body><div id='myDiv'></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");

        Element htmlNode = doc.rootElement();
        Assert.assertEquals(htmlTag, htmlNode.tag());

        Element bodyNode = htmlNode.child(0);
        Assert.assertEquals(bodyTag, bodyNode.tag());

        Element divNode = bodyNode.child(0);
        Assert.assertEquals(Tag.DIV, divNode.tag());
        Assert.assertEquals("myDiv", divNode.attr("id"));

        // test implicit tag creation
        Element pNode = divNode.appendChild(new TextNode("Test"));
        Assert.assertEquals(Tag.P, pNode.tag());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() throws IOException {
        String html = "<html><body></body>"; // missing head tag
        Parser.parse(html, "http://test.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml2() throws IOException {
        String html = "<html><body><div><div>"; // nested divs without proper parent tag
        Parser.parse(html, "http://test.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml3() throws IOException {
        String html = "<html><body><script>"; // script tag not allowed in body
        Parser.parse(html, "http://test.com");
    }
}