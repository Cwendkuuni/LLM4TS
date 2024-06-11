package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        boolean expected = true;
        boolean actual = reader.isEmpty();
        assertEquals(expected, actual);
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        char expected = 't';
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        char expected = 't';
        char actual = reader.consume();
        assertEquals(expected, actual);
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        char expected = 't';
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        char expected = 'e';
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.rewindToMark();
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "t";
        String actual = reader.consumeAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("test");
        int expected = 1;
        int actual = reader.nextIndexOf('e');
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfChar_NotFound() {
        CharacterReader reader = new CharacterReader("test");
        int expected = -1;
        int actual = reader.nextIndexOf('z');
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("test");
        int expected = 1;
        int actual = reader.nextIndexOf("es");
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfCharSequence_NotFound() {
        CharacterReader reader = new CharacterReader("test");
        int expected = -1;
        int actual = reader.nextIndexOf("xyz");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "t";
        String actual = reader.consumeTo("e");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo_NotFound() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "test";
        String actual = reader.consumeTo("z");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "t";
        String actual = reader.consumeToAny('e', 'i');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "test";
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "test";
        String actual = reader.consumeLetterSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        String expected = "test123";
        String actual = reader.consumeLetterThenDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("123abc");
        String expected = "123abc";
        String actual = reader.consumeHexSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123");
        String expected = "123";
        String actual = reader.consumeDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matches('t');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches_NotMatched() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = false;
        boolean actual = reader.matches('z');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matches("te");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesString_NotMatched() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = false;
        boolean actual = reader.matches("xyz");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("TeSt");
        boolean expected = true;
        boolean actual = reader.matchesIgnoreCase("tEsT");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase_NotMatched() {
        CharacterReader reader = new CharacterReader("TeSt");
        boolean expected = false;
        boolean actual = reader.matchesIgnoreCase("xyz");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matchesAny('t', 'z');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("t");
        boolean expected = true;
        boolean actual = reader.matchesLetter();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesLetter_NotMatched() {
        CharacterReader reader = new CharacterReader("1");
        boolean expected = false;
        boolean actual = reader.matchesLetter();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("1");
        boolean expected = true;
        boolean actual = reader.matchesDigit();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesDigit_NotMatched() {
        CharacterReader reader = new CharacterReader("t");
        boolean expected = false;
        boolean actual = reader.matchesDigit();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matchConsume("te");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsume_NotMatched() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = false;
        boolean actual = reader.matchConsume("xyz");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("TeSt");
        boolean expected = true;
        boolean actual = reader.matchConsumeIgnoreCase("tEsT");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase_NotMatched() {
        CharacterReader reader = new CharacterReader("TeSt");
        boolean expected = false;
        boolean actual = reader.matchConsumeIgnoreCase("xyz");
        assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test Test");
        boolean expected = true;
        boolean actual = reader.containsIgnoreCase("test");
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "test";
        String actual = reader.toString();
        assertEquals(expected, actual);
    }
}