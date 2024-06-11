package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import static org.jsoup.parser.Tokeniser.replacementChar;
import static org.jsoup.parser.Tokeniser.TokenType.*;

import org.jsoup.nodes.Entities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private CharacterReader getTestInputStream(String input) {
        return new CharacterReader() {
            private int index = 0;
            private final InputStream inputStream = new ByteArrayInputStream(input.getBytes());

            @Override
            public int read() throws IOException {
                return index < input.length() ? input.charAt(index++) : -1;
            }

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                if (len > input.length() - index)
                    throw new IOException("Length exceeds input size");
                System.arraycopy(input.substring(index, index + len).toCharArray(), 0, cbuf, off, len);
                index += len;
                return len;
            }

            @Override
            public boolean isEmpty() {
                return index >= input.length();
            }

            @Override
            public char current() {
                return (char) reader.read();
            }

            @Override
            public void mark(int readAheadLimit) {
            }

            @Override
            public void mark() {
            }

            @Override
            public void reset() {
                index--;
            }

            @Override
            public void close() throws IOException {
                inputStream.close();
            }
        };
    }

    @Test
    public void testRead() {
        CharacterReader reader = getTestInputStream("abc");
        Tokeniser tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();
        assertEquals(CHARACTER, token.type);
        assertEquals('a', token.character);

        token = tokeniser.read();
        assertEquals(CHARACTER, token.type);
        assertEquals('b', token.character);

        token = tokeniser.read();
        assertEquals(CHARACTER, token.type);
        assertEquals('c', token.character);

        token = tokeniser.read();
        assertEquals(EOF, token.type);

        assertEquals(3, tokeniser.errors.size());
    }

    @Test
    public void testEmit() {
        CharacterReader reader = getTestInputStream("a<b>");
        Tokeniser tokeniser = new Tokeniser(reader);

        tokeniser.emit(new Token.Character('a'));
        Token tagStart = tokeniser.emit(new Token.StartTag("b"));
        Token tagEnd = tokeniser.read();

        assertEquals(3, tokeniser.errors.size());
        assertEquals(tagStart.tagName, tagEnd.tagName);
        assertEquals(START_TAG, tagStart.type);
        assertEquals(END_TAG, tagEnd.type);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = getTestInputStream("&aaa;");
        Tokeniser tokeniser = new Tokeniser(reader);

        Character c = tokeniser.consumeCharacterReference(null, false);
        assertEquals((char) 65, c);

        c = tokeniser.consumeCharacterReference('a', false);
        assertEquals((char) 65, c);

        c = tokeniser.consumeCharacterReference(null, true);
        assertEquals(replacementChar, c);

        c = tokeniser.consumeCharacterReference("AA", true);
        assertEquals('A', c);

        assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader reader = getTestInputStream("<a><b></b></a>");
        Tokeniser tokeniser = new Tokeniser(reader);

        Token tagStartA = tokeniser.read();
        Token tagStartB = tokeniser.read();
        Token tagEndB = tokeniser.read();
        Token tagEndA = tokeniser.read();

        assertTrue(tokeniser.isAppropriateEndTagToken());
        assertEquals(tagStartA.tagName, tagEndA.tagName);
    }

    @Test
    public void testCreateTagPending() {
        CharacterReader reader = getTestInputStream("<a><b></b></a>");
        Tokeniser tokeniser = new Tokeniser(reader);

        Token tagStartA = tokeniser.read();
        Token tagStartB = tokeniser.read();
        Token tagEndB = tokeniser.read();
        Token tagEndA = tokeniser.read();

        tokeniser.emitTagPending();

        assertEquals(tagStartA.tagName, tagPending.tagName);
        assertEquals(START_TAG, tagPending.type);

        tokeniser.emitCommentPending();
        Token comment = tokeniser.read();
        assertEquals(COMMENT, comment.type);

        assertEquals(0, tokeniser.errors.size());
    }
}