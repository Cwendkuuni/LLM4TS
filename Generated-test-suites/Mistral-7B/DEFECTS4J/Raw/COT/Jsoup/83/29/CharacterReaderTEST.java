import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest { // Replace this with the actual name of your test class

    @Test
    public void testCharacterReaderConstructor() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertNotNull(reader);
    }

    @Test
    public void testCharacterReaderReaderConstructor() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertNotNull(reader);
    }

    @Test
    public void testCharacterReaderStringConstructor() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        assertNotNull(reader);
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Buffer Up"));
        reader.bufferUp();
        assertEquals(-1, reader.current());
        reader.bufferUp();
        reader.bufferUp();
        reader.bufferUp();
        reader.bufferUp();
        reader.bufferUp();
        reader.bufferUp();

        char expected = 'T';
        char actual = reader.current();
        assertEquals(expected, actual);

        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();

        assertEquals(expected, reader.current());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Position"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();

        int expected = 5;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Empty"));
        assertFalse(reader.isEmpty());

        reader.bufferUp();
        reader.consume();
        reader.consume();

        assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Test IOException"));
        reader.bufferUp();
        reader.reader.close();
        reader.consume();
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Current"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();

        char expected = 'T';
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Consume"));
        reader.bufferUp();
        reader.bufferUp();
        reader.bufferUp();

        char expected = 'T';
        char actual = reader.current();
        assertEquals(expected, actual);

        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();

        assertEquals(expected, reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Unconsume"));
        reader.bufferUp();
        reader.bufferUp();
        reader.bufferUp();

        char expected = 'T';
        char actual = reader.current();
        assertEquals(expected, actual);

        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();

        reader.unconsume();
        reader.unconsume();
        reader.unconsume();

        assertEquals(expected, reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Advance"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();

        int expected = 6;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Mark"));
        reader.bufferUp();
        reader.mark();
        reader.advance();
        reader.advance();
        reader.advance();

        reader.rewindToMark();

        int expected = 3;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Next Index Of"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        int expected = 3;
        int actual = reader.nextIndexOf('s');
        assertEquals(expected, actual);

        expected = 5;
        actual = reader.nextIndexOf('e');
        assertEquals(expected, actual);

        expected = -1;
        actual = reader.nextIndexOf('z');
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Next Index Of With CharSequence"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        String expected = "Test";
        int actual = reader.nextIndexOf(expected);
        assertEquals(3, actual);

        String substring = "Next";
        expected = 7;
        actual = reader.nextIndexOf(substring);
        assertEquals(expected, actual);

        expected = -1;
        actual = reader.nextIndexOf("Zebra");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Consume To"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        String expected = "To";
        String actual = reader.consumeTo(' ');
        assertEquals(expected, actual);

        expected = "Test";
        actual = reader.consumeTo('S');
        assertEquals(expected, actual);

        expected = "";
        actual = reader.consumeTo('Z');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToWithCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Consume To With CharSequence"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        String expected = "Test";
        String actual = reader.consumeTo("Is");
        assertEquals(expected, actual);

        expected = "";
        actual = reader.consumeToAny(new char[]{'Z'});
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Consume To Any"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        String expected = "Any";
        String actual = reader.consumeToAny(new char[]{'n', 'y'});
        assertEquals(expected, actual);

        expected = "";
        actual = reader.consumeToAny(new char[]{'Z'});
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Consume To Any Sorted"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        String expected = "";
        String actual = reader.consumeToAnySorted(new char[]{'Z', 'n'});
        assertEquals(expected, actual);

        expected = "Any";
        actual = reader.consumeToAnySorted(new char[]{'n', 'y'});
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Consume Data"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        String expected = "&, <, null";
        String actual = reader.consumeData();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Consume Tag Name"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        String expected = "/";
        String actual = reader.consumeTagName();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Consume To End"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        String expected = "Test Consume To End";
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Consume Letter Sequence"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();

        String expected = "Sequence";
        String actual = reader.consumeLetterSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Consume Letter Then Digit Sequence"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();

        String expected = "Digit Sequence";
        String actual = reader.consumeLetterThenDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Consume Hex Sequence"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();

        String expected = "Sequence";
        String actual = reader.consumeHexSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Consume Digit Sequence"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();

        String expected = "Digit Sequence";
        String actual = reader.consumeDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Matches"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        char expected = 's';
        char actual = reader.current();
        assertTrue(reader.matches(expected));
        reader.consume();

        expected = 't';
        assertFalse(reader.matches(expected));
    }

    @Test
    public void testMatchesWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Matches With String"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        String expected = "es";
        String actual = reader.consumeTo("es");
        assertTrue(reader.matches(expected));
        reader.consumeToEnd();

        expected = "Test";
        assertFalse(reader.matches(expected));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Matches Ignore Case"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        String expected = "ES";
        String actual = reader.consumeTo("ES").toUpperCase();
        assertTrue(reader.matchesIgnoreCase(expected));
        reader.consumeToEnd();

        expected = "Test";
        assertFalse(reader.matchesIgnoreCase(expected));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Matches Any"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        char[] expectedChars = {'s', 't'};
        assertTrue(reader.matchesAny(expectedChars));

        char[] unexpectedChars = {'z', 'a'};
        assertFalse(reader.matchesAny(unexpectedChars));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Matches Any Sorted"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        char[] expectedChars = {'s', 't'};
        assertTrue(reader.matchesAnySorted(expectedChars));

        char[] unexpectedChars = {'z', 'a'};
        assertFalse(reader.matchesAnySorted(unexpectedChars));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Matches Letter"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        char expected = 's';
        assertTrue(reader.matchesLetter());
        reader.consume();

        expected = 'd';
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Matches Digit"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();

        char expected = '6';
        assertTrue(reader.matchesDigit());
        reader.consume();

        expected = 'a';
        assertFalse(reader.matchesDigit());
    }
}