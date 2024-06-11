package org.jsoup.parser.test;

import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals(11, characterReader.length);
        assertEquals(0, characterReader.pos);
        assertEquals(0, characterReader.mark);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertFalse(characterReader.isEmpty());

        characterReader.consume();
        assertTrue(characterReader.isEmpty());

        characterReader.mark();
        characterReader.consumeLetterSequence();
        assertFalse(characterReader.isEmpty());

        characterReader.rewindToMark();
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.consume();
        assertEquals('e', characterReader.current());

        characterReader.consumeLetterSequence();
        assertEquals("ell", characterReader.consumeTo('l'));
        assertEquals("o", characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.consumeLetterSequence();
        characterReader.unconsume();
        assertEquals('l', characterReader.current());
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
        characterReader.consumeLetterSequence();
        characterReader.rewindToMark();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        String consumed = characterReader.consumeAsString();
        assertEquals("ll", consumed);
        assertEquals('o', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals(6, characterReader.nextIndexOf('l'));
        assertEquals(-1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals(4, characterReader.nextIndexOf("ll"));
        assertEquals(-1, characterReader.nextIndexOf("xx"));
    }

    @Test(expected = AssertionError.class)
    public void testConsumeToEndWithEmpty() {
        CharacterReader characterReader = new CharacterReader("");
        characterReader.consumeToEnd();
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        String consumed = characterReader.consumeToEnd();
        assertEquals("World", consumed);
        assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        String consumed = characterReader.consumeLetterSequence();
        assertEquals("ello", consumed);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader("10hello11world12");
        String consumed = characterReader.consumeLetterThenDigitSequence();
        assertEquals("10hello", consumed);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("0x1A 0X2b 0x3C");
        String consumed = characterReader.consumeHexSequence();
        assertEquals("1A", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("123 321 456");
        String consumed = characterReader.consumeDigitSequence();
        assertEquals("123", consumed);
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matches('l'));
        assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matches("ll"));
        assertFalse(characterReader.matches("xx"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchesIgnoreCase("ll"));
        assertFalse(characterReader.matchesIgnoreCase("XX"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader("1A 2b 3C");
        assertTrue(characterReader.matchesAny('A', 'B'));
        assertFalse(characterReader.matchesAny('D'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader("123 321 456");
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit('A'));
    }

    @Test
    public void testMatchConsumeString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchConsume("ll"));
        assertEquals(2, characterReader.pos);
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchConsumeIgnoreCase("LL"));
        assertEquals(2, characterReader.pos);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("<html><title>Test</title>");
        assertTrue(characterReader.containsIgnoreCase("title"));
        assertFalse(characterReader.containsIgnoreCase("Tag"));
    }

    @Test(expected = AssertionError.class)
    public void testToStringEmpty() {
        CharacterReader characterReader = new CharacterReader("");
        characterReader.toString();
    }

}