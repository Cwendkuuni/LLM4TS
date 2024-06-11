package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.helper.Validate;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };

        builder.initialiseParse(input, baseUri, errors);

        Validate.notNull(builder.doc, "Document should not be null after initialiseParse");
        Validate.notNull(builder.tokeniser, "Tokeniser should not be null after initialiseParse");
        Validate.notNull(builder.stack, "Stack should not be null after initialiseParse");
        Validate.notNull(builder.errors, "Errors should not be null after initialiseParse");
        Validate.assertEquals(builder.baseUri, baseUri, "BaseURI should be set correctly");
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com/";

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(String name) {
                if ("html".equals(name)) {
                    assertTrue(builder.currentElement() == null);
                    return true;
                }
                if ("head".equals(name)) {
                    assertNotNull(builder.currentElement());
                    assertEquals("html", builder.currentElement().tagName());
                    return true;
                }
                if ("title".equals(name)) {
                    assertNotNull(builder.currentElement().child(0));
                    assertEquals("head", builder.currentElement().tagName());
                    return true;
                }
                if ("body".equals(name)) {
                    assertNotNull(builder.currentElement().child(0));
                    assertEquals("head", builder.currentElement().parent().tagName());
                    return true;
                }
                if ("h1".equals(name)) {
                    assertNotNull(builder.currentElement().child(0));
                    assertEquals("body", builder.currentElement().parent().tagName());
                    return true;
                }
                return false;
            }
        };

        Document doc = builder.parse(input, baseUri);

        Element html = doc.body();
        assertNotNull(html);
        assertEquals("html", html.tagName());

        Element head = html.child(0);
        assertNotNull(head);
        assertEquals("head", head.tagName());

        Element title = head.child(0);
        assertNotNull(title);
        assertEquals("title", title.tagName());
        assertEquals("Test", title.text());

        Element body = html.child(1);
        assertNotNull(body);
        assertEquals("body", body.tagName());

        Element h1 = body.child(0);
        assertNotNull(h1);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello", h1.text());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com/";

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(String name) {
                if ("html".equals(name)) {
                    assertTrue(builder.currentElement() == null);
                    return true;
                }
                if ("head".equals(name)) {
                    assertNotNull(builder.currentElement());
                    assertEquals("html", builder.currentElement().tagName());
                    return true;
                }
                if ("title".equals(name)) {
                    assertNotNull(builder.currentElement().child(0));
                    assertEquals("head", builder.currentElement().tagName());
                    return false; // throw an error here
                }
                if ("body".equals(name)) {
                    assertNotNull(builder.currentElement().child(0));
                    assertEquals("head", builder.currentElement().parent().tagName());
                    return true;
                }
                if ("h1".equals(name)) {
                    assertNotNull(builder.currentElement().child(0));
                    assertEquals("body", builder.currentElement().parent().tagName());
                    return true;
                }
                return false;
            }
        };

        ParseErrorList errors = new ParseErrorList();
        Document doc = builder.parse(input, baseUri, errors);

        assertNotNull(doc);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(13, error.getLine());
        assertEquals(5, error.getColumn());
        assertEquals("Expected '>' but was '<' for tag 'title'", error.getMessage());
    }

    @Test
    public void testRunParser() {
        String input = "<html>";
        String baseUri = "http://example.com/";

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.EOF) {
                    assertEquals(1, builder.stack.size());
                    return true;
                }
                if (token.type == Token.TokenType.START_TAG) {
                    StartTag startTag = (StartTag) token;
                    Element newElement = doc.createElement(startTag.name());
                    doc.body().appendChild(newElement);
                    stack.add(newElement);
                    return true;
                }
                return false;
            }
        };

        builder.initialiseParse(input, baseUri, null);
        builder.runParser();

        assertEquals(1, builder.stack.size());
        Element root = builder.stack.get(0);
        assertEquals("html", root.tagName());
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    StartTag startTag = (StartTag) token;
                    if ("html".equals(startTag.name())) {
                        return processStartTag("html");
                    }
                    return false;
                }
                return true;
            }

            @Override
            protected boolean processStartTag(String name) {
                return processEndTag(name);
            }

            @Override
            protected boolean processEndTag(String name) {
                return false;
            }
        };

        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";

        builder.initialiseParse(input, "", null);
        Token token = builder.tokeniser.read();
        assertTrue(token.type == Token.TokenType.START_TAG);

        StartTag startTag = (StartTag) token;
        assertEquals("html", startTag.name());
        assertTrue(builder.processStartTag(startTag.name()));

        token = builder.tokeniser.read();
        assertTrue(token.type == Token.TokenType.START_TAG);

        startTag = (StartTag) token;
        assertEquals("head", startTag.name());
        assertFalse(builder.processStartTag(startTag.name())); // Should have thrown an error

        token = builder.tokeniser.read();
        assertTrue(token.type == Token.TokenType.START_TAG);

        startTag = (StartTag) token;
        assertEquals("title", startTag.name());
        assertTrue(builder.processStartTag(startTag.name()));

        token = builder.tokeniser.read();
        assertTrue(token.type == Token.TokenType.END_TAG);

        EndTag endTag = (EndTag) token;
        assertEquals("title", endTag.name());
        assertTrue(builder.processEndTag(endTag.name()));

        token = builder.tokeniser.read();
        assertTrue(token.type == Token.TokenType.START_TAG);

        startTag = (StartTag) token;
        assertEquals("body", startTag.name());
        assertTrue(builder.processEndTag(endTag.name())); // Should have thrown an error

        token = builder.tokeniser.read();
        assertTrue(token.type == Token.TokenType.START_TAG);

        startTag = (StartTag) token;
        assertEquals("h1", startTag.name());
        assertTrue(builder.processStartTag(startTag.name()));

        token = builder.tokeniser.read();
        assertTrue(token.type == Token.TokenType.END_TAG);

        endTag = (EndTag) token;
        assertEquals("h1", endTag.name());
        assertTrue(builder.processEndTag(endTag.name()));

        token = builder.tokeniser.read();
        assertTrue(token.type == Token.TokenType.EOF);
    }
}