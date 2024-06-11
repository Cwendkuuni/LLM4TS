package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Tokeniser.Token;
import org.jsoup.parser.Tokeniser.TokenType;
import org.jsoup.helper.Validate;

import java.util.Arrays;

import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new StringReader(""), new ParseErrorList());
        Token.StartTag startTag = tokeniser.createTagPending(true);
        Token.EndTag endTag = tokeniser.createTagPending(false);
        assertEquals(TokenType.StartTag, startTag.type);
        assertEquals(TokenType.EndTag, endTag.type);
        assertEquals("", startTag.tagName);
        assertEquals("", endTag.tagName);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new StringReader(""), new ParseErrorList());
        Token.Comment comment = tokeniser.createCommentPending();
        assertEquals(TokenType.Comment, comment.type);
        assertEquals("", comment.text());
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new StringReader(""), new ParseErrorList());
        Token.Doctype doctype = tokeniser.createDoctypePending();
        assertEquals(TokenType.Doctype, doctype.type);
        assertNull(doctype.data());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new StringReader(""), new ParseErrorList());
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emitTagPending();
        tokeniser.transition(TokeniserState.Character);
        tokeniser.emit("</div>");
        tokeniser.emitTagPending();
        tokeniser.emitTagPending();
        tokeniser.emit(new Token.StartTag("div"));
        tokeniser.emit(new Token.EndTag("div"));
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(new StringReader(""), new ParseErrorList()) {
            @Override
            public char current() {
                throw new IllegalStateException();
            }
        };
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.error(TokeniserState.StartTag);
        try {
            tokeniser.emitTagPending();
            Validate.fail("Expected ParseError to be thrown");
        } catch (ParseError e) {
            // expected
        }
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(new StringReader(""), new ParseErrorList());
        int[] result = tokeniser.consumeCharacterReference((char) 0, false);
        assertNull(result);
        result = tokeniser.consumeCharacterReference('#', false);
        assertNull(result);
        result = tokeniser.consumeCharacterReference("&apos;", false);
        assertNull(result);
        result = tokeniser.consumeCharacterReference("&#x1F600;", false);
        assertNull(result);
        result = tokeniser.consumeCharacterReference("&#x1F601;", false);
        assertArrayEquals(new int[]{0x1F601}, result);
        result = tokeniser.consumeCharacterReference("&#x1F602;&#x1F603;", false);
        assertArrayEquals(new int[]{0x1F602, 0x1F603}, result);
        result = tokeniser.consumeCharacterReference("&nbsp;", false);
        assertArrayEquals(new int[]{0x00A0}, result);
        result = tokeniser.consumeCharacterReference("&iexcl;", false);
        assertArrayEquals(new int[]{0x00A1}, result);
        result = tokeniser.consumeCharacterReference("&quot;", false);
        assertArrayEquals(new int[]{0x0022}, result);
        result = tokeniser.consumeCharacterReference("&amp;", false);
        assertArrayEquals(new int[]{0x0026}, result);
        result = tokeniser.consumeCharacterReference("&lt;", false);
        assertArrayEquals(new int[]{0x003C}, result);
        result = tokeniser.consumeCharacterReference("&gt;", false);
        assertArrayEquals(new int[]{0x003E}, result);
        result = tokeniser.consumeCharacterReference("&#128;", false);
        assertArrayEquals(new int[]{0x80}, result);
        result = tokeniser.consumeCharacterReference("&#65535;", false);
        assertArrayEquals(new int[]{0xFFFF}, result);
        result = tokeniser.consumeCharacterReference("&#x10FFFF;", false);
        assertArrayEquals(new int[]{0x10FFFF}, result);
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(new StringReader(""), new ParseErrorList());
        tokeniser.emit(new Token.Character('a'));
        tokeniser.emit(new Token.Character('b'));
        tokeniser.emit(new Token.Character('c'));
        StringBuilder expected = new StringBuilder();
        expected.append('a').append('b').append('c');
        String actual = tokeniser.unescapeEntities(false);
        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(new StringReader(""), new ParseErrorList());
        Token token = tokeniser.read();
        assertTrue(token.type == TokenType.Character || token.type == TokenType.EndTag || token.type == TokenType.StartTag || token.type == TokenType.Doctype);
        token = tokeniser.read();
        assertTrue(token.type == TokenType.Character || token.type == TokenType.EndTag || token.type == TokenType.StartTag || token.type == TokenType.Doctype);
        token = tokeniser.read();
        assertTrue(token.type == TokenType.Character || token.type == TokenType.EndTag || token.type == TokenType.StartTag || token.type == TokenType.Doctype);
        tokeniser.acknowledgeSelfClosingFlag();
        token = tokeniser.read();
        assertTrue(token.type == TokenType.Character || token.type == TokenType.EndTag || token.type == TokenType.StartTag || token.type == TokenType.Doctype);
    }

    @Test
    public void testTransition() {
        Tokeniser tokeniser = new Tokeniser(new StringReader(""), new ParseErrorList());
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emitTagPending();
        tokeniser.transition(TokeniserState.Character);
        tokeniser.emit("abc");
        tokeniser.transition(TokeniserState.EndTag);
        tokeniser.emitTagPending();
        tokeniser.transition(TokeniserState.Character);
        tokeniser.emit("def");
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.transition(TokeniserState.Character);
        tokeniser.emit("ghi");
        tokeniser.transition(TokeniserState.EndTag);
        tokeniser.emitTagPending();
        tokeniser.transition(TokeniserState.Character);
        tokeniser.emit("jkl");
    }
}

