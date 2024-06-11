package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testRead() {
        CharacterReader reader = new MockCharacterReader("abcdefg<tag>");
        Tokeniser tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.text);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("b", token.text);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("tag", token.tagName);
        assertFalse(token.selfClosing);

        token = tokeniser.read(); // should be null, since there are no more characters to read
        assertNull(token);
    }

    @Test
    public void testEmit() {
        CharacterReader reader = new MockCharacterReader("abcdefg<tag>");
        Tokeniser tokeniser = new Tokeniser(reader);

        tokeniser.emit(new Token.Character('a'));
        tokeniser.emit(new Token.Character('b'));

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("c", token.text);

        tokeniser.emit(new Token.StartTag("tag"));

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("tag", token.tagName);
        assertFalse(token.selfClosing);

        token = tokeniser.read(); // should be null, since there are no more characters to read
        assertNull(token);
    }

    @Test
    public void testReadAndEmitCharacter() {
        CharacterReader reader = new MockCharacterReader("abc");
        Tokeniser tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("b", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("c", token.text);

        tokeniser.emit(token);

        token = tokeniser.read(); // should be null, since there are no more characters to read
        assertNull(token);
    }

    @Test
    public void testReadAndEmitTag() {
        CharacterReader reader = new MockCharacterReader("</tag>");
        Tokeniser tokeniser = new Tokeniser(reader);

        Token startTag = tokeniser.emit(new Token.StartTag("tag"));
        assertFalse(startTag.selfClosing);

        Token endTag = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertEquals("tag", endTag.tagName);
        assertTrue(endTag.selfClosing);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new MockCharacterReader("&#123;");
        Tokeniser tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\uFFFD', character.intValue());

        character = tokeniser.consumeCharacterReference('\u0060', false);
        assertEquals('\u0060', character.intValue());

        reader.setCurrentPosition(0);
        reader.setCurrentCharacter('\u0061');
        tokeniser.consumeCharacterReference('\u0062', false);
        assertEquals('\u0061', reader.current());

        character = tokeniser.consumeCharacterReference("foo", false);
        assertEquals(Entities.getCharacterByName("foo"), character);

        reader.setCurrentPosition(0);
        reader.setCurrentCharacter('&');
        reader.consume();
        tokeniser.consumeCharacterReference("bar", true);
        assertEquals(Entities.getCharacterByName("bar"), tokeniser.lastStartTag.attributes.get("&"));
    }

    @Test
    public void testCreateCommentPending() {
        CharacterReader reader = new MockCharacterReader("<!--comment-->");
        Tokeniser tokeniser = new Tokeniser(reader);

        tokeniser.createCommentPending();
        Token comment = tokeniser.emitCommentPending();
        assertEquals(Token.TokenType.Comment, comment.type);
        assertEquals("comment", comment.text);
    }

    @Test
    public void testCreateDoctypePending() {
        CharacterReader reader = new MockCharacterReader("<!DOCTYPE html>");
        Tokeniser tokeniser = new Tokeniser(reader);

        tokeniser.createDoctypePending();
        Token doctype = tokeniser.emitDoctypePending();
        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertEquals("html", doctype.publicId);
        assertEquals("", doctype.systemId);
    }

    @Test
    public void testTrackErrors() {
        CharacterReader reader = new MockCharacterReader("abcdefg<tag>");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.setTrackErrors(false);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("b", token.text);

        tokeniser.emit(new Token.StartTag("tag"));

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("tag", token.tagName);
        assertTrue(token.selfClosing);

        token = tokeniser.read(); // should be null, since there are no more characters to read
        assertNull(token);

        assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testUnexpectedCharacter() {
        CharacterReader reader = new MockCharacterReader("abc<!");
        Tokeniser tokeniser = new Tokeniser(reader);

        tokeniser.read();
        tokeniser.read();
        tokeniser.error(TokeniserState.Character);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeCharacterReferenceInvalidCharacter() {
        CharacterReader reader = new MockCharacterReader("&#x");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.consumeCharacterReference(null, false);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new MockCharacterReader(""));
        tokeniser.emit(new Token.StartTag("p"));
        tokeniser.emit(new Token.StartTag("p"));
        assertTrue(tokeniser.isAppropriateEndTagToken());

        tokeniser.emit(new Token.Character('>'));
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }
}

class MockCharacterReader implements CharacterReader {
    private StringBuilder buffer;
    private int position;
    private int pos;
    private char current;

    public MockCharacterReader(String input) {
        buffer = new StringBuilder(input);
        position = 0;
        pos = 0;
    }

    @Override
    public int read() throws IOException {
        if (position >= buffer.length())
            return -1;
        current = buffer.charAt(position++);
        pos++;
        return (int) current;
    }

    @Override
    public int read(char[] cbuf, int off, int length) throws IOException {
        if (position >= buffer.length())
            return -1;
        int max = Math.min(buffer.length() - position, length);
        System.arraycopy(buffer.substring(position).toString().toCharArray(), 0, cbuf, off, max);
        position += max;
        pos += max;
        return max;
    }

    @Override
    public boolean readLine(CharBuffer out, int len) throws IOException {
        if (position >= buffer.length())
            return false;
        int max = Math.min(buffer.length() - position, len);
        out.put(buffer.substring(position).toString().toCharArray(), 0, max);
        position += max;
        pos += max;
        return true;
    }

    @Override
    public boolean isEmpty() {
        return position >= buffer.length();
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void mark(int readlimit) {
    }

    @Override
    public void mark() {
    }

    @Override
    public void reset() throws IOException {
        position = 0;
    }

    @Override
    public void advance() throws IOException {
        position++;
        pos++;
        current = buffer.charAt(position);
    }

    @Override
    public char current() {
        return current;
    }

    @Override
    public void unread() throws IOException {
        if (position > 0)
            position--;
        else
            throw new IllegalStateException();
        current = buffer.charAt(position);
    }

    @Override
    public boolean matches(int c) {
        return current == (char) c;
    }

    @Override
    public boolean matches(String s) {
        return buffer.substring(pos - s.length(), pos).equals(s);
    }

    @Override
    public boolean matchesIgnoreCase(String s) {
        return buffer.substring(pos - s.length(), pos).equalsIgnoreCase(s);
    }

    @Override
    public boolean matchesAny(char... allowed) {
        return (current == allowed[0] || Arrays.binarySearch(allowed, current) >= 0);
    }

    @Override
    public boolean matchesAny(int... allowed) {
        for (int c : allowed) {
            if (current == (char) c)
                return true;
        }
        return false;
    }

    @Override
    public boolean matchesLetter() {
        return Character.isLetter(current);
    }

    @Override
    public boolean matchesDigit() {
        return Character.isDigit(current);
    }

    @Override
    public boolean matchesWhiteSpace() {
        return Character.isWhitespace(current);
    }

    @Override
    public void consume() throws IOException {
        if (position < buffer.length()) {
            position++;
            pos++;
            current = buffer.charAt(position);
        }
    }

    @Override
    public void consume(int n) throws IOException {
        position += n;
        pos += n;
        if (position >= buffer.length()) {
            throw new EOFException();
        }
        current = buffer.charAt(position);
    }

    @Override
    public void consumeLetterSequence() throws IOException {
        StringBuilder sb = new StringBuilder();
        while (Character.isLetter(current)) {
            sb.append(current);
            consume();
        }
        reader.setCurrentPosition(reader.getPosition() - sb.length());
        reader.setCurrentCharacter(sb.charAt(0));
    }

    @Override
    public void consumeDigitSequence() throws IOException {
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(current)) {
            sb.append(current);
            consume();
        }
        reader.setCurrentPosition(reader.getPosition() - sb.length());
        reader.setCurrentCharacter(sb.charAt(0));
    }

    @Override
    public void consumeCharacterSequence(char c) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (current == c) {
            sb.append(current);
            consume();
        }
        reader.setCurrentPosition(reader.getPosition() - sb.length());
        reader.setCurrentCharacter(sb.charAt(0));
    }

    @Override
    public void consumeHexSequence() throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (Character.isDigit(current)) {
                sb.append(current);
                consume();
            } else if (Character.isLetter(current)) {
                sb.append(Character.toLowerCase(current));
                consume();
            } else {
                break;
            }
        }
        reader.setCurrentPosition(reader.getPosition() - sb.length());
        reader.setCurrentCharacter(sb.charAt(0));
        try {
            int value = Integer.parseInt(sb.toString(), 16);
            reader.setCurrentCharacter((char) value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException();
        }
    }
}