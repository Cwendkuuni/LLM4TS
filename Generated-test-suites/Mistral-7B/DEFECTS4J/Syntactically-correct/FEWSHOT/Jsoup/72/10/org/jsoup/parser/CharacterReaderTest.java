package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Locale;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test constructor with Reader input
        Reader reader = new StringReader("abcdef");
        CharacterReader charReader = new CharacterReader(reader);
        assertEquals("abcdef", charReader.toString());

        // Test constructor with String input
        CharacterReader charReader1 = new CharacterReader("abcdef");
        assertEquals("abcdef", charReader1.toString());

        // Test constructor with specific buffer size
        CharacterReader charReader2 = new CharacterReader(new StringReader("abcdef"), 5);
        assertEquals("abcdef", charReader2.toString());

        // Test bufferUp method
        charReader1.mark();
        charReader1.bufferUp();
        assertTrue(charReader1.pos() > charReader1.readerPos());
        charReader1.rewindToMark();
    }

    @Test
    public void testCurrentAndConsume() {
        Reader reader = new StringReader("abcdef");
        CharacterReader charReader = new CharacterReader(reader);

        // Test current method
        assertEquals('a', charReader.current());
        assertEquals('a', charReader.consume());
        assertEquals('b', charReader.current());

        // Test consumeTo method with char
        String expected = "bcdef";
        String result = charReader.consumeTo('c');
        assertEquals(expected, result);
        assertEquals('d', charReader.current());

        // Test consumeTo method with String
        String input = "efoo";
        String result1 = charReader.consumeTo(input);
        assertEquals("efoo", result1);
        assertEquals('f', charReader.current());

        // Test consumeToEnd method
        String result2 = charReader.consumeToEnd();
        assertEquals("def", result2);
        assertTrue(charReader.isEmpty());
    }

    @Test
    public void testConsumeToAnyAndConsumeToAnySorted() {
        Reader reader = new StringReader("abcdefgAaBbCc12345");
        CharacterReader charReader = new CharacterReader(reader);

        // Test consumeToAny method with char array
        char[] expectedChars = new char[]{'a', 'b', 'c', 'e', 'f'};
        String result1 = charReader.consumeToAny(expectedChars);
        assertArrayEquals(expectedChars, result1.toCharArray());

        // Test consumeToAnySorted method with char array
        char[] expectedCharsSorted = new char[]{'a', 'B', 'B', 'C', 'C', 'c', 'e', 'f'};
        String result2 = charReader.consumeToAnySorted(expectedCharsSorted);
        assertArrayEquals(expectedCharsSorted, result2.toCharArray());

        // Test consumeData method
        String result3 = charReader.consumeData();
        assertEquals("gAaBbCc12345", result3);
    }

    @Test
    public void testMatchesMethods() {
        Reader reader = new StringReader("abcdefghijklmnopqrstuvwxyz");
        CharacterReader charReader = new CharacterReader(reader);

        // Test matches method with char
        assertTrue(charReader.matches('a'));
        assertFalse(charReader.matches('z'));

        // Test matches method with String
        String input = "efghij";
        assertTrue(charReader.matches(input));
        assertFalse(charReader.matches("xyz"));

        // Test matchesIgnoreCase method with String
        String inputIgnoreCase = "efghij";
        assertTrue(charReader.matchesIgnoreCase(inputIgnoreCase));
        assertTrue(charReader.matchesIgnoreCase("EfGhIj"));
        assertFalse(charReader.matchesIgnoreCase("xyz"));

        // Test matchesAny method with char array
        char[] expectedChars = new char[]{'a', 'b', 'e', 'i'};
        assertTrue(charReader.matchesAny(expectedChars));
        assertFalse(charReader.matchesAny(new char[]{'z', 'x'}));

        // Test matchesAnySorted method with char array
        char[] expectedCharsSorted = new char[]{'a', 'e', 'i', 'i'};
        assertTrue(charReader.matchesAnySorted(expectedCharsSorted));
        assertFalse(charReader.matchesAnySorted(new char[]{'z', 'x'}));
    }

    @Test
    public void testConsumeLetterSequenceAndConsumeLetterThenDigitSequence() {
        Reader reader = new StringReader("abc123def456ghijk");
        CharacterReader charReader = new CharacterReader(reader);

        // Test consumeLetterSequence method
        String expected = "bcdef";
        String result = charReader.consumeLetterSequence();
        assertEquals(expected, result);
        assertEquals('d', charReader.current());

        // Test consumeLetterThenDigitSequence method
        String expected1 = "bcdef";
        String expected2 = "456ghijk";
        String result1 = charReader.consumeLetterThenDigitSequence();
        assertEquals(expected, result1);
        assertEquals(expected2, charReader.consumeLetterThenDigitSequence());
        assertTrue(charReader.isEmpty());
    }

    @Test
    public void testConsumeHexSequenceAndConsumeDigitSequence() {
        Reader reader = new StringReader("abc123def456ghijk");
        CharacterReader charReader = new CharacterReader(reader);

        // Test consumeHexSequence method
        String expected = "bcdef";
        String result = charReader.consumeHexSequence();
        assertEquals(expected, result);
        assertEquals('d', charReader.current());

        // Test consumeDigitSequence method
        String expected1 = "123";
        String expected2 = "456";
        String result1 = charReader.consumeDigitSequence();
        assertEquals(expected1, result1);
        assertEquals(expected2, charReader.consumeDigitSequence());
        assertTrue(charReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        Reader reader = new StringReader("abcdef");
        CharacterReader charReader = new CharacterReader(reader);
        charReader.bufferUp();
        reader.close();
        charReader.consume();
    }
}