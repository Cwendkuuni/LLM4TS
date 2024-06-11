package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderInitialization() {
        CharacterReader reader = new CharacterReader(new StringReader("test"), 100);
        assertEquals("test", reader.toString());
        assertEquals(100, reader.charBuf.length);
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""), 100);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderCurrentCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals('t', reader.current());
        assertEquals('t', reader.consume());
        assertEquals('e', reader.current());
    }

    @Test
    public void testCharacterReaderBufferUp() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.bufferUp();
        assertEquals(1, reader.bufLength);
        assertEquals('t', reader.charBuf[0]);
        reader.bufferUp();
        assertEquals(3, reader.bufLength);
        assertEquals('t', reader.charBuf[0]);
        assertEquals('e', reader.charBuf[1]);
        assertEquals('s', reader.charBuf[2]);
        reader.bufferUp();
        assertEquals(3, reader.bufLength);
        assertEquals('e', reader.charBuf[0]);
        assertEquals('s', reader.charBuf[1]);
        assertEquals('t', reader.charBuf[2]);
    }

    @Test
    public void testCharacterReaderPos() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(5, reader.pos());
        reader.rewindToMark();
        reader.mark();
        reader.consume();
        reader.consume();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals(3, reader.nextIndexOf('t'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testCharacterReaderNextIndexOfString() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals(0, reader.nextIndexOf("st"));
        assertEquals(-1, reader.nextIndexOf("zz"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("est", reader.consumeTo('s'));
        assertEquals("test", reader.toString());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("test", reader.consumeToEnd());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("est", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("test123"));
        assertEquals("123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("test#ff"));
        assertEquals("ff", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("test123"));
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertTrue(reader.matches('t'));
        assertTrue(reader.matches("es"));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertTrue(reader.matchesAny('t', 's', 'e'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertTrue(reader.matchesAnySorted('s', 'e', 't'));
        assertFalse(reader.matchesAnySorted('z'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        assertTrue(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matchesIgnoreCase("Zest"));
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("test", reader.toString());
    }
}