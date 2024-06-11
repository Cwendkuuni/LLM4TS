package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals(CharacterReader.EOF, characterReader.current());
        assertEquals(0, characterReader.pos());
        assertEquals(11, characterReader.length());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertFalse(characterReader.isEmpty());
        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals(CharacterReader.EOF, characterReader.current());
        characterReader.consume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.consume();
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("World", characterReader.consumeAsString());
        characterReader.consume();
        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals(8, characterReader.nextIndexOf('l'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("llo", characterReader.consumeTo('l'));
        characterReader.consume();
        assertEquals("llo", characterReader.consumeTo('l'));
        assertEquals("World", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("llo", characterReader.consumeTo("ll"));
        characterReader.consume();
        assertEquals("llo", characterReader.consumeTo("ll"));
        assertEquals("World", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("llo", characterReader.consumeToAny(new char[] {'l', 'l'}));
        characterReader.consume();
        assertEquals("llo", characterReader.consumeToAny(new char[] {'l', 'l'}));
        assertEquals("World", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("llo", characterReader.consumeToAnySorted(new char[] {'l', 'l'}));
        characterReader.consume();
        assertEquals("llo", characterReader.consumeToAnySorted(new char[] {'l', 'l'}));
        assertEquals("World", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader("& <null>");
        assertEquals("& <null>", characterReader.consumeData());
        characterReader.consume();
        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader("<title>Hello World</title>");
        assertEquals("title", characterReader.consumeTagName());
        characterReader.consume();
        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("<title>Hello World</title>");
        assertEquals("<title>Hello World</title>", characterReader.consumeToEnd());
        characterReader.consume();
        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("Hello", characterReader.consumeLetterSequence());
        characterReader.consume();
        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader("Hello123World");
        assertEquals("Hello123", characterReader.consumeLetterThenDigitSequence());
        characterReader.consume();
        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("&x0F&xAB&xCD");
        assertEquals("&#xF;&#xA;&#xC;&#xD", characterReader.consumeHexSequence());
        characterReader.consume();
        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("1234567890");
        assertEquals("1234567890", characterReader.consumeDigitSequence());
        characterReader.consume();
        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matches('l'));
        assertTrue(characterReader.matches("ll"));
        assertFalse(characterReader.matches('z'));
        assertFalse(characterReader.matches("zz"));
        characterReader.consume();
        assertFalse(characterReader.matches('l'));
        assertFalse(characterReader.matches("ll"));
        assertFalse(characterReader.matches('z'));
        assertFalse(characterReader.matches("zz"));
        characterReader.consumeToEnd();
        assertFalse(characterReader.matches('l'));
        assertFalse(characterReader.matches("ll"));
        assertFalse(characterReader.matches('z'));
        assertFalse(characterReader.matches("zz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchesIgnoreCase("ll"));
        assertTrue(characterReader.matchesIgnoreCase("Ll"));
        assertFalse(characterReader.matchesIgnoreCase("zz"));
        assertFalse(characterReader.matchesIgnoreCase("ZZ"));
        characterReader.consume();
        assertTrue(characterReader.matchesIgnoreCase("ll"));
        assertTrue(characterReader.matchesIgnoreCase("Ll"));
        assertFalse(characterReader.matchesIgnoreCase("zz"));
        assertFalse(characterReader.matchesIgnoreCase("ZZ"));
        characterReader.consumeToEnd();
        assertFalse(characterReader.matchesIgnoreCase("ll"));
        assertFalse(characterReader.matchesIgnoreCase("Ll"));
        assertFalse(characterReader.matchesIgnoreCase("zz"));
        assertFalse(characterReader.matchesIgnoreCase("ZZ"));
    }

    @Test(expected = AssertionError.class)
    public void testMatchesAnyFail() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertFalse(characterReader.matchesAny(new char[] {'z', 'z'}));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchesAny(new char[] {'l', 'l'}));
        assertTrue(characterReader.matchesAny(new char[] {'L', 'L'}));
        assertTrue(characterReader.matchesAny(new char[] {'l', 'L'}));
        assertTrue(characterReader.matchesAny(new char[] {'L', 'l'}));
        characterReader.consume();
        assertTrue(characterReader.matchesAny(new char[] {'l', 'l'}));
        assertTrue(characterReader.matchesAny(new char[] {'L', 'L'}));
        assertTrue(characterReader.matchesAny(new char[] {'l', 'L'}));
        assertTrue(characterReader.matchesAny(new char[] {'L', 'l'}));
        characterReader.consumeToEnd();
        assertFalse(characterReader.matchesAny(new char[] {'l', 'l'}));
        assertFalse(characterReader.matchesAny(new char[] {'L', 'L'}));
        assertFalse(characterReader.matchesAny(new char[] {'l', 'L'}));
        assertFalse(characterReader.matchesAny(new char[] {'L', 'l'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        assertTrue(characterReader.matchesLetter());
        characterReader.consumeToEnd();
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader("Hello123World");
        assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        assertTrue(characterReader.matchesDigit());
        characterReader.consumeToEnd();
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchConsume("ll"));
        characterReader.consume();
        assertTrue(characterReader.matchConsume("ll"));
        characterReader.consumeToEnd();
        assertFalse(characterReader.matchConsume("ll"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchConsumeIgnoreCase("ll"));
        characterReader.consume();
        assertTrue(characterReader.matchConsumeIgnoreCase("ll"));
        characterReader.consumeToEnd();
        assertFalse(characterReader.matchConsumeIgnoreCase("ll"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("<title>Hello World</title>");
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("/TITLE"));
        assertFalse(characterReader.containsIgnoreCase("/ZZZ"));
        characterReader.consume();
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("/TITLE"));
        assertFalse(characterReader.containsIgnoreCase("/ZZZ"));
        characterReader.consumeToEnd();
        assertFalse(characterReader.containsIgnoreCase("/title"));
        assertFalse(characterReader.containsIgnoreCase("/TITLE"));
        assertFalse(characterReader.containsIgnoreCase("/ZZZ"));
    }

    @Test(expected = AssertionError.class)
    public void testCacheStringNullStart() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.cacheString(0, 1);
    }

    @Test
    public void testCacheString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        String s = characterReader.cacheString(0, 3);
        assertEquals("Hel", s);
        assertEquals("Hel", characterReader.stringCache[0]);
        s = characterReader.cacheString(0, 3);
        assertEquals("Hel", s);
        assertEquals("Hel", characterReader.stringCache[0]);
        characterReader.consume();
        String s2 = characterReader.cacheString(0, 3);
        assertEquals("He", s2);
        assertEquals("He", characterReader.stringCache[0]);
    }
}