import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testIsEmpty_notEmpty() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.current());
    }

    @Test
    public void testCurrent_empty() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
    }

    @Test
    public void testConsume_empty() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('c', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("abcdef");

        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();

        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("a", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(3, reader.nextIndexOf('d'));
    }

    @Test
    public void testNextIndexOf_notFound() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(-1, reader.nextIndexOf('g'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(1, reader.nextIndexOf("bc"));
    }

    @Test
    public void testNextIndexOfCharSequence_notFound() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(-1, reader.nextIndexOf("cd"));
    }

    @Test
    public void testNextIndexOfCharSequence_empty() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(-1, reader.nextIndexOf("cd"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeTo('d'));
    }

    @Test
    public void testConsumeTo_notFound() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeTo('g'));
    }

    @Test
    public void testConsumeTo_String() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("ab", reader.consumeTo("cd"));
    }

    @Test
    public void testConsumeTo_String_notFound() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeTo("cd"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeToAny('d', 'e', 'f'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef123");
        assertEquals("abcdef", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        assertEquals("abcd123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcdef123");
        assertEquals("abcdef123", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abcdef123");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
    }

    @Test
    public void testMatches_String() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches("abc"));
        assertFalse(reader.matches("def"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesIgnoreCase("AbC"));
        assertFalse(reader.matchesIgnoreCase("def"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny('b', 'c', 'd'));
        assertFalse(reader.matchesAny('e', 'f', 'g'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("a1bcdef");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchConsume("abc"));
        assertFalse(reader.matchConsume("def"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchConsumeIgnoreCase("AbC"));
        assertFalse(reader.matchConsumeIgnoreCase("def"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertFalse(reader.containsIgnoreCase("hello"));
        assertTrue(reader.containsIgnoreCase("ABC"));
    }

}