package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.parser.Tokenizer;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;
    private CharacterReader characterReader;
    private Tokenizer tokenizer;
    private Document doc;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        characterReader = mock(CharacterReader.class);
        tokenizer = mock(Tokenizer.class);
        doc = mock(Document.class);
        errors = mock(ParseErrorList.class);

        treeBuilder = new TreeBuilder() {
            @Override
            protected Document parse(String input, String baseUri) {
                return parse(input, baseUri, errors);
            }

            @Override
            protected Document parse(String input, String baseUri, ParseErrorList errors) {
                return doc;
            }

            @Override
            protected void runParser() {
                //override to allow testing process method in isolation
            }

            @Override
            protected boolean process(Token token) {
                return true;
            }
        };

        treeBuilder.initialiseParse("input", "baseUri", errors);
        when(characterReader.read()).thenReturn(tokenizer.nextToken());
    }

    @Test
    public void testParseDocument() {
        treeBuilder.runParser();
        assertNotNull(doc);
    }

    @Test(expected = NullPointerException.class)
    public void testParseDocumentNullBaseUri() {
        treeBuilder.parse("input", null);
    }

    @Test(expected = NullPointerException.class)
    public void testParseDocumentNullInput() {
        treeBuilder.parse(null, "baseUri");
    }

    @Test
    public void testProcessStartTag() {
        Token startTag = new Token(Token.TokenType.START_TAG, "tag");
        when(tokenizer.nextToken()).thenReturn(startTag);

        treeBuilder.runParser();
        Element currentElement = treeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals("tag", currentElement.tagName());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        Attributes attrs = new Attributes();
        attrs.put("attributeName", "attributeValue");

        Token startTag = new Token(Token.TokenType.START_TAG, "tag");
        Token startTagName = new Token(Token.TokenType.START_TAG_NAME, "tag");
        Token startTagAttributes = new Token(Token.TokenType.START_TAG_ATTRIBUTES, "");

        when(tokenizer.nextToken()).thenReturn(startTagName).thenReturn(startTagAttributes).thenReturn(tokenizer.readAll()); //return empty token list to simulate end of attributes

        when(startTagAttributes.text()).thenReturn(" attributeName=\"attributeValue\"");

        treeBuilder.runParser();
        Element currentElement = treeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals("tag", currentElement.tagName());
        assertEquals("attributeValue", currentElement.attribute("attributeName"));
    }

    @Test
    public void testProcessEndTag() {
        Token endTag = new Token(Token.TokenType.END_TAG, "tag");
        when(tokenizer.nextToken()).thenReturn(endTag);

        treeBuilder.runParser();
        Element currentElement = treeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals("tag", currentElement.tagName());

        treeBuilder.processEndTag("tag");

        currentElement = treeBuilder.currentElement();
        assertNull(currentElement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessEndTagWrongName() {
        treeBuilder.processEndTag("wrongName");
    }
}