package tests;

import org.jsoup.helper.Validate;
import org.jsoup.parser.CharacterReader;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        String input = "Hello World";
        characterReader = new CharacterReader(new StringReader(input));
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        characterReader.advance();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        while (!characterReader.isEmpty()) {
            characterReader.consume();
        }
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello", characterReader.consumeTo(' '));
        assertEquals("World", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello", characterReader.consumeToAny(' '));
        assertEquals(" ", characterReader.consumeToAny('o', ' '));
    }

    @Test
    public void testConsumeData() {
        assertEquals("Hello", characterReader.consumeData());
        assertEquals(" ", characterReader.consumeData());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello World", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", characterReader.consumeLetterSequence());
        assertEquals(" ", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Hello", characterReader.consumeLetterThenDigitSequence());
        assertEquals(" W", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "123ABC789";
        characterReader = new CharacterReader(new StringReader(input));
        assertEquals("123ABC", characterReader.consumeHexSequence());
        assertEquals("789", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "123ABC789";
        characterReader = new CharacterReader(new StringReader(input));
        assertEquals("123", characterReader.consumeDigitSequence());
        assertEquals("ABC789", characterReader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('H'));
        assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('e', 'o'));
        assertFalse(characterReader.matchesAny('z', 'x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("HELLO"));
        assertFalse(characterReader.matchesIgnoreCase("hello"));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('H', 'e', 'l', 'o'));
        assertFalse(characterReader.matchesAnySorted('e', 'z', 'x'));
    }

}