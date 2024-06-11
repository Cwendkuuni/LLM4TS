package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.util.Arrays;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("test", reader.toString());
    }

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("error"));
        reader.bufferUp();
        reader.consume();
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals(2, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("est", reader.consumeTo('s'));
        assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("est", reader.consumeToAny(new char[]{'s'}));
        assertEquals("test", reader.consumeToAny(new char[]{'_'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("est", reader.consumeToAnySorted(new char[]{'s'}));
        assertEquals("test", reader.consumeToAnySorted(new char[]{'z'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        assertEquals("&<null", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><body><h1>Test</h1></body></html>"));
        assertEquals("html", reader.consumeTagName());
        assertEquals("body", reader.consumeTagName());
        assertEquals("h1", reader.consumeTagName());
        assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><body><h1>Test</h1></body></html>"));
        assertEquals("</html>", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("testLetterSequence"));
        assertEquals("LetterSequence", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("testLetterThenDigitSequence123"));
        assertEquals("LetterThenDigitSequence123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("test#xFFFF&"));
        assertEquals("#xFFFF", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("test123"));
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertTrue(reader.matches('t'));
        assertTrue(reader.matches("es"));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        assertTrue(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertTrue(reader.matchesAny(new char[]{'t'}));
        assertTrue(reader.matchesAny(new char[]{'s'}));
        assertFalse(reader.matchesAny(new char[]{'z'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertTrue(reader.matchesAnySorted(new char[]{'s'}));
        assertTrue(reader.matchesAnySorted(new char[]{'t'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("test123"));
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("est", reader.matchConsume("es"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        assertEquals("est", reader.matchConsumeIgnoreCase("es"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><body><h1>Test</h1></body></html>"));
        assertTrue(reader.containsIgnoreCase("/html"));
        assertTrue(reader.containsIgnoreCase("/body"));
        assertTrue(reader.containsIgnoreCase("/h1"));
        assertFalse(reader.containsIgnoreCase("/z"));
    }
}