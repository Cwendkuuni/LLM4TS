import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        String input = "Hello, World!";
        CharacterReader reader = new CharacterReader(input);

        assertEquals("Hello, World!", reader.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderNullInput() {
        CharacterReader reader = new CharacterReader(null);
    }

    // Test methods here...

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        assertEquals("World!", reader.toString());
    }

    @Test
    public void testConsumeEOF() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(-1, reader.consume());
    }

    @Test
    public void testConsumeSingleCharacter() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals('A', reader.consume());
        assertEquals('B', reader.consume());
        assertEquals('C', reader.consume());
        assertEquals(-1, reader.consume());
    }

    // Edge case and exception handling tests here...

    @Test
    public void testConsumeAll() {
        CharacterReader reader = new CharacterReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        assertEquals(expected, reader.consumeToEnd());
    }

    // Other test methods here...

    @Test
    public void testMarkRewind() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        reader.mark();
        reader.consume('a');
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1234567890ABCDEFABCDEFABCDEF");
        assertEquals("1234567890ABCDEFABCDEFABCDEF", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123456789012345678901234567890");
        assertEquals("1234567890", reader.consumeDigitSequence());
    }

    // Test methods for other CharacterReader methods here...

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches("xyz"));
    }

    // Test methods for other CharacterReader methods here...

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertTrue(reader.matchConsume("ab"));
        assertEquals("cd", reader.toString());
    }

    // Test methods for other CharacterReader methods here...

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABCdefghijklmNOPQRSTuvwXYZ");
        assertTrue(reader.containsIgnoreCase("ab"));
        assertTrue(reader.containsIgnoreCase("AB"));
        assertFalse(reader.containsIgnoreCase("xyz"));
    }

    // Test methods for other CharacterReader methods here...

    @Test
    public void testConsumeMultiByteCharacter() {
        CharacterReader reader = new CharacterReader("\u4E2D\u6587"); // Chinese characters
        assertEquals("\u4E2D\u6587", reader.consumeToEnd());
    }

    // Test methods for other CharacterReader methods here...

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeMalformedUTF8() {
        CharacterReader reader = new CharacterReader("\uD800\uDC00"); // Invalid UTF-8 sequence
        reader.consume();
    }

    // Test methods for other CharacterReader methods here...

    @Test
    public void testConsumeControlCharacter() {
        CharacterReader reader = new CharacterReader("\u0001"); // Control character
        assertEquals('\u0001', reader.consume());
    }

    // Test methods for other CharacterReader methods here...

    @Test
    public void testConsumeWhitespaceCharacter() {
        CharacterReader reader = new CharacterReader(" \t\r\n");
        assertEquals(" ", reader.consume());
        assertEquals("\t", reader.consume());
        assertEquals("\r", reader.consume());
        assertEquals("\n", reader.consume());
        assertEquals(-1, reader.consume());
    }

    // Test methods for other CharacterReader methods here...

    @Test(expected = IllegalStateException.class)
    public void testConsumeNullCharacter() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        reader.consume((char) 0); // Null character
    }

    // Test methods for other CharacterReader methods here...

    @Test
    public void testConsumeCharacterOutsideASCII() {
        CharacterReader reader = new CharacterReader("\uD800"); // High Surrogate
        assertEquals('\uD800', reader.consume());
    }
}