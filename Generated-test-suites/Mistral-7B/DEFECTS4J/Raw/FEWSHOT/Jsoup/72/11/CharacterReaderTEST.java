package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("foo", reader.consumeToEnd());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("foobar"));
        assertEquals("foo", reader.consumeTo('b'));
        assertEquals("bar", reader.consumeToEnd());
        assertEquals(7, reader.pos());
        assertFalse(reader.isEmpty());

        reader = new CharacterReader(new StringReader(""));
        assertEquals("", reader.consumeToEnd());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("abc"));
        assertEquals("a", reader.consumeLetterSequence());
        assertEquals("bc", reader.consumeLetterThenDigitSequence());
        assertEquals("abc", reader.consumeToEnd());
        assertEquals("abc", reader.toString());
    }

    @Test
    public void testCharacterReaderWithIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("foo")) {
            @Override
            void bufferUp() throws IOException {
                throw new IOException();
            }
        };
        assertNull(reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        new CharacterReader(null);
    }

    @Test
    public void testConsumeSpecialChars() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;foo&amp;&#x304;bar&gt;"));
        assertEquals("&lt;foo&amp;#x304;bar&gt;", reader.consumeToEnd());
        assertEquals("&lt;foo&amp;#x304;bar&gt;", reader.toString());
        reader.mark();
        assertEquals("&lt;", reader.consumeTo('<'));
        assertEquals("foo", reader.consumeTo('o'));
        assertEquals("&#x304;", reader.consume());
        assertEquals("bar", reader.consumeTo('r'));
        assertEquals(">", reader.consume());
        reader.rewindToMark();
        assertEquals("&lt;foo", reader.consumeTo('o'));
        reader.unconsume();
        assertEquals("&lt;foo", reader.toString());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"));
        assertEquals("html", reader.consumeTagName());
        assertEquals("head", reader.consumeTagName());
        assertEquals("title", reader.consumeTagName());
        assertEquals("Test", reader.consumeToEnd());
        assertEquals("body", reader.consumeTagName());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCdEfGhIjKlMnOpQrStUvWxYz"));
        assertEquals("AbCdEfGhIjKlMnOpQrStUvWxYz", reader.consumeToEnd());
        String[] sequences = {"Ab", "CdEf", "GhIj", "MnOp", "QuSt", "UvWx", "Yz"};
        for (String sequence : sequences) {
            assertEquals(sequence, reader.consumeLetterSequence());
        }
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("AbC123dE4F5gHi7J8KlMnOpQ9R0StUvWxYz"));
        String[] sequences = {"AbC", "dE", "gHi", "J8Kl", "MnOp", "QuSt", "UvWx", "Yz"};
        for (int i = 0; i < sequences.length; i++) {
            String sequence = sequences[i];
            assertEquals(sequence, reader.consumeLetterThenDigitSequence());
        }
    }

    @Test
    public void testConsumeHexSequence() {   
        CharacterReader reader = new CharacterReader(new StringReader("AbC#x61#x63#x61"));
        assertEquals("AbC&#x61&#x63&#x61", reader.consumeToEnd());
        assertEquals("AbC&#x61&#x63&#x61", reader.toString());
        reader.mark();
        assertEquals("AbC", reader.consumeLetterSequence());
        assertEquals("&#x61", reader.consumeHexSequence());
        assertEquals("&#x63", reader.consumeHexSequence());
        assertEquals("&#x61", reader.consumeHexSequence());
        reader.rewindToMark();
        assertEquals("AbC", reader.toString());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        assertEquals("1234567890", reader.consumeToEnd());
        String[] sequences = {"123", "456", "789"};
        for (String sequence : sequences) {
            assertEquals(sequence, reader.consumeDigitSequence());
        }
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('g'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEF"));
        assertTrue(reader.matchesIgnoreCase('a'));
        assertTrue(reader.matchesIgnoreCase('e'));
        assertFalse(reader.matchesIgnoreCase('g'));
        assertTrue(reader.matchesIgnoreCase('z'));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(reader.matchesAny('a', 'b', 'c', 'd', 'e', 'f'));
        assertFalse(reader.matchesAny('g', 'h'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(reader.matchesAnySorted('a', 'b', 'c', 'd', 'e', 'f'));
        assertFalse(reader.matchesAnySorted('f', 'g', 'h'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<bar>baz"));
        assertTrue(reader.matchConsume("foo"));
        assertEquals("bar", reader.consumeToEnd());
        assertEquals("baz", reader.consumeToEnd());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("FOO<BAR>BAZ"));
        assertTrue(reader.matchConsumeIgnoreCase("foo"));
        assertEquals("bar", reader.consumeToEnd());
        assertEquals("baz", reader.consumeToEnd());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/BODY"));
        assertFalse(reader.containsIgnoreCase("/head"));
    }
}