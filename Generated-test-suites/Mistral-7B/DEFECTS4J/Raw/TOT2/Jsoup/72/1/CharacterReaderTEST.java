package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for CharacterReader class.
 */
public final class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test constructor with valid input
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        // Test constructor with maxBufferLen input
        CharacterReader readerMaxBufferLen = new CharacterReader(new StringReader("Hello World"), CharacterReader.maxBufferLen);

        // Test constructor with String input
        CharacterReader readerString = new CharacterReader(new StringReader("Hello World"), "Hello World".length());

        // Test pos() method
        Assert.assertEquals(0, reader.pos());

        // Test isEmpty() method
        Assert.assertFalse(reader.isEmpty());

        // Test current() method
        Assert.assertEquals('H', reader.current());

        // Test consume() method
        Assert.assertEquals('H', reader.consume());
        Assert.assertEquals('H', reader.current());

        // Test consumeTo() method with char delimiter
        String consumed = reader.consumeTo('o');
        Assert.assertEquals("Hello", consumed);
        Assert.assertEquals('W', reader.current());

        // Test consumeTo() method with String delimiter
        consumed = reader.consumeTo("ld");
        Assert.assertEquals("Hello", consumed);

        // Test consumeToAny() method with char delimiters
        consumed = reader.consumeToAny(new char[]{'o', 'l'});
        Assert.assertEquals("lloWorld", consumed);

        // Test consumeToAnySorted() method with char delimiters
        consumed = reader.consumeToAnySorted(new char[]{'l', 'o'});
        Assert.assertEquals("lloWorld", consumed);

        // Test consumeData() method
        consumed = reader.consumeData();
        Assert.assertEquals("Hello", consumed);

        // Test consumeTagName() method
        consumed = reader.consumeTagName();
        Assert.assertEquals("Hello", consumed);

        // Test consumeToEnd() method
        consumed = reader.consumeToEnd();
        Assert.assertEquals("Hello World", consumed);

        // Test consumeLetterSequence() method
        consumed = reader.consumeLetterSequence();
        Assert.assertEquals("lloWorld", consumed);

        // Test consumeLetterThenDigitSequence() method
        consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("lloWorld1", consumed);

        // Test consumeHexSequence() method
        consumed = reader.consumeHexSequence();
        Assert.assertEquals("HelloWorld", consumed);

        // Test consumeDigitSequence() method
        consumed = reader.consumeDigitSequence();
        Assert.assertEquals("0123456789", consumed);

        // Test matches() method with char
        Assert.assertTrue(reader.matches('o'));

        // Test matches() method with String
        Assert.assertTrue(reader.matches("ld"));

        // Test matchesIgnoreCase() method with String
        Assert.assertTrue(reader.matchesIgnoreCase("ld"));

        // Test matchesAny() method with char array
        Assert.assertTrue(reader.matchesAny(new char[]{'l', 'o'}));

        // Test matchesAnySorted() method with char array
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}));

        // Test matchesLetter() method
        Assert.assertTrue(reader.matchesLetter());

        // Test matchesDigit() method
        Assert.assertTrue(reader.matchesDigit());

        // Test mark() and rewindToMark() methods
        reader.mark();
        reader.consume('o');
        reader.rewindToMark();
        Assert.assertEquals('H', reader.current());

        // Test nextIndexOf() method with char
        Assert.assertEquals(7, reader.nextIndexOf('l'));

        // Test nextIndexOf() method with String
        Assert.assertEquals(4, reader.nextIndexOf("ld"));

        // Test bufferUp() method
        reader.bufferUp();
        Assert.assertEquals('o', reader.current());

        // Test isEmpty() method after bufferUp()
        Assert.assertFalse(reader.isEmpty());

        // Test exception handling
        try {
            reader.consume();
            reader.consume(); // should throw an exception
        } catch (Exception e) {
            Assert.assertInstanceof(UncheckedIOException.class, e);
        }

        // Test bufferUp() method with IOException
        try {
            reader.reader.mark(10);
            reader.reader.skip(11);
            reader.bufferUp();
        } catch (IOException e) {
            Assert.assertFalse(reader.isEmpty());
        }

        // Test cacheString() method
        String string1 = reader.cacheString(charBuf, stringCache, 0, 5);
        String string2 = reader.cacheString(charBuf, stringCache, 0, 5);
        Assert.assertEquals(string1, string2);
    }
}