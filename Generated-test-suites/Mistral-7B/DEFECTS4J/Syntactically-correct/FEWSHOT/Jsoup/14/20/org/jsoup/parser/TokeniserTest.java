package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniser() {
        CharacterReader reader = new CharacterReader("") {
            @Override
            public char current() {
                return '\0';
            }

            @Override
            public int pos() {
                return 0;
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
            public boolean isEmpty() {
                return true;
            }

            @Override
            public boolean matches(char c) {
                return false;
            }

            @Override
            public boolean matchesAny(char... allowedChars) {
                return false;
            }

            @Override
            public boolean matchesIgnoreCase(String pattern) {
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
            public boolean matches(String pattern) {
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
            public String consumeHexSequence() {
                return "";
            }

            @Override
            public int consumeCharacterReference() {
                return -1;
            }
        };

        Tokeniser tokeniser = new Tokeniser(reader);

        // test default state
        assertEquals(TokeniserState.Data, tokeniser.getState());

        // test read with no pending tokens
        Token token = tokeniser.read();
        assertNull(token);

        // test emit with null token
        tokeniser.emit(null);

        // test emit with character token
        tokeniser.emit("a");
        token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("a", token.getData());

        // test emit with start tag token
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        token = tokeniser.read();
        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("<", token.getData().toString());

        // test emit with end tag token
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        token = tokeniser.read();
        assertEquals(Token.EndTag.class, token.getClass());
        assertEquals("</", token.getData().toString());

        // test emit and read string
        tokeniser.emit("abc");
        token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("a", token.getData());
        tokeniser.emit("def");
        token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("b", token.getData());
        token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("c", token.getData());
        token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("d", token.getData());
        token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("e", token.getData());
        token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("f", token.getData());

        // test consumeCharacterReference with single character
        tokeniser.emit("&#x1;");
        token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("\u0001", token.getData());

        // test consumeCharacterReference with single digits
        tokeniser.emit("&#1;");
        token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals('\u0001', token.getData().charAt(0));

        // test consumeCharacterReference with multi-digits
        tokeniser.emit("&#123;");
        token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("\u007b", token.getData());

        // test consumeCharacterReference with named entity
        tokeniser.emit("&amp;");
        token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("&", token.getData());

        // test consumeCharacterReference with invalid character reference
        tokeniser.emit("&#xyz;");
        token = tokeniser.read();
        assertNull(token);

        // test error with unexpected character
        tokeniser.emit("x");
        tokeniser.error(TokeniserState.ErrorHandling);
        token = tokeniser.read();
        assertNull(token);

        // test error with EOF
        tokeniser.emit("");
        tokeniser.eofError(TokeniserState.ErrorHandling);
        token = tokeniser.read();
        assertNull(token);

        // test character reference error
        tokeniser.emit("&#");
        tokeniser.characterReferenceError();
        token = tokeniser.read();
        assertNull(token);

        // test transition to new state
        tokeniser.transition(TokeniserState.StartTagBegin);
        assertEquals(TokeniserState.StartTagBegin, tokeniser.getState());

        // test acknowledge self-closing flag
        tokeniser.acknowledgeSelfClosingFlag();
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
    }
}