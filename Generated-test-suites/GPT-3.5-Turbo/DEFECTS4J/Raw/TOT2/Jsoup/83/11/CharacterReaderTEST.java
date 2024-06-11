
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Testing");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Testing");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Testing");
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Testing");
        assertEquals("Tes", reader.consumeTo('t'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Testing");
        assertEquals("Tes", reader.consumeToAny('t', 'e'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Testing");
        assertEquals("Testing", reader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Testing");
        assertTrue(reader.matches('T'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("Testing");
        assertTrue(reader.matches("Testing"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Testing");
        assertTrue(reader.matchesAny('T', 'e'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Testing");
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Testing");
        assertTrue(reader.matchConsume("Tes"));
        assertEquals(3, reader.pos());
    }
}
