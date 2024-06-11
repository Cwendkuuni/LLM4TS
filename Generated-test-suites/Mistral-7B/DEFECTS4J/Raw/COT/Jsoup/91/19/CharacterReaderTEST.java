package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        String testData = "Hello World! <tag>Test</tag> &amp; <>& \"'<>\n\r\f\t";
        reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes()), Locale.ENGLISH));
    }

    @Test
    public void testConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Data"));
        assertEquals(14, reader.pos());
    }

    @Test
    public void testConstructorWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Data"));
        assertEquals(14, reader.pos());
    }

    @Test
    public void testBufferUp() {
        reader.bufferUp();
        assertEquals(14, reader.pos());
        assertEquals(14, reader.isEmptyNoBufferUp() ? 0 : reader.pos() - reader.bufPos);
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIsEmptyThrowsIOException() {
        reader.reader.mark(1);
        reader.reader.reset();
        reader.reader.skip(1);
        reader.isEmpty();
    }

    @Test
    public void testIsEmptyNoBufferUp() {
        assertTrue(reader.isEmptyNoBufferUp());
        reader.consume();
        reader.consume();
        reader.advance();
        assertFalse(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        reader.consume();
        assertEquals('e', reader.current());

        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals('W', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(7, reader.nextIndexOf('<'));
        assertEquals(12, reader.nextIndexOf('&'));
        reader.advance();
        assertEquals(13, reader.nextIndexOf('&'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        assertEquals(-1, reader.nextIndexOf("test"));
        assertEquals(11, reader.nextIndexOf("<tag>Test</tag>"));
    }

    @Test
    public void testConsumeTo() {
        String result = reader.consumeTo('<');
        assertEquals("Hello Wo<", result);
        String result2 = reader.consumeTo("Test");
        assertEquals("Hello World! <tag>Test</tag>", result2);
    }

    @Test
    public void testConsumeToWithSequence() {
        String result = reader.consumeTo("Test</tag>");
        assertEquals("Hello World! <tag>Test</tag>", result);
    }

    @Test
    public void testConsumeToAny() {
        String result = reader.consumeToAny('&');
        assertEquals("Hello World! <tag>Test</tag> &amp; <>& \"'<>\n\r\f\t", result);
    }

    @Test
    public void testConsumeToAnySorted() {
        String result = reader.consumeToAnySorted(new char[]{'<', '&', '>'});
        assertEquals("Hello World! <tag>Test</tag> &amp; <>& \"'<>\n\r\f\t", result);
    }

    @Test
    public void testConsumeData() {
        String result = reader.consumeData();
        assertEquals("&, <, null", result);
    }

    @Test
    public void testConsumeTagName() {
        String result = reader.consumeTagName();
        assertEquals("<tag>", result);
    }

    @Test
    public void testConsumeToEnd() {
        String result = reader.consumeToEnd();
        assertEquals("Hello World! <tag>Test</tag> &amp; <>& \"'<>\n\r\f\t", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        String result = reader.consumeLetterSequence();
        assertEquals("Test", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String result = reader.consumeLetterThenDigitSequence();
        assertEquals("Test123", result);
    }

    @Test
    public void testConsumeHexSequence() {
        String result = reader.consumeHexSequence();
        assertEquals("Test1234abcd", result);
    }

    @Test
    public void testConsumeDigitSequence() {
        String result = reader.consumeDigitSequence();
        assertEquals("1234567890", result);
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('H'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesWithSequence() {
        assertTrue(reader.matches("Test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("test"));
        assertTrue(reader.matchesIgnoreCase("TEST"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('H', 'e'));
        assertTrue(reader.matchesAny('e', 'l'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted(new char[]{'e', 'l', 'H'}));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('3'));
    }

    @Test
    public void testMatchConsume() {
        String result = reader.matchConsume("Test");
        assertEquals("Test", result);
        assertEquals(13, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String result = reader.matchConsumeIgnoreCase("Test");
        assertEquals("Test", result);
        assertEquals(13, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("test"));
        assertTrue(reader.containsIgnoreCase("TEST"));
    }
}