package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest class
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test constructor with Reader input
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.consumeToEnd());
        Assert.assertEquals('H', reader.current());
        reader.advance();
        Assert.assertEquals('e', reader.current());

        // Test constructor with String input
        CharacterReader readerString = new CharacterReader("Java Test");
        Assert.assertEquals("Java Test", reader.consumeToEnd());
        Assert.assertEquals('J', reader.current());

        // Test bufferUp method
        reader.bufferUp();
        Assert.assertEquals(8, reader.pos()); // readerPos + bufPos

        // Test isEmpty method
        Assert.assertFalse(reader.isEmpty());

        // Test consume method
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals('a', reader.current());

        // Test unconsume method
        reader.unconsume();
        Assert.assertEquals('e', reader.current());

        // Test advance method
        reader.advance();
        Assert.assertEquals('v', reader.current());

        // Test mark and rewindToMark methods
        reader.mark();
        Assert.assertEquals('v', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('e', reader.current());

        // Test nextIndexOf method with char
        reader.bufferUp();
        reader.mark();
        Assert.assertEquals(4, reader.nextIndexOf('l'));
        Assert.assertEquals(4, reader.pos() - reader.readerPos); // readerPos + bufPos
        reader.rewindToMark();

        // Test nextIndexOf method with CharSequence
        reader.bufferUp();
        reader.mark();
        Assert.assertEquals(6, reader.nextIndexOf("va"));
        Assert.assertEquals(7, reader.pos() - reader.readerPos); // readerPos + bufPos
        reader.rewindToMark();

        // Test consumeTo method
        Assert.assertEquals("llo", reader.consumeTo('l'));

        // Test consumeTo method with CharSequence
        Assert.assertEquals("llo World", reader.consumeTo("llo "));

        // Test consumeToAny method
        reader.bufferUp();
        Assert.assertEquals("lo", reader.consumeToAny(new char[]{'l', 'o'}));

        // Test consumeToAnySorted method
        reader.bufferUp();
        Assert.assertEquals("lo", reader.consumeToAnySorted(new char[]{'o', 'l'}));

        // Test consumeData method
        reader.bufferUp();
        Assert.assertEquals("Java Test", reader.consumeData());

        // Test consumeTagName method
        reader.bufferUp();
        Assert.assertEquals("Java", reader.consumeTagName());

        // Test consumeToEnd method
        Assert.assertEquals("Test", reader.consumeToEnd());

        // Test consumeLetterSequence method
        Assert.assertEquals("LetterSequence", reader.consumeLetterSequence());

        // Test consumeLetterThenDigitSequence method
        Assert.assertEquals("LetterThenDigitSequence123", reader.consumeLetterThenDigitSequence());

        // Test consumeHexSequence method
        Assert.assertEquals("HexSequence0123", reader.consumeHexSequence());

        // Test consumeDigitSequence method
        Assert.assertEquals("DigitSequence12345", reader.consumeDigitSequence());

        // Test matches method
        reader.bufferUp();
        Assert.assertTrue(reader.matches('l'));
        Assert.assertFalse(reader.matches('o'));

        // Test matches method with String
        Assert.assertTrue(reader.matches("llo"));
        Assert.assertFalse(reader.matches("llo1"));

        // Test matchesIgnoreCase method
        Assert.assertTrue(reader.matchesIgnoreCase("LLo"));
        Assert.assertTrue(reader.matchesIgnoreCase("llo"));

        // Test matchesAny method
        reader.bufferUp();
        Assert.assertTrue(reader.matchesAny(new char[]{'l', 'o'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'a', 'z'}));

        // Test matchesAnySorted method
        reader.bufferUp();
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'z', 'a'}));

        // Test matchesLetter method
        reader.bufferUp();
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesDigit());

        // Test matchesDigit method
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertTrue(reader.matchesDigit());

        // Test matchConsume method
        reader.bufferUp();
        Assert.assertTrue(reader.matchConsume("llo"));

        // Test matchConsumeIgnoreCase method
        reader.bufferUp();
        Assert.assertTrue(reader.matchConsumeIgnoreCase("LLo"));

        // Test containsIgnoreCase method
        reader.bufferUp();
        Assert.assertTrue(reader.containsIgnoreCase("test"));
        Assert.assertTrue(reader.containsIgnoreCase("Test"));

        // Test heuristic to determine if the current buffer looks like binary content
        reader.bufferUp();
        byte[] binaryData = new byte[5];
        new String(binaryData, 0, 5).getChars(0, 5, charBuf, 0);
        Assert.assertTrue(reader.isBinaryContent());

        // Test toString method
        Assert.assertEquals("Java Test", reader.toString());
    }
}