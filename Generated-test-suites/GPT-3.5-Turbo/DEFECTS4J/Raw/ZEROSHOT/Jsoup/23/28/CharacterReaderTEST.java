import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals(true, reader1.isEmpty());

        CharacterReader reader2 = new CharacterReader("hello");
        assertEquals(false, reader2.isEmpty());
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
        CharacterReader reader = new CharacterReader("hello");
        assertEquals('h', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("hello");
        reader.consume();
        assertEquals('h', reader.current());

        reader.unconsume();
        assertEquals((char) -1, reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals('e', reader.consume());

        reader.advance();
        assertEquals('l', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals('h', reader.consume());

        reader.mark();
        reader.consume();
        assertEquals('e', reader.current());

        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals("h", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("hello ", reader.consumeTo(' '));
        assertEquals("world", reader.consumeTo(' '));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("hello", reader.consumeToAny(' ', 'w'));
        assertEquals(" ", reader.consumeToAny(' ', 'w'));
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
        CharacterReader reader = new CharacterReader("12ABg345");
        assertEquals("12ABg345", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345hello");
        assertEquals("12345", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals(true, reader.matches('h'));
        assertEquals(false, reader.matches('e'));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals(true, reader.matchesAny('h', 'e'));
        assertEquals(false, reader.matchesAny('z', 'x'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader1 = new CharacterReader("hello");
        assertEquals(true, reader1.matchesLetter());

        CharacterReader reader2 = new CharacterReader("12345");
        assertEquals(false, reader2.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader1 = new CharacterReader("hello");
        assertEquals(false, reader1.matchesDigit());

        CharacterReader reader2 = new CharacterReader("12345");
        assertEquals(true, reader2.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals(true, reader.matchConsume("he"));
        assertEquals(false, reader.matchConsume("el"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals(true, reader.matchConsumeIgnoreCase("He"));
        assertEquals(false, reader.matchConsumeIgnoreCase("ell"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        assertEquals(true, reader.containsIgnoreCase("ell"));
        assertEquals(true, reader.containsIgnoreCase("E"));
        assertEquals(false, reader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals("hello", reader.toString());
    }
}