import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "This is a test input string";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input, reader.input);
        assertEquals(input.length(), reader.length);
        assertEquals(0, reader.pos);
        assertEquals(0, reader.mark);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        assertTrue(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrentAndConsume() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('h', reader.current());
        reader.consume();
        // ... test all characters in the input string
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        reader.advance();
        assertEquals('h', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        String str = reader.consumeAsString();
        assertEquals("is a test input ", str);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        String str = reader.consumeTo(' ');
        assertEquals("is a test input ", str);
        reader.consume();
        str = reader.consumeTo(' ');
        assertEquals("is a test input s", str);
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeToNegativeIndex() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        reader.consumeTo(-1);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        String str = reader.consumeToEnd();
        assertEquals("string", str);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        String str = reader.consumeLetterSequence();
        assertEquals("is a test input", str);
        reader.consumeLetterSequence();
        assertEquals("string", reader.consumeLetterSequence());
        reader.consumeLetterSequence();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("This is a test #input string #with #hex #sequences");
        String str = reader.consumeHexSequence();
        assertEquals("test", str);
        reader.consumeHexSequence();
        assertEquals("input", reader.consumeHexSequence());
        reader.consumeHexSequence();
        assertEquals("string", reader.consumeHexSequence());
        reader.consumeHexSequence();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("This is a test 123 input string with digits 456 and 789");
        String str = reader.consumeDigitSequence();
        assertEquals("123", str);
        reader.consumeDigitSequence();
        assertEquals("456", reader.consumeDigitSequence());
        reader.consumeDigitSequence();
        assertEquals("789", reader.consumeDigitSequence());
        reader.consumeDigitSequence();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        assertTrue(reader.matches('i'));
        assertTrue(reader.matches("test"));
        assertFalse(reader.matches('x'));
        assertFalse(reader.matches("input"));
        reader.consume('i');
        assertFalse(reader.matches('i'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        assertTrue(reader.matchesIgnoreCase("is"));
        assertTrue(reader.matchesIgnoreCase("input"));
        assertFalse(reader.matchesIgnoreCase("TEST"));
        reader.consumeLetterSequence();
        assertTrue(reader.matchesIgnoreCase("string"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        assertTrue(reader.matchesAny('i', 's'));
        assertTrue(reader.matchesAny("is", "input"));
        assertFalse(reader.matchesAny('x', 's'));
        reader.consumeLetterSequence();
        assertTrue(reader.matchesAny('t', 'r'));
        reader.consumeLetterSequence();
        assertTrue(reader.matchesAny('i', 'g'));
        reader.consumeLetterSequence();
        assertFalse(reader.matchesAny('g', 'g'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        // ... test all letters
        reader.consumeLetterSequence();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        // ... test all digits
        reader.consumeDigitSequence();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("This is a test <input> string");
        assertTrue(reader.matchConsume("<input>"));
        assertEquals(6, reader.pos);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("This is a test <input> string");
        assertTrue(reader.matchConsumeIgnoreCase("<INPUT>"));
        assertEquals(6, reader.pos);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test Title</title></head><body>Test Body</body></html>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("body"));
        assertFalse(reader.containsIgnoreCase("Title"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("This is a test input string");
        assertEquals("input string", reader.toString());
    }
}