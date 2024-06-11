import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("example");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals('e', reader.consume());
        assertEquals('x', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("example");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('x', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("example");
        reader.advance();
        assertEquals('x', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("example");
        reader.mark();
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals(1, reader.nextIndexOf('x'));
        assertEquals(-1, reader.nextIndexOf('y'));
    }

    @Test
    public void testNextIndexOf_seq() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals(2, reader.nextIndexOf("am"));
        assertEquals(-1, reader.nextIndexOf("yz"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("e", reader.consumeTo('x'));
    }

    @Test
    public void testConsumeTo_seq() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("ex", reader.consumeTo("am"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("ex", reader.consumeToAny('/'));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("", reader.consumeToAnySorted('/'));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("example", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("example", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("example", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("example", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("example", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("example");
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatches_seq() {
        CharacterReader reader = new CharacterReader("example");
        assertTrue(reader.matches("ex"));
        assertFalse(reader.matches("yz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("example");
        assertTrue(reader.matchesIgnoreCase("EX"));
        assertFalse(reader.matchesIgnoreCase("YZ"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("example");
        assertTrue(reader.matchesAny('/'));
        assertFalse(reader.matchesAny('-'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("example");
        assertFalse(reader.matchesAnySorted('/'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("example");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("example");
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("example");
        assertTrue(reader.matchConsume("exa"));
        assertFalse(reader.matchConsume("xyz"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("example");
        assertTrue(reader.matchConsumeIgnoreCase("exA"));
        assertFalse(reader.matchConsumeIgnoreCase("xyz"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("example");
        assertTrue(reader.containsIgnoreCase("exA"));
        assertFalse(reader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("example");
        assertEquals("example", reader.toString());
    }
}