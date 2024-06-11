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
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testParseEmpty() {
        TreeBuilder builder = mock(TreeBuilder.class);
        Document doc = builder.parse("", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(0, doc.body().childSize());
    }

    @Test
    public void testParseWithSimpleStartTag() {
        TreeBuilder builder = mock(TreeBuilder.class);
        String input = "<html>";
        Document doc = builder.parse(input, "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        Element html = doc.body();
        assertNotNull(html);
        assertEquals("html", html.tagName());
    }

    @Test
    public void testParseWithSimpleEndTag() {
        TreeBuilder builder = mock(TreeBuilder.class);
        String input = "<html><body></body></html>";
        Document doc = builder.parse(input, "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        Element html = doc.body();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        assertEquals(0, html.childSize());
    }

    @Test
    public void testParseWithNestedStartAndEndTags() {
        TreeBuilder builder = mock(TreeBuilder.class);
        String input = "<html><body><p>Hello, world!</p></body></html>";
        Document doc = builder.parse(input, "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        Element html = doc.body();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        assertEquals(1, html.childSize());
        Element p = html.child(0);
        assertNotNull(p);
        assertEquals("p", p.tagName());
        assertEquals("Hello, world!", p.text());
    }

    @Test
    public void testParseWithAttributes() {
        TreeBuilder builder = mock(TreeBuilder.class);
        String input = "<p id=\"foo\" class=\"bar baz\"><i>Hello, world!</i></p>";
        Document doc = builder.parse(input, "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        Element p = doc.body().child(0);
        assertNotNull(p);
        assertEquals("p", p.tagName());
        Attributes attributes = p.attributes();
        assertEquals(3, attributes.size());
        assertEquals("foo", attributes.get("id"));
        assertEquals("bar baz", attributes.get("class"));
        Element i = p.child(0);
        assertNotNull(i);
        assertEquals("i", i.tagName());
        assertEquals("Hello, world!", i.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithMalformedInput() {
        TreeBuilder builder = mock(TreeBuilder.class);
        String input = "<html<body>hello";
        builder.parse(input, "http://example.com");
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    start((StartTag) token);
                    return true;
                }
                return false;
            }
        };
        String input = "<html>";
        StartTag startTag = new StartTag();
        when(startTag.name()).thenReturn("html");
        Document doc = builder.parse(input, "http://example.com");
        Element html = doc.body();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        assertEquals(1, builder.stack.size());
        assertEquals(html, builder.stack.get(builder.stack.size() - 1));
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.END_TAG) {
                    end((EndTag) token);
                    return true;
                }
                return false;
            }
        };
        String input = "<html><body></body></html>";
        EndTag endTag = new EndTag();
        when(endTag.name()).thenReturn("html");
        Document doc = builder.parse(input, "http://example.com");
        assertNotNull(doc);
        assertEquals(0, builder.stack.size());
    }

    private void start(StartTag startTag) {
        Element newElement = doc.createElement(startTag.name());
        if (baseUri != null) {
            newElement.baseUri(baseUri);
        }
        stack.add(newElement);
    }

    private void end(EndTag endTag) {
        Element parent = currentElement();
        if (parent != null) {
            parent.appendChild(stack.remove(stack.size() - 1));
        }
    }
}