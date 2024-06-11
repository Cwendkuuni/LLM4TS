import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.advance();
        reader.mark();
        assertEquals('b', reader.current());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals(2, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeTo('d'));
        assertEquals("defg", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("ab", reader.consumeTo("c"));
        assertEquals("c", reader.consumeToString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("ab", reader.consumeToAny('c', 'd'));
        assertEquals("d", reader.consumeToAny('e'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeTo('d'));
        assertEquals("defg", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("a1b2c3");
        assertEquals("a", reader.consumeLetterThenDigitSequence());
        assertEquals("1", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeHexSequence());
        assertEquals("123", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("abc"));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertFalse(reader.matchesIgnoreCase("XYZ"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertFalse(reader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchConsume("abc"));
        assertFalse(reader.matchConsume("xyz"));
        assertEquals("defg", reader.consumeToEnd());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchConsumeIgnoreCase("ABC"));
        assertFalse(reader.matchConsumeIgnoreCase("XYZ"));
        assertEquals("defg", reader.consumeToEnd());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("testingtheabcTESTINGTHEABC");
        assertTrue(reader.containsIgnoreCase("abc"));
        assertFalse(reader.containsIgnoreCase("xyz"));
    }
}