package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.TreeBuilder.Token.StartTag;
import org.jsoup.parser.TreeBuilder.Token.EndTag;
import org.junit.Before;
import org.junit.Test;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(input, treeBuilder.reader.input());
        assertSame(errors, treeBuilder.errors);
        assertEquals(Document.createShell(baseUri), treeBuilder.doc);
    }

    @Test
    public void testParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";

        Document document = treeBuilder.parse(input, baseUri);

        assertEquals(Document.createShell(baseUri), document);
    }

    @Test
    public void testParseWithErrorList() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        Document document = treeBuilder.parse(input, baseUri, errors);

        assertEquals(Document.createShell(baseUri), document);
        assertEquals(0, errors.size());
    }

    @Test
    public void testRunParser() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token token = new Token();

        token.type = Token.TokenType.EOF;
        tokeniser.addToken(token);

        treeBuilder.tokeniser = tokeniser;

        treeBuilder.runParser();

        assertNull(treeBuilder.currentToken);
    }

    @Test
    public void testProcessStartTag() {
        String name = "div";

        boolean result = treeBuilder.processStartTag(name);

        assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String name = "div";
        Attributes attrs = new Attributes();

        boolean result = treeBuilder.processStartTag(name, attrs);

        assertFalse(result);
    }

    @Test
    public void testProcessEndTag() {
        String name = "div";

        boolean result = treeBuilder.processEndTag(name);

        assertFalse(result);
    }

    @Test
    public void testCurrentElementWithEmptyStack() {
        Element element = treeBuilder.currentElement();

        assertNull(element);
    }

    @Test
    public void testCurrentElementWithStackNotEmpty() {
        String name = "div";
        Element element = new Element(name);

        treeBuilder.stack.add(element);

        Element currentElement = treeBuilder.currentElement();

        assertSame(element, currentElement);
    }

    private static class Token {

        TokenType type;

        enum TokenType {
            START_TAG,
            END_TAG,
            EOF
        }

        static class StartTag extends Token {
            StartTag reset() {
                type = TokenType.START_TAG;
                return this;
            }

            StartTag name(String name) {
                return this;
            }

            StartTag nameAttr(String name, Attributes attrs) {
                return this;
            }
        }

        static class EndTag extends Token {
            EndTag reset() {
                type = TokenType.END_TAG;
                return this;
            }

            EndTag name(String name) {
                return this;
            }
        }
    }

}