package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        // Set up the Tokeniser object with a test character reader and parse error list
        CharacterReader reader = new CharacterReader("<html>");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        // Test the read() method of Tokeniser
        Token token = tokeniser.read();
        assertNotNull(token); // Ensure that a token is returned
        assertEquals(Token.TokenType.StartTag, token.type); // Ensure that the token type is StartTag
        assertEquals("html", ((Token.StartTag) token).tagName); // Ensure that the start tag name is correct

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EOF, token.type); // Ensure that the token type is EOF
    }

    @Test
    public void testEmit() {
        // Test the emit(Token token) method of Tokeniser
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.emit(startTag);

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("div", ((Token.StartTag) token).tagName);
    }

    @Test
    public void testEmitString() {
        // Test the emit(String str) method of Tokeniser
        String str = "Hello, world!";
        tokeniser.emit(str);

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(str, ((Token.Character) token).getData());
    }

    @Test
    public void testEmitChar() {
        // Test the emit(char c) method of Tokeniser
        char c = 'a';
        tokeniser.emit(c);

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(String.valueOf(c), ((Token.Character) token).getData());
    }

    @Test
    public void testTransition() {
        // Test the transition(TokeniserState state) method of Tokeniser
        TokeniserState state = TokeniserState.Comment;
        tokeniser.transition(state);

        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        // Test the advanceTransition(TokeniserState state) method of Tokeniser
        TokeniserState state = TokeniserState.ScriptDataEscaped;
        tokeniser.advanceTransition(state);

        assertEquals(state, tokeniser.getState());

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test the acknowledgeSelfClosingFlag() method of Tokeniser
        tokeniser.acknowledgeSelfClosingFlag();

        // TODO: Add assertion for the expected change in selfClosingFlagAcknowledged
    }

    // TODO: Add more test methods for the remaining public methods of Tokeniser

}