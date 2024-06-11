
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertFalse(characterReader.isEmpty());
        characterReader.consume();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertEquals('H', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader("Hello");
        characterReader.consume();
        assertEquals(1, characterReader.pos());
        characterReader.unconsume();
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertEquals('H', characterReader.advance());
        assertEquals('e', characterReader.advance());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader characterReader = new CharacterReader("Hello");
        characterReader.advance();
        characterReader.mark();
        assertEquals(1, characterReader.mark);
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertEquals("H", characterReader.consumeAsString());
        assertEquals("e", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf_char() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOf_charSequence() {
        CharacterReader characterReader = new CharacterReader("abcde");
        assertEquals(0, characterReader.nextIndexOf("ab"));
        assertEquals(2, characterReader.nextIndexOf("cd"));
        assertEquals(-1, characterReader.nextIndexOf("fgh"));
    }

    @Test
    public void testConsumeTo_char() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertEquals("H", characterReader.consumeTo('e'));
        assertEquals("e", characterReader.consumeTo('l'));
    }

    @Test
    public void testConsumeTo_charSequence() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertEquals("H", characterReader.consumeTo("e"));
        assertEquals("e", characterReader.consumeTo("l"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("Hello ", characterReader.consumeToAny(' '));
        assertEquals("World", characterReader.consumeToAny(' '));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertEquals("Hello", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("Hello123");
        assertEquals("Hello", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader("Hello123");
        assertEquals("Hello123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("Hello123");
        assertEquals("H", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("Hello123");
        assertEquals("123", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches_char() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertTrue(characterReader.matches('H'));
        assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatches_charSequence() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertTrue(characterReader.matches("Hello"));
        assertFalse(characterReader.matches("e"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertTrue(characterReader.matchesIgnoreCase("hello"));
        assertFalse(characterReader.matchesIgnoreCase("e"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertTrue(characterReader.matchesAny('H','e'));
        assertFalse(characterReader.matchesAny('z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader("Hello123");
        assertFalse(characterReader.matchesDigit());
        characterReader.consumeLetterSequence();
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertTrue(characterReader.matchConsume("Hello"));
        assertFalse(characterReader.matchConsume("e"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertTrue(characterReader.matchConsumeIgnoreCase("hello"));
        assertFalse(characterReader.matchConsumeIgnoreCase("e"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertFalse(characterReader.containsIgnoreCase("world"));
        assertTrue(characterReader.containsIgnoreCase("Hello"));
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader("Hello");
        assertEquals("Hello", characterReader.toString());
        characterReader.advance();
        assertEquals("ello", characterReader.toString());
    }

}

