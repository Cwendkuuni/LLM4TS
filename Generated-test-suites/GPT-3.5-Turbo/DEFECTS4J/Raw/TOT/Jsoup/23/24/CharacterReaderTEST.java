import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader("Hello world!");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals(' ', reader.consume());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("H", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("l", reader.consumeAsString());
        assertEquals("l", reader.consumeAsString());
        assertEquals("o", reader.consumeAsString());
        assertEquals(" ", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hel", reader.consumeTo('l'));
        assertEquals("lo wor", reader.consumeTo('d'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello", reader.consumeToAny(' ', 'w'));
        assertEquals(" ", reader.consumeToAny(' ', 'w'));
        assertEquals("worl", reader.consumeToAny('r'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello world!", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('H'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("HELLO"));
        assertFalse(reader.matchesIgnoreCase("WORLD"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('H', 'e'));
        assertFalse(reader.matchesAny('w', 'o'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("Hel"));
        assertFalse(reader.matchConsume("lo"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("HEL"));
        assertFalse(reader.matchConsumeIgnoreCase("lo"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("HELLO"));
        assertFalse(reader.containsIgnoreCase("WORLD"));
    }

    @Test
    public void testRewindToMark() {
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('e', reader.current());
    }

}