package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""));
    }

    @Test
    public void testRead_shouldReturnCharacterToken_whenCharBufferNotEmpty() {
        tokeniser.emit("abc");

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("abc", ((Token.Character) token).data);
    }

    @Test
    public void testRead_shouldReturnEmitPendingToken_whenCharBufferEmpty() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);

        Token token = tokeniser.read();

        assertEquals(startTag, token);
    }

    @Test
    public void testEmit_shouldSetEmitPendingToken_whenTokenIsEmitted() {
        Token.Comment comment = new Token.Comment();
        tokeniser.emit(comment);

        assertEquals(comment, tokeniser.emitPending);
        assertEquals(true, tokeniser.isEmitPending);
    }

    @Test
    public void testEmit_shouldSetSelfClosingFlagAcknowledgedFalse_whenStartTagIsEmittedWithSelfClosingFlagTrue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;

        tokeniser.emit(startTag);

        assertEquals(false, tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testEmit_shouldSetIsEmitPendingFalse_andClearCharBuffer_whenCharBufferNotEmpty() {
        tokeniser.emit("abc");

        tokeniser.emit(new Token.StartTag());

        assertEquals(false, tokeniser.isEmitPending);
        assertEquals("", tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmit_shouldCallError_whenEndTagHasAttributes() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.attributes.add(new Token.Attribute());

        tokeniser.emit(endTag);

        // assert that error is thrown or error is recorded for later validation
    }

    // Add more test cases for each method in the Tokeniser class...

}