package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithInputReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("Hello World!", reader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithInputString() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertEquals("Hello World!", reader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithInputReaderAndSize() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"), 10);
        assertEquals("Hello World!", reader.toString());
    }

    @Test
    public void testBufferUpMethod() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        reader.bufferUp();
        assertEquals(0, reader.bufPos);
        assertEquals(12, reader.bufLength);
        reader.bufferUp();
        reader.mark();
        reader.rewindToMark();
        reader.advance();
        assertEquals(1, reader.bufPos);
        reader.consume();
        assertEquals(2, reader.bufPos);
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpMethodThrowsIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"), 1);
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.bufferUp();
        reader.consume(); // should consume one character, but an exception is thrown instead
    }

    @Test
    public void testPosMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(15, reader.pos());
    }

    @Test
    public void testIsEmptyMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertFalse(reader.isEmpty());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrentMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals((char) 0, reader.current());
        reader.consume();
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('W', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals('r', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('d', reader.current());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals((char) 0, reader.current());
    }

    @Test
    public void testConsumeMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        reader.consume();
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('W', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals('r', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('d', reader.current());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals((char) 0, reader.current());
    }

    @Test
    public void testUnconsumeMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('d', reader.current());
    }

    @Test
    public void testAdvanceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        reader.advance();
        reader.advance();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(15, reader.pos());
    }

    @Test
    public void testMarkMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        reader.mark();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('l', reader.current());
    }

    @Test
    public void testNextIndexOfMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals(7, reader.nextIndexOf(' '));
    }

    @Test
    public void testNextIndexOfMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals(4, reader.nextIndexOf("ll"));
    }

    @Test
    public void testNextIndexOfMethodWithEmptyString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals(-1, reader.nextIndexOf(""));
    }

    @Test
    public void testNextIndexOfMethodWithStringStartingBeforeBeginning() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals(-1, reader.nextIndexOf("lo"));
    }

    @Test
    public void testNextIndexOfMethodWithStringEndingAfterEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals(-1, reader.nextIndexOf("ld"));
    }

    @Test
    public void testConsumeToMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        String result = reader.consumeTo('l');
        assertEquals("He", result);
    }

    @Test
    public void testConsumeToMethodWithMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        String result = reader.consumeTo("ll");
        assertEquals("He ll", result);
    }

    @Test
    public void testConsumeToMethodWithStringEndingAfterEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        String result = reader.consumeTo("ld");
        assertEquals("Hello World!", reader.toString());
        assertEquals("Hello", result);
    }

    @Test
    public void testConsumeToMethodWithStringStartingBeforeBeginning() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        String result = reader.consumeTo("lo");
        assertEquals("Hello", result);
    }

    @Test
    public void testConsumeToAnyMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        String result = reader.consumeToAny('l');
        assertEquals("Hello", result);
    }

    @Test
    public void testConsumeToAnyMethodWithMultipleCharactersSorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        String result = reader.consumeToAnySorted(new char[]{'l', 'l', 'l'});
        assertEquals("Hello", result);
    }

    @Test
    public void testConsumeToAnyMethodWithMultipleCharactersUnsorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        String result = reader.consumeToAny(new char[]{'l', 'l', 'l'});
        assertEquals("Hello", result);
    }

    @Test
    public void testConsumeDataMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("&<Hello World!>"));
        String result = reader.consumeData();
        assertEquals("&<Hello World!>", result);
    }

    @Test
    public void testConsumeTagNameMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>Hello World!</title>"));
        String result = reader.consumeTagName();
        assertEquals("title", result);
    }

    @Test
    public void testConsumeToEndMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>Hello World!</title>"));
        String result = reader.consumeToEnd();
        assertEquals("</title>", result);
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        String result = reader.consumeLetterSequence();
        assertEquals("Hello", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello12345 World!"));
        String result = reader.consumeLetterThenDigitSequence();
        assertEquals("Hello12345", result);
    }

    @Test
    public void testMatchesMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(reader.matches('l'));
        assertFalse(reader.matches('.'));
    }

    @Test
    public void testMatchesMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(reader.matches("ll"));
        assertFalse(reader.matches("lll"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(reader.matchesIgnoreCase("ll"));
        assertFalse(reader.matchesIgnoreCase("lll"));
    }

    @Test
    public void testMatchesAnyMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(reader.matchesAny('l'));
        assertFalse(reader.matchesAny('.'));
    }

    @Test
    public void testMatchesAnySortedMethodWithMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(reader.matchesAnySorted(new char[]{'l', 'l'}));
        assertFalse(reader.matchesAnySorted(new char[]{'l', 'a'}));
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigitMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello12345 World!"));
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        reader.matchConsume('l');
        assertEquals(5, reader.pos());
    }

    @Test
    public void testMatchConsumeMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        reader.matchConsume("ll");
        assertEquals(7, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        reader.matchConsumeIgnoreCase("ll");
        assertEquals(7, reader.pos());
    }

    @Test
    public void testContainsIgnoreCaseMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>Hello World!</title>"));
        assertTrue(reader.containsIgnoreCase("<title>"));
        assertTrue(reader.containsIgnoreCase("</title>"));
        assertFalse(reader.containsIgnoreCase("<body>"));
    }

}