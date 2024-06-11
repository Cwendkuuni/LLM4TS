import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader("Hello World!");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsume() {
        char consumedChar = characterReader.consume();
        assertEquals('H', consumedChar);
        assertEquals(1, characterReader.pos);
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals(0, characterReader.pos);
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals(1, characterReader.pos);
    }

    @Test
    public void testMark() {
        characterReader.mark();
        assertEquals(0, characterReader.mark);
    }

    @Test
    public void testRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos);
    }

    @Test
    public void testConsumeAsString() {
        String consumedString = characterReader.consumeAsString();
        assertEquals("H", consumedString);
        assertEquals(1, characterReader.pos);
    }

    @Test
    public void testNextIndexOf() {
        int offset = characterReader.nextIndexOf('o');
        assertEquals(4, offset);
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        int offset = characterReader.nextIndexOf("World");
        assertEquals(6, offset);
    }

    @Test
    public void testConsumeTo() {
        String consumedString = characterReader.consumeTo('o');
        assertEquals("Hell", consumedString);
        assertEquals(4, characterReader.pos);
    }

    @Test
    public void testConsumeTo_CharSequence() {
        String consumedString = characterReader.consumeTo("World");
        assertEquals("Hello ", consumedString);
        assertEquals(6, characterReader.pos);
    }

    @Test
    public void testConsumeToAny() {
        String consumedString = characterReader.consumeToAny('o', 'l');
        assertEquals("Hel", consumedString);
        assertEquals(3, characterReader.pos);
    }

    @Test
    public void testConsumeToAnySorted() {
        String consumedString = characterReader.consumeToAnySorted('l', 'o');
        assertEquals("H", consumedString);
        assertEquals(1, characterReader.pos);
    }

    @Test
    public void testConsumeData() {
        String consumedString = characterReader.consumeData();
        assertEquals("Hello ", consumedString);
        assertEquals(6, characterReader.pos);
    }

    @Test
    public void testConsumeTagName() {
        String consumedString = characterReader.consumeTagName();
        assertEquals("Hello", consumedString);
        assertEquals(6, characterReader.pos);
    }

    @Test
    public void testConsumeToEnd() {
        String consumedString = characterReader.consumeToEnd();
        assertEquals("Hello World!", consumedString);
        assertEquals(12, characterReader.pos);
    }

    @Test
    public void testConsumeLetterSequence() {
        characterReader.consume();
        String consumedString = characterReader.consumeLetterSequence();
        assertEquals("ello", consumedString);
        assertEquals(5, characterReader.pos);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        characterReader.consume();
        String consumedString = characterReader.consumeLetterThenDigitSequence();
        assertEquals("ello", consumedString);
        assertEquals(5, characterReader.pos);
    }

    @Test
    public void testConsumeHexSequence() {
        characterReader.consume();
        String consumedString = characterReader.consumeHexSequence();
        assertEquals("ello", consumedString);
        assertEquals(5, characterReader.pos);
    }

    @Test
    public void testConsumeDigitSequence() {
        characterReader.consume();
        String consumedString = characterReader.consumeDigitSequence();
        assertEquals("ello", consumedString);
        assertEquals(5, characterReader.pos);
    }

    @Test
    public void testMatches() {
        boolean isMatch = characterReader.matches('H');
        assertTrue(isMatch);
    }

    @Test
    public void testMatches_String() {
        boolean isMatch = characterReader.matches("Hell");
        assertTrue(isMatch);
    }

    @Test
    public void testMatchesIgnoreCase() {
        boolean isMatch = characterReader.matchesIgnoreCase("hell");
        assertTrue(isMatch);
    }

    @Test
    public void testMatchesAny() {
        boolean isMatch1 = characterReader.matchesAny('H', 'h');
        assertTrue(isMatch1);

        characterReader.consume();
        boolean isMatch2 = characterReader.matchesAny('H', 'h');
        assertFalse(isMatch2);
    }

    @Test
    public void testMatchesAnySorted() {
        boolean isMatch1 = characterReader.matchesAnySorted('H', 'W');
        assertTrue(isMatch1);

        characterReader.consume();
        boolean isMatch2 = characterReader.matchesAnySorted('H', 'W');
        assertFalse(isMatch2);
    }

    @Test
    public void testMatchesLetter() {
        boolean isMatch = characterReader.matchesLetter();
        assertTrue(isMatch);
    }

    @Test
    public void testMatchesDigit() {
        boolean isMatch = characterReader.matchesDigit();
        assertFalse(isMatch);

        characterReader.advance();
        boolean isMatch2 = characterReader.matchesDigit();
        assertTrue(isMatch2);
    }

    @Test
    public void testMatchConsume() {
        boolean isMatch = characterReader.matchConsume("Hello");
        assertTrue(isMatch);
        assertEquals(5, characterReader.pos);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean isMatch = characterReader.matchConsumeIgnoreCase("hello");
        assertTrue(isMatch);
        assertEquals(5, characterReader.pos);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean contains = characterReader.containsIgnoreCase("world");
        assertTrue(contains);
    }

    @Test
    public void testToString() {
        String string = characterReader.toString();
        assertEquals("Hello World!", string);
    }

    @Test
    public void testCacheString() {
        String cachedString = characterReader.cacheString(0, 5);
        assertEquals("Hello", cachedString);
    }

    @Test
    public void testRangeEquals() {
        boolean equals = characterReader.rangeEquals(0, 5, "Hello");
        assertTrue(equals);
    }
}