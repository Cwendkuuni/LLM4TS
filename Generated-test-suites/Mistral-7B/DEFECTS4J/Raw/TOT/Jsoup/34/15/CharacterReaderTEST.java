import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals("", reader.consumeTo('a'));
        assertEquals("", reader.consumeToEnd());
        assertEquals("", reader.toString());
    }

    @Test
    public void testSimpleReader() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("a", reader.consumeAsString());
        assertEquals(1, reader.nextIndexOf('a'));
        assertEquals("bcdef", reader.consumeTo('b'));
        assertEquals("cdef", reader.consumeToEnd());
        assertEquals("bcdef", reader.toString());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testScanForSingleCharacter() {
        CharacterReader reader = new CharacterReader("abababac");
        assertEquals(1, reader.nextIndexOf('a'));
        assertEquals("ab", reader.consumeTo('a'));
        assertEquals("babac", reader.consumeToEnd());
    }

    @Test
    public void testScanForSequence() {
        CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
        assertEquals(3, reader.nextIndexOf("cd"));
        assertEquals("abcd", reader.consumeTo("cd"));
        assertEquals("abcdabcdabcd", reader.consumeToEnd());
    }

    @Test
    public void testScanForSurrogates() {
        CharacterReader reader = new CharacterReader("\uD835\uDD15\uD835\uDD35");
        assertEquals(1, reader.nextIndexOf("\uD835\uDD15"));
        assertEquals("\uD835\uDD15", reader.consumeTo("\uD835\uDD15"));
        assertEquals("\uD835\uDD35", reader.consumeToEnd());
    }

    @Test
    public void testScanForEmptySequence() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals(-1, reader.nextIndexOf(""));
        assertEquals("", reader.consumeTo(""));
        assertEquals("abcd", reader.consumeToEnd());
    }

    @Test
    public void testScanForNullSequence() {
        CharacterReader reader = new CharacterReader("abcd");
        assertNull(reader.nextIndexOf(null));
        assertNull(reader.consumeTo(null));
        assertEquals("abcd", reader.consumeToEnd());
    }

    @Test
    public void testScanForEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals("", reader.consumeTo('a'));
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testScanForMultipleMatches() {
        CharacterReader reader = new CharacterReader("abababac");
        assertEquals(1, reader.nextIndexOf('a'));
        assertEquals("ab", reader.consumeTo('a'));
        assertEquals(3, reader.nextIndexOf('a'));
        assertEquals("abab", reader.consumeTo('a', 2));
        assertEquals("c", reader.consumeLetter());
        assertEquals("ac", reader.consumeLetterThenDigitSequence());
        assertEquals("ac", reader.consumeLetterThenDigitSequence());
        assertEquals("ac", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());
        assertEquals("c", reader.consumeDigit());
        assertEquals("", reader.consumeDigit());
        assertEquals("", reader.consumeDigit());
        assertEquals("", reader.consumeDigit());
        assertEquals("", reader.consumeDigit());
        assertEquals("", reader.consumeDigit());
        assertEquals("c", reader.matches('c'));
        assertEquals("abc", reader.matches("abc"));
        assertEquals("ABC", reader.matchesIgnoreCase("ABC"));
        assertEquals(true, reader.matchesAny('a', 'b'));
        assertEquals(true, reader.matchesLetter());
        assertEquals(true, reader.matchesDigit());
        assertEquals(true, reader.matchConsume("ab"));
        assertEquals(true, reader.matchConsumeIgnoreCase("Ab"));
        assertEquals(true, reader.containsIgnoreCase("Ab"));
        assertEquals("ab", reader.toString());
    }

    @Test
    public void testScanForScanBeyondEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals(-1, reader.nextIndexOf('e'));
        assertEquals("", reader.consumeTo('e'));
        assertEquals("abcd", reader.consumeToEnd());
    }
}