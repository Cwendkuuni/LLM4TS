package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.util.Arrays;

public class CharacterReaderTest {

    private CharacterReader characterReader;
    private char[] testData = new char[]{'a', 'b', 'c', 'd', 'e'};

    @Before
    public void setUp() {
        characterReader = new CharacterReader(new String(testData).toCharArray());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(characterReader.isEmpty());
        characterReader.bufferUp();
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        characterReader.advance();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testConsume() {
        assertEquals('a', characterReader.consume());
        assertEquals('b', characterReader.consume());
        assertEquals('c', characterReader.consume());
        assertEquals('d', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals(CharacterReader.EOF, characterReader.consume());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("abc", characterReader.consumeTo('c'));
        assertEquals("defg", characterReader.consumeTo('g', "defg"));
        assertEquals("", characterReader.consumeTo('x'));
        assertEquals("", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("abcd", characterReader.consumeToAny(new char[]{'a', 'b', 'c', 'd'}));
        assertEquals("efg", characterReader.consumeToAny(new char[]{'e', 'f', 'g'}));
        assertEquals("", characterReader.consumeToAny(new char[]{'x'}));
        assertEquals("", characterReader.consumeToAny());
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("abcd", characterReader.consumeToAnySorted(new char[]{'a', 'b', 'c', 'd'}));
        assertEquals("efg", characterReader.consumeToAnySorted(new char[]{'e', 'f', 'g'}));
        assertEquals("", characterReader.consumeToAnySorted(new char[]{'x'}));
        assertEquals("", characterReader.consumeToAnySorted(new char[0][]));
    }

    @Test
    public void testConsumeData() {
        assertEquals("abcd", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("<tag>", characterReader.consumeTagName());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, characterReader.nextIndexOf('b'));
        assertEquals(3, characterReader.nextIndexOf('d'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfSequence() {
        assertEquals(2, characterReader.nextIndexOf("cd"));
        assertEquals(-1, characterReader.nextIndexOf("xy"));
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('a'));
        assertTrue(characterReader.matches('b'));
        assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatchesSequence() {
        assertTrue(characterReader.matches("ab"));
        assertFalse(characterReader.matches("xy"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("ab"));
        assertTrue(characterReader.matchesIgnoreCase("Ab"));
        assertFalse(characterReader.matchesIgnoreCase("xy"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('a', 'b'));
        assertFalse(characterReader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted(new char[]{'a', 'b'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'x'}));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit('a'));
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new IOException("Test Exception").getReader());
        reader.consume();
    }

    @Test
    public void testMarkAndRewind() {
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('a', characterReader.current());
    }

    @Test
    public void testBufferUp() {
        characterReader.bufferUp();
        assertEquals(5, characterReader.bufLength);
        assertEquals(0, characterReader.bufPos);
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("abcde", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("ab1cd2e3", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("abcdef", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("12345", characterReader.consumeDigitSequence());
    }

    @Test
    public void testToString() {
        assertEquals(new String(testData), characterReader.toString());
    }
}