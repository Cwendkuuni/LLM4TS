package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.helper.Constants.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testParseEmptyInput() {
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        Document doc = builder.parse("", "http://example.com", errors);
        assertEquals("", doc.body().text());
        assertEquals(0, doc.body().childElementCount());
        assertNull(doc.head());
    }

    @Test
    public void testParseSimpleInput() {
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(String name) {
                if (TAG_HTML.equals(name)) {
                    return process(start.reset().name(name));
                }
                if (TAG_HEAD.equals(name)) {
                    Element head = doc.createElement(name);
                    doc.appendChild(head);
                    stack.add(head);
                    return true;
                }
                if (TAG_BODY.equals(name)) {
                    Element body = doc.createElement(name);
                    doc.appendChild(body);
                    stack.add(body);
                    return true;
                }
                Element parent = currentElement();
                Element elem = doc.createElement(name);
                parent.appendChild(elem);
                stack.add(elem);
                return true;
            }

            @Override
            protected boolean processEndTag(String name) {
                Element elem = stack.remove(stack.size() - 1);
                elem.parent().removeChild(elem);
                return true;
            }
        };
        Document doc = builder.parse("<html><head></head><body><div>Hello World</div></body></html>", "http://example.com", errors);
        assertEquals("Hello World", doc.body().child(0).text());
        assertEquals(1, doc.body().childElementCount());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseErrors() {
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.EOF) {
                    return false;
                }
                errors.add(token.getLine() + ":" + token.getColumn(), "Unexpected token: " + token.name());
                return true;
            }
        };
        Document doc = builder.parse("<html><head></head><body><div>Hello World</div></body></html>", "http://example.com", errors);
        assertEquals(0, doc.body().childElementCount());
        assertEquals(1, errors.size());
        assertEquals("1:1: Unexpected token: <", errors.get(0).getMessage());
    }

    @Test
    public void testParseNestedElements() {
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(String name) {
                if (TAG_HTML.equals(name)) {
                    return process(start.reset().name(name));
                }
                if (TAG_HEAD.equals(name)) {
                    Element head = doc.createElement(name);
                    doc.appendChild(head);
                    stack.add(head);
                    return true;
                }
                if (TAG_BODY.equals(name)) {
                    Element body = doc.createElement(name);
                    doc.appendChild(body);
                    stack.add(body);
                    return true;
                }
                Element parent = currentElement();
                Element elem = doc.createElement(name);
                parent.appendChild(elem);
                stack.add(elem);
                return true;
            }

            @Override
            protected boolean processEndTag(String name) {
                Element elem = stack.remove(stack.size() - 1);
                elem.parent().removeChild(elem);
                return true;
            }
        };
        Document doc = builder.parse("<html><head><title>Hello World</title></head><body><h1>Hello World</h1></body></html>", "http://example.com", errors);
        assertEquals("Hello World", doc.title().text());
        assertEquals("Hello World", doc.body().child(0).child(0).text());
        assertEquals("http://example.com", doc.baseUri());
    }
}