package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Test class for TreeBuilder.
 */
public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "test";
        String baseUri = "test.com";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        treeBuilder.initialiseParse(input, baseUri, errors);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertNull(treeBuilder.currentToken);
        assertNull(treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "test.com";
        ParseErrorList errors = new ParseErrorList();
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
        };
        Document doc = treeBuilder.parse(input, baseUri);
        Element html = doc.rootElement();
        assertEquals("html", html.tagName());
        Element head = html.child(0);
        assertEquals("head", head.tagName());
        Element body = html.child(1);
        assertEquals("body", body.tagName());
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullInput() {
        String baseUri = "test.com";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        treeBuilder.parse(null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullBaseUri() {
        String input = "<html></html>";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        treeBuilder.parse(input, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidInput() {
        String input = "test";
        String baseUri = "test.com";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        treeBuilder.parse(input, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialiseParseNullInput() {
        String input = null;
        String baseUri = "test.com";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        treeBuilder.initialiseParse(input, baseUri, errors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialiseParseNullBaseUri() {
        String input = "<html></html>";
        String baseUri = null;
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        treeBuilder.initialiseParse(input, baseUri, errors);
    }

    @Test
    public void testProcessStartTag() {
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "test.com";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(Token token) {
                return true;
            }
        };
        Document doc = treeBuilder.parse(input, baseUri);
        Element html = doc.rootElement();
        Element title = html.child(0).child(0);
        assertEquals("title", title.tagName());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String input = "<html><head><title id=\"titleId\">Test</title></head><body></body></html>";
        String baseUri = "test.com";
        ParseErrorList errors = new ParseErrorList();
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
            public boolean processStartTag(String name, Attributes attrs) {
                assertEquals("title", name);
                assertEquals("titleId", attrs.get("id"));
                return true;
            }
        };
        Document doc = treeBuilder.parse(input, baseUri);
        Element html = doc.rootElement();
        Element title = html.child(0).child(0);
        assertEquals("title", title.tagName());
        assertEquals("titleId", title.attribute("id"));
    }

    @Test
    public void testProcessEndTag() {
        String input = "<html><head></head><body><p>Test</p></body></html>";
        String baseUri = "test.com";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processEndTag(Token token) {
                return true;
            }
        };
        Document doc = treeBuilder.parse(input, baseUri);
        Element p = doc.body().child(0);
        Element parent = treeBuilder.currentElement();
        assertEquals("p", p.tagName());
        assertEquals("body", parent.tagName());
    }

}