package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Tokens;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Test class for TreeBuilder.
 */
public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        String input = "Test input";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        assertEquals(input, builder.doc.body().text());
        assertEquals(baseUri, builder.doc.baseURI());
        assertNotNull(builder.doc);
        assertNotNull(builder.reader);
        assertNotNull(builder.tokeniser);
        assertNotNull(builder.stack);
        assertSame(baseUri, builder.baseUri);
        assertNull(builder.currentToken);
        assertNull(builder.errors);
    }

    @Test
    public void testParseWithNoErrors() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://test.com";

        Document doc = builder.parse(input, baseUri);

        Element html = doc.body();
        Element head = html.child(0);
        Element title = head.child(0);
        Element body = html.child(1);
        Element h1 = body.child(0);

        assertEquals(input, doc.body().text());
        assertEquals(baseUri, doc.baseURI());
        assertNotNull(doc);
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(title);
        assertNotNull(body);
        assertNotNull(h1);
        assertEquals("Test", title.text());
        assertEquals("Hello", h1.text());
    }

    @Test
    public void testParseWithErrors() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        String input = "<html><head><title>Test</title><script>alert('Hello')</script></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://test.com";

        ParseErrorList errors = new ParseErrorList();

        Document doc = builder.parse(input, baseUri, errors);

        assertEquals(input, doc.body().text());
        assertEquals(baseUri, doc.baseURI());
        assertNotNull(doc);
        assertFalse(errors.isEmpty());

        assertEquals(1, errors.size());
        Token errorToken = errors.get(0);
        assertEquals(Token.TokenType.SCRIPT_TAG, errorToken.type);
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    return true;
                }
                return false;
            }
        };

        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://test.com";

        Document doc = builder.parse(input, baseUri);

        Element html = doc.body();
        Element head = html.child(0);
        Element title = head.child(0);
        Element body = html.child(1);
        Element h1 = body.child(0);

        assertEquals(input, doc.body().text());
        assertEquals(baseUri, doc.baseURI());
        assertNotNull(doc);
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(title);
        assertNotNull(body);
        assertNotNull(h1);
        assertEquals("Test", title.text());
        assertEquals("Hello", h1.text());

        assertFalse(builder.errors.isEmpty());
        assertEquals(0, builder.errors.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessStartTagWithNullName() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        String input = "<html>";
        String baseUri = "http://test.com";
        Attributes attrs = new Attributes();

        builder.processStartTag(null, attrs);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    Token.StartTag startTag = (Token.StartTag) token;
                    Attributes attrs = startTag.tagNameAttrs();
                    assertEquals("test", attrs.get("test"));
                    return true;
                }
                return false;
            }
        };

        String input = "<div test='test'></div>";
        String baseUri = "http://test.com";

        Document doc = builder.parse(input, baseUri);

        Element div = doc.body().child(0);

        assertEquals(input, doc.body().text());
        assertEquals(baseUri, doc.baseURI());
        assertNotNull(doc);
        assertNotNull(div);
        assertFalse(builder.errors.isEmpty());
        assertEquals(0, builder.errors.size());
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.END_TAG) {
                    return true;
                }
                return false;
            }
        };

        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://test.com";

        Document doc = builder.parse(input, baseUri);

        Element html = doc.body();
        Element head = html.child(0);
        Element title = head.child(0);
        Element body = html.child(1);
        Element h1 = body.child(0);

        assertEquals(input, doc.body().text());
        assertEquals(baseUri, doc.baseURI());
        assertNotNull(doc);
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(title);
        assertNotNull(body);
        assertNotNull(h1);
        assertEquals("Test", title.text());
        assertEquals("Hello", h1.text());

        assertFalse(builder.errors.isEmpty());
        assertEquals(0, builder.errors.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessEndTagWithNullName() {
        TreeBuilder builder = new TreeBuilder();

        String input = "</div>";

        builder.processEndTag(null);
    }
}