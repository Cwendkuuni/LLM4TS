package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {
    private CharacterReader reader;
    private char[] testChars;

    @Before
    public void setUp() {
        String testString = "This is a test string with special characters: &<abc123>!@#$%^&*()_+-=<>?";
        testChars = testString.toCharArray();
        reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(testChars)));
    }

    @After
    public void tearDown() {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }

    @Test
    public void testConstructorWithReaderAndSize() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Test".getBytes())), 4);
        assertEquals(4, reader.charBuf.length);
    }

    @Test
    public void testConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Test".getBytes())));
        assertEquals(CharacterReader.maxBufferLen, reader.charBuf.length);
    }

    @Test
    public void testConstructorWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        assertEquals(CharacterReader.maxBufferLen, reader.charBuf.length);
    }

    @Test
    public void testBufferUp() throws IOException {
        reader.bufferUp();
        assertEquals(CharacterReader.maxBufferLen, reader.bufLength);

        reader.reader.mark(CharacterReader.maxBufferLen);
        reader.reader.skip(CharacterReader.maxBufferLen);
        reader.reader.reset();

        reader.bufferUp();

        assertEquals(testChars.length, reader.bufLength);
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpWithIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        reader = new CharacterReader(new InputStreamReader(inputStream), CharacterReader.maxBufferLen);
        reader.bufferUp();
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());

        reader.consume();
        reader.advance();
        reader.mark();
        reader.rewindToMark();

        assertEquals(0, reader.pos());
        reader.advance();

        assertEquals(1, reader.pos());
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
        assertEquals(testChars[0], reader.current());

        reader.consume();
        assertEquals(testChars[0], reader.current());

        reader.consume();
        assertEquals(testChars[1], reader.current());
    }

    @Test
    public void testConsume() {
        reader.consume();
        assertEquals(testChars[0], reader.current());

        reader.consume();
        assertEquals(testChars[1], reader.current());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();

        assertEquals(testChars[0], reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        reader.advance();

        assertEquals(testChars[1], reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.consumeToEnd();

        reader.rewindToMark();
        assertEquals(testChars[0], reader.current());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(-1, reader.nextIndexOf('z'));

        reader.consume('a');
        reader.consume('b');
        reader.consume('c');

        assertEquals(2, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        reader.consume('a');
        reader.consume('b');
        reader.consume('c');

        String testSequence = "bc";
        assertEquals(1, reader.nextIndexOf(testSequence));

        reader.consume('d');
        assertEquals(-1, reader.nextIndexOf(testSequence));
    }

    @Test
    public void testConsumeTo() {
        String expected = "is a test string with special";
        assertEquals(expected, reader.consumeTo(' '));

        reader.consume();
        reader.consume();

        assertEquals("test string with special characters: &<abc123>!@#$%^&*()_+-=<>?".substring(7), reader.consumeToEnd());
    }

    @Test
    public void testConsumeToWithString() {
        String expected = "is a test string with special";
        assertEquals(expected, reader.consumeTo(" "));

        reader.consume();
        reader.consume();

        assertEquals("abc123>!@#$%^&*()_+-=<>?".substring(7), reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("", reader.consumeToAny((char) 0));

        reader.consume('a');
        reader.consume('b');
        reader.consume('c');

        assertEquals("c", reader.consumeToAny('c'));
        assertEquals("bc", reader.consumeToAny('b'));
        assertEquals("abc", reader.consumeToAny('a'));
    }

    @Test
    public void testConsumeToAnySorted() {
        reader.consume('a');
        reader.consume('b');
        reader.consume('c');

        assertEquals("c", reader.consumeToAnySorted(new char[]{'a', 'b', 'c'}));
        assertEquals("b", reader.consumeToAnySorted(new char[]{'a', 'b', 'c', 'c'}));
        assertEquals("ab", reader.consumeToAnySorted(new char[]{'a', 'b'}));
    }

    @Test
    public void testConsumeData() {
        String expected = "&<abc123>!@#$%^&*()_+-=<>?";
        assertEquals(expected, reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        String expected = "<title>";
        reader.consumeLetterSequence();
        reader.consume('>');
        assertEquals(expected, reader.consumeTagName());

        reader.consumeTagName();
        reader.consumeTagName();

        assertEquals("<style>", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "abc123>!@#$%^&*()_+-=<>?";
        assertEquals(expected, reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "is a test string with special";
        assertEquals(expected, reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = "is a test string with special characters: 123";
        reader.consumeLetterThenDigitSequence();
        assertEquals(expected, reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "abc123";
        assertEquals(expected, reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "123";
        assertEquals(expected, reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('a'));
        reader.consume();
        assertFalse(reader.matches('a'));

        assertTrue(reader.matches('a'));
        reader.consume('b');
        assertFalse(reader.matches('a'));
    }

    @Test
    public void testMatchesWithString() {
        String expected = "test string";
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();

        reader.consumeLetterThenDigitSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeLetterThenDigitSequence();

        assertTrue(reader.matches(expected));

        reader.consume('a');
        reader.consume('b');
        assertFalse(reader.matches(expected));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String expected = "test string";
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();

        reader.consumeLetterThenDigitSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeLetterThenDigitSequence();

        assertTrue(reader.matchesIgnoreCase(expected.toLowerCase()));
        assertFalse(reader.matchesIgnoreCase("TesT StrIng"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('a'));
        reader.consume();
        assertFalse(reader.matchesAny('a'));

        assertTrue(reader.matchesAny('a'));
        reader.consume('b');
        assertTrue(reader.matchesAny('a'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted(new char[]{'a'}));
        reader.consume();
        assertFalse(reader.matchesAnySorted(new char[]{'a'}));

        assertTrue(reader.matchesAnySorted(new char[]{'a'}));
        reader.consume('b');
        assertTrue(reader.matchesAnySorted(new char[]{'a'}));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());

        assertTrue(reader.matchesLetter());
        reader.consume('b');
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());

        assertTrue(reader.matchesDigit());
        reader.consume('b');
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String expected = "test string";
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();

        reader.consumeLetterThenDigitSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeLetterThenDigitSequence();

        assertEquals(expected, reader.consumeTo(" "));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String expected = "test string";
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();

        reader.consumeLetterThenDigitSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeLetterThenDigitSequence();

        assertEquals(expected, reader.consumeToIgnoreCase(" tEsT sTrIng"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("<title>"));
        assertTrue(reader.containsIgnoreCase("<style>"));
        assertFalse(reader.containsIgnoreCase("<style>bla"));
    }

    @Test
    public void testToString() {
        String expected = "is a test string with special characters: &<abc123>!@#$%^&*()_+-=<>?";
        assertEquals(expected, reader.toString());
    }
}