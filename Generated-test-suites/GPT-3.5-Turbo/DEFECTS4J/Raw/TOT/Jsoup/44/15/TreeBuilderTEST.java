package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(input, treeBuilder.reader.getInput());
        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(errors, treeBuilder.errors);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.tokeniser);
    }

    @Test
    public void testParseWithoutErrors() {
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://example.com";

        Document document = treeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        Document document = treeBuilder.parse(input, baseUri, errors);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(errors, treeBuilder.errors);
    }

    @Test
    public void testRunParserWithEOFToken() {
        treeBuilder.tokeniser = new Tokeniser(null, null);
        Token token = new Token(Token.TokenType.EOF);
        token.reset();

        treeBuilder.runParser();

        assertEquals(token, treeBuilder.currentToken);
    }

    @Test
    public void testProcessStartTagWithName() {
        String tagName = "div";
        treeBuilder.start = new Token.StartTag();

        boolean result = treeBuilder.processStartTag(tagName);

        assertTrue(result);
        assertEquals(tagName, treeBuilder.start.name());
    }

    @Test
    public void testProcessStartTagWithNameAndAttributes() {
        String tagName = "div";
        Attributes attrs = new Attributes();
        treeBuilder.start = new Token.StartTag();

        boolean result = treeBuilder.processStartTag(tagName, attrs);

        assertTrue(result);
        assertEquals(tagName, treeBuilder.start.name());
        assertEquals(attrs, treeBuilder.start.attributes());
    }

    @Test
    public void testProcessEndTagWithName() {
        String tagName = "div";
        treeBuilder.end = new Token.EndTag();

        boolean result = treeBuilder.processEndTag(tagName);

        assertTrue(result);
        assertEquals(tagName, treeBuilder.end.name());
    }

    @Test
    public void testCurrentElementWithEmptyStack() {
        Element element = treeBuilder.currentElement();

        assertNull(element);
    }

    @Test
    public void testCurrentElementWithNonEmptyStack() {
        Element expectedElement = new Element("div");
        treeBuilder.stack.add(expectedElement);

        Element currentElement = treeBuilder.currentElement();

        assertEquals(expectedElement, currentElement);
    }
}