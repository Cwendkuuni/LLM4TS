package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_constructor() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.input);
        assertEquals(3, reader.length);
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReader_constructor_nullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());

        reader.advance();
        assertEquals(1, reader.pos());

        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());

        reader.advance();
        assertEquals('b', reader.current());

        reader.advance();
        assertEquals('c', reader.current());

        reader.advance(); // past EOF
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());

        reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertSame(reader, reader.consume()); // consume EOF
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        reader.advance();
        reader.unconsume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        reader.advance();
        assertEquals('c', reader.current());

        reader = new CharacterReader("abc");
        reader.advance();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.advance();
        reader.advance();

        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("ab", reader.consumeAsString());

        reader = new CharacterReader("abcd");
        reader.advance();
        reader.advance();
        assertEquals("cd", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("bc", reader.consumeTo('c'));

        reader = new CharacterReader("abcd");
        reader.advance();
        reader.advance();
        assertEquals("d", reader.consumeTo('d'));

        reader = new CharacterReader("abcd");
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(CharacterReader.EOF, reader.consumeTo('e'));

        reader = new CharacterReader("abcd");
        reader.advance();
        reader.consume(); // consume 'b'
        assertEquals("cd", reader.consumeTo('d'));
    }

    @Test
    public void testConsumeTo_emptySequence() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals(CharacterReader.EOF, reader.consumeTo(new char[0]));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("d", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("ab", reader.consumeLetterSequence());

        reader = new CharacterReader("1234");
        assertEquals("12
--more--
 34", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcd%#e5f0");
        assertEquals("%#e5", reader.consumeHexSequence());

        reader = new CharacterReader("abcd%#e5f0");
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals("f0", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234");
        assertEquals("1234", reader.consumeDigitSequence());

        reader = new CharacterReader("1234");
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));
        assertFalse(reader.matches('e'));

        reader = new CharacterReader("abcd");
        reader.advance();
        reader.advance();
        assertFalse(reader.matches('a'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCd");
        assertTrue(reader.matchesIgnoreCase("AbC"));
        assertTrue(reader.matchesIgnoreCase("AbCd"));
        assertFalse(reader.matchesIgnoreCase("ABC"));
        assertFalse(reader.matchesIgnoreCase("AbE"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAny(new char[]{'a', 'b', 'c'}));
        assertFalse(reader.matchesAny(new char[]{'e'}));

        reader = new CharacterReader("abcd");
        reader.advance();
        reader.advance();
        assertFalse(reader.matchesAny(new char[]{'a'}));
        assertTrue(reader.matchesAny(new char[]{'c'}));
        assertFalse(reader.matchesAny(new char[]{'e'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertFalse(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("1234");
        assertTrue(reader.matchesDigit());
        reader.advance();
        reader.advance();
        reader.advance();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchConsume("ab"));
        assertEquals(2, reader.pos());

        reader = new CharacterReader("abcd");
        reader.advance();
        reader.advance();
        assertTrue(reader.matchConsume("cd"));
        assertEquals(4, reader.pos());

        reader = new CharacterReader("abcd");
        reader.advance();
        reader.consume();
        assertTrue(reader.matchConsume("cd"));
        assertEquals(3, reader.pos());

        reader = new CharacterReader("abcd");
        reader.advance();
        reader.consume();
        reader.consume();
        assertTrue(reader.matchConsume("d"));
        assertEquals(3, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCd");
        assertTrue(reader.matchConsumeIgnoreCase("AbC"));
        assertEquals(3, reader.pos());

        reader = new CharacterReader("ABCd");
        reader.advance();
        reader.advance();
        assertTrue(reader.matchConsumeIgnoreCase("cd"));
        assertEquals(5, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABC");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/STYLE"));
        assertFalse(reader.containsIgnoreCase("/TITLE"));
        assertFalse(reader.containsIgnoreCase("/style"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("bc", reader.toString());
    }
}