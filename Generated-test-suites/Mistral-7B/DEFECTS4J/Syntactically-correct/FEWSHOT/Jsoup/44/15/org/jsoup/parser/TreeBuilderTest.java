package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.ArrayList;

public class TreeBuilderTest {

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        TreeBuilder parser = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        Document doc = parser.parse(input, "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        Element html = doc.body();
        assertEquals("html", html.tagName());
        Element title = html.head().title();
        assertEquals("Test", title.text());
        Element h1 = html.selectFirst("h1");
        assertEquals("Hello", h1.text());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><body><script>alert('Hello')</script></body></html>";
        TreeBuilder parser = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.SCRIPT_TAG) {
                    throw new AssertionError("Script tags are not allowed in input");
                }
                return true;
            }
        };
        ParseErrorList errors = new ParseErrorList();
        Document doc = parser.parse(input, "http://example.com", errors);
        assertEquals(1, errors.size());
        assertEquals("script tag <script>\n---\nline 2: column 1: script tags are not allowed in input", errors.getError(0).getMessage());
        assertNull(doc);
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder parser = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return processStartTag(token.getName());
            }

            protected boolean processStartTag(String name) {
                return name.equals("html");
            }
        };
        Document doc = parser.parse("<html></html>", "http://example.com");
        assertEquals("html", doc.tagName());
        assertNull(doc.body());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder parser = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    return processStartTag(token.getName(), token.getAttributes());
                }
                return true;
            }

            protected boolean processStartTag(String name, Attributes attrs) {
                if (name.equals("a")) {
                    attrs.put("href", "http://example.com");
                    return true;
                }
                return false;
            }
        };
        Document doc = parser.parse("<a href='http://example.com'>Link</a>", "http://example.com");
        Element link = doc.selectFirst("a");
        assertEquals("Link", link.text());
        assertEquals("http://example.com", link.absUrl("href"));
        assertEquals("http://example.com", link.attribute("href"));
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder parser = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.END_TAG) {
                    return processEndTag(token.getName());
                }
                return true;
            }

            protected boolean processEndTag(String name) {
                return name.equals("body");
            }
        };
        Document doc = parser.parse("<html><body></body></html>", "http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertEquals(0, body.childNodeSize());
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder parser = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    stack.add(new Element("div"));
                    return true;
                }
                return false;
            }

            @Override
            protected Element currentElement() {
                return stack.size() > 0 ? stack.get(stack.size() - 1) : null;
            }
        };
        parser.stack = new ArrayList<Element>(32);
        Document doc = parser.parse("<div><div></div></div>", "http://example.com");
        Element div1 = doc.selectFirst("div");
        Element div2 = div1.selectFirst("div");
        assertEquals("div", div1.tagName());
        assertEquals("div", div2.tagName());
        assertSame(div1, div2.parent());
        Element current = parser.currentElement();
        assertSame(div1, current);
    }
}