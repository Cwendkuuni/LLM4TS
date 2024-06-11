import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Hello");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkandRewindToMark() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("H", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello world");
        assertEquals("Hello", reader.consumeTo(' '));
        assertEquals("world", reader.consumeTo(' '));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello world");
        assertEquals("Hello", reader.consumeToAny(' ', 'w'));
        assertEquals("", reader.consumeToAny(' ', 'w'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello world");
        assertEquals("Hello world", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Hello12");
        assertEquals("Hello", reader.consumeLetterSequence());
        assertEquals("12", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ABC123");
        assertEquals("ABC", reader.consumeHexSequence());
        assertEquals("123", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123ABC");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("ABC", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matches('H'));
        assertFalse(reader.matches('e'));
        assertTrue(reader.matchesIgnoreCase("Hel"));
        assertFalse(reader.matchesIgnoreCase("Wor"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesAny('e', 'o'));
        assertFalse(reader.matchesAny('o', 'w'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchConsume("H"));
        assertFalse(reader.matchConsume("W"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchConsumeIgnoreCase("He"));
        assertFalse(reader.matchConsumeIgnoreCase("We"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.containsIgnoreCase("el"));
        assertFalse(reader.containsIgnoreCase("no"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("Hello", reader.toString());
    }
}