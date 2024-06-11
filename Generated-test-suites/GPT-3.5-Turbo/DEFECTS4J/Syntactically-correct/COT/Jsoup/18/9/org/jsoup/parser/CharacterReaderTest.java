import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("test");
    }

    @Test
    public void testPos() {
        int expected = 0;
        assertEquals(expected, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        char expected = 't';
        assertEquals(expected, reader.current());
    }

    @Test
    public void testConsume() {
        char expected = 't';
        assertEquals(expected, reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        char expected = 't';
        assertEquals(expected, reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        char expected = 'e';
        assertEquals(expected, reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        int expected = 0;
        assertEquals(expected, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        reader.advance();
        reader.mark();
        reader.rewindToMark();
        char expected = 't';
        assertEquals(expected, reader.current());
    }

    @Test
    public void testConsumeAsString() {
        String expected = "t";
        assertEquals(expected, reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        String expected = "te";
        assertEquals(expected, reader.consumeTo('s'));
    }

    @Test
    public void testConsumeToAny() {
        String expected = "t";
        assertEquals(expected, reader.consumeToAny('e','s'));
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "test";
        assertEquals(expected, reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "test";
        assertEquals(expected, reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        reader = new CharacterReader("0123456789ABCDEFabcdef");
        String expected = "0123456789ABCDEFabcdef";
        assertEquals(expected, reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        reader = new CharacterReader("0123456789");
        String expected = "0123456789";
        assertEquals(expected, reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('t'));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('t','f'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("te"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("TE"));
    }

    @Test
    public void testContainsIgnoreCase() {
        reader = new CharacterReader("test");
        assertFalse(reader.containsIgnoreCase("TE"));
    }

    @Test
    public void testToString() {
        String expected = "test";
        assertEquals(expected, reader.toString());
    }
}