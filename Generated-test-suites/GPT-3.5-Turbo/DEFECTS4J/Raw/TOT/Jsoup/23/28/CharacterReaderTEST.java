import org.mockito.runners.MockitoJUnitRunner;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Sample Input");
    }

    @Test
    public void testPos() {
        int expectedPos = 0;
        assertEquals(expectedPos, characterReader.pos()); // Typical Use Case

        characterReader.consume();
        expectedPos = 1;
        assertEquals(expectedPos, characterReader.pos()); // Typical Use Case
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty()); // Typical Use Case

        for (int i = 0; i < 12; i++) {
            characterReader.consume();
        }

        assertTrue(characterReader.isEmpty()); // Edge Case
    }

    @Test
    public void testCurrent() {
        char expectedCurrent = 'S';
        assertEquals(expectedCurrent, characterReader.current()); // Typical Use Case

        for (int i = 0; i < 12; i++) {
            characterReader.consume();
        }

        assertEquals(CharacterReader.EOF, characterReader.current()); // Edge Case
    }

    @Test
    public void testConsume() {
        char expectedVal = 'S';
        assertEquals(expectedVal, characterReader.consume()); // Typical Use Case

        for (int i = 0; i < 12; i++) {
            characterReader.consume();
        }

        expectedVal = CharacterReader.EOF;
        assertEquals(expectedVal, characterReader.consume()); // Edge Case
    }

    @Test
    public void testUnconsume() {
        characterReader.consume(); // Consumes 'S'

        characterReader.unconsume(); // Unconsumes 'S'

        char expectedVal = 'S';
        assertEquals(expectedVal, characterReader.consume()); // Typical Use Case
    }

    @Test
    public void testAdvance() {
        characterReader.advance(); // Advances position

        int expectedPos = 1;
        assertEquals(expectedPos, characterReader.pos()); // Typical Use Case
    }

    @Test
    public void testMark() {
        characterReader.mark(); // Marks current position

        int expectedMark = 0;
        assertEquals(expectedMark, characterReader.pos()); // Typical Use Case
    }

    @Test
    public void testRewindToMark() {
        characterReader.mark(); // Marks current position
        characterReader.advance(); // Advances position

        characterReader.rewindToMark(); // Rewinds position to marked position

        int expectedPos = 0;
        assertEquals(expectedPos, characterReader.pos()); // Typical Use Case
    }

    @Test
    public void testConsumeAsString() {
        String expectedString = "S";
        assertEquals(expectedString, characterReader.consumeAsString()); // Typical Use Case
    }

    @Test
    public void testConsumeToChar() {
        String expectedString = "Sam";
        assertEquals(expectedString, characterReader.consumeTo('p')); // Typical Use Case

        expectedString = "ple Input";
        assertEquals(expectedString, characterReader.consumeToEnd()); // Edge Case
    }

    @Test
    public void testConsumeToString() {
        String expectedString = "Sample ";
        assertEquals(expectedString, characterReader.consumeTo("In")); // Typical Use Case

        expectedString = "put";
        assertEquals(expectedString, characterReader.consumeToEnd()); // Edge Case
    }

    @Test
    public void testConsumeToAny() {
        String expectedString = "Sample";
        assertEquals(expectedString, characterReader.consumeToAny(' ', 'I')); // Typical Use Case

        expectedString = "";
        assertEquals(expectedString, characterReader.consumeToEnd()); // Edge Case
    }

    @Test
    public void testConsumeToEnd() {
        String expectedString = "Sample Input";
        assertEquals(expectedString, characterReader.consumeToEnd()); // Typical Use Case
    }

    @Test
    public void testConsumeLetterSequence() {
        characterReader.consumeTo("Sample "); // Consumes 'Sample '

        String expectedString = "In";
        assertEquals(expectedString, characterReader.consumeLetterSequence()); // Typical Use Case

        String expectedEmptyString = "";
        assertEquals(expectedEmptyString, characterReader.consumeLetterSequence()); // Edge Case
    }

    @Test
    public void testConsumeHexSequence() {
        characterReader.consumeTo("Sample Input"); // Consumes the whole input

        String expectedString = "";
        assertEquals(expectedString, characterReader.consumeHexSequence()); // Typical Use Case
    }

    @Test
    public void testConsumeDigitSequence() {
        String expectedString = "";
        assertEquals(expectedString, characterReader.consumeDigitSequence()); // Typical Use Case
    }

    @Test
    public void testMatchesChar() {
        assertTrue(characterReader.matches('S')); // Typical Use Case

        characterReader.consume();
        assertFalse(characterReader.matches('S')); // Edge Case
    }

    @Test
    public void testMatchesString() {
        assertTrue(characterReader.matches("Sam")); // Typical Use Case

        characterReader.consumeTo("p");
        assertFalse(characterReader.matches("Input")); // Edge Case
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("sam")); // Typical Use Case

        characterReader.consumeTo("Sample");
        assertFalse(characterReader.matchesIgnoreCase("INPUT")); // Edge Case
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('S', 'a')); // Typical Use Case

        characterReader.consume();
        assertFalse(characterReader.matchesAny('S', 'a')); // Edge Case
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter()); // Typical Use Case

        characterReader.consumeTo("Sample");
        assertFalse(characterReader.matchesLetter()); // Edge Case
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit()); // Typical Use Case

        characterReader.consumeTo("Sample");
        assertFalse(characterReader.matchesDigit()); // Edge Case
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Sample")); // Typical Use Case

        assertFalse(characterReader.matchConsume("Sample")); // Edge Case
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("Sample")); // Typical Use Case

        assertFalse(characterReader.matchConsumeIgnoreCase("Sample")); // Edge Case
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("sample")); // Typical Use Case

        characterReader.consumeTo("Sample");
        assertFalse(characterReader.containsIgnoreCase("sample")); // Edge Case
    }

    @Test
    public void testToString() {
        String expectedString = "Sample Input";
        assertEquals(expectedString, characterReader.toString()); // Typical Use Case
    }
}