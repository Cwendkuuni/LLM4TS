
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        reader = new CharacterReader("abcd");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.advance();
        assertEquals('b', reader.current());
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
        assertEquals("a", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals(2, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('e'));
    }

    @Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals(2, reader.nextIndexOf("cd"));
        assertEquals(-1, reader.nextIndexOf("ef"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("a", reader.consumeTo('b'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("a", reader.consumeToAny('b', 'c'));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("abc", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<a>");
        assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("abcd", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc123", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('d'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.matches("abc"));
        assertFalse(reader.matches("def"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC123");
        assertTrue(reader.matchesIgnoreCase("abc"));
        assertFalse(reader.matchesIgnoreCase("def"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('d', 'e'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.matchesAnySorted(new char[] {'a', 'b'}));
        assertFalse(reader.matchesAnySorted(new char[] {'d', 'e'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc123");
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
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.matchConsume("abc"));
        assertFalse(reader.matchConsume("def"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC123");
        assertTrue(reader.matchConsumeIgnoreCase("abc"));
        assertFalse(reader.matchConsumeIgnoreCase("def"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC123");
        assertTrue(reader.containsIgnoreCase("ABC"));
        assertTrue(reader.containsIgnoreCase("abc"));
        assertFalse(reader.containsIgnoreCase("def"));
    }
}