class StringReader implements CharacterReader {

    private final String content;
    private int pos = 0;

    public StringReader(String content) {
        this.content = content;
    }

    @Override
    public char current() {
        return content.charAt(pos);
    }

    @Override
    public boolean isEmpty() {
        return pos >= content.length();
    }

    @Override
    public void mark(int readAheadLimit) {
        // not implemented
    }

    @Override
    public void release(int readAheadLimit) {
        // not implemented
    }

    @Override
    public void advance() {
        pos++;
    }

    @Override
    public boolean matches(char expected) {
        return current() == expected;
    }

    @Override
    public boolean matchesAny(char... chars) {
        return Arrays.stream(chars).anyMatch(this::matches);
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
    public boolean matches(char[] expected) {
        int len = expected.length;
        for (int i = 0; i < len; i++) {
            if (current() != expected[i]) {
                return false;
            }
            advance();
        }
        return true;
    }

    @Override
    public boolean matchesIgnoreCase(String regex) {
        return content.matches(regex);
    }

    @Override
    public void consume(int length) {
        pos += length;
    }

    @Override
    public void consumeLetterThenDigitSequence() {
        StringBuilder sb = new StringBuilder();
        while (Character.isAlphabetic(current())) {
            sb.append(current());
            advance();
        }
        while (Character.isDigit(current())) {
            sb.append(current());
            advance();
        }
        if (sb.length() > 0) {
            consume(sb.length());
        }
    }

    @Override
    public void consumeDigitSequence() {
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(current())) {
            sb.append(current());
            advance();
        }
        if (sb.length() > 0) {
            consume(sb.length());
        }
    }

    @Override
    public void consumeTo(char end) {
        StringBuilder sb = new StringBuilder();
        while (current() != end) {
            sb.append(current());
            advance();
        }
        if (sb.length() > 0) {
            consume(sb.length());
        }
    }

    @Override
    public void consumeHexSequence() {
        StringBuilder sb = new StringBuilder();
        boolean firstHex = true;
        while (Character.isHexDigit(current())) {
            if (firstHex) {
                firstHex = false;
            } else {
                sb.append("0x");
            }
            sb.append(current());
            advance();
        }
        consume(Math.max(sb.length() - 1, 0));
        consume(1);
    }
}