package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * Unit tests for Tokeniser.
 */
public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        errors = new ParseErrorList(0); // Initialize the error list with capacity 0
        tokeniser = new Tokeniser(new CharacterReader(""), errors); // Initialize the Tokeniser with empty CharacterReader and empty error list
    }

    @Test
    public void tokeniserReadShouldReturnCharacterTokenWhenCharBufferIsNotEmpty() {
        tokeniser.charBuffer.append("abc");
        Token token = tokeniser.read();

        assertTrue(token instanceof Token.Character);
        assertEquals("abc", ((Token.Character) token).getData());
        assertEquals(0, tokeniser.charBuffer.length()); // charBuffer should be cleared after emitting the Token
    }

    @Test
    public void tokeniserReadShouldReturnEmitPendingTokenWhenCharBufferIsEmpty() {
        Token tagToken = new Token.StartTag();
        tokeniser.emitPending = tagToken;
        tokeniser.isEmitPending = true;

        Token token = tokeniser.read();

        assertSame(tagToken, token);
    }

    @Test
    public void emitShouldThrowExceptionIfThereIsAlreadyAnUnreadTokenPending() {
        Token tagToken = new Token.StartTag();
        tokeniser.emitPending = tagToken;
        tokeniser.isEmitPending = true;

        try {
            tokeniser.emit(new Token.Character("abc"));
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("There is an unread token pending!", e.getMessage());
        }
    }

    @Test
    public void emitShouldSetEmitPendingTokenAndIsEmitPendingFlag() {
        Token tagToken = new Token.StartTag();
        tokeniser.emit(tagToken);

        assertSame(tagToken, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void emitShouldSetLastStartTagAndSelfClosingFlagAcknowledgedIfTokenIsStartTagWithSelfClosingFlag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;

        tokeniser.emit(startTag);

        assertSame(startTag, tokeniser.lastStartTag);
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void emitShouldThrowErrorIfTokenIsEndTagAndHasAttributesPresent() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.attributes.add("attr1");

        try {
            tokeniser.emit(endTag);
            fail("Expected AssertionError to be thrown");
        } catch (AssertionError e) {
            assertEquals("Attributes incorrectly present on end tag", e.getMessage());
        }
    }

    @Test
    public void emitShouldNotThrowErrorIfTokenIsEndTagAndHasNoAttributesPresent() {
        Token.EndTag endTag = new Token.EndTag();

        tokeniser.emit(endTag);
        // no assertion needed, exception should not be thrown
    }

    @Test
    public void emitStringShouldAppendStringToCharBuffer() {
        tokeniser.emit("abc");

        assertEquals("abc", tokeniser.charBuffer.toString());
    }

    @Test
    public void emitCharShouldAppendCharToCharBuffer() {
        tokeniser.emit('a');

        assertEquals("a", tokeniser.charBuffer.toString());
    }

    @Test
    public void getStateShouldReturnCurrentTokeniserState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void transitionShouldSetTokeniserStateToGivenState() {
        tokeniser.transition(TokeniserState.ScriptDataEndTagName);

        assertEquals(TokeniserState.ScriptDataEndTagName, tokeniser.getState());
    }

    @Test
    public void advanceTransitionShouldAdvanceCharacterReaderAndSetTokeniserStateToGivenState() {
        StringReader reader = new StringReader("abc");
        tokeniser.reader = new CharacterReader(reader);

        tokeniser.advanceTransition(TokeniserState.CharacterReferenceInData);

        assertEquals('b', tokeniser.reader.current());
        assertEquals(TokeniserState.CharacterReferenceInData, tokeniser.getState());
    }

    @Test
    public void acknowledgeSelfClosingFlagShouldSetSelfClosingFlagAcknowledgedToTrue() {
        tokeniser.selfClosingFlagAcknowledged = false;

        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    // Test other methods...

}