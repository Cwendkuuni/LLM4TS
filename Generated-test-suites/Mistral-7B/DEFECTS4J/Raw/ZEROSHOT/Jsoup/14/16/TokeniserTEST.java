package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the Tokeniser class.
 */
public class TokeniserTest {

    private Tokeniser tokeniser;
    private List<ParseError> errors;

    @Before
    public void setUp() {
        String input = "<html><head><title>Test</title></head><body><script>console.log('Hello World');</script></body></html>";
        CharacterReader reader = new StringCharacterReader(input);
        tokeniser = new Tokeniser(reader);
        errors = tokeniser.getErrors();
    }

    @After
    public void tearDown() {
        tokeniser = null;
    }

    @Test
    public void testReadEmptyInput() {
        Token token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadCharacter() {
        for (int i = 0; i < input.length(); i++) {
            Token token = tokeniser.read();
            Assert.assertNotNull(token);
            Assert.assertEquals(Token.TokenType.Character, token.type);
            Assert.assertEquals(String.valueOf((char) input.charAt(i)), token.data);
            Assert.assertEquals(i + 1, tokeniser.reader.getPos());
            Assert.assertEquals(0, errors.size());
        }
    }

    @Test
    public void testReadHtmlTags() {
        Token token = null;
        while ((token = tokeniser.read()) != null) {
            switch (token.type) {
                case Token.TokenType.StartTag:
                    Assert.assertEquals("<html>", token.tag.tagName);
                    Assert.assertEquals(Token.StartTag.Open, token.tag.isStartTagOpen);
                    Assert.assertEquals(Token.StartTag.IsSelfClosing, token.tag.isSelfClosing);
                    Assert.assertEquals(0, errors.size());
                    break;
                case Token.TokenType.EndTag:
                    Assert.assertEquals("</html>", token.tag.tagName);
                    Assert.assertEquals(Token.EndTag.IsSelfClosing, token.tag.isSelfClosing);
                    Assert.assertEquals(0, errors.size());
                    break;
                case Token.TokenType.Character:
                    // continue
                    break;
                default:
                    Assert.fail("Unexpected token type: " + token.type);
            }
        }
    }

    @Test
    public void testReadScriptTag() {
        Token token = null;
        while ((token = tokeniser.read()) != null) {
            if (token.type == Token.TokenType.StartTag && token.tag.tagName.equals("script")) {
                Assert.assertEquals(Token.StartTag.Open, token.tag.isStartTagOpen);
                Assert.assertEquals(Token.StartTag.IsSelfClosing, token.tag.isSelfClosing);
                Assert.assertEquals(0, errors.size());
                break;
            }
        }
    }

    @Test
    public void testReadCharacterReference() {
        tokeniser.transition(TokeniserState.CharacterReference);
        Character c = tokeniser.consumeCharacterReference(null, false);
        Assert.assertEquals((char) 0x26, c);
        Assert.assertEquals(0, errors.size());

        tokeniser.transition(TokeniserState.CharacterReference);
        c = tokeniser.consumeCharacterReference('#', false);
        Assert.assertEquals((char) 0x23, c);
        Assert.assertEquals(0, errors.size());

        tokeniser.transition(TokeniserState.CharacterReference);
        c = tokeniser.consumeCharacterReference("ampersand", false);
        Assert.assertEquals((char) 0x26, c);
        Assert.assertEquals(0, errors.size());

        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Invalid character reference", errors.get(0).getMessage());
        Assert.assertEquals(input.length(), tokeniser.reader.getPos());

        tokeniser.setTrackErrors(false);
        tokeniser.transition(TokeniserState.CharacterReference);
        c = tokeniser.consumeCharacterReference("ampersand", false);
        Assert.assertNull(c);
    }

    @Test
    public void testReadNamedCharacterReference() {
        tokeniser.transition(TokeniserState.NamedCharacterReference);
        tokeniser.consumeLetterSequence();
        tokeniser.consumeLetter();
        Assert.assertEquals("amp", tokeniser.reader.readString().toLowerCase());
        tokeniser.consumeLetter();
        Assert.assertEquals(";", tokeniser.reader.readString().toLowerCase());
        Assert.assertEquals((char) 0x26, tokeniser.read().data.charAt(0));
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadInvalidNamedCharacterReference() {
        tokeniser.transition(TokeniserState.NamedCharacterReference);
        tokeniser.consumeLetterSequence();
        tokeniser.consumeLetter();
        Assert.assertEquals("amp", tokeniser.reader.readString().toLowerCase());
        tokeniser.consumeLetter();
        Assert.assertEquals("x", tokeniser.reader.readString().toLowerCase());
        Assert.assertEquals(";", tokeniser.reader.readString().toLowerCase());
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Invalid named character reference: 'ampx'", errors.get(0).getMessage());
        Assert.assertEquals(input.length(), tokeniser.reader.getPos());
    }
}

class StringCharacterReader extends CharacterReader {

    private final String input;

    public StringCharacterReader(String input) {
        this.input = input;
    }

    @Override
    public int read() throws IOException {
        if (pos < input.length()) {
            current = input.charAt(pos);
            pos++;
            return (int) current;
        } else {
            return -1;
        }
    }

    @Override
    public int read(char[] cbuf, int off, int length) throws IOException {
        int charsRead = 0;
        for (int i = off; i < off + length && pos < input.length(); i++, charsRead++) {
            cbuf[i] = input.charAt(pos++);
        }
        return charsRead;
    }

    @Override
    public boolean markSupported(boolean readAhead) {
        return false;
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        // not supported
    }

    @Override
    public void reset() throws IOException {
        // not supported
    }

    @Override
    public boolean isEOF() {
        return pos >= input.length();
    }

    @Override
    public boolean isEmpty() {
        return pos == 0;
    }

    @Override
    public int getPos() {
        return pos;
    }

    @Override
    public int getLimit() {
        return input.length();
    }

    @Override
    public char current() {
        return current;
    }

    @Override
    public void unread(int c) throws IOException {
        if (pos > 0) {
            pos--;
            current = input.charAt(pos);
        }
    }

    @Override
    public boolean matches(int c) {
        if (pos < input.length() && input.charAt(pos) == (char) c) {
            pos++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean matchesAny(char... c) {
        for (char ch : c) {
            if (matches(ch)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean matchesIgnoreCase(int c) {
        if (pos < input.length() && Character.toLowerCase(input.charAt(pos)) == Character.toLowerCase((char) c)) {
            pos++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean matchesLetter() {
        if (Character.isLetter(current)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean matchesDigit() {
        if (Character.isDigit(current)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean matchesAny(char... c) {
        return matchesAny(c);
    }

    @Override
    public boolean matchesIgnoreCase(String s) {
        int posLocal = pos;
        boolean matchFound = false;
        for (int i = 0; i < s.length() && posLocal < input.length(); i++, posLocal++) {
            if (Character.toLowerCase(current) != Character.toLowerCase(s.charAt(i))) {
                break;
            }
            if (i == s.length() - 1) {
                pos++;
                matchFound = true;
            }
        }
        if (matchFound) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String readString() throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = read()) != -1) {
            sb.append((char) c);
        }
        return sb.toString();
    }

    @Override
    public char[] currentLine() throws IOException {
        int startPos = pos;
        while (pos < input.length() && (current = read()) != '\n') {
        }
        return input.substring(startPos, pos).toCharArray();
    }

    @Override
    public char[] consumed(int len) throws IOException {
        char[] cbuf = new char[len];
        for (int i = 0; i < len && pos < input.length(); i++) {
            cbuf[i] = input.charAt(pos++);
        }
        return cbuf;
    }

    @Override
    public void close() throws IOException {
        // not supported
    }
}