package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.helper.Validate;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for TreeBuilder.
 */
public class TreeBuilderTest {

    private TreeBuilder treeBuilder;
    private CharacterReader characterReader;
    private Tokeniser tokeniser;
    private Document doc;
    private ArrayList<Element> stack;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        characterReader = mock(CharacterReader.class);
        tokeniser = mock(Tokeniser.class);

        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected void initialiseParse(String input, String baseUri, ParseErrorList errors) {
                this.characterReader = characterReader;
                this.tokeniser = tokeniser;
                this.errors = errors;
            }
        };

        doc = mock(Document.class);
        stack = new ArrayList<Element>(32);
        errors = mock(ParseErrorList.class);

        treeBuilder.initialiseParse("input", "baseUri", errors);
        treeBuilder.tokeniser = tokeniser;
        treeBuilder.doc = doc;
        treeBuilder.stack = stack;
        treeBuilder.baseUri = "baseUri";
    }

    @Test
    public void testParseDocument() {
        when(tokeniser.read()).thenReturn(new Token("{", Token.TokenType.LT));
        when(tokeniser.read()).thenReturn(new Token("html", Token.TokenType.START_TAG));
        when(tokeniser.read()).thenReturn(new Token(">", Token.TokenType.EOF));

        treeBuilder.parse("input", "baseUri");

        assertNotNull(treeBuilder.doc);
        assertNull(treeBuilder.errors.size());
    }

    @Test
    public void testParseDocumentWithErrors() {
        when(tokeniser.read()).thenReturn(new Token("{", Token.TokenType.LT));
        when(tokeniser.read()).thenReturn(new Token("html", Token.TokenType.START_TAG));
        when(tokeniser.read()).thenReturn(new Token(">", Token.TokenType.EOF));
        when(tokeniser.read()).thenThrow(new RuntimeException("Test Error"));

        treeBuilder.parse("input", "baseUri", new ParseErrorList());

        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.errors.size());
    }

    @Test
    public void testProcessStartTag() {
        StartTag startTag = new StartTag("html");
        when(tokeniser.read()).thenReturn(new Token("{", Token.TokenType.LT));
        when(tokeniser.read()).thenReturn(startTag);
        when(tokeniser.read()).thenReturn(new Token(">", Token.TokenType.EOF));

        treeBuilder.processStartTag(startTag.name());

        assertNotNull(treeBuilder.currentElement());
        assertNotNull(treeBuilder.currentElement().tagName());
        assertNotNull(treeBuilder.currentElement().attributes());
    }

    @Test
    public void testProcessEndTag() {
        EndTag endTag = new EndTag("html");
        when(tokeniser.read()).thenReturn(new Token("{", Token.TokenType.LT));
        when(tokeniser.read()).thenReturn(new Token("/html", Token.TokenType.START_TAG));
        when(tokeniser.read()).thenReturn(endTag);
        when(tokeniser.read()).thenReturn(new Token(">", Token.TokenType.EOF));

        treeBuilder.processEndTag(endTag.name());

        assertNull(treeBuilder.currentElement());
    }
}