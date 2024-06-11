package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;

public class TreeBuilderTest {

    @Test
    public void testParseEmptyString() {
        TreeBuilder tb = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        Document doc = tb.parse("", "");
        assertNotNull(doc);
        assertEquals(0, doc.body().childNodeSize());
    }

    @Test
    public void testParseSimpleHTML() {
        TreeBuilder tb = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    Element element = new Element(token.name());
                    for (String name : token.attributes().names()) {
                        element.attribute(name, token.attributes().get(name()));
                    }
                    tb.stack.add(element);
                    return true;
                } else if (token.type == Token.TokenType.END_TAG) {
                    Element parent = tb.currentElement();
                    if (parent != null) {
                        parent.decrementChildren();
                        tb.stack.remove(tb.stack.size() - 1);
                    }
                    return true;
                }
                return false;
            }
        };
        Document doc = tb.parse("<html><head></head><body><p>Hello World!</p></body></html>", "");
        assertNotNull(doc);
        Element html = doc.body();
        assertEquals("html", html.tagName());
        Element head = html.child(0);
        assertEquals("head", head.tagName());
        Element body = html.child(1);
        assertEquals("body", body.tagName());
        Element p = body.child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello World!", p.text());
    }

    @Test
    public void testParseNestedHTML() {
        TreeBuilder tb = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    Element element = new Element(token.name());
                    for (String name : token.attributes().names()) {
                        element.attribute(name, token.attributes().get(name()));
                    }
                    tb.stack.add(element);
                    return true;
                } else if (token.type == Token.TokenType.END_TAG) {
                    Element parent = tb.currentElement();
                    if (parent != null) {
                        parent.decrementChildren();
                        tb.stack.remove(tb.stack.size() - 1);
                    }
                    return true;
                }
                return false;
            }
        };
        Document doc = tb.parse("<html><head><title>My Page</title></head><body><h1>Welcome</h1><p>This is<p><p>a test.<p></p></body></html>", "");
        assertNotNull(doc);
        Element html = doc.body();
        assertEquals("html", html.tagName());
        Element head = html.child(0);
        assertEquals("head", head.tagName());
        Element title = head.child(0);
        assertEquals("title", title.tagName());
        assertEquals("My Page", title.text());
        Element body = html.child(1);
        assertEquals("body", body.tagName());
        Element h1 = body.child(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Welcome", h1.text());
        Element p1 = body.child(1);
        assertEquals("p", p1.tagName());
        assertNull(p1.text());
        Element p2 = body.child(2);
        assertEquals("p", p2.tagName());
        assertNull(p2.text());
        Element p3 = body.child(3);
        assertEquals("p", p3.tagName());
        assertEquals("a test.", p3.text());
    }

    @Test
    public void testParseWithErrors() {
        TreeBuilder tb = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    Element element = new Element(token.name());
                    for (String name : token.attributes().names()) {
                        element.attribute(name, token.attributes().get(name()));
                    }
                    tb.stack.add(element);
                    return true;
                } else if (token.type == Token.TokenType.END_TAG) {
                    Element parent = tb.currentElement();
                    if (parent != null) {
                        parent.decrementChildren();
                        tb.stack.remove(tb.stack.size() - 1);
                    }
                    return true;
                }
                tb.errors.error("Error: Unrecognised token: " + token.type);
                return false;
            }
        };
        ParseErrorList errors = new ParseErrorList();
        Document doc = tb.parse("<html><head><title>My Page</title><body><h1>Welcome<h1><p>This is<body></html>", "");
        assertNull(doc);
        assertEquals(3, tb.errors.size());
        assertEquals("Error: Unrecognised token: BODY", tb.errors.get(0).getMessage());
        assertEquals("Error: Unrecognised tag name: h1", tb.errors.get(1).getMessage());
        assertEquals("Error: Unrecognised tag name: BODY", tb.errors.get(2).getMessage());
    }

    @Test
    public void testParseWithCustomErrorMessage() {
        TreeBuilder tb = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    Element element = new Element(token.name());
                    for (String name : token.attributes().names()) {
                        element.attribute(name, token.attributes().get(name()));
                    }
                    tb.stack.add(element);
                    return true;
                } else if (token.type == Token.TokenType.END_TAG) {
                    Element parent = tb.currentElement();
                    if (parent != null) {
                        parent.decrementChildren();
                        tb.stack.remove(tb.stack.size() - 1);
                    }
                    return true;
                } else {
                    tb.errors.error("Error: Unrecognised token: " + token.type + " at line " + token.line() + ", column " + token.columns());
                    return false;
                }
            }
        };
        ParseErrorList errors = new ParseErrorList();
        Document doc = tb.parse("<html><head><title>My Page</title><body><h1>Welcome<h1><p>This is<body></html>", "");
        assertNull(doc);
        assertEquals(3, tb.errors.size());
        Element error = tb.errors.get(0);
        assertEquals("Error: Unrecognised token: BODY at line 4, column 1", error.getMessage());
        error = tb.errors.get(1);
        assertEquals("Error: Unrecognised tag name: h1 at line 2, column 7", error.getMessage());
        error = tb.errors.get(2);
        assertEquals("Error: Unrecognised token: BODY at line 5, column 1", error.getMessage());
    }

    @Test
    public void testProcessStartTag() {
        Element element = new Element("div");
        TreeBuilder tb = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    return processStartTag(token.name());
                }
                return true;
            }
        };
        tb.processStartTag("div");
        assertSame(element, tb.currentElement());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        Element element = new Element("a");
        Attributes attrs = new Attributes();
        attrs.put("href", "http://example.com");
        TreeBuilder tb = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    return processStartTag(token.name(), attrs);
                }
                return true;
            }
        };
        tb.processStartTag("a", attrs);
        assertSame(element, tb.currentElement());
        assertEquals("http://example.com", element.attribute("href"));
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder tb = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.END_TAG) {
                    return processEndTag(token.name());
                }
                return true;
            }
        };
        Element element = new Element("div");
        tb.stack.add(element);
        tb.processEndTag("div");
        assertNull(tb.currentElement());
    }
}