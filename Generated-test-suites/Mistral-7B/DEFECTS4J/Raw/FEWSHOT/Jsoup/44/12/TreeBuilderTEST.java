package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Tokeniser;

import java.util.ArrayList;

public class TreeBuilderTest {

    @Test
    public void testProcessStartTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.STARTTAG) {
                    Element element = new Element(token.name());
                    stack.add(element);
                    doc.body().append(element);
                    return true;
                }
                return false;
            }
        };

        builder.initialiseParse("<html><body><p>", "http://example.com", ParseErrorList.noTracking());
        builder.processStartTag("html");
        assertEquals("html", builder.currentElement().tagName());
        assertEquals("http://example.com", builder.currentElement().baseUri());

        builder.processStartTag("head");
        Element head = builder.currentElement();
        assertEquals("head", head.tagName());
        assertEquals("html", head.parent().tagName());

        builder.processStartTag("title");
        Element title = builder.currentElement();
        assertEquals("title", title.tagName());
        assertEquals("head", title.parent().tagName());

        builder.processEndTag("title");
        assertEquals(3, builder.stack.size());
        assertEquals("html", builder.stack.get(builder.stack.size() - 1).tagName());

        builder.processEndTag("head");
        assertEquals(2, builder.stack.size());
        assertEquals("html", builder.stack.get(builder.stack.size() - 1).tagName());

        builder.processEndTag("html");
        assertEquals(0, builder.stack.size());
        assertNotNull(builder.doc);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.STARTTAG) {
                    Element element = new Element(token.name());
                    stack.add(element);
                    doc.body().append(element);

                    Attributes attributes = new Attributes();
                    for (int i = 0; i < token.attrCount(); i++) {
                        attributes.put(token.attrName(i), token.attrValue(i));
                    }
                    element.attributes().putAll(attributes);

                    return true;
                }
                return false;
            }
        };

        builder.initialiseParse("<html lang=\"en\">", "http://example.com", ParseErrorList.noTracking());
        builder.processStartTag("html");
        Element html = builder.currentElement();
        assertEquals("html", html.tagName());
        assertEquals("http://example.com", html.baseUri());
        assertTrue(html.hasAttr("lang"));
        assertEquals("en", html.attr("lang"));

        builder.processStartTag("head");
        Element head = builder.currentElement();
        assertEquals("head", head.tagName());
        assertEquals("html", head.parent().tagName());

        builder.processStartTag("title");
        Element title = builder.currentElement();
        assertEquals("title", title.tagName());
        assertEquals("head", title.parent().tagName());

        Attributes titleAttributes = title.attributes();
        assertTrue(titleAttributes.hasKey("lang"));
        assertEquals("en", titleAttributes.get("lang"));

        builder.processEndTag("title");
        assertEquals(3, builder.stack.size());
        assertEquals("html", builder.stack.get(builder.stack.size() - 1).tagName());

        builder.processEndTag("head");
        assertEquals(2, builder.stack.size());
        assertEquals("html", builder.stack.get(builder.stack.size() - 1).tagName());

        builder.processEndTag("html");
        assertEquals(0, builder.stack.size());
        assertNotNull(builder.doc);
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.ENDTAG) {
                    stack.remove(stack.size() - 1);
                    return true;
                }
                return false;
            }
        };

        builder.initialiseParse("<html><body><p></p></body></html>", "http://example.com", ParseErrorList.noTracking());
        builder.processStartTag("html");
        assertEquals("html", builder.currentElement().tagName());

        builder.processStartTag("body");
        assertEquals("body", builder.currentElement().tagName());
        assertEquals("html", builder.currentElement().parent().tagName());

        builder.processStartTag("p");
        assertEquals("p", builder.currentElement().tagName());
        assertEquals("body", builder.currentElement().parent().tagName());

        builder.processEndTag("p");
        assertEquals(2, builder.stack.size());
        assertEquals("body", builder.stack.get(builder.stack.size() - 1).tagName());

        builder.processEndTag("body");
        assertEquals(1, builder.stack.size());
        assertEquals("html", builder.stack.get(builder.stack.size() - 1).tagName());

        builder.processEndTag("html");
        assertEquals(0, builder.stack.size());
        assertNotNull(builder.doc);
    }

    @Test
    public void testProcessError() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.ERROR) {
                    errors.add(new ParseError(token.line(), token.column(), token.message()));
                    return false;
                }
                return true;
            }
        };

        builder.initialiseParse("<html><body><p></p>", "http://example.com", new ParseErrorList());
        Token errorToken = new Token(1, 1, "Unexpected '{'", Token.TokenType.ERROR);
        builder.tokeniser.addError(errorToken);

        builder.runParser();

        assertEquals(0, builder.stack.size());
        assertNotNull(builder.doc);
        assertTrue(builder.errors.size() > 0);
        ParseError error = builder.errors.get(0);
        assertEquals(1, error.line());
        assertEquals(1, error.column());
        assertEquals("Unexpected '{'", error.message());
    }

    @Test
    public void testProcessEOF() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.EOF) {
                    return false;
                }
                return true;
            }
        };

        builder.initialiseParse("<html><body><p></p>", "http://example.com", ParseErrorList.noTracking());
        builder.runParser();

        assertEquals(0, builder.stack.size());
        assertNotNull(builder.doc);
    }
}