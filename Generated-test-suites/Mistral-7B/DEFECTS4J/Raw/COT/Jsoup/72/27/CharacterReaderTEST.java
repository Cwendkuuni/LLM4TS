package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        final String input = "Hello World!";
        CharacterReader reader = new CharacterReader(new StringReader(input));
        assertEquals("Hello World!", reader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        final String input = "Hello World!";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("Hello World!", reader.toString());
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        assertEquals(5, reader.bufLength);
        assertEquals('a', reader.charBuf[0]);
        assertEquals('b', reader.charBuf[1]);
        assertEquals('c', reader.charBuf[2]);
        assertEquals('d', reader.charBuf[3]);
        assertEquals('e', reader.charBuf[4]);
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello") {
            @Override
            public int read(char[] c, int off, int len) {
                throw new IOException();
            }
        });
        reader.bufferUp();
        fail("Expected IOException");
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(7, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals('d', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdeabcdefg"));
        reader.bufferUp();
        assertEquals(5, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdeabcdefg"));
        String result = reader.consumeTo('e');
        assertEquals("abcd", result);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdeabcdefg"));
        String result = reader.consumeToEnd();
        assertEquals("abcdeabcdefg", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        String result = reader.consumeLetterSequence();
        assertEquals("abcdef", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));
        String result = reader.consumeLetterThenDigitSequence();
        assertEquals("abc123def456", result);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc#def#ghi"));
        String result = reader.consumeHexSequence();
        assertEquals("abc#def#", result);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        String result = reader.consumeDigitSequence();
        assertEquals("1234567890", result);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJK"));
        assertTrue(reader.matchesIgnoreCase("E"));
        assertFalse(reader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        assertTrue(reader.matchesAny('e', 'i'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        assertTrue(reader.matchesAnySorted(new char[] {'e', 'i'}));
        assertFalse(reader.matchesAnySorted(new char[] {'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag name=\"value\"/>"));
        String result = reader.consumeTagName();
        assertEquals("tag", result);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk<lop/mop>"));
        String result = reader.consumeToAny(new char[] {'l', 'o'});
        assertEquals("abcfghijk<mop>", result);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk<lop/mop>"));
        String result = reader.consumeToAnySorted(new char[] {'l', 'o'});
        assertEquals("abcd<mop>", result);
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;abc&gt;def"));
        String result = reader.consumeData();
        assertEquals("&lt;abc&gt;def", result);
    }
}