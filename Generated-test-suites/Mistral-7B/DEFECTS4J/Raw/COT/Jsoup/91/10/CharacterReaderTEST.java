package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;

public class CharacterReaderTest {

    

    @Test
    public void testCharacterReaderConstructor_Reader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testCharacterReaderConstructor_String() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testCharacterReaderConstructor_Reader_int() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), 10);
        assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals(-1, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());

        reader.consume();
        reader.consume();
        reader.unconsume();
        reader.unconsume();
        assertEquals('a', reader.current());
        assertEquals('b', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        assertEquals('b', reader.current());
        reader.advance();
        assertEquals('c', reader.current());
        reader.advance();
        assertEquals(-1, reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testMarkInvalid() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.unconsume();
        reader.rewindToMark();
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(10, reader.nextIndexOf('j'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(10, reader.nextIndexOf("jkl"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("jkl", reader.consumeTo("klm"));
        assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeTo_CharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("bc", reader.consumeTo("c"));
        assertEquals("jkl", reader.consumeTo("lmno"));
        assertEquals("", reader.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("bcd", reader.consumeToAny(new char[]{'b', 'c', 'd'}));
        assertEquals("jkl", reader.consumeToAny(new char[]{'j', 'k', 'l'}));
        assertEquals("", reader.consumeToAny(new char[]{'x', 'y', 'z'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("bcd", reader.consumeToAnySorted(new char[]{'b', 'c', 'd'}));
        assertEquals("jkl", reader.consumeToAnySorted(new char[]{'j', 'k', 'l'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'x', 'y', 'z'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        assertEquals("&<null", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader(" <title>Hello World</title>"));
        assertEquals("<title>", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader(" <title>Hello World</title>"));
        assertEquals("<title>Hello World</title>", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("abcdefghij", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches('z'));

        reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertFalse(reader.matches('x'));

        reader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(reader.matches('!'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("e"));
        assertTrue(reader.matchesIgnoreCase("z"));

        reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertFalse(reader.matchesIgnoreCase("x"));

        reader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(reader.matchesIgnoreCase("!"));

        reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchesIgnoreCase("world"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matchesAny(new char[]{'a', 'e'}));
        assertTrue(reader.matchesAny(new char[]{'z'}));
        assertFalse(reader.matchesAny(new char[]{'x'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'e', 'z'}));
        assertFalse(reader.matchesAnySorted(new char[]{'x'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matchConsume("a"));
        assertTrue(reader.matchConsume("e"));
        assertFalse(reader.matchConsume("x"));

        reader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(reader.matchConsume("!"));

        reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchConsume("world"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matchConsumeIgnoreCase("a"));
        assertTrue(reader.matchConsumeIgnoreCase("e"));
        assertFalse(reader.matchConsumeIgnoreCase("x"));

        reader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(reader.matchConsumeIgnoreCase("!"));

        reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchConsumeIgnoreCase("world"));

        reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchConsumeIgnoreCase("Hello"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String str = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos);
        assertEquals("abcdefghijklmnopqrstuvwxyz", str);
    }

    
}