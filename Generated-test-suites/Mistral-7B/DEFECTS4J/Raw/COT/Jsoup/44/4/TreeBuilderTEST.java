package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Test class for TreeBuilder.
 */
public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "Some HTML input";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // This method is not tested in this test case
                return true;
            }
        };
        treeBuilder.initialiseParse(input, baseUri, errors);

        // Check that the expected objects have been initialised
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertNull(treeBuilder.currentToken);
        assertNull(treeBuilder.errors);
    }

    @Test
    public void testParseWithNullInput() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // This method is not tested in this test case
                return true;
            }
        };

        // Test with null input
        try {
            treeBuilder.parse(null, "http://example.com");
            fail("Expected ParseErrorList to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithNullBaseUri() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // This method is not tested in this test case
                return true;
            }
        };

        // Test with null baseUri
        try {
            treeBuilder.parse("Some HTML input", null);
            fail("Expected ParseErrorList to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithEmptyInput() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // This method is not tested in this test case
                return true;
            }
        };

        String input = "";
        ParseErrorList errors = new ParseErrorList();
        Document doc = treeBuilder.parse(input, "http://example.com");
        assertEquals(0, doc.body().childNodeSize());
    }

    @Test
    public void testParseWithBasicHtmlInput() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // This method is not tested in this test case
                return true;
            }
        };

        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        ParseErrorList errors = new ParseErrorList();
        Document doc = treeBuilder.parse(input, "http://example.com");

        Element html = doc.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());

        Element title = html.child(0);
        assertNotNull(title);
        assertEquals("head", title.parent().tagName());
        assertEquals("title", title.tagName());
        assertEquals("Test", title.text());

        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());

        Element h1 = body.child(0);
        assertNotNull(h1);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseWithStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    // This test case only checks if the processStartTag method is called with the correct name
                    assertEquals("div", token.startTag().name());
                    return true;
                }
                return false;
            }
        };

        String input = "<div>";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder.parse(input, "http://example.com", errors, treeBuilder);
    }

    @Test
    public void testParseWithStartTagAndAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    // This test case only checks if the processStartTag method is called with the correct name and attributes
                    assertEquals("img", token.startTag().name());
                    assertEquals("src=\"image.jpg\" alt=\"Test Image\"", token.startTag().toString());
                    return true;
                }
                return false;
            }
        };

        String input = "<img src=\"image.jpg\" alt=\"Test Image\">";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder.parse(input, "http://example.com", errors, treeBuilder);
    }

    @Test(expected = IllegalStateException.class)
    public void testParseWithMissingEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // This method is not tested in this test case
                return true;
            }
        };

        String input = "<p>Hello World<div";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder.parse(input, "http://example.com", errors, treeBuilder);
    }

    @Test
    public void testParseWithNestedStartTags() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // This method is not tested in this test case
                return true;
            }
        };

        String input = "<div><p>Hello World</p></div>";
        ParseErrorList errors = new ParseErrorList();
        Document doc = TreeBuilder.parse(input, "http://example.com", errors, treeBuilder);

        Element div = doc.body().child(0);
        Element p = div.child(0);

        assertEquals("div", div.tagName());
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseWithSelfClosingStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // This method is not tested in this test case
                return true;
            }
        };

        String input = "<img src=\"image.jpg\" alt=\"Test Image\" />";
        ParseErrorList errors = new ParseErrorList();
        Document doc = TreeBuilder.parse(input, "http://example.com", errors, treeBuilder);

        Element img = doc.body().child(0);
        assertEquals("img", img.tagName());
        assertEquals("image.jpg", img.attr("src"));
        assertEquals("Test Image", img.attr("alt"));
    }

    @Test
    public void testParseWithEmptyElement() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // This method is not tested in this test case
                return true;
            }
        };

        String input = "<br>";
        ParseErrorList errors = new ParseErrorList();
        Document doc = TreeBuilder.parse(input, "http://example.com", errors, treeBuilder);

        Element br = doc.body().child(0);
        assertEquals("br", br.tagName());
    }

    @Test
    public void testParseWithProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.END_TAG) {
                    // This test case only checks if the processEndTag method is called with the correct name
                    assertEquals("div", token.endTag().name());
                    return true;
                }
                return false;
            }
        };

        String input = "</div>";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder.parse(input, "http://example.com", errors, treeBuilder);
    }
}