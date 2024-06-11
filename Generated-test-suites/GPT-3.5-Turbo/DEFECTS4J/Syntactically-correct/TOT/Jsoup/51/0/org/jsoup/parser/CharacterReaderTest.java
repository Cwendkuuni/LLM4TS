import org.junit.*;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader("");
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals('t', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader("test");
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader("test");
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader("test");
        characterReader.mark();
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader characterReader = new CharacterReader("test");
        characterReader.advance();
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals("t", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals(2, characterReader.nextIndexOf('s'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals(1, characterReader.nextIndexOf("es"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals("te", characterReader.consumeTo('s'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals("t", characterReader.consumeToAny('e','s'));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals("e", characterReader.consumeToAnySorted('s','e'));
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals("test", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader("<tag>");
        assertEquals("<tag", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals("test", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("Test123");
        assertEquals("Test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader("Test123");
        assertEquals("Test123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("AB12");
        assertEquals("AB12", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("123Test");
        assertEquals("123", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader("test");
        assertTrue(characterReader.matches('t'));
    }

    @Test
    public void testMatches2() {
        CharacterReader characterReader = new CharacterReader("test");
        assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatches3() {
        CharacterReader characterReader = new CharacterReader("test");
        assertTrue(characterReader.matches("te"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("test");
        assertTrue(characterReader.matchesIgnoreCase("TE"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader("test");
        assertTrue(characterReader.matchesAny('t','e'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader("test");
        assertTrue(characterReader.matchesAnySorted('e','t'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader("test");
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader("test");
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader("test");
        assertTrue(characterReader.matchConsume("te"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("test");
        assertTrue(characterReader.matchConsumeIgnoreCase("TE"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("test");
        assertTrue(characterReader.containsIgnoreCase("es"));
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals("test", characterReader.toString());
    }

    @Test
    public void testRangeEquals() {
        CharacterReader characterReader = new CharacterReader("test");
        assertFalse(characterReader.rangeEquals(0, 1, "e"));
    }
}