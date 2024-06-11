import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Hello World");
    }

    // Test for pos()
    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        characterReader.consume(); // Consume the first character
        assertEquals(1, characterReader.pos());
    }

    // Test for isEmpty()
    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        characterReader = new CharacterReader(""); // Create an empty character reader
        assertTrue(characterReader.isEmpty());
    }

    // Test for current()
    @Test
    public void testCurrent() {
        assertEquals('H', characterReader.current());
        characterReader.consume(); // Consume the first character
        assertEquals('e', characterReader.current());
    }

    // Test for consume()
    @Test
    public void testConsume() {
        assertEquals('H', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals('l', characterReader.consume());
    }

    // Test for unconsume()
    @Test
    public void testUnconsume() {
        characterReader.consume(); // Consume the first character
        characterReader.unconsume(); // Unconsume the consumed character
        assertEquals('H', characterReader.current());
    }

    // Test for advance()
    @Test
    public void testAdvance() {
        assertEquals('e', characterReader.advance());
        assertEquals('l', characterReader.advance());
        assertEquals('l', characterReader.advance());
    }

    // Test for mark() and rewindToMark()
    @Test
    public void testMarkAndRewindToMark() {
        characterReader.consume(); // Consume the first character
        characterReader.mark(); // Mark the current position
        characterReader.consume(); // Consume the second character
        characterReader.rewindToMark(); // Rewind to the marked position
        assertEquals('H', characterReader.current());
    }

    // Test for consumeAsString()
    @Test
    public void testConsumeAsString() {
        assertEquals("H", characterReader.consumeAsString());
        assertEquals("e", characterReader.consumeAsString());
        assertEquals("l", characterReader.consumeAsString());
    }

    // Test for consumeTo(char c)
    @Test
    public void testConsumeToChar() {
        assertEquals("Hello W", characterReader.consumeTo('o'));
        assertEquals("orld", characterReader.consumeTo('d'));
    }

    // Test for consumeTo(String seq)
    @Test
    public void testConsumeToString() {
        assertEquals("Hello W", characterReader.consumeTo("orl"));
        assertEquals("orld", characterReader.consumeTo("d"));
    }

    // Test for consumeToAny(char... seq)
    @Test
    public void testConsumeToAny() {
        assertEquals("Hell", characterReader.consumeToAny('o', 'W'));
        assertEquals("o", characterReader.consumeToAny('o', 'r', 'l', 'd'));
    }

    // Test for consumeToEnd()
    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello World", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }

    // Test for consumeLetterSequence()
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", characterReader.consumeLetterSequence());
        assertEquals(" ", characterReader.consumeLetterSequence());
    }

    // Test for consumeHexSequence()
    @Test
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("ABC123");
        assertEquals("ABC123", characterReader.consumeHexSequence());
        assertTrue(characterReader.isEmpty());
    }

    // Test for consumeDigitSequence()
    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("123ABC");
        assertEquals("123", characterReader.consumeDigitSequence());
        assertTrue(characterReader.isEmpty());
    }

    // Test for matches(char c)
    @Test
    public void testMatchesChar() {
        assertTrue(characterReader.matches('H'));
        characterReader.consume(); // Consume the first character
        assertTrue(characterReader.matches('e'));
        assertFalse(characterReader.matches('H'));
    }

    // Test for matches(String seq)
    @Test
    public void testMatchesString() {
        assertTrue(characterReader.matches("Hello"));
        characterReader.consume(); // Consume the first character
        assertFalse(characterReader.matches("Hello"));
        assertTrue(characterReader.matches("ello"));
    }

    // Test for matchesIgnoreCase(String seq)
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("Hello"));
        characterReader.consume(); // Consume the first character
        assertFalse(characterReader.matchesIgnoreCase("Hello"));
        assertTrue(characterReader.matchesIgnoreCase("ELLO"));
    }

    // Test for matchesAny(char... seq)
    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('H', 'e', 'l'));
        characterReader.consume(); // Consume the first character
        assertFalse(characterReader.matchesAny('H', 'e', 'l'));
        assertTrue(characterReader.matchesAny('e', 'l', 'o'));
    }

    // Test for matchesLetter()
    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.consume(); // Consume the first character
        assertFalse(characterReader.matchesLetter());
        assertTrue(characterReader.matchesLetter());
    }

    // Test for matchesDigit()
    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.consume(); // Consume the first character
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit());
    }

    // Test for matchConsume(String seq)
    @Test
    public void testMatchConsumeString() {
        assertTrue(characterReader.matchConsume("Hello"));
        assertFalse(characterReader.matchConsume("Hello"));
        assertTrue(characterReader.matchConsume(" "));
    }

    // Test for matchConsumeIgnoreCase(String seq)
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("Hello"));
        assertFalse(characterReader.matchConsumeIgnoreCase("Hello"));
        assertTrue(characterReader.matchConsumeIgnoreCase(" "));
    }

    // Test for containsIgnoreCase(String seq)
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("Hello"));
        assertFalse(characterReader.containsIgnoreCase("hello"));
    }

    // Test for toString()
    @Test
    public void testToString() {
        assertEquals("Hello World", characterReader.toString());
        characterReader.consume(); // Consume the first character
        assertEquals("ello World", characterReader.toString());
    }
}