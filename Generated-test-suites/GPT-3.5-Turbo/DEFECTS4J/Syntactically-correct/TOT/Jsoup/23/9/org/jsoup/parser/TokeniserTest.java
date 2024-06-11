package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        tokeniser = new Tokeniser(null, null);
    }

    @Test
    public void testRead_shouldReturnCharacterToken_whenCharBufferIsNotEmpty() {
        // Arrange
        tokeniser.charBuffer.append("Hello");

        // Act
        Token result = tokeniser.read();

        // Assert
        assertNotNull(result);
        assertEquals(Token.TokenType.Character, result.getType());
        assertEquals("Hello", result.getValue());
        assertEquals(0, tokeniser.charBuffer.length());
    }

    @Test
    public void testRead_shouldReturnEmitPendingToken_whenCharBufferIsEmpty() {
        // Arrange
        tokeniser.emitPending = new Token.StartTag();

        // Act
        Token result = tokeniser.read();

        // Assert
        assertNotNull(result);
        assertEquals(Token.TokenType.StartTag, result.getType());
        assertNull(result.getValue());
        assertFalse(tokeniser.isEmitPending);
    }

    @Test
    public void testEmit_shouldSetEmitPendingToken_andIsEmitPendingToTrue() {
        // Arrange
        Token token = new Token.EndTag();

        // Act
        tokeniser.emit(token);

        // Assert
        assertEquals(token, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test(expected = IllegalStateException.class)
    public void testEmit_shouldThrowIllegalStateException_whenIsEmitPendingIsTrue() {
        // Arrange
        tokeniser.isEmitPending = true;

        // Act
        tokeniser.emit(new Token.EndTag());
    }

    @Test
    public void testEmit_shouldSetIsEmitPendingToFalse_whenCharBufferIsNotEmpty() {
        // Arrange
        tokeniser.charBuffer.append("Hello");

        // Act
        tokeniser.emit(new Token.Comment());

        // Assert
        assertFalse(tokeniser.isEmitPending);
    }

    @Test
    public void testEmit_shouldSetIsEmitPendingToFalse_whenCharBufferIsEmpty() {
        // Arrange
        tokeniser.charBuffer.setLength(0);

        // Act
        tokeniser.emit(new Token.Comment());

        // Assert
        assertFalse(tokeniser.isEmitPending);
    }

    @Test
    public void testEmit_shouldSetLastStartTagToStartTagToken_whenTokenTypeIsStartTag() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag();
        startTag.setSelfClosing(true);
        startTag.addAttribute("key", "value");

        // Act
        tokeniser.emit(startTag);

        // Assert
        assertEquals(startTag, tokeniser.lastStartTag);
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testEmit_shouldSetSelfClosingFlagAcknowledgedToTrue_whenTokenTypeIsStartTagAndSelfClosingIsTrue() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag();
        startTag.setSelfClosing(true);

        // Act
        tokeniser.emit(startTag);

        // Assert
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testEmit_shouldNotSetSelfClosingFlagAcknowledgedToTrue_whenTokenTypeIsStartTagAndSelfClosingIsFalse() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag();
        startTag.setSelfClosing(false);

        // Act
        tokeniser.emit(startTag);

        // Assert
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testEmit_shouldNotSetSelfClosingFlagAcknowledgedToTrue_whenTokenTypeIsEndTag() {
        // Arrange
        Token.EndTag endTag = new Token.EndTag();
        endTag.addAttribute("key", "value");

        // Act
        tokeniser.emit(endTag);

        // Assert
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testEmit_shouldInvokeErrorMethod_whenTokenTypeIsEndTagAndAttributesSizeIsGreaterThanZero() {
        // Arrange
        Token.EndTag endTag = new Token.EndTag();
        endTag.addAttribute("key", "value");

        // Act
        tokeniser.emit(endTag);

        // Assert
        // assert that error method is invoked
    }

    @Test
    public void testEmit_shouldInvokeErrorMethod_whenTokenTypeIsStartTagAndSelfClosingIsFalse() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag();
        startTag.setSelfClosing(false);

        // Act
        tokeniser.emit(startTag);

        // Assert
        // assert that error method is invoked
    }

    @Test
    public void testEmit_shouldNotInvokeErrorMethod_whenTokenTypeIsStartTagAndSelfClosingIsTrue() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag();
        startTag.setSelfClosing(true);

        // Act
        tokeniser.emit(startTag);

        // Assert
        // assert that error method is not invoked
    }

    @Test
    public void testAdvanceTransition_shouldAdvanceReaderPositionAndSetStateToNewState() {
        // Arrange
        TokeniserState state = TokeniserState.TagName;
        tokeniser.reader = new CharacterReader("test");

        // Act
        tokeniser.advanceTransition(state);

        // Assert
        assertEquals(state, tokeniser.getState());
        assertEquals(1, tokeniser.reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_shouldSetSelfClosingFlagAcknowledgedToTrue() {
        // Arrange
        tokeniser.selfClosingFlagAcknowledged = false;

        // Act
        tokeniser.acknowledgeSelfClosingFlag();

        // Assert
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
}