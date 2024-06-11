package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setup() {
        treeBuilder = new TreeBuilderTestImpl();
    }

    @Test
    public void initialiseParse_withNonNullInputAndBaseUriAndErrors_shouldInitializeFields() {
        String input = "input";
        String baseUri = "baseUri";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(new Document(baseUri), treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertEquals(baseUri, treeBuilder.baseUri);
        assertNotNull(treeBuilder.currentToken);
        assertEquals(errors, treeBuilder.errors);
    }

    @Test(expected = NullPointerException.class)
    public void initialiseParse_withNullInput_shouldThrowNullPointerException() {
        String baseUri = "baseUri";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(null, baseUri, errors);
    }

    @Test(expected = NullPointerException.class)
    public void initialiseParse_withNullBaseUri_shouldThrowNullPointerException() {
        String input = "input";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, null, errors);
    }

    @Test
    public void parse_withInputBaseUriAndErrors_shouldReturnDocument() {
        String input = "input";
        String baseUri = "baseUri";
        ParseErrorList errors = new ParseErrorList();

        Document document = treeBuilder.parse(input, baseUri, errors);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void parse_withInputAndBaseUri_shouldReturnDocument() {
        String input = "input";
        String baseUri = "baseUri";

        Document document = treeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void runParser_withValidToken_shouldProcessToken() {
        Token token = new Token(Token.TokenType.START_TAG);
        token.reset();

        boolean result = treeBuilder.process(token);

        assertTrue(result);
    }

    @Test
    public void processStartTag_withValidName_shouldProcessStartTag() {
        String name = "name";

        boolean result = treeBuilder.processStartTag(name);

        assertTrue(result);
    }

    @Test
    public void processStartTag_withValidNameAndAttributes_shouldProcessStartTag() {
        String name = "name";
        Attributes attrs = new Attributes();

        boolean result = treeBuilder.processStartTag(name, attrs);

        assertTrue(result);
    }

    @Test
    public void processEndTag_withValidName_shouldProcessEndTag() {
        String name = "name";

        boolean result = treeBuilder.processEndTag(name);

        assertTrue(result);
    }

    @Test
    public void currentElement_withEmptyStack_shouldReturnNull() {
        Element currentElement = treeBuilder.currentElement();

        assertNull(currentElement);
    }

    private class TreeBuilderTestImpl extends TreeBuilder {

        @Override
        protected boolean process(Token token) {
            return false;
        }
    }
}