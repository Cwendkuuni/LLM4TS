package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

public class TokeniserTest {

    @Test
    public void testTokeniser() {
        CharacterReader reader = new CharacterReader("") {
            @Override
            public char current() {
                return ' ';
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public void advance() {
                // no-op
            }

            @Override
            public void mark(int readAheadLimit) {
                // no-op
            }

            @Override
            public void rewindToMark() {
                // no-op
            }

            @Override
            public boolean matches(char expected) {
                return false;
            }

            @Override
            public boolean matchesAny(char... expected) {
                return false;
            }

            @Override
            public boolean matchesLetter() {
                return false;
            }

            @Override
            public boolean matchesDigit() {
                return false;
            }

            @Override
            public boolean matchesIgnoreCase(String expected) {
                return false;
            }

            @Override
            public String consumeLetterSequence() {
                return "";
            }

            @Override
            public String consumeDigitSequence() {
                return "";
            }

            @Override
            public boolean consumeCharacterReference() {
                return false;
            }

            @Override
            public String consumeHexSequence() {
                return "";
            }
        };

        Tokeniser tokeniser = new Tokeniser(reader);

        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertFalse(tokeniser.isEmitPending());
        assertNull(tokeniser.emitPending);
        assertNull(tokeniser.charBuffer.toString());
        assertNull(tokeniser.dataBuffer);
        assertNull(tokeniser.tagPending);
        assertNull(tokeniser.doctypePending);
        assertNull(tokeniser.commentPending);
        assertNull(tokeniser.lastStartTag);
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
        assertTrue(tokeniser.errors.isEmpty());

        tokeniser.transition(TokeniserState.Script);
        assertEquals(TokeniserState.Script, tokeniser.getState());

        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.acknowledgeSelfClosingFlag();
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);

        tokeniser.emit("foo");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("foo", token.data);
        assertEquals("foo", tokeniser.charBuffer.toString());
        assertNull(tokeniser.emitPending);

        tokeniser.emit("\n");
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("TokeniserTest$Token", token.tagName);
        assertEquals(false, token.selfClosing);
        tokeniser.emitTagPending();

        tokeniser.emit("<script>");
        tokeniser.createTempBuffer();
        tokeniser.emit(reader.current());
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.emitDoctypePending();

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("script", token.tagName);
        assertEquals(false, token.selfClosing);

        tokeniser.emit("</script>");
        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("script", token.tagName);
        assertEquals(true, token.selfClosing);

        tokeniser.emit("</script>");
        token = tokeniser.read();
        assertEquals(Token.TokenType.Error, token.type);
        assertTrue(token.errorMessage.contains("Unexpected character in input"));

        tokeniser.setTrackErrors(false);
        tokeniser.error("Unexpected character in input");
        assertEquals(0, tokeniser.errors.size());
    }
}