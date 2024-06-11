package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructionWithReader() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(reader, characterReader.reader);
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderConstructionWithNullReader() {
        CharacterReader characterReader = new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderConstructionWithString() {
        CharacterReader characterReader = new CharacterReader("Test String");
        assertEquals("Test String", characterReader.reader.toString());
    }

    @Test
    public void testBufferUp() throws IOException {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.bufferUp();
        assertEquals(0, characterReader.bufPos);
        characterReader.bufferUp();
        assertEquals(6, characterReader.bufLength);
        assertEquals(6, characterReader.bufSplitPoint);
        assertEquals(0, characterReader.readerPos);
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpWithIOException() throws IOException {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.reader.mark(1);
        characterReader.reader.read();
        characterReader.bufferUp();
        characterReader.reader.reset();
        characterReader.reader.skip(1);
        characterReader.reader.mark(1);
        characterReader.reader.read();
        characterReader.bufferUp();
        characterReader.reader.close();
        characterReader.bufferUp();
    }

    @Test
    public void testPos() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(0, characterReader.pos());
        characterReader.consume();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.isEmpty());
        characterReader.consume();
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals('\u0000', characterReader.current());
        characterReader.consume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsume() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.consume();
        assertEquals('T', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(5, characterReader.nextIndexOf('t'));
    }

    @Test
    public void testNextIndexOfWithEmptySequence() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(-1, characterReader.nextIndexOf(""));
    }

    @Test
    public void testNextIndexOfWithNull() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(-1, characterReader.nextIndexOf(null));
    }

    @Test
    public void testConsumeTo() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Test", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToEnd() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("String", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        Reader reader = new StringReader("Test String < TestString>");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Test String<", characterReader.consumeToAny(new char[]{'<', 'T'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        Reader reader = new StringReader("Test String < TestString>");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Test String", characterReader.consumeToAnySorted(new char[]{'<', 'S'}));
    }

    @Test
    public void testConsumeData() {
        Reader reader = new StringReader("Test & String < TestString>");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Test & String < TestString>", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        Reader reader = new StringReader("<html><head><title>Test Title</title></head><body>Test Body</body></html>");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("<html><head><title>", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeLetterSequence() {
        Reader reader = new StringReader("Test Letters Sequence");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Test Letters", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        Reader reader = new StringReader("Test123Letters Sequence");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Test123Letters", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        Reader reader = new StringReader("Test #FFF000 Color");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("#FFF000", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        Reader reader = new StringReader("Test 12345 Digits");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("12345", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        Reader reader = new StringReader("Test Tester Tested Testing");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matches('t'));
        assertTrue(characterReader.matches("es"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Reader reader = new StringReader("Test Tester Tested Testing");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matchesIgnoreCase("es"));
    }

    @Test
    public void testMatchesAny() {
        Reader reader = new StringReader("Test123Test456");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matchesAny(new char[]{'1', '3'}));
        assertTrue(characterReader.matchesAny(new char[]{'.', '5'}));
    }

    @Test
    public void testMatchesAnySorted() {
        Reader reader = new StringReader("Test123Test456");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matchesAnySorted(new char[]{'1', '3', '5'}));
    }

    @Test
    public void testMatchesLetter() {
        Reader reader = new StringReader("Test Letters Sequence");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        Reader reader = new StringReader("Test123Letters Sequence");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        Reader reader = new StringReader("Test <html>Test <body>Test</body></html>");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matchConsume("<html>"));
        assertTrue(characterReader.matchConsume("body>"));
        assertFalse(characterReader.matchConsume("head>"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        Reader reader = new StringReader("Test <html>Test <body>Test</body></html>");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matchConsumeIgnoreCase("body>"));
        assertTrue(characterReader.matchConsumeIgnoreCase("html>"));
        assertFalse(characterReader.matchConsumeIgnoreCase("Head>"));
    }

    @Test
    public void testContainsIgnoreCase() {
        Reader reader = new StringReader("Test <html><head><title>Test Title</title></head><body>Test Body</body></html>");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.containsIgnoreCase("/title>"));
        assertTrue(characterReader.containsIgnoreCase("/body>"));
        assertFalse(characterReader.containsIgnoreCase("/head>"));
    }

    @Test
    public void testToString() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Test String", characterReader.toString());
    }
}