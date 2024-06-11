package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.parser.TreeBuilder;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "https://example.com/";
        ParseErrorList errors = ParseErrorList.noTracking();

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        builder.initialiseParse(input, baseUri, errors);

        assertNotNull(builder.doc);
        assertEquals(baseUri, builder.baseUri);
        assertNull(builder.errors);
    }

    @Test
    public void testParseWithNoErrors() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "https://example.com/";

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        Document doc = builder.parse(input, baseUri);

        assertNotNull(doc.body());
        assertNotNull(doc.head());
        assertNotNull(doc.title());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head></head><body><p>foo<bar></p></body></html>";
        String baseUri = "https://example.com/";

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG && token.name.equals("bar")) {
                    throw new RuntimeException("Unexpected <bar> tag");
                }
                return true;
            }
        };
        ParseErrorList errors = new ParseErrorList();

        Document doc = builder.parse(input, baseUri, errors);

        assertNotNull(doc);
        assertNotNull(errors.size());
        assertEquals(1, errors.size());
        assertSame(errors.get(0).line(), 3);
        assertSame(errors.get(0).column(), 5);
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(Token token) {
                return false;
            }
        };
        String input = "<html><head></head><body></body></html>";

        builder.initialiseParse(input, "", ParseErrorList.noTracking());

        Token token = new Token("html", Token.TokenType.START_TAG);
        assertFalse(builder.processStartTag(token));
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(Token token) {
                return false;
            }
        };
        String input = "<h1 id='foo' class='bar baz'>Hello, world!</h1>";

        builder.initialiseParse(input, "", ParseErrorList.noTracking());

        Token token = new Token("h1", Token.TokenType.START_TAG);
        Attributes attrs = new Attributes();
        attrs.put("id", "foo");
        attrs.put("class", "bar baz");
        token.setAttributes(attrs);

        assertFalse(builder.processStartTag(token));
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processEndTag(Token token) {
                return false;
            }
        };
        String input = "<html><head></head><body><p>foo</p></body></html>";

        builder.initialiseParse(input, "", ParseErrorList.noTracking());

        Token token = new Token("p", Token.TokenType.END_TAG);
        assertFalse(builder.processEndTag(token));
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        String input = "<html><head></head><body><p>foo</p></body></html>";

        builder.initialiseParse(input, "", ParseErrorList.noTracking());

        Element element = new Element("p");
        stack.add(element);

        assertEquals(element, builder.currentElement());
    }

    @Test
    public void testCurrentElementWhenStackIsEmpty() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };

        assertEquals(null, builder.currentElement());
    }
}