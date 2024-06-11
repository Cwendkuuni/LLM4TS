package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("test", reader.toString());
    }

    /**
     * Test pos() method
     */
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        assertEquals(2, reader.pos());
    }

    /**
     * Test isEmpty() method
     */
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertTrue(reader.isEmpty());
        reader.advance();
        assertFalse(reader.isEmpty());
    }

    /**
     * Test current() method
     * @throws IOException
     */
    @Test
    public void testCurrent() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        assertEquals('t', reader.current());
    }

    /**
     * Test consume() method
     * @throws IOException
     */
    @Test
    public void testConsume() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        char c = reader.consume();
        assertEquals('t', c);
        assertEquals(2, reader.pos());
    }

    /**
     * Test unconsume() method
     * @throws IOException
     */
    @Test
    public void testUnconsume() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.consume();
        reader.unconsume();
        assertEquals('t', reader.current());
        assertEquals(2, reader.pos());
    }

    /**
     * Test advance() method
     * @throws IOException
     */
    @Test
    public void testAdvance() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        assertEquals('s', reader.current());
        assertEquals(3, reader.pos());
    }

    /**
     * Test mark() method
     * @throws IOException
     */
    @Test
    public void testMark() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('t', reader.current());
        assertEquals(2, reader.pos());
    }

    /**
     * Test nextIndexOf() method with a single char
     * @throws IOException
     */
    @Test
    public void testNextIndexOfSingleChar() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        int index = reader.nextIndexOf('s');
        assertEquals(5, index);
    }

    /**
     * Test nextIndexOf() method with a multi-char sequence
     * @throws IOException
     */
    @Test
    public void testNextIndexOfMultiCharSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        reader.advance();
        int index = reader.nextIndexOf("es");
        assertEquals(3, index);
    }

    /**
     * Test consumeTo() method with a single char
     * @throws IOException
     */
    @Test
    public void testConsumeToSingleChar() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        String result = reader.consumeTo('s');
        assertEquals("ts", result);
        assertEquals(5, reader.pos());
    }

    /**
     * Test consumeTo() method with a multi-char sequence
     * @throws IOException
     */
    @Test
    public void testConsumeToMultiCharSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        reader.advance();
        String result = reader.consumeTo("es");
        assertEquals("test", result);
        assertEquals(5, reader.pos());
    }

    /**
     * Test consumeToAny() method
     * @throws IOException
     */
    @Test
    public void testConsumeToAny() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        String result = reader.consumeToAny(new char[] {'s', 'e'}
);
        assertEquals("test", result);
        assertEquals(5, reader.pos());
    }

    /**
     * Test consumeToAnySorted() method
     * @throws IOException
     */
    @Test
    public void testConsumeToAnySorted() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        String result = reader.consumeToAnySorted(new char[] {'s', 'e'}
);
        assertEquals("test", result);
        assertEquals(5, reader.pos());
    }

    /**
     * Test consumeData() method
     * @throws IOException
     */
    @Test
    public void testConsumeData() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test&"));
        String result = reader.consumeData();
        assertEquals("test&", result);
        assertEquals(5, reader.pos());
    }

    /**
     * Test consumeTagName() method
     * @throws IOException
     */
    @Test
    public void testConsumeTagName() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("<test>"));
        String result = reader.consumeTagName();
        assertEquals("<test>", result);
        assertEquals(6, reader.pos());
    }

    /**
     * Test consumeToEnd() method
     * @throws IOException
     */
    @Test
    public void testConsumeToEnd() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        String result = reader.consumeToEnd();
        assertEquals("test", result);
        assertEquals(6, reader.pos());
    }

    /**
     * Test consumeLetterSequence() method
     * @throws IOException
     */
    @Test
    public void testConsumeLetterSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        String result = reader.consumeLetterSequence();
        assertEquals("test", result);
        assertEquals(6, reader.pos());
    }

    /**
     * Test consumeLetterThenDigitSequence() method
     * @throws IOException
     */
    @Test
    public void testConsumeLetterThenDigitSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test123"));
        String result = reader.consumeLetterThenDigitSequence();
        assertEquals("test123", result);
        assertEquals(7, reader.pos());
    }

    /**
     * Test consumeHexSequence() method
     * @throws IOException
     */
    @Test
    public void testConsumeHexSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test#f0f0"));
        String result = reader.consumeHexSequence();
        assertEquals("test#f0f0", result);
        assertEquals(8, reader.pos());
    }

    /**
     * Test consumeDigitSequence() method
     * @throws IOException
     */
    @Test
    public void testConsumeDigitSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test123"));
        String result = reader.consumeDigitSequence();
        assertEquals("test123", result);
        assertEquals(7, reader.pos());
    }

    /**
     * Test matches() method with a single char
     */
    @Test
    public void testMatchesSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('s'));
    }

    /**
     * Test matches() method with a multi-char sequence
     */
    @Test
    public void testMatchesMultiCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        assertTrue(reader.matches("es"));
        assertFalse(reader.matches("et"));
    }

    /**
     * Test matchesIgnoreCase() method with a single char
     */
    @Test
    public void testMatchesIgnoreCaseSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        assertTrue(reader.matchesIgnoreCase("t"));
        assertFalse(reader.matchesIgnoreCase("s"));
    }

    /**
     * Test matchesIgnoreCase() method with a multi-char sequence
     */
    @Test
    public void testMatchesIgnoreCaseMultiCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        assertTrue(reader.matchesIgnoreCase("es"));
        assertFalse(reader.matchesIgnoreCase("et"));
    }

    /**
     * Test matchesAny() method
     */
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        assertTrue(reader.matchesAny(new char[] {'t', 's'}));
        assertFalse(reader.matchesAny(new char[] {'f', 's'}));
    }

    /**
     * Test matchesAnySorted() method
     */
    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        assertTrue(reader.matchesAnySorted(new char[] {'s', 't'}));
        assertFalse(reader.matchesAnySorted(new char[] {'f', 's'}));
    }

    /**
     * Test matchesLetter() method
     */
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    /**
     * Test matchesDigit() method
     */
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("test123"));
        reader.advance();
        reader.advance();
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    /**
     * Test matchConsume() method with a single char
     */
    @Test
    public void testMatchConsumeSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        assertTrue(reader.matchConsume('t'));
        assertEquals(2, reader.pos());
    }

    /**
     * Test matchConsume() method with a multi-char sequence
     */
    @Test
    public void testMatchConsumeMultiCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        assertTrue(reader.matchConsume("es"));
        assertEquals(4, reader.pos());
    }

    /**
     * Test matchConsumeIgnoreCase() method with a single char
     */
    @Test
    public void testMatchConsumeIgnoreCaseSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        reader.advance();
        reader.advance();
        assertTrue(reader.matchConsumeIgnoreCase("es"));
        assertEquals(4, reader.pos());
    }

    /**
     * Test matchConsumeIgnoreCase() method with a multi-char sequence
     */
    @Test
    public void testMatchConsumeIgnoreCaseMultiCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        reader.advance();
        reader.advance();
        assertTrue(reader.matchConsumeIgnoreCase("Es"));
        assertEquals(4, reader.pos());
    }

    /**
     * Test containsIgnoreCase() method
     */
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("TITLE"));
        assertFalse(reader.containsIgnoreCase("head"));
    }

    /**
     * Test toString() method
     */
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        String result = reader.toString();
        assertEquals("test", result);
    }

    /**
     * Test cacheString() method
     */
    @Test
    public void testCacheString() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        String result = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufPos + 2);
        assertEquals("ts", result);
        assertEquals("ts", reader.stringCache[0]);
    }

    /**
     * Test rangeEquals() method
     */
    @Test
    public void testRangeEquals() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        assertTrue(reader.rangeEquals(reader.charBuf, reader.bufPos, 2, "ts"));
    }
}