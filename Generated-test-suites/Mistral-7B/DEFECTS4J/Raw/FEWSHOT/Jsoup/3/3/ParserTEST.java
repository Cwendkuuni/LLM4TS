package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.*;

/**
 * Test class for Parser class in jsoup library.
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html>\n" +
                "<head><title>Test</title></head>\n" +
                "<body>\n" +
                "<h1>Heading</h1>\n" +
                "<p>Paragraph</p>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element root = doc.getRootElement();
        assertEquals(htmlTag, root.tag());
        assertNotNull(root.head());
        assertNotNull(root.body());

        Element title = root.head().title();
        assertEquals("Test", title.text());

        Element h1 = root.body().getElementsByTag("h1").get(0);
        assertEquals("Heading", h1.text());

        Element p = root.body().getElementsByTag("p").get(0);
        assertEquals("Paragraph", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Heading</h1>\n" +
                "<p>Paragraph</p>";

        String baseUri = "https://example.com";
        Parser parser = new Parser(bodyHtml, baseUri, true);
        Document doc = parser.parse();

        Element root = doc;
        assertEquals(htmlTag, root.tag());
        assertEquals(bodyTag, root.tagName());

        Element h1 = root.getElementsByTag("h1").get(0);
        assertEquals("Heading", h1.text());

        Element p = root.getElementsByTag("p").get(0);
        assertEquals("Paragraph", p.text());

        assertEquals(1, doc.numChildren());
        assertEquals(1, root.numChildren());
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<h1>Heading</h1>\n" +
                "<p>Paragraph</p>";

        String baseUri = "https://example.com";
        Parser parser = new Parser(bodyHtml, baseUri, true);
        Document doc = parser.parse();

        Element root = doc;
        assertEquals(htmlTag, root.tag());
        assertEquals(bodyTag, root.tagName());

        Element h1 = root.getElementsByTag("h1").get(0);
        assertEquals("Heading", h1.text());

        Element p = root.getElementsByTag("p").get(0);
        assertEquals("Paragraph", p.text());

        assertEquals(1, doc.numChildren());
        assertEquals(1, root.numChildren());

        Element body = root;
        assertEquals(bodyTag, body.tagName());
        assertNull(body.head());

        Element h1WithoutImplicitTag = body.getElementsByTag("h1").get(0);
        assertEquals("Heading", h1WithoutImplicitTag.text());
        assertEquals(h1WithoutImplicitTag.tagName(), "h1");
    }

    @Test
    public void testParseComment() {
        String html = "<html>\n" +
                "<!-- This is a comment -->\n" +
                "</html>";

        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element root = doc.getRootElement();
        assertEquals(htmlTag, root.tag());

        Element comment = root.getElementsByTag("comment").get(0);
        assertNotNull(comment);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "</html>";

        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element root = doc.getRootElement();
        assertEquals(htmlTag, root.tag());

        Element xmlDeclaration = root.getElementsByTag("xml-declaration").get(0);
        assertEquals("xml version='1.0' encoding='UTF-8'", xmlDeclaration.xml());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html>\n" +
                "<body>\n" +
                "<p>Paragraph</p>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element root = doc.getRootElement();
        assertEquals(htmlTag, root.tag());

        Element body = root.getElementsByTag("body").get(0);
        assertEquals("body", body.tagName());
        assertEquals(1, body.numChildren());

        Element p = body.getElementsByTag("p").get(0);
        assertEquals("Paragraph", p.text());

        parser.parseEndTag();

        assertEquals(0, body.numChildren());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html>\n" +
                "<body>\n" +
                "<h1>Heading</h1>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element root = doc.getRootElement();
        assertEquals(htmlTag, root.tag());

        Element body = root.getElementsByTag("body").get(0);
        assertEquals("body", body.tagName());
        assertEquals(1, body.numChildren());

        Element h1 = body.getElementsByTag("h1").get(0);
        assertEquals("Heading", h1.text());
        assertEquals("h1", h1.tagName());

        parser.parseStartTag("img");

        assertEquals(1, body.numChildren());
        assertNull(body.getElementsByTag("img").get(0));
    }

    @Test
    public void testParseAttribute() {
        String html = "<input type='text' name='username' value='John Doe'>";

        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element input = doc.getElementsByTag("input").get(0);
        assertEquals("input", input.tagName());
        assertEquals("text", input.tag().getAttributeValue("type"));
        assertEquals("username", input.tag().getAttributeValue("name"));
        assertEquals("John Doe", input.tag().getAttributeValue("value"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element root = doc.getRootElement();
        assertEquals(htmlTag, root.tag());

        Element body = root.getElementsByTag("body").get(0);
        assertEquals("body", body.tagName());
        assertEquals(1, body.numChildren());

        Element p = body.getElementsByTag("p").get(0);
        assertEquals("p", p.tagName());
        assertEquals(1, p.numChildren());

        TextNode textNode = (TextNode) p.getChildren().get(0);
        assertEquals("Test", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html>\n" +
                "<body>\n" +
                "<script><![CDATA[function test() { alert('Hello, world!'); }]]></script>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element root = doc.getRootElement();
        assertEquals(htmlTag, root.tag());

        Element body = root.getElementsByTag("body").get(0);
        assertEquals("body", body.tagName());
        assertEquals(1, body.numChildren());

        Element script = body.getElementsByTag("script").get(0);
        assertEquals("script", script.tagName());
        assertEquals(1, script.numChildren());

        TextNode textNode = (TextNode) script.getChildren().get(0);
        assertEquals("function test() { alert('Hello, world!'); }", textNode.text());
    }
}