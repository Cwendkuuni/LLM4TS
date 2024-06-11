package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.jsoup.helper.Validate.notNull;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.CharacterReader;

public class TreeBuilderTest {

    private TreeBuilder builder;
    private Tokeniser tokeniser;
    private CharacterReader reader;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        String input = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://www.example.com/";
        errors = new ParseErrorList();

        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };

        builder.initialiseParse(input, baseUri, errors);
        tokeniser = builder.tokeniser;
        reader = builder.reader;
    }

    @Test
    public void testParse() {
        Document doc = builder.parse(reader.input, baseUri);
        notNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("html", doc.nodeName());
    }

    @Test
    public void testParseWithErrors() {
        ParseErrorList errors = new ParseErrorList();
        Document doc = builder.parse(reader.input, baseUri, errors);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testProcessStartTag() {
        Token token = new Token(Token.TokenType.START_TAG, 1, 1, 11, "html");
        assertTrue(builder.processStartTag(token.name()));
        assertEquals("html", builder.currentElement().nodeName());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String input = "<html lang='en'>";
        reader.input = input;

        Token token = new Token(Token.TokenType.START_TAG, 1, 1, 6, "html");
        Attributes attrs = new Attributes();
        attrs.put("lang", "en");
        token.setAttributes(attrs);

        assertTrue(builder.processStartTag(token.name(), token.attributes()));
        assertEquals("html", builder.currentElement().nodeName());
        assertEquals("en", builder.currentElement().attr("lang"));
    }

    @Test
    public void testProcessEndTag() {
        Token token = new Token(Token.TokenType.END_TAG, 1, 1, 12, "html");
        assertTrue(builder.processEndTag(token.name()));
        assertNull(builder.currentElement());
    }

    @Test
    public void testProcessInvalidToken() {
        Token token = new Token(Token.TokenType.COMMENT, 1, 1, 10, "test");
        assertFalse(builder.process(token));
    }

    @Test
    public void testProcessEOF() {
        Token token = new Token(Token.TokenType.EOF, 1, 1, 1, null);
        assertFalse(builder.process(token));
    }
}