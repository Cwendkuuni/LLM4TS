import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('H', reader.current());
        assertEquals('H', reader.consume());
        assertEquals('H', reader.current());
        assertEquals(1, reader.pos());
        assertEquals("llo W", reader.consumeAsString());
        assertEquals("World", reader.consumeToEnd());
    }

    // Test other methods with similar logic

    // Edge cases and exception handling tests

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        CharacterReader reader = new CharacterReader(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeEndOfEmptyString() {
        CharacterReader reader = new CharacterReader("");
        reader.consumeToEnd();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeInvalidCharacterSequence() {
        CharacterReader reader = new CharacterReader("Hello\0World");
        reader.consumeToEnd();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeInvalidCharacterSequence2() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        reader.consumeToAny('\0');
    }

    @Test
    public void testEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
        assertEquals(-1, reader.consume());
    }

    @Test
    public void testSpecialCharacters() {
        String input = "\r\nHello World\tHere are some special characters: \0\b\f";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("", reader.consumeAsString());
        assertEquals("Hello World\tHere are some special characters: ", reader.consumeToEnd());
    }

    // Test other edge cases and exception handling methods
}