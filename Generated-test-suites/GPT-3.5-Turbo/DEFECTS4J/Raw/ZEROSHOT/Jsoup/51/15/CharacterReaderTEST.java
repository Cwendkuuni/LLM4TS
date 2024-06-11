import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        assertEquals('b', reader.current());
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(1, reader.nextIndexOf('b'));
    }

    @Test
    public void testNextIndexOfCharNotFound() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(-1, reader.nextIndexOf('d'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.nextIndexOf("a"));
    }

    @Test
    public void testNextIndexOfCharSequenceNotFound() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(-1, reader.nextIndexOf("d"));
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("ab", reader.consumeTo('c'));
        assertEquals('c', reader.current());
    }

    @Test
    public void testConsumeToCharNotFound() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeTo('d'));
    }

    @Test
    public void testConsumeToSequence() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeTo("b"));
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsumeToSequenceNotFound() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeTo("d"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("", reader.consumeToAny('x', 'y', 'z'));
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("", reader.consumeToAnySorted('x', 'y', 'z'));
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeToEnd());
        assertEquals('c', reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("a1b2");
        assertEquals("a1b2", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0123456789ABCDEF");
        assertEquals("0123456789ABCDEF", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("0123456789");
        assertEquals("0123456789", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches("abc"));
        assertFalse(reader.matches("def"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Abc");
        assertTrue(reader.matchesIgnoreCase("abc"));
        assertFalse(reader.matchesIgnoreCase("DEF"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('c', 'd'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAnySorted('a', 'b'));
        assertFalse(reader.matchesAnySorted('c', 'd'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc123");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("a"));
        assertFalse(reader.matchConsume("d"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Abc");
        assertTrue(reader.matchConsumeIgnoreCase("abc"));
        assertFalse(reader.matchConsumeIgnoreCase("DEF"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("html");
        assertTrue(reader.containsIgnoreCase("HTML"));
        assertFalse(reader.containsIgnoreCase("CSS"));
    }

}