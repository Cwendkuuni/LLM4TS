package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {
    @Test
    public void testRead() {
        CharacterReader reader = new MockCharacterReader("abcdef<tag>");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("b", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("tag", token.data.tagName);
        assertNull(token.data.attributes);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testEmit() {
        CharacterReader reader = new MockCharacterReader("abcdef<tag>");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        tokeniser.emit(new Token.Character('a'));
        tokeniser.emit(new Token.Character('b'));
        Token startTag = new Token.StartTag("tag");
        tokeniser.emit(startTag);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("c", token.data.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("d", token.data.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("tag", token.data.tagName);
        assertNull(token.data.attributes);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testTransition() {
        CharacterReader reader = new MockCharacterReader("abcdef<tag>");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());
        TokeniserState initialState = tokeniser.getState();

        tokeniser.transition(TokeniserState.TagName);
        assertEquals(TokeniserState.TagName, tokeniser.getState());

        tokeniser.transition(TokeniserState.InTag);
        assertEquals(TokeniserState.InTag, tokeniser.getState());

        tokeniser.transition(TokeniserState.Attributes);
        assertEquals(TokeniserState.Attributes, tokeniser.getState());

        tokeniser.transition(TokeniserState.BeforeTagEnd);
        assertEquals(TokeniserState.BeforeTagEnd, tokeniser.getState());

        tokeniser.transition(TokeniserState.Data);
        assertEquals(TokeniserState.Data, tokeniser.getState());

        tokeniser.transition(TokeniserState.Script);
        assertEquals(TokeniserState.Script, tokeniser.getState());

        tokeniser.transition(initialState);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new MockCharacterReader("&a;&b;&#160;&#x1F600;&#x123456;");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        Character reference = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\u0061', reference);

        reference = tokeniser.consumeCharacterReference('a', false);
        assertEquals('\u0061', reference);

        reference = tokeniser.consumeCharacterReference(null, true);
        assertNull(reference);

        reference = tokeniser.consumeCharacterReference("aaa", true);
        assertNull(reference);

        reference = tokeniser.consumeCharacterReference("abc", false);
        assertNull(reference);

        reference = tokeniser.consumeCharacterReference("&A", false);
        assertEquals('A', reference);

        reference = tokeniser.consumeCharacterReference("&#160;", false);
        assertEquals('\u00A0', reference);

        reference = tokeniser.consumeCharacterReference("&#x1F600;", false);
        assertEquals('\uD835\uDD33', reference);

        reference = tokeniser.consumeCharacterReference("&&#x123456;", false);
        assertEquals('\u123456', reference);

        reference = tokeniser.consumeCharacterReference("&xyz;", false);
        assertNull(reference);
    }

    @Test
    public void testCharacterReferenceError() {
        CharacterReader reader = new MockCharacterReader("&invalid;");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        tokeniser.consumeCharacterReference(null, false);
        tokeniser.errors.assertErrorCount(1);
        assertEquals("Invalid character reference: unexpected character 'i' in input state [Data]", tokeniser.errors.getError(0).getMessage());
    }

    @Test
    public void testCreateTagPending() {
        CharacterReader reader = new MockCharacterReader("<tag>");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        Token tagPending = tokeniser.createTagPending(true);
        assertEquals(Token.StartTag.class, tagPending.getClass());
        assertEquals("tag", tagPending.tagName);
        assertNull(tagPending.attributes);

        tagPending = tokeniser.createTagPending(false);
        assertEquals(Token.EndTag.class, tagPending.getClass());
        assertNull(tagPending.tagName);
        assertNull(tagPending.attributes);
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader reader = new MockCharacterReader("<tag>");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        Token tagPending = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("tag", token.data.tagName);
        assertNull(token.data.attributes);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testCreateCommentPending() {
        CharacterReader reader = new MockCharacterReader("<!--comment-->");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        tokeniser.createCommentPending();
        Token commentPending = tokeniser.commentPending;

        tokeniser.emitCommentPending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("comment", token.data.commentOrScript.data);
        assertEquals(Token.Comment.class, token.data.commentOrScript.getClass());

        token = tokeniser.read();
        assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testEmitCommentPendingWithoutCommentClosing() {
        CharacterReader reader = new MockCharacterReader("<!--");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        tokeniser.errors.assertErrorCount(1);
        assertEquals("Missing comment closing '--'> in input state [Data]", tokeniser.errors.getError(0).getMessage());
    }

    @Test
    public void testCreateDoctypePending() {
        CharacterReader reader = new MockCharacterReader("<!DOCTYPE html>");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        tokeniser.createDoctypePending();
        Token doctypePending = tokeniser.doctypePending;

        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("html", token.data.publicId);
        assertEquals(null, token.data.systemId);
        assertEquals(null, token.data.internalSubset);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader reader = new MockCharacterReader("<tag1><tag2></tag2></tag1>");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        assertTrue(tokeniser.isAppropriateEndTagToken());

        tokeniser.transition(TokeniserState.BeforeTagEnd);
        assertFalse(tokeniser.isAppropriateEndTagToken());

        tokeniser.transition(TokeniserState.Data);
        assertFalse(tokeniser.isAppropriateEndTagToken());

        tokeniser.transition(TokeniserState.Script);
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        CharacterReader reader = new MockCharacterReader("<tag1><tag2></tag2></tag1>");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        assertEquals("tag1", tokeniser.appropriateEndTagName());

        tokeniser.transition(TokeniserState.BeforeTagEnd);
        assertNull(tokeniser.appropriateEndTagName());

        tokeniser.transition(TokeniserState.Data);
        assertNull(tokeniser.appropriateEndTagName());

        tokeniser.transition(TokeniserState.Script);
        assertNull(tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        CharacterReader reader = new MockCharacterReader("<tag>");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        tokeniser.error(TokeniserState.Data);
        tokeniser.errors.assertErrorCount(1);
        assertEquals("Unexpected character '<' in input state [Data]", tokeniser.errors.getError(0).getMessage());
    }

    @Test
    public void testEofError() {
        CharacterReader reader = new MockCharacterReader("abc");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        tokeniser.eofError(TokeniserState.Data);
        tokeniser.errors.assertErrorCount(1);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", tokeniser.errors.getError(0).getMessage());
    }
}

class MockCharacterReader implements CharacterReader {
    private String input;
    private int index;

    public MockCharacterReader(String input) {
        this.input = input;
        this.index = 0;
    }

    @Override
    public int read() throws IOException {
        if (index < input.length()) {
            return (int) input.charAt(index++);
        } else {
            return -1;
        }
    }

    @Override
    public int read(char[] cbuf, int off, int length) throws IOException {
        if (index + length > input.length()) {
            length = input.length() - index;
        }
        System.arraycopy(input.substring(index, index + length).toCharArray(), off, cbuf, off, length);
        index += length;
        return length;
    }

    @Override
    public boolean isBig5() {
        return false;
    }

    @Override
    public boolean isReadAhead() {
        return false;
    }

    @Override
    public boolean isUTF8() {
        return true;
    }

    @Override
    public void setErrorHandlingPolicy(ErrorHandlingPolicy policy) {
        // empty
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        // empty
    }

    @Override
    public void mark() throws IOException {
        mark(DEFAULT_READ_AHEAD_LIMIT);
    }

    @Override
    public void reset() throws IOException {
        index = 0;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void close() throws IOException {
        // empty
    }

    @Override
    public void advance() throws IOException {
        if (index < input.length()) {
            index++;
        }
    }

    @Override
    public int pos() {
        return index;
    }

    @Override
    public boolean matches(int c) {
        if (index < input.length() && input.charAt(index) == (char) c) {
            index++;
            return true;
        }
        return false;
    }

    @Override
    public boolean matches(String s) {
        int startIndex = index;
        return input.regionMatches(true, startIndex, s, 0, s.length()) && index + s.length() <= input.length();
    }

    @Override
    public boolean matchesAny(char... chars) {
        for (char c : chars) {
            if (matches(c)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean matchesLetter() {
        return matchesAny("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    @Override
    public boolean matchesDigit() {
        return matchesAny("0123456789");
    }

    @Override
    public boolean isEmpty() {
        return input.length() == 0;
    }

    @Override
    public char current() {
        if (index < input.length()) {
            return input.charAt(index);
        }
        return '\u0000';
    }

    @Override
    public void consume() throws IOException {
        if (index < input.length()) {
            index++;
        }
    }

    @Override
    public void consumeHexSequence() {
        String hex = input.substring(index, index + 2);
        index += 2;
        try {
            int hexValue = Integer.parseInt(hex, 16);
            consume();
            consume();
            emit((char) hexValue);
        } catch (NumberFormatException e) {
            consume();
        }
    }

    @Override
    public void consumeDigitSequence() {
        String digits = input.substring(index, index + Character.isDigit(input.charAt(index)) ? Character.digit(input.charAt(index)) + 1 : input.length() - index);
        index += digits.length();
        try {
            int numberValue = Integer.parseInt(digits);
            emit((char) numberValue);
        } catch (NumberFormatException e) {
            // empty
        }
    }

    @Override
    public void consumeLetterSequence() {
        String letters = input.substring(index, index + Character.isLetter(input.charAt(index)) ? Character.length(input.substring(index, index + Character.length(input.substring(index, index + Character.isLetter(input.charAt(index))) + 1))) + 1 : input.length() - index);
        index += letters.length();
        emit(letters.charAt(0));
    }

    @Override
    public void consume(int n) {
        for (int i = 0; i < n; i++) {
            consume();
        }
    }

    @Override
    public void unconsume() {
        index--;
    }

    @Override
    public List<ParseError> getErrors() {
        return tokeniser.errors;
    }

    @Override
    public void mark(int readAheadLimit, int pos) {
        // empty
    }

    @Override
    public void consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) {
        tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
    }
}