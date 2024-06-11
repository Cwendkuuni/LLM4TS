import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testUnconsume_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        reader.unconsume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testAdvance_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        reader.advance();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMark_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals("", reader.consumeAsString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testNextIndexOf_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(-1, reader.nextIndexOf('a'));
    }

    @Test
    public void testNextIndexOf_emptyInputSequence() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(-1, reader.nextIndexOf("abc"));
    }

    // Additional test cases for other methods

    @Test
    public void testConsumeTo_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals("", reader.consumeTo('a'));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAny_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals("", reader.consumeToAny('a', 'b', 'c'));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeData_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals("", reader.consumeData());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeTagName_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals("", reader.consumeTagName());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToEnd_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals("", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals("", reader.consumeLetterSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterThenDigitSequence_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals("", reader.consumeLetterThenDigitSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeHexSequence_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals("", reader.consumeHexSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeDigitSequence_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertEquals("", reader.consumeDigitSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatches_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertFalse(reader.matches('a'));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatches_anyChar_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertFalse(reader.matchesAny('a', 'b', 'c'));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchesAnySorted_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertFalse(reader.matchesAnySorted(new char[] {'a', 'b', 'c'}));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchesLetter_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertFalse(reader.matchesLetter());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchesDigit_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.isEmpty());
    }

    // Continue with test cases for the remaining methods...

}