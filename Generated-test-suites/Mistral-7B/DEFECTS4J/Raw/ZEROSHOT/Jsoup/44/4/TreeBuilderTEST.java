package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import java.util.ArrayList;

public class TreeBuilderTest {

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                System.out.println("Processing token: " + token.toString());
                return true;
            }
        };

        builder.initialiseParse(input, baseUri, errors);
        Document doc = builder.parse(input, baseUri);

        assertEquals("html", doc.tagName());
        Element html = doc.body();
        assertEquals("body", html.tagName());
        Element title = doc.head().title();
        assertEquals("title", title.tagName());
        assertEquals("Test", title.text());
        Element h1 = html.child(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello World", h1.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullInput() {
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder builder = new TreeBuilder();
        builder.initialiseParse(null, baseUri, errors);
        builder.parse(null, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder builder = new TreeBuilder();
        builder.initialiseParse(input, null, errors);
        builder.parse(input, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullErrors() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com/";

        TreeBuilder builder = new TreeBuilder();
        builder.initialiseParse(input, baseUri, null);
        builder.parse(input, baseUri);
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean processStartTag(Token token) {
                System.out.println("Processing start tag: " + token.toString());
                return true;
            }
        };

        String input = "<html>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);
        builder.parse(input, baseUri);

        Element html = builder.currentElement();
        assertEquals("html", html.tagName());
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean processEndTag(Token token) {
                System.out.println("Processing end tag: " + token.toString());
                return true;
            }
        };

        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);
        builder.parse(input, baseUri);

        Element body = builder.currentElement();
        assertEquals("body", body.tagName());
        builder.processEndTag("h1");
        Element h1 = builder.currentElement();
        assertNull(h1);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean processStartTag(Token token) {
                System.out.println("Processing start tag: " + token.toString());
                return true;
            }
        };

        String input = "<img src=\"image.jpg\" alt=\"An image\">";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);
        builder.parse(input, baseUri);

        Element img = builder.currentElement();
        assertEquals("img", img.tagName());

        Attributes attributes = img.attributes();
        assertEquals("src", attributes.get("src"));
        assertEquals("image.jpg", attributes.get("src").attr("href"));
        assertEquals("alt", attributes.get("alt"));
        assertEquals("An image", attributes.get("alt").text());
    }

    @Test
    public void testErrors() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.EOF) {
                    builder.errors.addError("Unexpected end of input", token.getLine(), token.getColumn());
                }
                return true;
            }
        };

        String input = "<html><head><title>Test</title>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);
        builder.parse(input, baseUri);

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected end of input", error.getMessage());
        assertEquals(1, error.getLine());
        assertEquals(19, error.getColumn());
    }
}