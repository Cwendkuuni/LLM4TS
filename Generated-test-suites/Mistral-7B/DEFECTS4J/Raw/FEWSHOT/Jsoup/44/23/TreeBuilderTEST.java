package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;

import java.util.ArrayList;

public class TreeBuilderTest {

    @Test
    public void testParseEmptyInput() {
        TreeBuilder builder = mock(TreeBuilder.class);
        Document doc = builder.parse("", "http://test.com");
        assertNotNull(doc);
        assertEquals(0, doc.body().childSize());
    }

    @Test
    public void testParseInputWithSingleTag() {
        TreeBuilder builder = mock(TreeBuilder.class);
        Document doc = builder.parse("<html></html>", "http://test.com");
        assertNotNull(doc);
        assertEquals(1, doc.body().childSize());
        Element html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());
    }

    @Test
    public void testParseInputWithMultipleTags() {
        TreeBuilder builder = mock(TreeBuilder.class);
        Document doc = builder.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://test.com");
        assertNotNull(doc);
        assertEquals(2, doc.body().childSize());
        Element html = doc.body();
        assertEquals("html", html.tagName());
        Element head = html.child(0);
        assertEquals("head", head.tagName());
        assertEquals(1, head.childSize());
        Element title = head.child(0);
        assertEquals("title", title.tagName());
        assertEquals("Test", title.text());
        Element body = html.child(1);
        assertEquals("body", body.tagName());
        assertEquals(1, body.childSize());
        Element p = body.child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseInputWithErrors() {
        TreeBuilder builder = mock(TreeBuilder.class);
        ParseErrorList errors = new ParseErrorList();
        Document doc = builder.parse("<html><head><title<error />", "http://test.com", errors);
        assertNotNull(doc);
        assertEquals(1, doc.body().childSize());
        Element html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());
        assertNull(errors.getErrorCount());

        errors = new ParseErrorList();
        doc = builder.parse("<html><head><title<error />", "http://test.com", errors);
        assertNotNull(doc);
        assertEquals(1, doc.body().childSize());
        html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());
        assertEquals(1, errors.getErrorCount());
        assertEquals("Error parsing start tag : title", errors.get(0).getMessage());
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }

            @Override
            protected boolean processStartTag(String name) {
                Element element = new Element(name);
                stack.add(element);
                doc.body().append(element);
                return true;
            }
        };

        builder.initialiseParse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://test.com", new ParseErrorList());
        Document doc = builder.parse("");
        assertNotNull(doc);
        assertEquals(2, doc.body().childSize());
        Element html = doc.body();
        assertEquals("html", html.tagName());
        Element head = html.child(0);
        assertEquals("head", head.tagName());
        assertEquals(1, head.childSize());
        Element title = head.child(0);
        assertEquals("title", title.tagName());
        assertEquals("Test", title.text());
        Element body = html.child(1);
        assertEquals("body", body.tagName());
        assertEquals(1, body.childSize());
        Element p = body.child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }

            @Override
            protected boolean processStartTag(String name) {
                Element element = new Element(name);
                stack.add(element);
                doc.body().append(element);
                return true;
            }

            @Override
            protected boolean processEndTag(String name) {
                Element element = stack.remove(stack.size() - 1);
                doc.body().remove(element);
                return true;
            }
        };

        builder.initialiseParse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://test.com", new ParseErrorList());
        Document doc = builder.parse("");
        assertNotNull(doc);
        assertEquals(1, doc.body().childSize());
        Element html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());

        builder.initialiseParse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://test.com", new ParseErrorList());
        doc = builder.parse("");
        assertNotNull(doc);
        assertEquals(1, doc.body().childSize());
        html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());

        builder.initialiseParse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://test.com", new ParseErrorList());
        doc = builder.parse("");
        assertNotNull(doc);
        assertEquals(1, doc.body().childSize());
        html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());

        builder.initialiseParse("<html><head><title>Test</title></head><body><p>Hello World<p>Hello Again</p></body></html>", "http://test.com", new ParseErrorList());
        doc = builder.parse("");
        assertNotNull(doc);
        assertEquals(1, doc.body().childSize());
        html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());

        builder.initialiseParse("<html><head><title>Test</title></head><body><p>Hello World<p>Hello Again</p></body></html>", "http://test.com", new ParseErrorList());
        doc = builder.parse("");
        assertNotNull(doc);
        assertEquals(1, doc.body().childSize());
        html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());

        stack = new ArrayList<>();

        builder.initialiseParse("<html><head><title>Test</title></head><body><p>Hello World<p>Hello Again</p></body></html>", "http://test.com", new ParseErrorList());
        doc = builder.parse("");
        assertNotNull(doc);
        assertEquals(1, doc.body().childSize());
        html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());

        builder.initialiseParse("<html><head><title>Test</title></head><body><p>Hello World<p>Hello Again</p></body></html>", "http://test.com", new ParseErrorList());
        doc = builder.parse("");
        assertNotNull(doc);
        assertEquals(1, doc.body().childSize());
        html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());

        builder.initialiseParse("<html><head><title>Test</title></head><body><p>Hello World<p>Hello Again</p></body></html>", "http://test.com", new ParseErrorList());
        doc = builder.parse("");
        assertNotNull(doc);
        assertEquals(1, doc.body().childSize());
        html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());

        builder.initialiseParse("<html><head><title>Test</title></head><body><p>Hello World<p>Hello Again</p></body></html>", "http://test.com", new ParseErrorList());
        doc = builder.parse("");
        assertNotNull(doc);
        assertEquals(1, doc.body().childSize());
        html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());
    }

    @Test
    public void testProcessTokenWhenNoOpenTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }

            @Override
            protected boolean processStartTag(String name) {
                return true;
            }

            @Override
            protected boolean processEndTag(String name) {
                return true;
            }
        };

        Document doc = builder.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://test.com");
        assertNotNull(doc);
        assertEquals(2, doc.body().childSize());
        Element html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());

        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }

            @Override
            protected boolean processStartTag(String name) {
                return true;
            }

            @Override
            protected boolean processEndTag(String name) {
                return true;
            }
        };

        doc = builder.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://test.com");
        assertNotNull(doc);
        assertEquals(2, doc.body().childSize());
        html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());

        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }

            @Override
            protected boolean processStartTag(String name) {
                return true;
            }

            @Override
            protected boolean processEndTag(String name) {
                return true;
            }
        };

        doc = builder.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://test.com");
        assertNotNull(doc);
        assertEquals(2, doc.body().childSize());
        html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());

        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }

            @Override
            protected boolean processStartTag(String name) {
                return true;
            }

            @Override
            protected boolean processEndTag(String name) {
                return true;
            }
        };

        doc = builder.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://test.com");
        assertNotNull(doc);
        assertEquals(2, doc.body().childSize());
        html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());

        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }

            @Override
            protected boolean processStartTag(String name) {
                return true;
            }

            @Override
            protected boolean processEndTag(String name) {
                return true;
            }
        };

        doc = builder.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://test.com");
        assertNotNull(doc);
        assertEquals(2, doc.body().childSize());
        html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }

            @Override
            protected boolean processStartTag(String name) {
                Element element = new Element(name);
                stack.add(element);
                doc.body().append(element);
                return true;
            }

            @Override
            protected boolean processStartTag(String name, Attributes attrs) {
                Element element = new Element(name);
                stack.add(element);
                doc.body().append(element);
                for (String name2 : attrs.keySet()) {
                    element.attr(name2, attrs.get(name2));
                }
                return true;
            }
        };

        builder.initialiseParse("<html><head><title attr1=\"value1\" attr2=\"value2\">Test</title></head><body><p attr3=\"value3\">Hello World</p></body></html>", "http://test.com", new ParseErrorList());
        Document doc = builder.parse("");
        assertNotNull(doc);
        assertEquals(2, doc.body().childSize());
        Element html = doc.body();
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());

        Element title = html.child(0);
        assertEquals("title", title.tagName());
        assertEquals(2, title.attributes().size());
        assertEquals("value1", title.attr("attr1"));
        assertEquals("value2", title.attr("attr2"));

        Element p = title.child(0);
        assertEquals("p", p.tagName());
        assertEquals(1, p.attributes().size());
        assertEquals("value3", p.attr("attr3"));
    }
}