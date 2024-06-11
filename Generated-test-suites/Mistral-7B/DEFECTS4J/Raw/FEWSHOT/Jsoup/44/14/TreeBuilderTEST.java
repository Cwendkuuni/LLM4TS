package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.parser.Tokenizer;

import java.util.ArrayList;

public class TreeBuilderTest {
    private TreeBuilder builder;
    private Document doc;
    private Tokenizer tokenizer;
    private ArrayList<Element> stack;

    @Before
    public void setup() {
        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        doc = builder.parse("<html><head></head><body></body></html>", "http://example.com");
        tokenizer = new Tokenizer(new CharacterReader(""), null);
        stack = new ArrayList<>(32);
        builder.initialiseParse("", "http://example.com", null);
    }

    @Test
    public void testInitialisation() {
        assertEquals("http://example.com", builder.baseUri);
        assertNull(builder.doc);
        assertNull(builder.stack);
        assertNull(builder.currentToken);
        assertNull(builder.errors);
    }

    @Test
    public void testParseDocument() {
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(2, doc.childNodeSize());

        Element head = doc.head();
        assertEquals("head", head.tagName());
        assertEquals(0, head.childNodeSize());

        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(0, body.childNodeSize());
    }

    @Test
    public void testProcessStartTag() {
        builder.processStartTag("html");
        assertEquals(1, stack.size());
        assertSame(doc, stack.get(0));

        builder.processStartTag("head");
        assertEquals(2, stack.size());
        assertSame(doc.head(), stack.get(1));

        builder.processStartTag("body");
        assertEquals(3, stack.size());
        assertSame(doc.body(), stack.get(2));
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        Attributes attrs = new Attributes();
        attrs.put("class", "example");

        builder.processStartTag("div", attrs);
        Element div = stack.get(stack.size() - 1);
        assertEquals("div", div.tagName());
        assertEquals("example", div.classAttr());
        assertEquals(0, div.childNodeSize());
    }

    @Test
    public void testProcessEndTag() {
        builder.processStartTag("html");
        builder.processStartTag("head");
        builder.processStartTag("title");
        builder.processEndTag("title");
        assertEquals(2, stack.size());
    }

    @Test
    public void testProcessEndTagNotFound() {
        builder.processStartTag("html");
        builder.processStartTag("head");
        builder.processStartTag("body");
        builder.processEndTag("title"); // should not crash
        assertEquals(3, stack.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testProcessStartTagAfterEndTag() {
        builder.processStartTag("html");
        builder.processStartTag("head");
        builder.processEndTag("head");
        builder.processStartTag("title"); // should throw exception
    }

    @Test
    public void testTokeniserReadsAllTokens() {
        StringBuilder input = new StringBuilder();
        input.append("<html><head><title>Test</title></head></html>");
        tokenizer.reset();
        Token token;
        while ((token = tokenizer.next()) != null) {
            input.append(token.text());
        }
        assertEquals("<html><head><title>Test</title></head></html>", input.toString());
    }

    @Test
    public void testProcessEOF() {
        builder.processStartTag("html");
        builder.processStartTag("head");
        builder.processStartTag("title");
        tokenizer.next(); // consume start tag token
        tokenizer.next(); // consume text token
        tokenizer.next(); // consume end tag token

        // process EOF should not throw exception and should return true
        assertTrue(builder.process(tokenizer.read()));
    }
}