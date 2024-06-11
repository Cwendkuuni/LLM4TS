package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.Validate;

import java.util.ArrayList;

public class TreeBuilderTest {

    @org.junit.Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String input = "test input";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertNull(treeBuilder.currentToken);
        assertNull(treeBuilder.errors);
    }

    @org.junit.Test
    public void testParse_ValidInput() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };

        String input = "<html><body><h1>Hello</h1></body></html>";
        String baseUri = "http://test.com";

        Document doc = treeBuilder.parse(input, baseUri);

        assertNotNull(doc);
        assertEquals("http://test.com", doc.baseUri());
        Element body = doc.body();
        Element h1 = body.child(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello", h1.text());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testParse_NullInput() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String baseUri = "http://test.com";

        treeBuilder.parse(null, baseUri);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testParse_NullBaseUri() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String input = "<html><body></html>";

        treeBuilder.parse(input, null);
    }

    @org.junit.Test
    public void testRunParser() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.EOF) {
                    System.out.println("End of file reached");
                    return true;
                }
                System.out.println("Processing token: " + token.toString());
                return true;
            }
        };

        String input = "<html><body><h1>Hello</h1></body></html>";
        String baseUri = "http://test.com";

        treeBuilder.parse(input, baseUri);

        // This test assumes that the tokens are processed in the correct order.
        // You may want to modify this test to check the state of the stack and other variables instead.
        assertEquals(10, treeBuilder.tokeniser.getPosition());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testProcessStartTag_NullName() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return processStartTag(token.asStartTag());
            }

            protected boolean processStartTag(Token.StartTag startTag) {
                Validate.notNull(startTag.name(), "Name cannot be null");
                return true;
            }
        };

        treeBuilder.initialiseParse("", "", null);
        treeBuilder.processStartTag(null, null);
    }

    @org.junit.Test
    public void testProcessStartTag_ValidName() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return processStartTag(token.asStartTag());
            }

            protected boolean processStartTag(Token.StartTag startTag) {
                assertEquals("div", startTag.name());
                return true;
            }
        };

        String input = "<div></div>";
        String baseUri = "http://test.com";

        treeBuilder.initialiseParse(input, baseUri, null);
        treeBuilder.processStartTag(new Token.StartTag("div"));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testProcessStartTag_NullAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return processStartTag(token.asStartTag());
            }

            protected boolean processStartTag(Token.StartTag startTag) {
                startTag.attributes();
                return true;
            }
        };

        treeBuilder.initialiseParse("", "", null);
        treeBuilder.processStartTag(new Token.StartTag("div"));
    }

    @org.junit.Test
    public void testProcessStartTag_ValidAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return processStartTag(token.asStartTag());
            }

            protected boolean processStartTag(Token.StartTag startTag) {
                Attributes attributes = startTag.attributes();
                assertEquals("class", attributes.classAttr());
                assertEquals("test", attributes.classAttr("class"));
                return true;
            }
        };

        String input = "<div class='test'></div>";
        String baseUri = "http://test.com";

        treeBuilder.initialiseParse(input, baseUri, null);
        treeBuilder.processStartTag(new Token.StartTag("div"));
    }

    @org.junit.Test
    public void testProcessEndTag_ValidName() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return processEndTag(token.asEndTag());
            }

            protected boolean processEndTag(Token.EndTag endTag) {
                assertEquals("div", endTag.name());
                return true;
            }
        };

        String input = "<div><div></div></div>";
        String baseUri = "http://test.com";

        treeBuilder.initialiseParse(input, baseUri, null);
        treeBuilder.processEndTag(new Token.EndTag("div"));
    }
}