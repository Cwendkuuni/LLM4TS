import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader("test");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('t', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals('s', characterReader.consume());
        assertEquals('t', characterReader.consume());
        assertEquals((char) -1, characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("t", characterReader.consumeAsString());
        assertEquals("e", characterReader.consumeAsString());
        assertEquals("s", characterReader.consumeAsString());
        assertEquals("t", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(2, characterReader.nextIndexOf('s'));
        assertEquals(3, characterReader.nextIndexOf('t'));
        assertEquals(-1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        assertEquals(1, characterReader.nextIndexOf("es"));
        assertEquals(2, characterReader.nextIndexOf("st"));
        assertEquals(-1, characterReader.nextIndexOf("xt"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("t", characterReader.consumeTo('e'));
        assertEquals("e", characterReader.consumeTo('s'));
        assertEquals("s", characterReader.consumeTo('t'));
        assertEquals("t", characterReader.consumeTo('x'));
    }

    @Test
    public void testConsumeToSequence() {
        assertEquals("t", characterReader.consumeTo("es"));
        assertEquals("e", characterReader.consumeTo("st"));
        assertEquals("s", characterReader.consumeTo("xt"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("t", characterReader.consumeToAny('e', 's'));
        assertEquals("st", characterReader.consumeToAny('t'));
        assertEquals("xt", characterReader.consumeToAny('t', 'x'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("test", characterReader.consumeToEnd());
        assertEquals("", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        characterReader = new CharacterReader("abc123");
        assertEquals("abc", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        characterReader = new CharacterReader("abc123");
        assertEquals("abc123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("abc123");
        assertEquals("abc123", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("abc123");
        assertEquals("123", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('t'));
        assertFalse(characterReader.matches('e'));
        assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatchesSequence() {
        assertTrue(characterReader.matches("tes"));
        assertFalse(characterReader.matches("ext"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("tES"));
        assertFalse(characterReader.matchesIgnoreCase("xST"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('t', 'e'));
        assertFalse(characterReader.matchesAny('x'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("tes"));
        assertFalse(characterReader.matchConsume("ext"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("tES"));
        assertFalse(characterReader.matchConsumeIgnoreCase("xST"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("es"));
        assertFalse(characterReader.containsIgnoreCase("ex"));
    }

    @Test
    public void testToString() {
        assertEquals("test", characterReader.toString());
    }
}