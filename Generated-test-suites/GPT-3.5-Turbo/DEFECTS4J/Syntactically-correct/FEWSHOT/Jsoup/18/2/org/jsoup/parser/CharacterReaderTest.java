import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals(0, reader.pos());

        reader.consume();
        assertEquals(1, reader.pos());

        reader.consume();
        reader.consume();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("hi");
        assertFalse(reader.isEmpty());

        reader.consume();
        assertFalse(reader.isEmpty());

        reader.consume();
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals('a', reader.current());

        reader.consume();
        assertEquals('b', reader.current());

        reader.consume();
        assertEquals('c', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.consume();
        reader.consume();
        reader.consume();
        reader.unconsume();

        assertEquals('c', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.consume();
        reader.advance();

        assertEquals('c', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.mark();
        reader.consume();
        reader.advance();
        reader.rewindToMark();

        assertEquals('b', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());

        reader.consume();
        assertEquals("c", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abcdef", reader.consumeTo('g'));

        reader.consume();
        assertEquals("g", reader.consumeTo('g'));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcDefg");
        assertEquals("abc", reader.consumeToIgnoreCase("def"));

        reader.consume();
        assertEquals("fg", reader.consumeToIgnoreCase("def"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abcde", reader.consumeToAny('e', 'g'));

        reader.consume();
        assertEquals("g", reader.consumeToAny('e', 'g'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abcdefg", reader.consumeToEnd());

        reader.consume();
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterSequence());

        reader.consume();
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("", reader.consumeHexSequence());

        reader.consume();
        assertEquals("bc123", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("", reader.consumeDigitSequence());

        reader.consume();
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));

        reader.consume();
        assertTrue(reader.matches("bcdefg"));
        assertFalse(reader.matches("abcdefg"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchesIgnoreCase("aBc"));
        assertFalse(reader.matchesIgnoreCase("bcd"));

        reader.consume();
        assertTrue(reader.matchesIgnoreCase("bcDEFG"));
        assertFalse(reader.matchesIgnoreCase("abcdef"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('c', 'd'));

        reader.consume();
        assertTrue(reader.matchesAny('b', 'c'));
        assertFalse(reader.matchesAny('c', 'd'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.matchesLetter());

        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc123");
        assertFalse(reader.matchesDigit());

        reader.consume();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchConsume("abc"));
        assertFalse(reader.matchConsume("bcd"));

        assertTrue(reader.matchConsumeIgnoreCase("De"));
        assertFalse(reader.matchConsumeIgnoreCase("efg"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcDeFg");
        assertTrue(reader.containsIgnoreCase("def"));
        assertFalse(reader.containsIgnoreCase("ghi"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abcdefg", reader.toString());

        reader.consume();
        assertEquals("bcdefg", reader.toString());
    }
}