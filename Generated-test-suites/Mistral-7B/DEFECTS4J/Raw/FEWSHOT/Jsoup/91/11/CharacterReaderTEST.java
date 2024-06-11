package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    private final char[] testData = new char[]{'A', 'B', 'C', 'D', 'E', 'F', '\n'}; // for consumeLetterSequence test
    private final char[] testDataDigit = new char[]{'1', '2', '3', '4', '5', '6', '\n'}; // for consumeLetterThenDigitSequence test
    private final char[] testDataHex = new char[]{'4', '1', '6', 'F', 'D', '4', '\n'}; // for consumeHexSequence test
    private final char[] testDataDigitHex = new char[]{'1', '3', '5', 'F', 'D', '4', '\n'}; // for consumeDigitSequence test
    private final char[] testDataMixed = new char[]{'A', '1', 'F', 'D', '4', 'E', '\n'}; // for consumeAny test
    private final char[] testDataMixedSorted = new char[]{'1', 'A', 'D', 'E', 'F'}; // for consumeAnySorted test
    private final char[] testDataTagName = new char[]{'<', 'h', 't', 'm', 'l', '>', '\n'}; // for consumeTagName test

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new String(testData).toCharArray());
        assertEquals("ABCDEF", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new String(testDataDigit).toCharArray());
        assertEquals("123456", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new String(testDataHex).toCharArray());
        assertEquals("416FD4", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new String(testDataDigit).toCharArray());
        assertEquals("13564", reader.consumeDigitSequence());
    }

    @Test
    public void testConsumeAny() {
        CharacterReader reader = new CharacterReader(new String(testDataMixed).toCharArray());
        assertEquals("AD4EF", reader.consumeAny(new char[]{'A', 'D', '4', 'E', 'F'}));
    }

    @Test
    public void testConsumeAnySorted() {
        CharacterReader reader = new CharacterReader(new String(testDataMixed).toCharArray());
        assertEquals("AD4EF", reader.consumeAnySorted(new char[]{'A', 'D', '4', 'E', 'F'}));
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new String(testDataTagName).toCharArray());
        assertEquals("<html>", reader.consumeTagName());
    }
}