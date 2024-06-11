package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals("TestString", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("TestString");
        assertEquals("TestString", characterReader.toString());
    }

    @Test
    public void testBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        characterReader.bufferUp();
        assertEquals("TestStringTest", characterReader.toString());
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        assertEquals(0, characterReader.pos());
        characterReader.consume();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        assertFalse(characterReader.isEmpty());
        characterReader.consumeTo('T');
        assertTrue(characterReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        characterReader.reader.close();
        characterReader.bufferUp();
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        assertEquals('T', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        characterReader.consume();
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        characterReader.mark();
        characterReader.consumeTo('T');
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        assertEquals(-1, characterReader.nextIndexOf('Z'));
        assertEquals(0, characterReader.nextIndexOf('T'));
        characterReader.consumeTo('T');
        assertEquals(1, characterReader.nextIndexOf('T'));
        characterReader.consumeToEnd();
        assertEquals(-1, characterReader.nextIndexOf('T'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        assertEquals(-1, characterReader.nextIndexOf("Test"));
        characterReader.consumeTo("Test");
        assertEquals(0, characterReader.nextIndexOf("Test"));
        characterReader.consumeToEnd();
        assertEquals(-1, characterReader.nextIndexOf("Test"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        assertEquals("ring", characterReader.consumeTo('r'));
        assertEquals("TestTest", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToWithSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        assertEquals("Test", characterReader.consumeTo("est"));
        assertEquals("TestStringTest", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        assertEquals("ing", characterReader.consumeToAny(new char[]{'i', 'n', 'g'}));
        assertEquals("TestStringTest", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        assertEquals("ing", characterReader.consumeToAnySorted(new char[]{'g', 'i', 'n'}));
        assertEquals("TestStringTest", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&<null"));
        assertEquals("&<null", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        assertEquals("title", characterReader.consumeTagName());
        assertEquals("</title>", characterReader.consumeTagName());
        assertEquals("", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        assertEquals("/html>", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        assertEquals("Test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123Test"));
        assertEquals("Test123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestTest"));
        assertTrue(characterReader.matches('T'));
        assertTrue(characterReader.matches("Test"));
        assertFalse(characterReader.matches('X'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestTest"));
        assertTrue(characterReader.matchesIgnoreCase("test"));
        assertFalse(characterReader.matchesIgnoreCase("X"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        assertTrue(characterReader.matchesAny('T', 's', 't'));
        assertFalse(characterReader.matchesAny('X'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        assertTrue(characterReader.matchesAnySorted(new char[]{'s', 't', 'T'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'X'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest"));
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter('1'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTest1"));
        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
    }
}