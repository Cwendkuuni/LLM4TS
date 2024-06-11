package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_constructor() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abcd", reader.input);
        assertEquals(4, reader.length);
    }

    @Test
    public void testCurrent_whenNotAtEnd_returnsCorrectCharacter() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testCurrent_whenAtEnd_returnsEOF() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume_whenNotAtEnd_returnsCorrespondingCharacter() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        char expected = 'a';
        char actual = reader.consume();
        assertEquals(expected, actual);
        reader.advance();
        expected = 'b';
        actual = reader.consume();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsume_whenAtEnd_returnsEOF() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        char actual = reader.consume();
        assertEquals(CharacterReader.EOF, actual);
    }

    @Test
    public void testConsumeAsString_returnsCorrespondingSubstring() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        String expected = "ab";
        String actual = reader.consumeAsString();
        assertEquals(expected, actual);
        reader.advance();
        reader.advance();
        expected = "cd";
        actual = reader.consumeAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo_returnsCorrespondingSubstring() {
        String input = "abcd--efgh";
        CharacterReader reader = new CharacterReader(input);
        String expected = "ef";
        String actual = reader.consumeTo('f');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo_whenEndNotReached_returnsEmptyString() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        String actual = reader.consumeTo('z');
        assertEquals("", actual);
    }

    @Test
    public void testConsumeTo_whenEndReached_returnsCorrespondingSubstring() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        String expected = "";
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence_returnsCorrespondingSubstring() {
        String input = "abcd12efgHij";
        CharacterReader reader = new CharacterReader(input);
        String expected = "abcd";
        String actual = reader.consumeLetterSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence_returnsCorrespondingSubstring() {
        String input = "abcd12efgHij";
        CharacterReader reader = new CharacterReader(input);
        String expected = "12";
        String actual = reader.consumeHexSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence_returnsCorrespondingSubstring() {
        String input = "abcd12efgHij";
        CharacterReader reader = new CharacterReader(input);
        String expected = "12";
        String actual = reader.consumeDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty_returnsCorrectValue() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.isEmpty());
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchConsume_returnsCorrespondingBoolean() {
        String input = "abcd--efgh--<title>";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsume("--"));
        assertTrue(reader.matchConsume("<title>"));
    }

    @Test
    public void testMatchConsumeIgnoreCase_returnsCorrespondingBoolean() {
        String input = "abcd--efgh--<title>";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsumeIgnoreCase("--"));
        assertTrue(reader.matchConsumeIgnoreCase("<title>"));
    }

    @Test
    public void testContainsIgnoreCase_returnsCorrespondingBoolean() {
        String input = "abcd--efgh--<title>";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("--"));
        assertTrue(reader.containsIgnoreCase("<title>"));
    }

    @Test
    public void testMark_marksCorrectPosition() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        reader.advance();
        reader.advance();
        int expectedMark = 2;
        int actualMark = reader.mark;
        assertEquals(expectedMark, actualMark);
    }

    @Test
    public void testRewindToMark_rewindsToCorrectPosition() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        int expectedPos = 2;
        int actualPos = reader.pos;
        assertEquals(expectedPos, actualPos);
    }
}