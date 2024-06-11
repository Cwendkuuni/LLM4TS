package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.util.Arrays;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue( reader.isEmpty() );
        assertEquals(-1, reader.pos() );
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("A") );
        assertFalse( reader.isEmpty() );
        assertEquals(0, reader.pos() );
        assertEquals('A', reader.current() );
        assertEquals('A', reader.consume() );
        assertEquals(-1, reader.current() );
        assertEquals(1, reader.pos() );
        assertTrue( reader.isEmpty() );
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC") );
        assertFalse( reader.isEmpty() );
        assertEquals(0, reader.pos() );
        assertEquals('A', reader.current() );
        assertEquals('A', reader.consume() );
        assertEquals('B', reader.current() );
        assertEquals('B', reader.consume() );
        assertEquals('C', reader.current() );
        assertEquals('C', reader.consume() );
        assertEquals(-1, reader.current() );
        assertEquals(4, reader.pos() );
        assertTrue( reader.isEmpty() );
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        } );
        reader.consume();
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC") );
        assertFalse( reader.isEmpty() );
        reader.mark();
        assertEquals('A', reader.current() );
        reader.advance();
        assertEquals('B', reader.current() );
        reader.rewindToMark();
        assertEquals('A', reader.current() );
        assertEquals(1, reader.pos() );
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertEquals(5, reader.nextIndexOf('E') );
        assertEquals(-1, reader.nextIndexOf('Z') );
        assertEquals(0, reader.nextIndexOf('A') );
    }

    @Test
    public void testCharacterReaderNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertEquals(5, reader.nextIndexOf("EF") );
        assertEquals(-1, reader.nextIndexOf("ZX") );
        assertEquals(0, reader.nextIndexOf("A") );
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertEquals("BCDEFG", reader.consumeTo('E') );
        assertEquals("DEFG", reader.consumeTo('F') );
        assertEquals("G", reader.consumeTo('H') );
        assertEquals("", reader.consumeTo('A') );
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertEquals("BCDEFG", reader.consumeTo("EF") );
        assertEquals("DEFG", reader.consumeTo("F") );
        assertEquals("G", reader.consumeTo("H") );
        assertEquals("", reader.consumeTo("A") );
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertEquals("BCDEFG", reader.consumeToAny(new char[]{'B', 'C', 'D', 'E', 'F', 'G'}));
        assertEquals("G", reader.consumeToAny(new char[]{'H'}));
        assertEquals("", reader.consumeToAny(new char[]{'Z'}));
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertEquals("BCDEFG", reader.consumeToAnySorted(new char[]{'B', 'C', 'D', 'E', 'F', 'G'}));
        assertEquals("G", reader.consumeToAnySorted(new char[]{'H', 'G'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'Z'}));
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null") );
        assertEquals("&<null", reader.consumeData() );
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag name=\"foo\" />") );
        assertEquals("tag", reader.consumeTagName() );
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertEquals("ABCDEFG", reader.consumeToEnd() );
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertEquals("ABC", reader.consumeLetterSequence() );
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("AB123CD456EFG") );
        assertEquals("AB123", reader.consumeLetterThenDigitSequence() );
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&x01&x2f&x41") );
        assertEquals("012f41", reader.consumeHexSequence() );
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890") );
        assertEquals("1234567890", reader.consumeDigitSequence() );
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertTrue( reader.matches('A') );
        assertTrue( reader.matches('B') );
        assertTrue( reader.matches('C') );
        assertFalse( reader.matches('D') );
        assertEquals('B', reader.consume() );
        assertFalse( reader.matches('B') );
        assertTrue( reader.matches('C') );
    }

    @Test
    public void testCharacterReaderMatchesString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertTrue( reader.matches("AB") );
        assertTrue( reader.matches("BC") );
        assertFalse( reader.matches("CD") );
        assertEquals("BC", reader.consumeTo("BC") );
        assertFalse( reader.matches("BC") );
        assertTrue( reader.matches("CD") );
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertTrue( reader.matchesIgnoreCase("ab") );
        assertTrue( reader.matchesIgnoreCase("BC") );
        assertFalse( reader.matchesIgnoreCase("CD") );
        assertEquals("BC", reader.consumeToIgnoreCase("BC") );
        assertFalse( reader.matchesIgnoreCase("BC") );
        assertTrue( reader.matchesIgnoreCase("CD") );
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertTrue( reader.matchesAny('A') );
        assertTrue( reader.matchesAny('B') );
        assertTrue( reader.matchesAny('C') );
        assertFalse( reader.matchesAny('D') );
        assertEquals('C', reader.consume() );
        assertTrue( reader.matchesAny('C') );
        assertFalse( reader.matchesAny('D') );
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertTrue( reader.matchesAnySorted('A') );
        assertTrue( reader.matchesAnySorted('B') );
        assertTrue( reader.matchesAnySorted('C') );
        assertFalse( reader.matchesAnySorted('D') );
        assertEquals('C', reader.consume() );
        assertTrue( reader.matchesAnySorted('C') );
        assertFalse( reader.matchesAnySorted('D') );
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertTrue( reader.matchesLetter() );
        assertTrue( reader.matchesLetter() );
        assertTrue( reader.matchesLetter() );
        assertFalse( reader.matchesLetter() );
        assertFalse( reader.matchesLetter() );
        assertFalse( reader.matchesLetter() );
        assertEquals('G', reader.consumeLetter() );
        assertFalse( reader.matchesLetter() );
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890") );
        assertTrue( reader.matchesDigit() );
        assertTrue( reader.matchesDigit() );
        assertTrue( reader.matchesDigit() );
        assertTrue( reader.matchesDigit() );
        assertTrue( reader.matchesDigit() );
        assertTrue( reader.matchesDigit() );
        assertTrue( reader.matchesDigit() );
        assertTrue( reader.matchesDigit() );
        assertFalse( reader.matchesDigit() );
        assertEquals('0', reader.consumeDigit() );
        assertFalse( reader.matchesDigit() );
    }

    @Test
    public void testCharacterReaderMatchConsumeString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertTrue( reader.matchConsume("AB") );
        assertTrue( reader.matchConsume("BC") );
        assertFalse( reader.matchConsume("CD") );
        assertEquals("BC", reader.consumeTo("BC") );
        assertFalse( reader.matchConsume("BC") );
        assertTrue( reader.matchConsume("CD") );
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG") );
        assertTrue( reader.matchConsumeIgnoreCase("ab") );
        assertTrue( reader.matchConsumeIgnoreCase("BC") );
        assertFalse( reader.matchConsumeIgnoreCase("CD") );
        assertEquals("BC", reader.consumeToIgnoreCase("BC") );
        assertFalse( reader.matchConsumeIgnoreCase("BC") );
        assertTrue( reader.matchConsumeIgnoreCase("CD") );
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>My Page</title></head><body><h1>My Page</h1></body></html>") );
        assertTrue( reader.containsIgnoreCase("/title") );
        assertTrue( reader.containsIgnoreCase("/head") );
        assertTrue( reader.containsIgnoreCase("/body") );
        assertFalse( reader.containsIgnoreCase("/footer") );
    }
}