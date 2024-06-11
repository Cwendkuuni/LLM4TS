package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Tokeniser;

import java.util.Arrays;

public class TokeniserTest {

    @Test
    public void testRead() {
        CharacterReader readerMock = new CharacterReader() {
            int index = 0;
            String data = "abcdefg";

            @Override
            public int read() throws IOException {
                if (index >= data.length()) return -1;
                return (int) data.charAt(index++);
            }

            @Override
            public int read(char[] c, int off, int len) throws IOException {
                System.arraycopy(data.substring(index, index + len).toCharArray(), off, c, off, len);
                index += len;
                return len;
            }

            @Override
            public char current() {
                return data.charAt(index);
            }

            @Override
            public boolean isEmpty() {
                return index >= data.length();
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
            public void rewind(int readAheadLimit) {
                index -= readAheadLimit;
            }

            @Override
            public void rewind() {
                index--;
            }

            @Override
            public boolean matches(int c) {
                return data.charAt(index) == (char) c;
            }

            @Override
            public boolean matches(String s) {
                return data.regionMatches(index, s, 0, s.length());
            }

            @Override
            public boolean matchesAny(char[] chars) {
                for (char c : chars) {
                    if (matches(c)) return true;
                }
                return false;
            }

            @Override
            public boolean matchesAnySorted(char[] chars) {
                for (char c : chars) {
                    if (matches(c)) return true;
                    if (index >= data.length() || data.charAt(index) != c) continue;
                    index++;
                }
                return false;
            }

            @Override
            public boolean matchesLetter() {
                return isAlpha((int) current());
            }

            @Override
            public boolean matchesDigit() {
                return isDigit((int) current());
            }

            @Override
            public boolean matchesAny(char c1, char c2) {
                return matches(c1) || matches(c2);
            }

            @Override
            public void advance() throws IOException {
                index++;
            }
        };

        ParseErrorList errorsMock = new ParseErrorList() {
            int errorCount = 0;

            @Override
            public boolean canAddError() {
                return errorCount < MAX_ERRORS;
            }

            @Override
            public void add(ParseError error) {
                errorCount++;
            }
        };

        Tokeniser tokeniser = new Tokeniser(readerMock, errorsMock);

        Token token;
        for (int i = 0; i < 10; i++) {
            token = tokeniser.read();
            if (token != null) {
                assertEquals(Token.TokenType.Character, token.type);
                assertEquals(String.valueOf(data.charAt(index)), token.charPending.data());
                index++;
            }
        }

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        CharacterReader readerMock = new CharacterReader() {
            int index = 0;
            String data = "abcdefg";

            @Override
            public int read() throws IOException {
                if (index >= data.length()) return -1;
                return (int) data.charAt(index++);
            }

            @Override
            public int read(char[] c, int off, int len) throws IOException {
                System.arraycopy(data.substring(index, index + len).toCharArray(), off, c, off, len);
                index += len;
                return len;
            }

            @Override
            public char current() {
                return data.charAt(index);
            }

            @Override
            public boolean isEmpty() {
                return index >= data.length();
            }
        };

        ParseErrorList errorsMock = new ParseErrorList() {
            int errorCount = 0;

            @Override
            public boolean canAddError() {
                return errorCount < MAX_ERRORS;
            }

            @Override
            public void add(ParseError error) {
                errorCount++;
            }
        };

        Tokeniser tokeniser = new Tokeniser(readerMock, errorsMock);

        StringBuilder expected = new StringBuilder();
        tokeniser.emit("abc");
        tokeniser.emit("defg");
        assertEquals(expected.append("abc").append("defg").toString(), tokeniser.read().charPending.data());

        expected.setLength(0);
        tokeniser.transition(TokeniserState.ProcessingInstruction);
        tokeniser.emit("<?xml");
        assertEquals(expected.append("<?xml").toString(), tokeniser.read().toString());
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader readerMock = new CharacterReader() {
            int index = 0;
            String data = "abcdefg#x41#x26;";

            @Override
            public int read() throws IOException {
                if (index >= data.length()) return -1;
                return (int) data.charAt(index++);
            }

            @Override
            public int read(char[] c, int off, int len) throws IOException {
                System.arraycopy(data.substring(index, index + len).toCharArray(), off, c, off, len);
                index += len;
                return len;
            }

            @Override
            public char current() {
                return data.charAt(index);
            }

            @Override
            public boolean isEmpty() {
                return index >= data.length();
            }
        };

        ParseErrorList errorsMock = new ParseErrorList() {
            int errorCount = 0;

            @Override
            public boolean canAddError() {
                return errorCount < MAX_ERRORS;
            }

            @Override
            public void add(ParseError error) {
                errorCount++;
            }
        };

        Tokeniser tokeniser = new Tokeniser(readerMock, errorsMock);

        int[] result = tokeniser.consumeCharacterReference(null, false);
        assertEquals(Character.toChars(0x41), result);
        assertEquals(Character.toChars(0x26), tokeniser.consumeCharacterReference(null, false));

        tokeniser.consumeCharacterReference(Character.forDigit(1, 16), false);
        tokeniser.consumeCharacterReference('A', false);
        tokeniser.consumeCharacterReference('&', false);
        assertNull(tokeniser.consumeCharacterReference('Z', false));

        tokeniser.consumeCharacterReference(null, true);
        assertNull(tokeniser.consumeCharacterReference(null, true));
    }

    @Test
    public void testCreateTagPending() {
        CharacterReader readerMock = new CharacterReader() {
            int index = 0;
            String data = "<div><p>";

            @Override
            public int read() throws IOException {
                if (index >= data.length()) return -1;
                return (int) data.charAt(index++);
            }

            @Override
            public int read(char[] c, int off, int len) throws IOException {
                System.arraycopy(data.substring(index, index + len).toCharArray(), off, c, off, len);
                index += len;
                return len;
            }

            @Override
            public char current() {
                return data.charAt(index);
            }

            @Override
            public boolean isEmpty() {
                return index >= data.length();
            }
        };

        ParseErrorList errorsMock = new ParseErrorList() {
            int errorCount = 0;

            @Override
            public boolean canAddError() {
                return errorCount < MAX_ERRORS;
            }

            @Override
            public void add(ParseError error) {
                errorCount++;
            }
        };

        Tokeniser tokeniser = new Tokeniser(readerMock, errorsMock);

        Token.Tag tag = tokeniser.createTagPending(true);
        Token.Tag tag2 = tokeniser.createTagPending(false);

        assertTrue(tag.isStartTag());
        assertTrue(tag2.isEndTag());
        assertEquals("<div>", tag.tagName());
        assertEquals("div", tag2.tagName());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader readerMock = new CharacterReader() {
            int index = 0;
            String data = "<div><p></p></div>";

            @Override
            public int read() throws IOException {
                if (index >= data.length()) return -1;
                return (int) data.charAt(index++);
            }

            @Override
            public int read(char[] c, int off, int len) throws IOException {
                System.arraycopy(data.substring(index, index + len).toCharArray(), off, c, off, len);
                index += len;
                return len;
            }

            @Override
            public char current() {
                return data.charAt(index);
            }

            @Override
            public boolean isEmpty() {
                return index >= data.length();
            }
        };

        ParseErrorList errorsMock = new ParseErrorList() {
            int errorCount = 0;

            @Override
            public boolean canAddError() {
                return errorCount < MAX_ERRORS;
            }

            @Override
            public void add(ParseError error) {
                errorCount++;
            }
        };

        Tokeniser tokeniser = new Tokeniser(readerMock, errorsMock);

        assertTrue(tokeniser.isAppropriateEndTagToken());
        assertEquals("p", tokeniser.appropriateEndTagName());

        tokeniser.transition(TokeniserState.Scripting);
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }
}