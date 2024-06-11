package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;

import java.util.Arrays;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_validConstruction() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals(6, reader.length());
        assertEquals(0, reader.pos());
        assertEquals(0, reader.mark());
        assertEquals(CharacterReader.EOF, reader.current());
        assertFalse(reader.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReader_nullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testCurrent_validCharacter() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.advance();
        assertEquals('h', reader.current());
    }

    @Test
    public void testCurrent_endOfStream() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.advance(reader.length());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume_validCharacter() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals('h', reader.consume());
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume_endOfStream() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.advance(reader.length());
        assertEquals(CharacterReader.EOF, reader.consume());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume_consumesMultipleCharacters() {
        CharacterReader reader = new CharacterReader("hello world");
        String expected = "hell";
        reader.advance(3);
        String consumed = reader.consumeAsString();
        assertEquals(expected, consumed);
        assertEquals("old", reader.current());
    }

    @Test
    public void testConsumeAsString_singleCharacter() {
        CharacterReader reader = new CharacterReader("hello world");
        String result = reader.consumeAsString();
        assertEquals("h", result);
    }

    @Test
    public void testConsumeAsString_multipleCharacters() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.advance(3);
        String result = reader.consumeAsString();
        String expected = "hell";
        assertEquals(expected, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeTo_invalidCharacterSequence() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.consume(3);
        reader.consume(3);
        reader.consume(3);
        reader.consume(3);
        reader.consume(3);
        reader.consumeTo('z');
    }

    @Test
    public void testConsumeTo_validCharacterSequence() {
        CharacterReader reader = new CharacterReader("hello world");
        String expected = "hello";
        String result = reader.consumeTo('l').toString();
        assertEquals(expected, result);
        assertEquals('w', reader.current());
    }

    @Test
    public void testConsumeTo_endOfStream() {
        CharacterReader reader = new CharacterReader("hello world");
        String expected = "world";
        String result = reader.consumeToEnd().toString();
        assertEquals(expected, result);
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("hello123world");
        String result = reader.consumeToAny('1', '2', '3').toString();
        String expected = "123";
        assertEquals(expected, result);
        assertEquals('w', reader.current());
    }

    @Test
    public void testConsumeToAny_emptySequence() {
        CharacterReader reader = new CharacterReader("hello123world");
        String result = reader.consumeToAny().toString();
        assertEquals("", result);
        assertEquals('w', reader.current());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.advance();
        assertTrue(reader.matches('h'));
        reader.advance();
        assertTrue(reader.matches('e'));
        reader.advance();
        assertFalse(reader.matches(' '));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.advance();
        assertTrue(reader.matchesIgnoreCase("h"));
        reader.advance();
        assertTrue(reader.matchesIgnoreCase("e"));
        reader.advance();
        assertFalse(reader.matchesIgnoreCase(" "));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.advance();
        assertTrue(reader.matchesAny('h', 'e'));
        reader.advance();
        assertTrue(reader.matchesAny('e', 'l'));
        reader.advance();
        assertFalse(reader.matchesAny(' '));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("hello123world");
        reader.advance();
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.advance();
        assertEquals("hello", reader.matchConsume("he").toString());
        assertEquals("ld", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.advance();
        assertEquals("Hello", reader.matchConsumeIgnoreCase("He").toString());
        assertEquals(" World", reader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/body"));
        assertFalse(reader.containsIgnoreCase("/bodyhead"));
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.advance();
        reader.unconsume();
        assertEquals('h', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals('w', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('h', reader.current());
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.advance();
        reader.advance();
        reader.advance();
        String expected = "world";
        String result = reader.toString();
        assertEquals(expected, result);
    }
}