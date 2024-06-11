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
 * Test class for TreeBuilder
 */
public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "Test input";
        String baseUri = "http://test.com";
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse(input, baseUri, null);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertNull(treeBuilder.currentToken);
        assertNull(treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "Test input";
        String baseUri = "http://test.com";
        TreeBuilder treeBuilder = new TreeBuilder();
        Document parsedDoc = treeBuilder.parse(input, baseUri);
        assertNotNull(parsedDoc);
    }

    @Test
    public void testParseWithErrors() {
        String input = "Test input";
        String baseUri = "http://test.com";
        TreeBuilder treeBuilder = new TreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        Document parsedDoc = treeBuilder.parse(input, baseUri, errors);
        assertNotNull(parsedDoc);
        assertFalse(errors.getErrors().isEmpty());
    }

    @Test
    public void testRunParser() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("Test input", "http://test.com", null);
        treeBuilder.runParser();
    }

    @Test(expected = NullPointerException.class)
    public void testProcessNullToken() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.process(null);
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return token.type == Token.TokenType.STARTTAG;
            }
        };
        treeBuilder.initialiseParse("Test input", "http://test.com", null);
        Token startTagToken = new Token.StartTag();
        assertTrue(treeBuilder.processStartTag(startTagToken.name));
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return token.type == Token.TokenType.ENDTAG;
            }
        };
        treeBuilder.initialiseParse("Test input", "http://test.com", null);
        Token endTagToken = new Token.EndTag();
        assertTrue(treeBuilder.processEndTag(endTagToken.name));
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return token.type == Token.TokenType.STARTTAG;
            }
        };
        treeBuilder.initialiseParse("Test <div id='test' class='test'></div> input", "http://test.com", null);
        Token startTagToken = new Token.StartTag();
        startTagToken.name = "div";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        attributes.put("class", "test");
        startTagToken.setAttributes(attributes);
        assertTrue(treeBuilder.processStartTag(startTagToken.name, startTagToken.getAttributes()));
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.STARTTAG) {
                    treeBuilder.stack.add(new Element("test"));
                    return true;
                }
                return false;
            }
        };
        treeBuilder.initialiseParse("<div><p><span></span></p></div>", "http://test.com", null);
        Element currentElement = treeBuilder.currentElement();
        assertEquals("div", currentElement.tagName());
    }

    @Test(expected = IllegalStateException.class)
    public void testCurrentElementWhenNoOpenElement() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return token.type == Token.TokenType.STARTTAG;
            }
        };
        treeBuilder.initialiseParse("Test input", "http://test.com", null);
        Element currentElement = treeBuilder.currentElement();
        assertNull(currentElement);
    }
}