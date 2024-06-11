import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals(false, reader.isEmpty());

        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.current());

        reader.consume();
        assertEquals('b', reader.current());

        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();

        reader.advance();
        reader.advance();
        reader.advance();

        assertEquals('d', reader.current());

        reader.rewindToMark();

        assertEquals('b', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("a", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo_char() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("ab", reader.consumeTo('c'));
    }

    @Test
    public void testConsumeTo_String() {
        CharacterReader reader = new CharacterReader("abcdxyz");
        assertEquals("abcd", reader.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcd1234");
        assertEquals("abcd", reader.consumeToAny('1', '2', '3', '4'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("abcd", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc123", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches_char() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals(true, reader.matches('a'));
        assertEquals(false, reader.matches('b'));
    }

    @Test
    public void testMatches_String() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals(true, reader.matches("abc"));
        assertEquals(false, reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase_String() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals(true, reader.matchesIgnoreCase("ABC"));
        assertEquals(false, reader.matchesIgnoreCase("XYZ"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals(true, reader.matchesAny('1', 'a', 'b', 'c'));
        assertEquals(false, reader.matchesAny('2', 'd', 'e', 'f'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals(true, reader.matchesLetter());
        reader.consume();
        assertEquals(false, reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals(false, reader.matchesDigit());
        reader.consume();
        assertEquals(true, reader.matchesDigit());
    }

    @Test
    public void testMatchConsume_String() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals(true, reader.matchConsume("abc"));
        assertEquals(false, reader.matchConsume("xyz"));
        assertEquals('1', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase_String() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals(true, reader.matchConsumeIgnoreCase("ABC"));
        assertEquals(false, reader.matchConsumeIgnoreCase("XYZ"));
        assertEquals('1', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc123XYZ");
        assertEquals(true, reader.containsIgnoreCase("xyz"));
        assertEquals(false, reader.containsIgnoreCase("def"));
    }

    // Add more test cases for the remaining methods...

    //

    // Existing test cases from above

    // Additional test cases for other methods

    //
}