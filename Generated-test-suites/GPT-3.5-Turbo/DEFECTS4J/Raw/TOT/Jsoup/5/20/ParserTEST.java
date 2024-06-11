package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private String html = "<html>\n" +
        "<head>\n" +
        "<title>Test Title</title>\n" +
        "</head>\n" +
        "<body>\n" +
        "<h1>This is a heading</h1>\n" +
        "<p>This is a paragraph.</p>\n" +
        "</body>\n" +
        "</html>";

    @Before
    public void setup() {
        parser = new Parser();
    }

    @Test
    public void testParse_shouldReturnDocumentObject() {
        Document document = parser.parse(html, "http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseBodyFragment_shouldReturnDocumentObjectWithHtmlParsedIntoBody() {
        String bodyHtml = "<h1>This is a heading</h1>\n" +
            "<p>This is a paragraph.</p>";
        Document document = parser.parseBodyFragment(bodyHtml, "http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Element bodyElement = document.body();
        Assert.assertNotNull(bodyElement);
        Assert.assertEquals(2, bodyElement.children().size());
        Assert.assertEquals("h1", bodyElement.child(0).tagName());
        Assert.assertEquals("p", bodyElement.child(1).tagName());
        Assert.assertEquals("This is a heading", bodyElement.child(0).text());
        Assert.assertEquals("This is a paragraph.", bodyElement.child(1).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed_shouldReturnDocumentObjectWithHtmlParsedIntoBody() {
        String bodyHtml = "<h1>This is a heading</h1>\n" +
            "<p>This is a paragraph.</p>";
        Document document = parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Element bodyElement = document.body();
        Assert.assertNotNull(bodyElement);
        Assert.assertEquals(2, bodyElement.children().size());
        Assert.assertEquals("h1", bodyElement.child(0).tagName());
        Assert.assertEquals("p", bodyElement.child(1).tagName());
        Assert.assertEquals("This is a heading", bodyElement.child(0).text());
        Assert.assertEquals("This is a paragraph.", bodyElement.child(1).text());
    }

    @Test
    public void testParse_withEmptyHtml_shouldReturnEmptyDocument() {
        Document document = parser.parse("", "http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertTrue(document.children().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testParse_withNullHtml_shouldThrowNullPointerException() {
        parser.parse(null, "http://example.com");
    }

    @Test(expected = NullPointerException.class)
    public void testParse_withNullBaseUri_shouldThrowNullPointerException() {
        parser.parse(html, null);
    }
  
    @Test
    public void testParse_withInvalidHtml_shouldReturnDocumentWithRemainingTextAsTextNode() {
        String invalidHtml = "<html>\n" +
            "<head>\n" +
            "<title>Test Title</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<h1>This is a heading</h1>\n" +
            "<p>This is a paragraph.</p>\n" +
            "</html>";
        Document document = parser.parse(invalidHtml, "http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals(1, document.children().size());
        Assert.assertEquals("html", document.child(0).tagName());
        Assert.assertEquals(1, document.child(0).children().size());
        Assert.assertEquals("body", document.child(0).child(0).tagName());
        Assert.assertEquals(2, document.child(0).child(0).children().size());
        Assert.assertEquals("h1", document.child(0).child(0).child(0).tagName());
        Assert.assertEquals("This is a heading", document.child(0).child(0).child(0).text());

        Element bodyElement = document.child(0).child(0);
        Assert.assertEquals(TextNode.class, bodyElement.childNode(1).getClass());
        Assert.assertEquals("This is a paragraph.", ((TextNode) bodyElement.childNode(1)).text());

        Assert.assertEquals(1, document.textNodes().size());
        Assert.assertEquals("This is a paragraph.", document.text());
    }
}