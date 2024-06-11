package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link Parser} class.
 */
public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1>" +
                "<p>Test Paragraph 1</p>" +
                "<p>Test Paragraph 2</p>" +
                "<img src=\"test.jpg\" alt=\"Test Image\"/>" +
                "<p>Test Paragraph 3</p>" +
                "</body></html>";
        parser = new Parser(html, "http://localhost:8080/", false);
    }

    @Test
    public void testParser() {
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("Test Title", doc.title());
        assertEquals(5, doc.numberOfElements());

        // Basic checks for different tags
        Element htmlNode = doc.body().child(0);
        Assert.assertEquals("html", htmlNode.tagName());
        Element headNode = doc.head();
        Assert.assertEquals("head", headNode.tagName());
        Element titleNode = headNode.child(0);
        Assert.assertEquals("title", titleNode.tagName());
        Element bodyNode = doc.body();
        Assert.assertEquals("body", bodyNode.tagName());

        Element h1Node = bodyNode.child(0);
        Assert.assertEquals("h1", h1Node.tagName());

        Element p1Node = bodyNode.child(1);
        Assert.assertEquals("p", p1Node.tagName());

        Element imgNode = bodyNode.child(3);
        Assert.assertEquals("img", imgNode.tagName());
        Assert.assertEquals("test.jpg", imgNode.attr("src"));
        Assert.assertEquals("Test Image", imgNode.attr("alt"));

        Element p3Node = bodyNode.child(4);
        Assert.assertEquals("p", p3Node.tagName());

        // Test empty element
        Element emptyElement = new Element(Tag.valueOf("br"), "http://test.com", null);
        Document emptyElementDoc = new Document("http://test.com");
        emptyElementDoc.appendChild(emptyElement);
        Document parsedEmptyElementDoc = parser.parseBodyFragment(emptyElementDoc.html(), "http://test.com");
        assertTrue(parsedEmptyElementDoc.body().child(0).isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        Parser parser = new Parser(null, "http://localhost:8080/", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        Parser parser = new Parser("<html></html>", null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyBodyFragment() {
        Parser parser = new Parser("", "http://localhost:8080/", true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHtmlWithInvalidTag() {
        String html = "<html><body><invalidTag></invalidTag></body></html>";
        Parser parser = new Parser(html, "http://localhost:8080/", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStartTag() {
        String html = "<html><body><invalidStartTag></body></html>";
        Parser parser = new Parser(html, "http://localhost:8080/", false);
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- test comment -->" +
                "<body></body></html>";
        Document doc = Parser.parse(html, "http://localhost:8080/");
        Element commentNode = doc.body().child(0).child(0);
        Assert.assertEquals("test comment", commentNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[test cdata]]>" +
                "<body></body></html>";
        Document doc = Parser.parse(html, "http://localhost:8080/");
        Element cdataNode = doc.body().child(0);
        String cdataContent = cdataNode.child(0).text();
        Assert.assertEquals("test cdata", cdataContent);
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:test=\"http://test.com\" xml:lang=\"en\">" +
                "<head></head>" +
                "<body></body></html>";
        Document doc = Parser.parse(html, "http://localhost:8080/");
        XmlDeclaration xmlDeclaration = doc.head().child(0);
        Assert.assertEquals("test", xmlDeclaration.namespace());
        Assert.assertEquals("en", xmlDeclaration.language());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Test Paragraph</p></p></body></html>";
        Document doc = Parser.parse(html, "http://localhost:8080/");
        Element pNode = doc.body().child(0);
        Assert.assertEquals(1, pNode.childSize());
        Element textNode = pNode.child(0);
        Assert.assertEquals("Test Paragraph", textNode.text());

        String htmlWithExtraEndTag = "<html><body><p>Test Paragraph</p></p></p></body></html>";
        Document docWithExtraEndTag = Parser.parse(htmlWithExtraEndTag, "http://localhost:8080/");
        Element pNodeWithExtraEndTag = docWithExtraEndTag.body().child(0);
        Assert.assertEquals(1, pNodeWithExtraEndTag.childSize());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p><a href=\"test.html\">Test Link</a></p></body></html>";
        Document doc = Parser.parse(html, "http://localhost:8080/");
        Element pNode = doc.body().child(0);
        Element aNode = pNode.child(0).child(0);
        Assert.assertEquals("a", aNode.tagName());
        Assert.assertEquals("test.html", aNode.attr("href"));
    }

    @Test
    public void testParseAttributes() {
        String html = "<html><body><p class=\"test\" id=\"test-id\">" +
                "<img src=\"test.jpg\" alt=\"Test Image\" title=\"Test Title\"/></p></body></html>";
        Document doc = Parser.parse(html, "http://localhost:8080/");
        Element pNode = doc.body().child(0);
        Element imgNode = pNode.child(0).child(0);

        List<String> classNames = Arrays.asList("test");
        Map<String, String> attributes = imgNode.attributes();
        Assert.assertEquals(4, attributes.size());
        Assert.assertEquals("test.jpg", attributes.get("src"));
        Assert.assertEquals("Test Image", attributes.get("alt"));
        Assert.assertEquals("Test Title", attributes.get("title"));
        Assert.assertEquals(classNames, pNode.classes());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Test Paragraph</p></body></html>";
        Document doc = Parser.parse(html, "http://localhost:8080/");
        Element pNode = doc.body().child(0);
        TextNode textNode = (TextNode) pNode.child(0);
        Assert.assertEquals("Test Paragraph", textNode.text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html>" +
                "<head>" +
                "<base href=\"/new_base/\"/>" +
                "</head>" +
                "<body>" +
                "<a href=\"relative_link.html\">Link</a>" +
                "</body>" +
                "</html>";
        Document doc = Parser.parse(html, "http://localhost:8080/");
        String baseUri = doc.baseUri();
        Assert.assertEquals("/new_base/relative_link.html", doc.head().child(0).attr("href"));
        Assert.assertEquals("/new_base/", baseUri);
    }

    @Test
    public void testParseImplicitParents() {
        String html = "<html>" +
                "<body>" +
                "<ul>" +
                "<li>Item 1</li>" +
                "<li>Item 2" +
                "<ul>" +
                "<li>Sub-item 1</li>" +
                "</ul>" +
                "</li>" +
                "</ul>" +
                "</body>" +
                "</html>";
        Document doc = Parser.parse(html, "http://localhost:8080/");

        Element item1 = doc.body().child(0).child(0);
        Element item2 = doc.body().child(0).child(1);
        Element subItem1 = item2.child(0);

        assertEquals("ul", item1.tagName());
        assertEquals("li", item2.tagName());
        assertEquals("ul", subItem1.tagName());
    }
}