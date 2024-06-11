package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader reader = new StringReader("test");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(reader, characterReader.reader);
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderConstructorWithNullReader() {
        CharacterReader characterReader = new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderConstructorWithReaderAndSize() {
        Reader reader = new StringReader("test");
        CharacterReader characterReader = new CharacterReader(reader, 10);
        assertEquals(reader, characterReader.reader);
        assertEquals(10, characterReader.charBuf.length);
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderConstructorWithNullString() {
        CharacterReader characterReader = new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals("test", characterReader.toString());
        assertEquals(13, characterReader.pos());
        assertEquals(-1, characterReader.nextIndexOf('a'));
        assertEquals(12, characterReader.nextIndexOf("est"));
    }

    @Test
    public void testBufferUp() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.bufferUp();
        assertEquals(13, characterReader.pos());
        assertEquals(-1, characterReader.nextIndexOf('a'));
        assertEquals(12, characterReader.nextIndexOf("est"));
        characterReader.advance();
        characterReader.bufferUp();
        assertEquals(14, characterReader.pos());
        assertEquals(-1, characterReader.nextIndexOf('a'));
        assertEquals(13, characterReader.nextIndexOf("est"));
    }

    @Test
    public void testIsEmpty() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertFalse(characterReader.isEmpty());
        characterReader.consume();
        assertFalse(characterReader.isEmpty());
        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals('t', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
        characterReader.bufferUp();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsume() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testUnconsume() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testAdvance() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.advance();
        characterReader.advance();
        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testMark() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('t', characterReader.current());
        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testRewindToMarkWhenNothingMarked() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.consume();
        characterReader.rewindToMark();
    }

    @Test
    public void testNextIndexOf() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals(-1, characterReader.nextIndexOf('z'));
        assertEquals(4, characterReader.nextIndexOf('t'));
        assertEquals(12, characterReader.nextIndexOf("est"));
        characterReader.consume();
        assertEquals(5, characterReader.nextIndexOf('t'));
        characterReader.consumeToEnd();
        assertEquals(-1, characterReader.nextIndexOf('a'));
    }

    @Test
    public void testNextIndexOfWithSequence() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals(-1, characterReader.nextIndexOf("abc"));
        assertEquals(4, characterReader.nextIndexOf("est"));
        characterReader.consume();
        assertEquals(5, characterReader.nextIndexOf("est"));
        characterReader.consumeToEnd();
        assertEquals(-1, characterReader.nextIndexOf("abc"));
    }

    @Test
    public void testConsumeTo() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals("est", characterReader.consumeTo('t'));
        assertEquals("test", characterReader.toString());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeToWithSequence() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals("est", characterReader.consumeTo("est"));
        assertEquals("test", characterReader.toString());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeToAny() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals("est", characterReader.consumeToAny('s', 't'));
        assertEquals("test", characterReader.toString());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeToAnySorted() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals("est", characterReader.consumeToAnySorted('s', 't'));
        assertEquals("test", characterReader.toString());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeData() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;html&gt;"));
        assertEquals("&lt;html", characterReader.consumeData());
        assertEquals("", characterReader.toString());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeTagName() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html>"));
        assertEquals("<html", characterReader.consumeTagName());
        assertEquals("", characterReader.toString());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeToEnd() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals("test", characterReader.consumeToEnd());
        assertEquals("", characterReader.toString());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeLetterSequence() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals("est", characterReader.consumeLetterSequence());
        assertEquals("test", characterReader.toString());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test123"));
        assertEquals("123", characterReader.consumeLetterThenDigitSequence());
        assertEquals("test", characterReader.toString());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeHexSequence() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test&#x61;x&#x63;"));
        assertEquals("&#x61;&#x63;x", characterReader.consumeHexSequence());
        assertEquals("test", characterReader.toString());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeDigitSequence() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test123"));
        assertEquals("123", characterReader.consumeDigitSequence());
        assertEquals("test", characterReader.toString());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testMatches() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matches('t'));
        assertFalse(characterReader.matches('z'));
        characterReader.consume();
        assertTrue(characterReader.matches('e'));
    }

    @Test
    public void testMatchesSequence() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matches("es"));
        assertFalse(characterReader.matches("foo"));
        characterReader.consumeTo("es");
        assertTrue(characterReader.matches("est"));
    }

    @Test
    public void testMatchesIgnoreCase() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test"));
        assertTrue(characterReader.matchesIgnoreCase("es"));
        assertFalse(characterReader.matchesIgnoreCase("foo"));
        characterReader.consumeToAnyIgnoreCase(Arrays.asList('s', 't'));
        assertTrue(characterReader.matchesIgnoreCase("est"));
    }

    @Test
    public void testMatchesAny() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchesAny('e', 't'));
        assertFalse(characterReader.matchesAny('z'));
        characterReader.consume();
        assertTrue(characterReader.matchesAny('e', 's'));
    }

    @Test
    public void testMatchesAnySorted() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchesAnySorted(Arrays.asList('e', 't')));
        assertFalse(characterReader.matchesAnySorted(Arrays.asList('z')));
        characterReader.consume();
        assertTrue(characterReader.matchesAnySorted(Arrays.asList('e', 's')));
    }

    @Test
    public void testMatchesLetter() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter('1'));
        characterReader.consume();
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test123"));
        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        assertFalse(characterReader.matchesDigit());
        characterReader.consume();
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeSequence() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchConsume("es"));
        assertEquals("est", characterReader.toString());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testMatchConsumeSequenceIgnoreCase() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test"));
        assertTrue(characterReader.matchConsumeIgnoreCase("es"));
        assertEquals("est", characterReader.toString());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testContainsIgnoreCase() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><title>Test</title>"));
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("test"));
        assertFalse(characterReader.containsIgnoreCase("/body"));
        characterReader.consumeTagName();
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertFalse(characterReader.containsIgnoreCase("/body"));
    }

    @Test
    public void testToString() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals("test", characterReader.toString());
        characterReader.consumeToEnd();
        assertEquals("", characterReader.toString());
    }
}