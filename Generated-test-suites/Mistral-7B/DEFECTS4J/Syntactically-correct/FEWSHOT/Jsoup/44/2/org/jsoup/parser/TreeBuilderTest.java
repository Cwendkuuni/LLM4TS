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
    public void testInitialiseParse() {
        String input = "<html><head></head><body>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                fail("Should not reach here");
                return false;
            }
        };
        treeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(input, treeBuilder.reader.input());
        assertEquals(baseUri, treeBuilder.baseUri);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNull(treeBuilder.errors);
    }

    @Test
    public void testParseWithNoErrors() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        Document doc = treeBuilder.parse(input, baseUri);

        assertNotNull(doc);
        assertEquals(input, treeBuilder.reader.input());
        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(1, doc.childNodeSize());
        assertEquals(1, doc.body().childNodeSize());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head><script>alert('XSS')</script></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                errors.add(token.getLine(), token.getColumn(), "Error message");
                return false;
            }
        };
        Document doc = treeBuilder.parse(input, baseUri, errors);

        assertNotNull(doc);
        assertEquals(input, treeBuilder.reader.input());
        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(1, doc.childNodeSize());
        assertEquals(1, doc.body().childNodeSize());
        assertEquals(1, errors.size());
        assertEquals(1, errors.get(0).getLine());
        assertEquals(8, errors.get(0).getColumn());
        assertEquals("Error message", errors.get(0).getMessage());
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    assertEquals("html", token.name());
                    return true;
                }
                return false;
            }
        };
        treeBuilder.initialiseParse("<html><head></head><body>", "http://example.com", ParseErrorList.noTracking());
        treeBuilder.processStartTag("html");
        assertNotNull(treeBuilder.doc);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    String name = token.name();
                    if ("img".equals(name)) {
                        Attributes attrs = token.getAttributes();
                        assertEquals("src", attrs.get("src"));
                        assertEquals("alt", attrs.get("alt"));
                        return true;
                    }
                    return false;
                }
                return false;
            }
        };
        treeBuilder.initialiseParse("<img src=\"image.png\" alt=\"Image\" />", "http://example.com", ParseErrorList.noTracking());
        treeBuilder.processStartTag("img");
        assertNotNull(treeBuilder.doc);
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.END_TAG) {
                    assertEquals("body", token.name());
                    return true;
                }
                return false;
            }
        };
        treeBuilder.initialiseParse("<html><head></head><body></body></html>", "http://example.com", ParseErrorList.noTracking());
        treeBuilder.processEndTag("body");
        assertNotNull(treeBuilder.doc);
    }

    @Test
    public void testCurrentElement() {
        String input = "<html><head><body><p>Content</p></body></head></html>";
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        treeBuilder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking());

        assertEquals(null, treeBuilder.currentElement());

        treeBuilder.processStartTag("html");
        assertEquals("html", treeBuilder.currentElement().tagName());

        treeBuilder.processStartTag("head");
        assertEquals("head", treeBuilder.currentElement().tagName());

        treeBuilder.processStartTag("body");
        assertEquals("body", treeBuilder.currentElement().tagName());

        treeBuilder.processStartTag("p");
        assertEquals("p", treeBuilder.currentElement().tagName());

        treeBuilder.processEndTag("p");
        assertEquals("body", treeBuilder.currentElement().tagName());

        treeBuilder.processEndTag("body");
        assertEquals("html", treeBuilder.currentElement().tagName());

        treeBuilder.processEndTag("html");
        assertEquals(null, treeBuilder.currentElement());
    }
}