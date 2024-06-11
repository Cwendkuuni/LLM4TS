import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test if pos() method returns the correct position
        CharacterReader reader = new CharacterReader("test");
        int expectedPosition = 0;
        int actualPosition = reader.pos();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testIsEmpty() {
        // Test if isEmpty() method returns true for an empty reader
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        // Test if isEmpty() method returns false for a non-empty reader
        CharacterReader reader2 = new CharacterReader("not empty");
        assertFalse(reader2.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Test if current() method returns the current character, or EOF if the reader is empty
        CharacterReader reader = new CharacterReader("test");
        char expectedCurrent = 't';
        char actualCurrent = reader.current();
        assertEquals(expectedCurrent, actualCurrent);

        CharacterReader reader2 = new CharacterReader("");
        char expectedCurrent2 = CharacterReader.EOF;
        char actualCurrent2 = reader2.current();
        assertEquals(expectedCurrent2, actualCurrent2);
    }

    @Test
    public void testConsume() {
        // Test if consume() method consumes the current character and returns it
        CharacterReader reader = new CharacterReader("test");
        char expectedConsumed = 't';
        char actualConsumed = reader.consume();
        assertEquals(expectedConsumed, actualConsumed);

        // Test if consume() method advances the position to the next character
        char expectedPosition = 1;
        int actualPosition = reader.pos();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testUnconsume() {
        // Test if unconsume() method sets the position back to the previous character
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        int expectedPosition = 0;
        int actualPosition = reader.pos();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testAdvance() {
        // Test if advance() method advances the position to the next character
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        int expectedPosition = 1;
        int actualPosition = reader.pos();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testMark() {
        // Test if mark() method sets the mark to the current position
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        int expectedMark = 0;
        int actualMark = reader.pos();
        assertEquals(expectedMark, actualMark);
    }

    @Test
    public void testRewindToMark() {
        // Test if rewindToMark() method sets the position back to the mark
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        int expectedPosition = 0;
        int actualPosition = reader.pos();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testConsumeAsString() {
        // Test if consumeAsString() method consumes the current character and returns it as a string
        CharacterReader reader = new CharacterReader("test");
        String expectedConsumed = "t";
        String actualConsumed = reader.consumeAsString();
        assertEquals(expectedConsumed, actualConsumed);

        // Test if consumeAsString() method advances the position to the next character
        int expectedPosition = 1;
        int actualPosition = reader.pos();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testConsumeTo() {
        // Test if consumeTo(char c) method consumes characters until it reaches the provided character
        CharacterReader reader = new CharacterReader("test");
        String expectedConsumed = "te";
        String actualConsumed = reader.consumeTo('s');
        assertEquals(expectedConsumed, actualConsumed);

        // Test if consumeTo(char c) method advances the position to the next character after the provided character
        int expectedPosition = 3;
        int actualPosition = reader.pos();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testConsumeToAny() {
        // Test if consumeToAny(char... seq) method consumes characters until it reaches any of the provided characters
        CharacterReader reader = new CharacterReader("test");
        String expectedConsumed = "te";
        String actualConsumed = reader.consumeToAny('s', 't');
        assertEquals(expectedConsumed, actualConsumed);

        // Test if consumeToAny(char... seq) method advances the position to the next character after any of the provided characters
        int expectedPosition = 3;
        int actualPosition = reader.pos();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testConsumeToEnd() {
        // Test if consumeToEnd() method consumes all the remaining characters and returns them as a string
        CharacterReader reader = new CharacterReader("test");
        String expectedConsumed = "test";
        String actualConsumed = reader.consumeToEnd();
        assertEquals(expectedConsumed, actualConsumed);

        // Test if consumeToEnd() method sets the position to the end of the input
        int expectedPosition = 4;
        int actualPosition = reader.pos();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testConsumeLetterSequence() {
        // Test if consumeLetterSequence() method consumes a sequence of letters and returns them as a string
        CharacterReader reader = new CharacterReader("test123");
        String expectedConsumed = "test";
        String actualConsumed = reader.consumeLetterSequence();
        assertEquals(expectedConsumed, actualConsumed);

        // Test if consumeLetterSequence() method advances the position to the next non-letter character
        int expectedPosition = 4;
        int actualPosition = reader.pos();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testConsumeDigitSequence() {
        // Test if consumeDigitSequence() method consumes a sequence of digits and returns them as a string
        CharacterReader reader = new CharacterReader("test123");
        String expectedConsumed = "123";
        String actualConsumed = reader.consumeDigitSequence();
        assertEquals(expectedConsumed, actualConsumed);

        // Test if consumeDigitSequence() method advances the position to the next non-digit character
        int expectedPosition = 7;
        int actualPosition = reader.pos();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testMatches() {
        // Test if matches(char c) method returns true if the current character matches the provided character
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));

        // Test if matches(char c) method returns false if the current character does not match the provided character
        assertFalse(reader.matches('s'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        // Test if matchesIgnoreCase(String seq) method returns true if the current characters match the provided string case-insensitively
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("TEsT"));

        // Test if matchesIgnoreCase(String seq) method returns false if the current characters do not match the provided string case-insensitively
        assertFalse(reader.matchesIgnoreCase("no match"));
    }

    @Test
    public void testMatchesAny() {
        // Test if matchesAny(char... seq) method returns true if the current character matches any of the provided characters
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('t', 's'));

        // Test if matchesAny(char... seq) method returns false if the current character does not match any of the provided characters
        assertFalse(reader.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesLetter() {
        // Test if matchesLetter() method returns true if the current character is a letter
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());

        // Test if matchesLetter() method returns false if the current character is not a letter
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        // Test if matchesDigit() method returns true if the current character is a digit
        CharacterReader reader = new CharacterReader("test123");
        assertFalse(reader.matchesDigit());

        // Test if matchesDigit() method returns false if the current character is not a digit
        reader.consumeLetterSequence();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        // Test if matchConsume(String seq) method returns true and consumes the provided string if it matches the current characters
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("te"));
        int expectedPosition = 2;
        int actualPosition = reader.pos();
        assertEquals(expectedPosition, actualPosition);

        // Test if matchConsume(String seq) method returns false and does not consume anything if the provided string does not match the current characters
        assertFalse(reader.matchConsume("no match"));
        int expectedPosition2 = 2;
        int actualPosition2 = reader.pos();
        assertEquals(expectedPosition2, actualPosition2);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        // Test if matchConsumeIgnoreCase(String seq) method returns true and consumes the provided string case-insensitively if it matches the current characters
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsumeIgnoreCase("TE"));
        int expectedPosition = 2;
        int actualPosition = reader.pos();
        assertEquals(expectedPosition, actualPosition);

        // Test if matchConsumeIgnoreCase(String seq) method returns false and does not consume anything if the provided string does not match the current characters
        assertFalse(reader.matchConsumeIgnoreCase("no match"));
        int expectedPosition2 = 2;
        int actualPosition2 = reader.pos();
        assertEquals(expectedPosition2, actualPosition2);
    }

    @Test
    public void testContainsIgnoreCase() {
        // Test if containsIgnoreCase(String seq) method returns true if the provided string is present in the remaining input case-insensitively
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("TEs"));

        // Test if containsIgnoreCase(String seq) method returns false if the provided string is not present in the remaining input case-insensitively
        assertFalse(reader.containsIgnoreCase("no match"));
    }

    @Test
    public void testToString() {
        // Test if toString() method returns the remaining input as a string
        CharacterReader reader = new CharacterReader("test");
        String expectedToString = "test";
        String actualToString = reader.toString();
        assertEquals(expectedToString, actualToString);
    }
}