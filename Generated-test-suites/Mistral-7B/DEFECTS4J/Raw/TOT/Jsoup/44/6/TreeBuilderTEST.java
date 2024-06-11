package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TreeBuilderTest {
    private TreeBuilder builder;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        builder = new TreeBuilder() {
            @Override
            protected boolean process(TreeBuilder.Token token) {
                return true; // assume all tokens are processed successfully
            }
        };
        errors = new ParseErrorList();
    }

    @Test
    public void testParseWithInputAndBaseUri() {
        builder.initialiseParse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com", errors);
        Document doc = builder.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.body().get(0).get(0).text());
    }

    @Test
    public void testParseWithInputAndBaseUriAndErrors() {
        builder.initialiseParse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com", errors);
        Document doc = builder.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.body().get(0).get(0).text());

        // verify no errors were thrown
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testParseWithNullInput() {
        builder.initialiseParse(null, "http://example.com", errors);
        try {
            builder.parse(null, "http://example.com");
            fail("Expected ParseException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testParseWithNullBaseUri() {
        builder.initialiseParse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", null, errors);
        try {
            builder.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testProcessStartTag() {
        Element element = new Element("div");
        builder.stack.add(element);
        assertTrue(builder.processStartTag("html"));
        assertEquals("html", element.tagName());

        element = new Element("head");
        builder.stack.add(element);
        assertTrue(builder.processStartTag("head"));
        assertEquals("head", element.tagName());

        element = new Element("title");
        builder.stack.add(element);
        assertTrue(builder.processStartTag("title"));
        assertEquals("title", element.tagName());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        Element element = new Element("div");
        builder.stack.add(element);
        Attributes attrs = new Attributes();
        attrs.put("id", "myId");
        attrs.put("class", "myClass");

        assertTrue(builder.processStartTag("div", attrs));
        assertEquals("div", element.tagName());
        assertEquals("myId", element.attributes().get("id"));
        assertEquals("myClass", element.attributes().get("class"));
    }

    @Test
    public void testProcessEndTag() {
        Element div = new Element("div");
        Element body = new Element("body");
        builder.stack.add(body);
        builder.stack.add(div);

        assertTrue(builder.processStartTag("div"));
        assertTrue(builder.processStartTag("body"));
        assertTrue(builder.processEndTag("div"));
        assertNull(builder.currentElement());
    }
}