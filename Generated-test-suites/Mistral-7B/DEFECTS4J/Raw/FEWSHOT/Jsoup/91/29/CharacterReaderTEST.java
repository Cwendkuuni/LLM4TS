package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.util.Arrays;

public class CharacterReaderTest {

    private CharacterReader reader;
    private char[] testData = new char[100];

    @Before
    public void setup() {
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=[]{}|;:,.<>/?`~";
        reader = new CharacterReader(new StringReader(input));
        System.arraycopy(input.toCharArray(), 0, testData, 0, input.length());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
        assertEquals('c', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testMarkInvalid() {
        reader.mark();
        reader.mark();
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(2, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('z'));
        reader.consumeToEnd();
        assertEquals(-1, reader.nextIndexOf('c'));
    }

    @Test
    public void testNextIndexOfSequence() {
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
        reader.consumeToEnd();
        assertEquals(-1, reader.nextIndexOf("bc"));
    }

    @Test
    public void testConsumeTo() {
        String result = reader.consumeTo('c');
        assertEquals("ab", result);
        reader.consume();
        reader.consume();
        assertEquals("cd", reader.consumeTo('d'));
        reader.consumeToEnd();
        assertEquals("", reader.consumeTo('x'));
    }

    @Test
    public void testConsumeToSequence() {
        String result = reader.consumeTo("cd");
        assertEquals("abcd", result);
        reader.consumeToEnd();
        assertEquals("", reader.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        String result = reader.consumeToAny(new char[]{'c', 'd'});
        assertEquals("abcd", result);
        reader.consumeToEnd();
        assertEquals("", reader.consumeToAny(new char[]{'x', 'y'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        String result = reader.consumeToAnySorted(new char[]{'d', 'c'});
        assertEquals("abcd", result);
        reader.consumeToEnd();
        assertEquals("", reader.consumeToAnySorted(new char[]{'x', 'y'}));
    }

    @Test
    public void testConsumeData() {
        String result = reader.consumeData();
        assertEquals(new String(testData, 0, 100), result);
        reader.consumeToEnd();
        assertEquals("", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        String result = reader.consumeTagName();
        assertEquals("<abcdefg>", result);
        reader.consumeToEnd();
        assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        String result = reader.consumeToEnd();
        assertEquals(new String(testData, 0, 100), result);
        reader.consumeToEnd();
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String result = reader.consumeLetterSequence();
        assertEquals("abcdefghijklmno", result);
        reader.consumeToEnd();
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String result = reader.consumeLetterThenDigitSequence();
        assertEquals("abc123def456", result);
        reader.consumeToEnd();
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ab"));
        reader.consumeToEnd();
        assertFalse(reader.matches('a'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertTrue(reader.matchesIgnoreCase("ab"));
        reader.consumeToEnd();
        assertFalse(reader.matchesIgnoreCase("Ab"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('a', 'b'));
        assertTrue(reader.matchesAny("ab", 'c'));
        reader.consumeToEnd();
        assertFalse(reader.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted(new char[]{'b', 'a'}));
        assertTrue(reader.matchesAnySorted("ba".toCharArray()));
        reader.consumeToEnd();
        assertFalse(reader.matchesAnySorted(new char[]{'b', 'a'}));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader.consumeToEnd();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(reader.matchesDigit());
        reader.consumeToEnd();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        reader.matchConsume("abc");
        assertEquals("defg", reader.current());
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        reader.matchConsumeIgnoreCase("AbC");
        assertEquals("defg", reader.current());
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("</title>"));
        reader.consumeToEnd();
        assertFalse(reader.containsIgnoreCase("</title>"));
    }

    @Test
    public void testToString() {
        assertEquals(new String(testData, 0, 100), reader.toString());
        reader.consumeToEnd();
        assertEquals("", reader.toString());
    }
}