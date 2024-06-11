package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document doc = new TreeBuilder().parse(html, "http://example.com/");
        assertNotNull(doc);
        assertEquals("Test", doc.title().text());
        assertEquals("Hello", doc.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithErrors() {
        String html = "<html><head><title>Test</title><script>alert('XSS')</script></head><body><h1>Hello</h1></body></html>";
        ParseErrorList errors = new ParseErrorList();
        Document doc = new TreeBuilder().parse(html, "http://example.com/", errors);
        assertNotNull(doc);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.STARTTAG) {
                    Element parent = currentElement();
                    Element element = doc.createElement(token.name());
                    parent.appendChild(element);
                    stack.add(element);
                    return true;
                }
                return false;
            }
        };

        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document doc = builder.parse(html, "http://example.com/");
        Element body = doc.body();
        Element h1 = body.getElementsByTag("h1").get(0);
        assertEquals("Hello", h1.text());
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.ENDTAG) {
                    Element element = stack.remove(stack.size() - 1);
                    return true;
                }
                return false;
            }
        };

        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document doc = builder.parse(html, "http://example.com/");
        assertEquals(0, stack.size());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.STARTTAG) {
                    Element parent = currentElement();
                    Element element = doc.createElement(token.name());
                    Attributes attrs = token.attributes();
                    for (String name : attrs.keySet()) {
                        element.attribute(name, attrs.get(name()));
                    }
                    parent.appendChild(element);
                    stack.add(element);
                    return true;
                }
                return false;
            }
        };

        String html = "<p class='test' id='test'>Test</p>";
        Document doc = builder.parse(html, "http://example.com/");
        Element p = doc.getElementsByClass("test").get(0);
        assertEquals("test", p.className());
        assertEquals("test", p.id());
        assertEquals("Test", p.text());
    }
}