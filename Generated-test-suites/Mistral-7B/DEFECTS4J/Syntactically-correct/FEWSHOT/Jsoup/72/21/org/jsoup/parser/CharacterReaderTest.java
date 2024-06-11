package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(3, reader.pos());
        assertEquals(true, reader.isEmpty());

        reader = new CharacterReader(new StringReader(""));
        assertEquals(0, reader.pos());
        assertEquals(true, reader.isEmpty());
        assertNull(reader.current());
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.bufferUp();
        assertEquals(3, reader.bufLength);
        assertEquals(0, reader.bufPos);
        assertEquals(3, reader.bufSplitPoint);
        assertEquals(0, reader.readerPos);
        assertEquals(3, reader.reader.mark(3).read(reader.charBuf));
        assertEquals(3, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('a', reader.current());

        reader.bufferUp();
        assertEquals(3, reader.bufLength);
        assertEquals(3, reader.bufPos);
        assertEquals(3, reader.bufSplitPoint);
        assertEquals(3, reader.readerPos);
        assertEquals(0, reader.reader.read(reader.charBuf));
        assertEquals(3, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('\u0000', reader.current());

        reader.bufferUp();
        assertEquals(3, reader.bufLength);
        assertEquals(3, reader.bufPos);
        assertEquals(3, reader.bufSplitPoint);
        assertEquals(3, reader.readerPos);
        assertEquals(0, reader.reader.read(reader.charBuf));
        assertEquals(3, reader.pos());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(true, reader.isEmpty());

        reader = new CharacterReader(new StringReader(""));
        assertEquals('\u0000', reader.consume());
        assertEquals('\u0000', reader.consume());
        assertEquals('\u0000', reader.consume());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals("a", reader.consumeTo('b').trim());
        assertEquals("bc", reader.consumeTo('c').trim());
        assertEquals("", reader.consumeTo('d').trim());
        assertEquals("", reader.consumeToAny(new char[]{'e', 'f', 'g'}).trim());
        assertEquals("", reader.consumeToAnySorted(new char[]{'e', 'f', 'g'}).trim());

        reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("abc", reader.consumeTo('d').trim());
        assertEquals("defg", reader.consumeToAny(new char[]{'e', 'f'}).trim());
        assertEquals("efg", reader.consumeToAnySorted(new char[]{'e', 'f'}).trim());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals("", reader.consumeToAny(new char[]{'d', 'e'}).trim());
        assertEquals("", reader.consumeToAny(new char[]{'a', 'b'}).trim());
        assertEquals("c", reader.consumeToAny(new char[]{'c'}).trim());
        assertEquals("", reader.consumeToAny(new char[]{'\u0000'}).trim());

        reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("abc", reader.consumeToAny(new char[]{'a', 'b'}).trim());
        assertEquals("efg", reader.consumeToAny(new char[]{'e', 'f'}).trim());
        assertEquals("defg", reader.consumeToAny(new char[]{'d'}).trim());
        assertEquals("", reader.consumeToAny(new char[]{'\u0000'}).trim());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        assertEquals("&", reader.consumeData().trim());
        assertEquals("<", reader.consumeData().trim());
        assertEquals("", reader.consumeData().trim());

        reader = new CharacterReader(new StringReader("&<null"));
        reader.mark();
        reader.rewindToMark();
        assertEquals("&<", reader.consumeData().trim());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader(" \t\n\r\f<title>\n"));
        assertEquals("title", reader.consumeTagName().trim());

        reader = new CharacterReader(new StringReader(" \t\n\r\f<title>\n"));
        reader.mark();
        reader.rewindToMark();
        assertEquals("<title>", reader.consumeTagName().trim());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals("abc", reader.consumeToEnd().trim());

        reader = new CharacterReader(new StringReader(""));
        assertEquals("", reader.consumeToEnd().trim());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("abc", reader.consumeLetterSequence().trim());

        reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertEquals("ABC", reader.consumeLetterSequence().trim());

        reader = new CharacterReader(new StringReader("12345"));
        assertEquals("", reader.consumeLetterSequence().trim());

        reader = new CharacterReader(new StringReader("12345abc"));
        assertEquals("abc", reader.consumeLetterSequence().trim());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));
        assertEquals("abc123", reader.consumeLetterThenDigitSequence().trim());

        reader = new CharacterReader(new StringReader("abc123def456"));
        reader.mark();
        reader.rewindToMark();
        assertEquals("def456", reader.consumeLetterThenDigitSequence().trim());

        reader = new CharacterReader(new StringReader("abc"));
        assertEquals("", reader.consumeLetterThenDigitSequence().trim());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&amp;#x61;#x64;#x64;"));
        assertEquals("amppp", reader.consumeHexSequence().trim());

        reader = new CharacterReader(new StringReader("&amp;#x61;#x64;#x64;"));
        reader.mark();
        reader.rewindToMark();
        assertEquals("ampamp", reader.consumeHexSequence().trim());

        reader = new CharacterReader(new StringReader("abc"));
        assertEquals("", reader.consumeHexSequence().trim());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        assertEquals("12345", reader.consumeDigitSequence().trim());

        reader = new CharacterReader(new StringReader("12345"));
        reader.mark();
        reader.rewindToMark();
        assertEquals("", reader.consumeDigitSequence().trim());

        reader = new CharacterReader(new StringReader("abc"));
        assertEquals("", reader.consumeDigitSequence().trim());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));

        reader = new CharacterReader(new StringReader(""));
        assertFalse(reader.matches('a'));

        reader = new CharacterReader(new StringReader("abc"));
        assertFalse(reader.matches("def"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesIgnoreCase("AbC"));
        assertTrue(reader.matchesIgnoreCase("aBc"));
        assertFalse(reader.matchesIgnoreCase("def"));

        reader = new CharacterReader(new StringReader(""));
        assertFalse(reader.matchesIgnoreCase("AbC"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
        assertTrue(reader.matchesAny('c'));
        assertFalse(reader.matchesAny('d'));

        reader = new CharacterReader(new StringReader(""));
        assertFalse(reader.matchesAny('a'));

        reader = new CharacterReader(new StringReader("abc"));
        assertFalse(reader.matchesAny("def"));

        reader = new CharacterReader(new StringReader(""));
        assertFalse(reader.matchesAny(new char[]{'a', 'b'}));

        reader = new CharacterReader(new StringReader("abc"));
        assertFalse(reader.matchesAny(new char[]{'_', '!'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAnySorted('a'));
        assertTrue(reader.matchesAnySorted('b'));
        assertTrue(reader.matchesAnySorted('c'));
        assertFalse(reader.matchesAnySorted('d'));

        reader = new CharacterReader(new StringReader(""));
        assertFalse(reader.matchesAnySorted('a'));

        reader = new CharacterReader(new StringReader("abc"));
        assertFalse(reader.matchesAnySorted("def"));

        reader = new CharacterReader(new StringReader(""));
        assertFalse(reader.matchesAnySorted(new char[]{'a', 'b'}));

        reader = new CharacterReader(new StringReader("abc"));
        assertFalse(reader.matchesAnySorted(new char[]{'_', '!'}));

        reader = new CharacterReader(new StringReader("ABC"));
        assertTrue(reader.matchesAnySorted('A'));
        assertTrue(reader.matchesAnySorted('B'));
        assertTrue(reader.matchesAnySorted('C'));
        assertFalse(reader.matchesAnySorted('d'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());

        reader = new CharacterReader(new StringReader(""));
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());

        reader = new CharacterReader(new StringReader(""));
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchConsume("a"));
        assertTrue(reader.matchConsume("b"));
        assertTrue(reader.matchConsume("c"));
        assertEquals(3, reader.pos());
        assertEquals(true, reader.isEmpty());

        reader = new CharacterReader(new StringReader("abc"));
        assertFalse(reader.matchConsume("d"));
        assertEquals(3, reader.pos());
        assertEquals(true, reader.isEmpty());

        reader = new CharacterReader(new StringReader("abc"));
        assertFalse(reader.matchConsume(""));
        assertEquals(3, reader.pos());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchConsumeIgnoreCase("AbC"));
        assertTrue(reader.matchConsumeIgnoreCase("aBc"));
        assertEquals(3, reader.pos());
        assertEquals(true, reader.isEmpty());

        reader = new CharacterReader(new StringReader("abc"));
        assertFalse(reader.matchConsumeIgnoreCase("def"));
        assertEquals(3, reader.pos());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/head"));
        assertFalse(reader.containsIgnoreCase("/body"));

        reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        reader.mark();
        reader.rewindToMark();
        assertTrue(reader.containsIgnoreCase("/HEAD"));
        assertTrue(reader.containsIgnoreCase("/TITLE"));
        assertFalse(reader.containsIgnoreCase("/BODY"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals("abc", reader.toString());

        reader = new CharacterReader(new StringReader(""));
        assertEquals("", reader.toString());
    }
}