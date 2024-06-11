package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

    private CharacterReader reader;
    private Tokeniser tokeniser;
    private List<ParseError> errors;

    @Before
    public void setup() {
        reader = new MockCharacterReader("");
        errors = new ArrayList<>();
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testReadEmpty() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadSingleCharacter() {
        reader.setCurrent('a');
        reader.mark(1); // mark position before setting character
        tokeniser.read();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.getString());
        assertEquals(reader.getCurrent(), 'a');
        reader.advance(); // advance to next character
    }

    @Test
    public void testReadMultipleCharacters() {
        reader.setCurrent('a');
        reader.mark(1); // mark position before setting characters
        tokeniser.emit("ab");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("ab", token.getString());
        assertEquals(reader.getCurrent(), 'c'); // next character should be skipped
        reader.advance(); // advance to next character
    }

    @Test
    public void testReadSelfClosingTag() {
        reader.setCurrent('<');
        reader.mark(1);
        tokeniser.read();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("<", token.getString());
        assertEquals(TokeniserState.TagStart, tokeniser.getState());

        reader.setCurrent('/');
        reader.mark(1);
        tokeniser.read();
        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("", token.getString());
        assertEquals(TokeniserState.InsideHTML, tokeniser.getState());

        reader.setCurrent('>');
        reader.mark(1);
        tokeniser.read();
        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("", token.getString());
        assertEquals(TokeniserState.InsideHTML, tokeniser.getState());
    }

    @Test
    public void testReadError() {
        reader.setCurrent('<');
        reader.mark(1);
        tokeniser.read();
        tokeniser.error("Unexpected character in input");
        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(1, errors.size());
        assertEquals("Unexpected character in input", errors.get(0).getMessage());
    }

    @Test
    public void testReadEOF() {
        reader.setCurrent('a');
        reader.mark(1);
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.eofError(TokeniserState.InsideHTML);
        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
    }

    @Test
    public void testConsumeCharacterReference() {
        reader.setCurrent('&');
        reader.mark(1);
        tokeniser.consumeCharacterReference(null, false);
        assertNull(tokeniser.read());

        reader.setCurrent('&amp;');
        reader.mark(5);
        tokeniser.consumeCharacterReference(null, false);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("&amp;", token.getString());

        reader.setCurrent('&apos;');
        reader.mark(4);
        tokeniser.consumeCharacterReference(Character.QUOTE, false);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("'", token.getString());

        reader.setCurrent('&apos;');
        reader.mark(4);
        tokeniser.consumeCharacterReference(Character.QUOTE, true); // in attribute
        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertEquals(Token.StartTag.class, tagPending.getClass());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        tagPending = null; // reset tagPending for next test
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertEquals(Token.Comment.class, commentPending.getClass());
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        commentPending = null; // reset commentPending for next test
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertEquals(Token.Doctype.class, doctypePending.getClass());
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
        doctypePending = null; // reset doctypePending for next test
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.transition(TokeniserState.TagStart);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isAppropriateEndTagToken());

        tokeniser.transition(TokeniserState.InsideHTML);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }
}

class MockCharacterReader implements CharacterReader {
    private StringBuilder buffer = new StringBuilder();
    private int pos = 0;

    @Override
    public char current() {
        return buffer.charAt(pos);
    }

    @Override
    public void mark(int readAheadLimit) {
        // no-op
    }

    @Override
    public void mark() {
        // no-op
    }

    @Override
    public void reset() {
        // no-op
    }

    @Override
    public boolean matches(char expected) {
        if (pos < buffer.length()) {
            return buffer.charAt(pos) == expected;
        }
        return false;
    }

    @Override
    public boolean matchesIgnoreCase(char expected) {
        if (pos < buffer.length()) {
            return Character.toUpperCase(buffer.charAt(pos)) == Character.toUpperCase(expected);
        }
        return false;
    }

    @Override
    public void matchConsume() {
        if (pos < buffer.length()) {
            pos++;
        }
    }

    @Override
    public void matchConsumeIgnoreCase() {
        if (pos < buffer.length()) {
            char c = buffer.charAt(pos);
            if (Character.toUpperCase(c) == Character.toUpperCase(expected)) {
                pos++;
            }
        }
    }

    @Override
    public void consume() {
        if (pos < buffer.length() - 1) {
            pos++;
        } else {
            buffer.setLength(pos);
        }
    }

    @Override
    public void consumeDigitSequence() {
        StringBuilder sequence = new StringBuilder();
        while (matchesAny(Character.isDigit(current()))) {
            sequence.append(current());
            consume();
        }
        buffer.setLength(pos - sequence.length());
        buffer.append(sequence);
    }

    @Override
    public void consumeLetterSequence() {
        StringBuilder sequence = new StringBuilder();
        while (matchesAny(Character.isLetter(current()))) {
            sequence.append(current());
            consume();
        }
        buffer.setLength(pos - sequence.length());
        buffer.append(sequence);
    }

    @Override
    public void consumeHexSequence() {
        StringBuilder sequence = new StringBuilder();
        boolean hexMode = false;
        while (matchesAny("Xx")) {
            char c = current();
            consume();
            if (hexMode) {
                int hex = Integer.parseInt(Character.toString(c), 16);
                buffer.append((char) hex);
            } else {
                hexMode = true;
            }
        }
        consume();
    }

    @Override
    public boolean matchesAny(char... expected) {
        if (pos < buffer.length()) {
            for (char expectedChar : expected) {
                if (buffer.charAt(pos) == expectedChar) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void advance() {
        if (pos < buffer.length() - 1) {
            pos++;
        } else {
            buffer.append(replacementChar);
        }
    }

    @Override
    public void mark(long readLimit) {
        // no-op
    }

    public void setCurrent(char c) {
        if (buffer.length() > pos) {
            buffer.setCharAt(pos, c);
        } else {
            buffer.append(c);
        }
        pos++;
    }

    @Override
    public boolean isEmpty() {
        return buffer.length() == pos;
    }
}