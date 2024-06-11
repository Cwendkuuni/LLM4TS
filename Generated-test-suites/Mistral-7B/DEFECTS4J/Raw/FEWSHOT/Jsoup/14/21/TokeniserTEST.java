package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private CharacterReader createReader(String input) {
        return new MockCharacterReader(input);
    }

    @Test
    public void testNormalFlow() {
        Tokeniser tokeniser = new Tokeniser(createReader(""));
        List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = tokeniser.read()) != null) {
            tokens.add(token);
        }
        assertEquals(0, tokens.size());

        tokeniser = new Tokeniser(createReader("foo"));
        tokens.clear();
        while ((token = tokeniser.read()) != null) {
            tokens.add(token);
        }
        assertEquals(1, tokens.size());
        Token firstToken = tokens.get(0);
        assertTrue(firstToken.type == Token.TokenType.Character);
        assertEquals("foo", firstToken.data);

        tokeniser = new Tokeniser(createReader("foo<bar>baz"));
        tokens.clear();
        while ((token = tokeniser.read()) != null) {
            tokens.add(token);
        }
        assertEquals(4, tokens.size());
        Token firstCharacterToken = tokens.get(0);
        Token barStartTag = tokens.get(1);
        Token barEndTag = tokens.get(3);
        assertTrue(firstCharacterToken.type == Token.TokenType.Character);
        assertEquals("foo", firstCharacterToken.data);
        assertTrue(barStartTag.type == Token.TokenType.StartTag);
        assertTrue(barEndTag.type == Token.TokenType.EndTag);
        assertEquals("bar", barStartTag.tagName);
    }

    @Test
    public void testReadAfterError() {
        Tokeniser tokeniser = new Tokeniser(createReader("foo<"));
        tokeniser.read(); // read "foo"
        tokeniser.error("Unexpected character in input: <");
        assertEquals(1, tokeniser.errors.size());

        Token token = tokeniser.read();
        assertNull(token);

        tokeniser = new Tokeniser(createReader("foo<"));
        tokeniser.transition(TokeniserState.Error);
        tokeniser.error("Unexpected character in input: <");
        assertEquals(1, tokeniser.errors.size());

        token = tokeniser.read();
        assertNull(token);

        tokeniser = new Tokeniser(createReader("foo<"));
        tokeniser.transition(TokeniserState.Error);
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.read(); // read "foo"
        tokeniser.error("Unexpected character in input: <");
        assertEquals(1, tokeniser.errors.size());

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(createReader("&foo;"));
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("foo", token.data);

        tokeniser = new Tokeniser(createReader("&foo;bar"));
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("foo", token.data);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("bar", token.data);

        tokeniser = new Tokeniser(createReader("&foo;"));
        tokeniser.transition(TokeniserState.Data);
        tokeniser.consumeCharacterReference(null, false);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("foo", token.data);

        tokeniser = new Tokeniser(createReader("&foo;"));
        tokeniser.transition(TokeniserState.AttributeValue);
        tokeniser.consumeCharacterReference(';', true);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("foo", token.data);

        tokeniser = new Tokeniser(createReader("&foo;"));
        tokeniser.transition(TokeniserState.AttributeValue);
        tokeniser.consumeCharacterReference(';', true);
        tokeniser.characterReferenceError();
        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testTag() {
        Tokeniser tokeniser = new Tokeniser(createReader("<bar>"));
        Token tagStart = tokeniser.read();
        assertTrue(tagStart.type == Token.TokenType.StartTag);
        assertTrue(tagStart.tagName.equals("bar"));

        tokeniser = new Tokeniser(createReader("<bar />"));
        Token tagStartSelfClosing = tokeniser.read();
        assertTrue(tagStartSelfClosing.type == Token.TokenType.StartTag);
        assertTrue(tagStartSelfClosing.selfClosing);
        assertTrue(tagStartSelfClosing.tagName.equals("bar"));

        tokeniser = new Tokeniser(createReader("<foo <bar"));
        tokeniser.read(); // read "<foo"
        tokeniser.error("Unexpected character in input: <");
        assertEquals(1, tokeniser.errors.size());

        tokeniser = new Tokeniser(createReader("<foo <bar"));
        tokeniser.transition(TokeniserState.Error);
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.read(); // read "<foo"
        tokeniser.error("Unexpected character in input: <");
        assertEquals(1, tokeniser.errors.size());

        tokeniser = new Tokeniser(createReader("<foo <bar"));
        tokeniser.transition(TokeniserState.Error);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testComment() {
        Tokeniser tokeniser = new Tokeniser(createReader("<!--"));
        Token commentStart = tokeniser.read();
        assertTrue(commentStart.type == Token.TokenType.Comment);

        tokeniser = new Tokeniser(createReader("<!-- --"));
        commentStart = tokeniser.read();
        assertTrue(commentStart.type == Token.TokenType.Comment);
        tokeniser.emitCommentPending();

        tokeniser = new Tokeniser(createReader("<!--"));
        tokeniser.transition(TokeniserState.Comment);
        tokeniser.createCommentPending();
        token = tokeniser.read();
        assertNull(token);
        tokeniser.emitCommentPending();
    }

    @Test
    public void testDoctype() {
        Tokeniser tokeniser = new Tokeniser(createReader("<!DOCTYPE html>"));
        Token doctype = tokeniser.read();
        assertTrue(doctype.type == Token.TokenType.Doctype);
        assertEquals("html", doctype.data);

        tokeniser = new Tokeniser(createReader("<!DOCTYPE html>"));
        tokeniser.transition(TokeniserState.Doctype);
        tokeniser.emitDoctypePending();
        token = tokeniser.read();
        assertNull(token);
        tokeniser.emitDoctypePending();
    }

    @Test
    public void testErrorHandling() {
        Tokeniser tokeniser = new Tokeniser(createReader("foo<"));
        tokeniser.read(); // read "foo"
        tokeniser.error("Unexpected character in input: <");
        assertEquals(1, tokeniser.errors.size());

        tokeniser = new Tokeniser(createReader("foo<"));
        tokeniser.transition(TokeniserState.Error);
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.read(); // read "foo"
        tokeniser.error("Unexpected character in input: <");
        assertEquals(1, tokeniser.errors.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testIsAppropriateEndTagTokenWithInvalidState() {
        Tokeniser tokeniser = new Tokeniser(createReader(""));
        tokeniser.isAppropriateEndTagToken();
    }

    @Test(expected = IllegalStateException.class)
    public void testIsAppropriateEndTagTokenWithInvalidTagPending() {
        Tokeniser tokeniser = new Tokeniser(createReader(""));
        tokeniser.tagPending = new Token.Comment();
        tokeniser.isAppropriateEndTagToken();
    }

    private static class MockCharacterReader implements CharacterReader {
        private String input;
        private int pos;
        private char current;

        public MockCharacterReader(String input) {
            this.input = input;
            this.pos = 0;
            this.current = input.charAt(pos);
        }

        @Override
        public char current() {
            return current;
        }

        @Override
        public void mark(int readAheadLimit) {
            // noop
        }

        @Override
        public void mark() {
            // noop
        }

        @Override
        public void rewind() {
            rewind(1);
        }

        @Override
        public void rewind(int offset) {
            pos -= offset;
            if (pos < 0) {
                throw new IndexOutOfBoundsException();
            }
            current = input.charAt(pos);
        }

        @Override
        public boolean matches(char c) {
            return current() == c;
        }

        @Override
        public boolean matchesAny(char... c) {
            for (char cc : c) {
                if (matches(cc)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean matchesIgnoreCase(String s) {
            return input.substring(pos, pos + s.length()).equalsIgnoreCase(s);
        }

        @Override
        public boolean matchesLetter() {
            return Character.isLetter(current());
        }

        @Override
        public boolean matchesDigit() {
            return Character.isDigit(current());
        }

        @Override
        public boolean isEmpty() {
            return pos >= input.length();
        }

        @Override
        public char consume() {
            char c = current();
            pos++;
            current = input.charAt(pos);
            return c;
        }

        @Override
        public char consumeHexSequence() {
            String hex = "";
            while (matchesAny('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')) {
                hex += consume();
            }
            return (char) Integer.parseInt(hex, 16);
        }

        @Override
        public char consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) {
            return Tokeniser.this.consumeCharacterReference(current(), inAttribute);
        }

        @Override
        public char consumeDigitSequence() {
            StringBuilder sb = new StringBuilder();
            while (matchesDigit()) {
                sb.append(consume());
            }
            return sb.toString().charAt(0);
        }

        @Override
        public char consumeLetterSequence() {
            StringBuilder sb = new StringBuilder();
            while (matchesLetter()) {
                sb.append(consume());
            }
            return sb.toString().charAt(0);
        }

        @Override
        public int pos() {
            return pos;
        }
    }
}