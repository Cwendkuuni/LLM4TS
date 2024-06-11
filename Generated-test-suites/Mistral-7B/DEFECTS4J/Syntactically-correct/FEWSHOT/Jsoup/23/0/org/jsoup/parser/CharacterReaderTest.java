package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

public class CharacterReaderTest {

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

        reader.consume();
        reader.consume();

        assertEquals(2, reader.pos());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());

        reader.consume();

        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.consume();
        reader.consume();

        reader.unconsume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.advance();
        reader.advance();

        assertEquals('c', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        reader.consume();

        reader.rewindToMark();

        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.consume();
        reader.consume();

        assertEquals("bc", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abdefg");
        assertEquals("cd", reader.consumeTo('d'));
        assertEquals("efg", reader.consumeTo('g'));
        assertEquals("", reader.consumeTo('a'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abc123def");
        assertEquals("123", reader.consumeToAny('1', '3'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("d", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd1efg");
        assertEquals("abcd", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcd1efg");
        assertEquals("1e", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd1efg");
        assertEquals("1", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABCdef");
        assertTrue(reader.matchesIgnoreCase("aB"));
        assertFalse(reader.matchesIgnoreCase("zX"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd1efg");
        assertTrue(reader.matchesAny('1', '3'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd1efg");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('1'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcd1efg");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('1'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcd1efg");
        assertTrue(reader.matchConsume("ef"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbCd1EfG");
        assertTrue(reader.matchConsumeIgnoreCase("ef"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd<title>efg</title>");
        assertTrue(reader.containsIgnoreCase("<title>"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }
}