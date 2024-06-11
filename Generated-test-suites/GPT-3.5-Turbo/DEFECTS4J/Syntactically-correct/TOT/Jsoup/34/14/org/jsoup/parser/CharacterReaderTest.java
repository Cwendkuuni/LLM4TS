package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = false;
        boolean actual = reader.isEmpty();
        assertEquals(expected, actual);
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        char expected = 'a';
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        char expected = 'a';
        char actual = reader.consume();
        assertEquals(expected, actual);
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        reader.consume();
        reader.unconsume();
        char expected = 'a';
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        reader.advance();
        char expected = 'b';
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        reader.mark();
        int expected = reader.pos();
        int actual = reader.mark;
        assertEquals(expected, actual);
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        reader.advance();
        reader.advance();
        reader.advance();
        reader.mark();
        reader.rewindToMark();
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        String expected = "a";
        String actual = reader.consumeAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        int expected = 5;
        int actual = reader.nextIndexOf('f');
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfCharNotFound() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        int expected = -1;
        int actual = reader.nextIndexOf('x');
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        int expected = 13;
        int actual = reader.nextIndexOf("nop");
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfCharSequenceNotFound() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        int expected = -1;
        int actual = reader.nextIndexOf("xyz");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        String expected = "abcdefghi";
        String actual = reader.consumeTo('j');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToCharNotFound() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        String expected = "abcdefghijklmnopqrstuvwxyz";
        String actual = reader.consumeTo(' ');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        String expected = "abcdefghi";
        String actual = reader.consumeTo("jkl");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToStringNotFound() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        String expected = "abcdefghijklmnopqrstuvwxyz";
        String actual = reader.consumeTo("xyz");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        String expected = "abcd";
        String actual = reader.consumeToAny('e', 'f', 'g', 'h');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        String expected = "abcdefghijklmnopqrstuvwxyz";
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        String expected = "abcdefghijklmnopqrstuvwxyz";
        String actual = reader.consumeLetterSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        String expected = "abcdefghijklmnopqrstuvwxyz";
        String actual = reader.consumeLetterThenDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        String expected = "abcdefghijklmnopqrstuvwxyz";
        String actual = reader.consumeHexSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        String expected = "abcdefghijklmnopqrstuvwxyz";
        String actual = reader.consumeDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = true;
        boolean actual = reader.matches('a');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesNotFound() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = false;
        boolean actual = reader.matches('x');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = true;
        boolean actual = reader.matches("abcdefghijklmno");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesSequenceNotFound() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = false;
        boolean actual = reader.matches("xyz");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = true;
        boolean actual = reader.matchesIgnoreCase("abcdefghijklmno");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCaseNotFound() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = false;
        boolean actual = reader.matchesIgnoreCase("xyz");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = true;
        boolean actual = reader.matchesAny('z', 'y', 'x');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAnyNotFound() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = false;
        boolean actual = reader.matchesAny('p', 'q', 'r');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = true;
        boolean actual = reader.matchesLetter();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesLetterNotFound() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = false;
        boolean actual = reader.matchesLetter();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = false;
        boolean actual = reader.matchesDigit();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesDigitNotFound() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = false;
        boolean actual = reader.matchesDigit();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = true;
        boolean actual = reader.matchConsume("abcdefghi");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeNotFound() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = false;
        boolean actual = reader.matchConsume("xyz");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = true;
        boolean actual = reader.matchConsumeIgnoreCase("abcdefghijklmno");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCaseNotFound() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = false;
        boolean actual = reader.matchConsumeIgnoreCase("xyz");
        assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = true;
        boolean actual = reader.containsIgnoreCase("mnopq");
        assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCaseNotFound() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        boolean expected = false;
        boolean actual = reader.containsIgnoreCase("xyz");
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        String expected = "abcdefghijklmnopqrstuvwxyz";
        String actual = reader.toString();
        assertEquals(expected, actual);
    }
}