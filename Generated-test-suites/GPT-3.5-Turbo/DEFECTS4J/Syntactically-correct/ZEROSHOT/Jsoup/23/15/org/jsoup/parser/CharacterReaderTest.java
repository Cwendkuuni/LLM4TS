package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test the pos() method
        CharacterReader reader = new CharacterReader("test");
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test the isEmpty() method
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Test the current() method
        CharacterReader reader = new CharacterReader("test");
        char expectedCurrent = 't';
        assertEquals(expectedCurrent, reader.current());
    }

    @Test
    public void testConsume() {
        // Test the consume() method
        CharacterReader reader = new CharacterReader("test");
        char expectedVal = 't';
        assertEquals(expectedVal, reader.consume());
    }

    @Test
    public void testUnconsume() {
        // Test the unconsume() method
        CharacterReader reader = new CharacterReader("test");
        reader.unconsume();
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testAdvance() {
        // Test the advance() method
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        int expectedPos = 1;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testMark() {
        // Test the mark() method
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        int expectedMark = 0;
        assertEquals(expectedMark, reader.mark);
    }

    @Test
    public void testRewindToMark() {
        // Test the rewindToMark() method
        CharacterReader reader = new CharacterReader("test");
        reader.rewindToMark();
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        // Test the consumeAsString() method
        CharacterReader reader = new CharacterReader("test");
        String expectedString = "t";
        assertEquals(expectedString, reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        // Test the consumeTo() method
        CharacterReader reader = new CharacterReader("test");
        String expectedString = "tes";
        assertEquals(expectedString, reader.consumeTo('t'));
    }

    @Test
    public void testConsumeTo_2() {
        // Test the consumeTo() method when the character is not found
        CharacterReader reader = new CharacterReader("test");
        String expectedString = "test";
        assertEquals(expectedString, reader.consumeTo('z'));
    }

    @Test
    public void testConsumeTo_3() {
        // Test the consumeTo() method when the sequence is found
        CharacterReader reader = new CharacterReader("test123");
        String expectedString = "test";
        assertEquals(expectedString, reader.consumeTo("123"));
    }

    @Test
    public void testConsumeTo_4() {
        // Test the consumeTo() method when the sequence is not found
        CharacterReader reader = new CharacterReader("test");
        String expectedString = "test";
        assertEquals(expectedString, reader.consumeTo("123"));
    }

    @Test
    public void testConsumeToAny() {
        // Test the consumeToAny() method
        CharacterReader reader = new CharacterReader("test123");
        String expectedString = "test";
        assertEquals(expectedString, reader.consumeToAny('1', '2', '3'));
    }

    @Test
    public void testConsumeToEnd() {
        // Test the consumeToEnd() method
        CharacterReader reader = new CharacterReader("test");
        String expectedString = "test";
        assertEquals(expectedString, reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        // Test the consumeLetterSequence() method
        CharacterReader reader = new CharacterReader("test");
        String expectedString = "test";
        assertEquals(expectedString, reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        // Test the consumeHexSequence() method
        CharacterReader reader = new CharacterReader("test");
        String expectedString = "";
        assertEquals(expectedString, reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        // Test the consumeDigitSequence() method
        CharacterReader reader = new CharacterReader("123");
        String expectedString = "123";
        assertEquals(expectedString, reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        // Test the matches() method
        CharacterReader reader = new CharacterReader("test");
        boolean expectedMatch = true;
        assertEquals(expectedMatch, reader.matches('t'));
    }

    @Test
    public void testMatches_2() {
        // Test the matches() method when the character does not match
        CharacterReader reader = new CharacterReader("test");
        boolean expectedMatch = false;
        assertEquals(expectedMatch, reader.matches('z'));
    }

    @Test
    public void testMatches_3() {
        // Test the matches() method when the sequence matches
        CharacterReader reader = new CharacterReader("test");
        boolean expectedMatch = true;
        assertEquals(expectedMatch, reader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        // Test the matchesIgnoreCase() method
        CharacterReader reader = new CharacterReader("test");
        boolean expectedMatch = true;
        assertEquals(expectedMatch, reader.matchesIgnoreCase("TEST"));
    }

    @Test
    public void testMatchesIgnoreCase_2() {
        // Test the matchesIgnoreCase() method when the sequence does not match
        CharacterReader reader = new CharacterReader("test");
        boolean expectedMatch = false;
        assertEquals(expectedMatch, reader.matchesIgnoreCase("123"));
    }

    @Test
    public void testMatchesAny() {
        // Test the matchesAny() method
        CharacterReader reader = new CharacterReader("test");
        boolean expectedMatch = true;
        assertEquals(expectedMatch, reader.matchesAny('t', 'e', 's'));
    }

    @Test
    public void testMatchesAny_2() {
        // Test the matchesAny() method when the character does not match any of the sequences
        CharacterReader reader = new CharacterReader("test");
        boolean expectedMatch = false;
        assertEquals(expectedMatch, reader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchesLetter() {
        // Test the matchesLetter() method
        CharacterReader reader = new CharacterReader("test");
        boolean expectedMatch = true;
        assertEquals(expectedMatch, reader.matchesLetter());
    }

    @Test
    public void testMatchesLetter_2() {
        // Test the matchesLetter() method when the character is not a letter
        CharacterReader reader = new CharacterReader("123");
        boolean expectedMatch = false;
        assertEquals(expectedMatch, reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        // Test the matchesDigit() method
        CharacterReader reader = new CharacterReader("123");
        boolean expectedMatch = true;
        assertEquals(expectedMatch, reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit_2() {
        // Test the matchesDigit() method when the character is not a digit
        CharacterReader reader = new CharacterReader("test");
        boolean expectedMatch = false;
        assertEquals(expectedMatch, reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        // Test the matchConsume() method
        CharacterReader reader = new CharacterReader("test");
        boolean expectedMatch = true;
        assertEquals(expectedMatch, reader.matchConsume("test"));
        int expectedPos = 4;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testMatchConsume_2() {
        // Test the matchConsume() method when the sequence does not match
        CharacterReader reader = new CharacterReader("test");
        boolean expectedMatch = false;
        assertEquals(expectedMatch, reader.matchConsume("123"));
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        // Test the matchConsumeIgnoreCase() method
        CharacterReader reader = new CharacterReader("test");
        boolean expectedMatch = true;
        assertEquals(expectedMatch, reader.matchConsumeIgnoreCase("TEST"));
        int expectedPos = 4;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase_2() {
        // Test the matchConsumeIgnoreCase() method when the sequence does not match
        CharacterReader reader = new CharacterReader("test");
        boolean expectedMatch = false;
        assertEquals(expectedMatch, reader.matchConsumeIgnoreCase("123"));
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        // Test the containsIgnoreCase() method
        CharacterReader reader = new CharacterReader("test12TEST");
        boolean expectedContains = true;
        assertEquals(expectedContains, reader.containsIgnoreCase("test"));
    }

    @Test
    public void testContainsIgnoreCase_2() {
        // Test the containsIgnoreCase() method when the sequence is not contained
        CharacterReader reader = new CharacterReader("test");
        boolean expectedContains = false;
        assertEquals(expectedContains, reader.containsIgnoreCase("123"));
    }

    @Test
    public void testToString() {
        // Test the toString() method
        CharacterReader reader = new CharacterReader("test");
        String expectedString = "test";
        assertEquals(expectedString, reader.toString());
    }
    
}