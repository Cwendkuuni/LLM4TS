package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testTokeniser() {
        CharacterReader reader = new CharacterReader("") {
            @Override
            public char current() {
                return '\0';
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public int pos() {
                return 0;
            }

            @Override
            public void advance() {
            }

            @Override
            public boolean matches(char expected) {
                return false;
            }

            @Override
            public boolean matchesAny(char... expectedChars) {
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
            public boolean markSupported() {
                return false;
            }

            @Override
            public void mark(int readAheadLimit) {
            }

            @Override
            public void reset() {
            }

            @Override
            public void consume(int numChars) {
            }

            @Override
            public void consumeLetterSequence() {
            }

            @Override
            public void consumeDigitSequence() {
            }

            @Override
            public String consumeHexSequence() {
                return "";
            }

            @Override
            public void unconsume() {
            }

            @Override
            public boolean consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) {
                return false;
            }
        };

        Tokeniser tokeniser = new Tokeniser(reader);

        // Test empty constructor
        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertFalse(tokeniser.isEmitPending());
        assertNull(tokeniser.emitPending);
        assertEquals(0, tokeniser.errors.size());

        // Transition to different states
        tokeniser.transition(TokeniserState.AfterName);
        assertEquals(TokeniserState.AfterName, tokeniser.getState());
        tokeniser.transition(TokeniserState.Data);
        assertEquals(TokeniserState.Data, tokeniser.getState());

        // Test read method
        tokeniser.transition(TokeniserState.AfterName);
        tokeniser.reader.mark();
        tokeniser.reader.consumeLetterSequence();
        tokeniser.reader.unconsume();
        tokeniser.advanceTransition(TokeniserState.AfterAttribute);
        tokeniser.emit('>');
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("", token.data);

        // Test emit method
        tokeniser.emit(new Token.Character('a'));
        tokeniser.emit(new Token.Character('b'));
        List<Token> tokens = new ArrayList<>();
        tokens.add(tokeniser.emitPending);
        assertEquals(2, tokens.size());

        // Test error handling
        tokeniser.transition(TokeniserState.Data);
        tokeniser.error("Unexpected character in input");
        assertTrue(tokeniser.errors.size() > 0);
        assertEquals("Unexpected character in input", tokeniser.errors.get(0).getMessage());
        assertEquals(-1, tokeniser.errors.get(0).getLineNumber());
        assertEquals(-1, tokeniser.errors.get(0).getColumnNumber());

        tokeniser.errors.clear();
        tokeniser.eofError(TokeniserState.Data);
        assertTrue(tokeniser.errors.size() > 0);
        assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.errors.get(0).getMessage());
        assertEquals(-1, tokeniser.errors.get(0).getLineNumber());
        assertEquals(-1, tokeniser.errors.get(0).getColumnNumber());
    }
}