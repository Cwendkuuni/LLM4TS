package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import java.util.ArrayList;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "some input";
        String baseUri = "http://example.com";
        ParseErrorList errors = mock(ParseErrorList.class);

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(input, treeBuilder.reader.input());
        assertEquals(baseUri, treeBuilder.baseUri);
        assertNull(treeBuilder.currentToken);
        assertSame(errors, treeBuilder.errors);
        assertNull(treeBuilder.doc);
        assertEquals(0, treeBuilder.stack.size());
    }

    @Test(expected = NullPointerException.class)
    public void testInitialiseParseNullInput() {
        String baseUri = "http://example.com";
        ParseErrorList errors = mock(ParseErrorList.class);

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };

        treeBuilder.initialiseParse(null, baseUri, errors);
    }

    @Test(expected = NullPointerException.class)
    public void testInitialiseParseNullBaseUri() {
        String input = "some input";
        ParseErrorList errors = mock(ParseErrorList.class);

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };

        treeBuilder.initialiseParse(input, null, errors);
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>foo</title></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = mock(ParseErrorList.class);

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(Token token) {
                return true;
            }

            @Override
            protected boolean processEndTag(Token token) {
                return true;
            }

            @Override
            protected Element currentElement() {
                return null;
            }

            @Override
            protected Document doc() {
                return doc;
            }
        };

        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        Document doc = treeBuilder.doc;
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.childNodes().size());

        Element html = doc.childNodes().get(0);
        assertEquals("html", html.tagName());
        assertEquals(1, html.childNodes().size());

        Element head = html.childNodes().get(0);
        assertEquals("head", head.tagName());
        assertEquals(1, head.childNodes().size());

        Element title = head.childNodes().get(0);
        assertEquals("title", title.tagName());
        assertEquals("foo", title.text());

        Element body = html.childNodes().get(1);
        assertEquals("body", body.tagName());
        assertEquals(0, body.childNodes().size());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head><title>foo</title><script src=\"javascript:alert('Hello')\"></script></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = mock(ParseErrorList.class);

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(Token token) {
                return true;
            }

            @Override
            protected boolean processEndTag(Token token) {
                return true;
            }

            @Override
            protected Element currentElement() {
                return null;
            }

            @Override
            protected Document doc() {
                return doc;
            }

            @Override
            protected void reportError(ParseError error) {
                errors.add(error);
            }
        };

        when(errors.size()).thenReturn(0);

        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        Document doc = treeBuilder.doc;
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.childNodes().size());

        Element html = doc.childNodes().get(0);
        assertEquals("html", html.tagName());
        assertEquals(1, html.childNodes().size());

        Element head = html.childNodes().get(0);
        assertEquals("head", head.tagName());
        assertEquals(2, head.childNodes().size());

        Element title = head.childNodes().get(0);
        assertEquals("title", title.tagName());
        assertEquals("foo", title.text());

        Element script = head.childNodes().get(1);
        assertEquals("script", script.tagName());
        assertNull(script.attributes());
        assertNull(script.text());

        when(errors.size()).thenReturn(1);
        treeBuilder.runParser();

        Element error = doc.childNodes().get(1);
        assertEquals("script", error.tagName());
        assertEquals("http://example.com/script", error.attributes().get("src"));
        assertEquals("Scripting is disabled", error.text());
    }
}