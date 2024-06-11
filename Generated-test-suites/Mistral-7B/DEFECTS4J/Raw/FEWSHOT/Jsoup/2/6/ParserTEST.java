package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.jsoup.nodes.*;

import java.util.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(headTag, doc.head());
        assertEquals(titleTag, doc.head().title());
        assertEquals("Test", doc.head().title().text());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(h1Tag, doc.body().childNodes().get(0).tag());
        assertEquals("Hello World", doc.body().childNodes().get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.body());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(h1Tag, doc.body().childNodes().get(0).tag());
        assertEquals("Hello World", doc.body().childNodes().get(0).text());
    }

    @Test
    public void testParseEmptyDocument() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.body());
        assertEquals(0, doc.body().childNodes().size());
    }

    @Test
    public void testParseComments() {
        String html = "<html><!-- This is a comment --><head><title>Test</title></head><body><!-- Another comment -->" +
                "<h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.rootElement();
        assertEquals(htmlTag, root.tag());
        assertEquals(1, root.childNodes().size());
        Element head = root.childNodes().get(0);
        assertEquals(headTag, head.tag());
        assertEquals(1, head.childNodes().size());
        Element title = head.childNodes().get(0);
        assertEquals(titleTag, title.tag());
        assertEquals("Test", title.text());
        assertEquals(1, root.getElementsByTag("html").size());
        assertEquals(1, root.getElementsByTag("head").size());
        assertEquals(1, root.getElementsByTag("title").size());
        assertEquals(1, root.getElementsByTag("body").size());
        assertEquals(1, root.getElementsByTag("h1").size());
        assertEquals(0, root.getElementsByTag("script").size());

        Comment comment1 = (Comment) root.getElementsByTag("comment").get(0);
        assertEquals("This is a comment", comment1.data());
        Comment comment2 = (Comment) root.getElementsByTag("comment").get(1);
        assertEquals("Another comment", comment2.data());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[This is CDATA]]><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.body());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(h1Tag, doc.body().childNodes().get(0).tag());
        assertEquals("Hello World", doc.body().childNodes().get(0).text());

        Node cdata = doc.body().getElementsByTag("cdata").get(0);
        assertNotNull(cdata);
        assertEquals("This is CDATA", cdata.toString());
    }

    @Test
    public void testParseXmlDeclaration() {
        String html = "<html xmlns:xlink=\"http://example.com/xmlns/1999/xlink\" xml:lang=\"en\">";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.rootElement().attributes().size());
        Attribute xmlnsXlink = doc.rootElement().attributes().get("xmlns:xlink");
        assertEquals("http://example.com/xmlns/1999/xlink", xmlnsXlink.value());
        assertEquals(1, doc.rootElement().attributes().get("xml:lang").value().length());
        assertEquals("en", doc.rootElement().attributes().get("xml:lang").value());
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<html><head><title><!-- empty title --></title></head><body><img src=\"\" alt=\"\"/></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(headTag, doc.head());
        assertEquals(1, doc.head().childNodes().size());
        Element title = doc.head().childNodes().get(0);
        assertEquals(titleTag, title.tag());
        assertEquals(0, title.childNodes().size());

        assertEquals(1, doc.body().childNodes().size());
        Element img = doc.body().childNodes().get(0);
        assertEquals("img", img.tagName());
        assertEquals(2, img.attributes().size());
        assertEquals("", img.attr("src"));
        assertEquals("", img.attr("alt"));
    }

    @Test
    public void testParseSelfClosedElement() {
        String html = "<html><head><title>Test</title></head><body><img src=\"test.png\" alt=\"Test\" />" +
                "</body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(headTag, doc.head());
        assertEquals(1, doc.head().childNodes().size());
        Element title = doc.head().childNodes().get(0);
        assertEquals(titleTag, title.tag());
        assertEquals("Test", title.text());

        assertEquals(1, doc.body().childNodes().size());
        Element img = doc.body().childNodes().get(0);
        assertEquals("img", img.tagName());
        assertEquals(3, img.attributes().size());
        assertEquals("test.png", img.attr("src"));
        assertEquals("Test", img.attr("alt"));

        assertEquals(0, img.childNodes().size());
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<html><body><input type=\"text\" name=\"username\"/></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.body());
        assertEquals(1, doc.body().childNodes().size());
        Element input = doc.body().childNodes().get(0);
        assertEquals("input", input.tagName());
        assertEquals(2, input.attributes().size());
        assertEquals("text", input.attr("type"));
        assertEquals("username", input.attr("name"));

        assertEquals(0, input.childNodes().size());

        Element form = new Element(FormElement.class, baseUri);
        Element inputInForm = new Element(input.tag(), baseUri);
        form.appendChild(inputInForm);
        Element root = addChildToParent(form, false);
        assertEquals(inputInForm, root.body().getElementsByTag(input.tag()).get(0));
    }

    @Test
    public void testParseInvalidTagOrder() {
        String html = "<html><body><title>Hello World</title><h1>Test</h1></body></html>";
        try {
            Parser.parse(html, "http://example.com");
            fail("Expected ParseException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseMissingEndTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</html>";
        try {
            Parser.parse(html, "http://example.com");
            fail("Expected ParseException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseInvalidEndTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</body></html>";
        try {
            Parser.parse(html, "http://example.com");
            fail("Expected ParseException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private Element addChildToParent(Element child, boolean isEmptyElement) {
        return Parser.parseBodyFragment("" +
                "<html>" +
                "<head><title>Test</title></head>" +
                "<body>" +
                "<" + child.tag().name() + (isEmptyElement ? " />" : ">") +
                child.toString() +
                "</" + child.tag().name() + ">" +
                "</body>" +
                "</html>", "http://example.com").body();
    }
}