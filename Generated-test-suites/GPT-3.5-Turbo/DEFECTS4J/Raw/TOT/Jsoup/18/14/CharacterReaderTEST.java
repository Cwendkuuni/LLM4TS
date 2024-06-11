import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Hello World!");
    }

    @Test
    public void pos_ShouldReturnInitialPosition() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void isEmpty_ShouldReturnFalseIfNotEmpty() {
        assertFalse(reader.isEmpty());
    }

    @Test
    public void isEmpty_ShouldReturnTrueIfEmpty() {
        reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void current_ShouldReturnCurrentChar() {
        assertEquals('H', reader.current());
    }

    @Test
    public void consume_ShouldConsumeAndReturnChar() {
        assertEquals('H', reader.consume());
        assertEquals('e', reader.current());
    }

    @Test
    public void unconsume_ShouldMovePositionBack() {
        assertEquals('H', reader.consume());
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void advance_ShouldMovePositionForward() {
        assertEquals('H', reader.current());
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void mark_ShouldSetCurrentPosition() {
        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void rewindToMark_ShouldSetPositionToMark() {
        reader.unconsume();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void consumeAsString_ShouldConsumeAndReturnSubstring() {
        assertEquals("Hello", reader.consumeAsString());
        assertEquals(' ', reader.current());
    }

    @Test
    public void consumeTo_ShouldConsumeAndReturnSubstring() {
        assertEquals("Hello", reader.consumeTo(' '));
        assertEquals(' ', reader.current());
    }

    @Test
    public void consumeTo_ShouldReturnRemainingSubstringIfNotFound() {
        assertEquals("Hello World!", reader.consumeTo('@'));
    }

    @Test
    public void consumeTo_ShouldConsumeAndReturnSubstringIfFound() {
        assertEquals("Hello", reader.consumeTo(" "));
        assertEquals(' ', reader.current());
    }

    @Test
    public void consumeTo_ShouldConsumeAndReturnRemainingSubstringIfNotFound() {
        assertEquals("Hello World!", reader.consumeTo(" @"));
    }

    @Test
    public void consumeToAny_ShouldConsumeAndReturnSubstring() {
        assertEquals("Hello", reader.consumeToAny(' ', '!'));
        assertEquals(' ', reader.current());
    }

    @Test
    public void consumeToAny_ShouldReturnEmptyStringIfNoMatch() {
        assertEquals("", reader.consumeToAny('@', '#'));
    }

    @Test
    public void consumeToEnd_ShouldConsumeAndReturnRemainingSubstring() {
        assertEquals("Hello World!", reader.consumeToEnd());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void consumeLetterSequence_ShouldConsumeAndReturnLetterSequence() {
        assertEquals("Hello", reader.consumeLetterSequence());
        assertEquals(' ', reader.current());
    }

    @Test
    public void consumeLetterSequence_ShouldReturnEmptyStringIfNoLetterSequence() {
        reader = new CharacterReader("123456");
        assertEquals("", reader.consumeLetterSequence());
        assertEquals('1', reader.current());
    }

    @Test
    public void consumeHexSequence_ShouldConsumeAndReturnHexSequence() {
        reader = new CharacterReader("A1F");
        assertEquals("A1F", reader.consumeHexSequence());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void consumeHexSequence_ShouldReturnEmptyStringIfNoHexSequence() {
        reader = new CharacterReader("GHI");
        assertEquals("", reader.consumeHexSequence());
        assertEquals('G', reader.current());
    }

    @Test
    public void consumeDigitSequence_ShouldConsumeAndReturnDigitSequence() {
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals(' ', reader.current());
    }

    @Test
    public void consumeDigitSequence_ShouldReturnEmptyStringIfNoDigitSequence() {
        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeDigitSequence());
        assertEquals('a', reader.current());
    }

    @Test
    public void matches_ShouldReturnTrueIfMatch() {
        assertTrue(reader.matches('H'));
        assertFalse(reader.matches('h'));
    }

    @Test
    public void matches_ShouldReturnFalseIfEmpty() {
        reader = new CharacterReader("");
        assertFalse(reader.matches('H'));
    }

    @Test
    public void matches_ShouldReturnFalseIfNoMatch() {
        assertFalse(reader.matches('W'));
    }

    @Test
    public void matches_ShouldReturnTrueIfMatchSequence() {
        assertTrue(reader.matches("Hell"));
        assertFalse(reader.matches("hell"));
    }

    @Test
    public void matchesIgnoreCase_ShouldReturnTrueIfMatchIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("Hell"));
    }

    @Test
    public void matchesIgnoreCase_ShouldReturnFalseIfNoMatchIgnoreCase() {
        assertFalse(reader.matchesIgnoreCase("hell"));
    }

    @Test
    public void matchesAny_ShouldReturnTrueIfMatchAny() {
        assertTrue(reader.matchesAny('e', 'l', 'o'));
    }

    @Test
    public void matchesAny_ShouldReturnFalseIfNoMatchAny() {
        assertFalse(reader.matchesAny('a', 'b', 'c'));
    }

    @Test
    public void matchesLetter_ShouldReturnTrueIfMatchLetter() {
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void matchesLetter_ShouldReturnFalseIfNoMatchLetter() {
        reader = new CharacterReader("123");
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void matchesDigit_ShouldReturnTrueIfMatchDigit() {
        reader = new CharacterReader("456");
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void matchesDigit_ShouldReturnFalseIfNoMatchDigit() {
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void matchConsume_ShouldReturnTrueAndConsumeIfMatch() {
        assertTrue(reader.matchConsume("Hell"));
        assertEquals('o', reader.current());
    }

    @Test
    public void matchConsume_ShouldReturnFalseIfNoMatch() {
        assertFalse(reader.matchConsume("hello"));
        assertEquals('H', reader.current());
    }

    @Test
    public void matchConsumeIgnoreCase_ShouldReturnTrueAndConsumeIfMatchIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("Hell"));
        assertEquals('o', reader.current());
    }

    @Test
    public void matchConsumeIgnoreCase_ShouldReturnFalseIfNoMatchIgnoreCase() {
        assertFalse(reader.matchConsumeIgnoreCase("hello"));
        assertEquals('H', reader.current());
    }

    @Test
    public void containsIgnoreCase_ShouldReturnTrueIfContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("hello"));
    }

    @Test
    public void containsIgnoreCase_ShouldReturnFalseIfNoContainsIgnoreCase() {
        assertFalse(reader.containsIgnoreCase("goodbye"));
    }

    @Test
    public void toString_ShouldReturnRemainingSubstring() {
        assertEquals("Hello World!", reader.toString());
    }
}