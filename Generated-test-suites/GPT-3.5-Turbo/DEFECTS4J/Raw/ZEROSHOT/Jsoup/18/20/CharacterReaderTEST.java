import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        assertTrue(reader1.isEmpty());

        CharacterReader reader2 = new CharacterReader("hello");
        assertFalse(reader2.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals((char) -1, reader1.current());

        CharacterReader reader2 = new CharacterReader("hello");
        assertEquals('h', reader2.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals((char) -1, reader1.consume());

        CharacterReader reader2 = new CharacterReader("hello");
        assertEquals('h', reader2.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("hello");
        reader.consume();
        reader.unconsume();
        assertEquals('h', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("hello");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("hello");
        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("hello");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals("h", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader1 = new CharacterReader("hello");
        assertEquals("hello", reader1.consumeTo('x'));

        CharacterReader reader2 = new CharacterReader("hello world");
        assertEquals("hello ", reader2.consumeTo('o'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader1 = new CharacterReader("hello");
        assertEquals("", reader1.consumeToAny('x'));

        CharacterReader reader2 = new CharacterReader("hello world");
        assertEquals("hello ", reader2.consumeToAny('o', 'w'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals("hello", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("hello123");
        assertEquals("hello", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader1 = new CharacterReader("h");
        assertTrue(reader1.matches('h'));

        CharacterReader reader2 = new CharacterReader("h");
        assertFalse(reader2.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader1 = new CharacterReader("h");
        assertTrue(reader1.matchesIgnoreCase("H"));

        CharacterReader reader2 = new CharacterReader("h");
        assertFalse(reader2.matchesIgnoreCase("X"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader1 = new CharacterReader("h");
        assertTrue(reader1.matchesAny('h'));

        CharacterReader reader2 = new CharacterReader("h");
        assertFalse(reader2.matchesAny('x'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader1 = new CharacterReader("h");
        assertTrue(reader1.matchesLetter());

        CharacterReader reader2 = new CharacterReader("1");
        assertFalse(reader2.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader1 = new CharacterReader("1");
        assertTrue(reader1.matchesDigit());

        CharacterReader reader2 = new CharacterReader("h");
        assertFalse(reader2.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader1 = new CharacterReader("hello");
        assertTrue(reader1.matchConsume("he"));

        CharacterReader reader2 = new CharacterReader("hello");
        assertFalse(reader2.matchConsume("hx"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader1 = new CharacterReader("hello");
        assertTrue(reader1.matchConsumeIgnoreCase("HE"));

        CharacterReader reader2 = new CharacterReader("hello");
        assertFalse(reader2.matchConsumeIgnoreCase("HX"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader1 = new CharacterReader("hello");
        assertTrue(reader1.containsIgnoreCase("HELLO"));

        CharacterReader reader2 = new CharacterReader("hello");
        assertFalse(reader2.containsIgnoreCase("WORLD"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals("hello", reader.toString());
    }

}