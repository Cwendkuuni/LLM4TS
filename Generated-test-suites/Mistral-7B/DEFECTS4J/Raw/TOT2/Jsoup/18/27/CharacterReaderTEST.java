import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstruction() {
        // Test constructor with valid input
        CharacterReader reader = new CharacterReader("TestInput");
        assertEquals("TestInput", reader.input);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("TestInput");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("TestInput");
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("TestInput");
        assertEquals('T', reader.consume());
        assertEquals('e', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("TestInput");
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("TestInput");
        reader.advance();
        assertEquals('e', reader.current());
    }

    // ... Testing other methods with basic test cases ...

    // Testing edge cases and exception handling scenarios
    @Test(expected = NullPointerException.class)
    public void testCharacterReaderNullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testConsumeEnd() {
        CharacterReader reader = new CharacterReader("TestInput");
        for (int i = 0; i < reader.length(); i++) {
            reader.consume();
        }
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeSpecialCharacters() {
        CharacterReader reader = new CharacterReader("TestInput!@#$%^&*()_+={}[]|\\:;\"'<,>.?/");
        assertEquals('#', reader.consume());
    }

    @Test
    public void testConsumeMultipleTargetCharacters() {
        CharacterReader reader = new CharacterReader("TestTargetTestTarget");
        String consumed = reader.consumeTo('T');
        assertEquals("Test", consumed);
        assertEquals('R', reader.current());
    }

    @Test
    public void testConsumeTargetCharacterAtBeginning() {
        CharacterReader reader = new CharacterReader("TargetTestInput");
        String consumed = reader.consumeTo('T');
        assertEquals("Target", consumed);
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeEmptyTargetString() {
        CharacterReader reader = new CharacterReader("TestInput");
        String consumed = reader.consumeTo("");
        assertEquals("", consumed);
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeTargetStringNotAtBeginning() {
        CharacterReader reader = new CharacterReader("TestInputTest");
        String consumed = reader.consumeTo("Test");
        assertEquals("Input", consumed);
        assertEquals('T', reader.current());
    }

    @Test
    public void testMatchesCharacterMultipleTimes() {
        CharacterReader reader = new CharacterReader("TestTest");
        assertTrue(reader.matches('T'));
        assertTrue(reader.matches('T'));
    }

    @Test
    public void testMatchesCharacterAtBeginning() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matches('T'));
    }

    @Test
    public void testMatchesEmptyString() {
        CharacterReader reader = new CharacterReader("Test");
        assertFalse(reader.matches(""));
    }

    @Test
    public void testMatchesStringNotAtBeginning() {
        CharacterReader reader = new CharacterReader("TestInput");
        assertFalse(reader.matches("Input"));
    }

    @Test
    public void testMatchesIgnoreCaseCharacterMultipleTimes() {
        CharacterReader reader = new CharacterReader("TestTest");
        assertTrue(reader.matchesIgnoreCase('T'));
        assertTrue(reader.matchesIgnoreCase('T'));
    }

    @Test
    public void testMatchesIgnoreCaseCharacterAtBeginning() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matchesIgnoreCase('T'));
    }

    @Test
    public void testMatchesIgnoreCaseEmptyString() {
        CharacterReader reader = new CharacterReader("Test");
        assertFalse(reader.matchesIgnoreCase(""));
    }

    @Test
    public void testMatchesIgnoreCaseStringNotAtBeginning() {
        CharacterReader reader = new CharacterReader("TestInput");
        assertFalse(reader.matchesIgnoreCase("Input"));
    }

    @Test
    public void testContainsIgnoreCaseStringMultipleTimes() {
        CharacterReader reader = new CharacterReader("TestTestTest");
        assertTrue(reader.containsIgnoreCase("Test"));
        assertTrue(reader.containsIgnoreCase("Test"));
    }

    @Test
    public void testContainsIgnoreCaseStringAtBeginning() {
        CharacterReader reader = new CharacterReader("TestTest");
        assertTrue(reader.containsIgnoreCase("Test"));
    }

    @Test
    public void testContainsIgnoreCaseEmptyString() {
        CharacterReader reader = new CharacterReader("Test");
        assertFalse(reader.containsIgnoreCase(""));
    }

    @Test
    public void testContainsIgnoreCaseStringNotAtBeginning() {
        CharacterReader reader = new CharacterReader("TestInput");
        assertFalse(reader.containsIgnoreCase("Input"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals("", reader.toString());
    }
}