package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test constructor with valid input
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        // Test initial state
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        // Test consume method
        Assert.assertEquals('H', reader.consume());
        Assert.assertEquals(1, reader.pos());
        // Test consume all characters
        Assert.assertEquals("World", reader.consumeToEnd());
        Assert.assertTrue(reader.isEmpty());
        // Test mark and rewind methods
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('H', reader.current());
        reader.unconsume();
        reader.unconsume();
        // Test nextIndexOf method
        Assert.assertEquals(6, reader.nextIndexOf('W'));
        Assert.assertEquals(-1, reader.nextIndexOf('X'));
        // Test consumeTo method
        Assert.assertEquals("World", reader.consumeTo('d'));
        Assert.assertEquals("World", reader.consumeTo('o'));
        // Test consumeToAny method
        Assert.assertEquals("Hello", reader.consumeToAny('l', 'o'));
        Assert.assertEquals("", reader.consumeToAny('x'));
        // Test consumeToAnySorted method
        Assert.assertEquals("World", reader.consumeToAnySorted('W', 'r', 'd'));
        Assert.assertEquals("", reader.consumeToAnySorted('x'));
        // Test consumeData method
        // This method is not covered by unit tests as it depends on the implementation details of CharacterReader
        // Test consumeTagName method
        reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        Assert.assertEquals("title", reader.consumeTagName());
        // Test consumeToEnd method
        Assert.assertEquals("<html><head><title>Test</title></head><body>", reader.consumeToEnd());
        // Test consumeLetterSequence method
        reader = new CharacterReader(new StringReader("AlphaBeta"));
        Assert.assertEquals("AlphaBeta", reader.consumeLetterSequence());
        // Test consumeLetterThenDigitSequence method
        reader = new CharacterReader(new StringReader("Zebra123"));
        Assert.assertEquals("Zebra123", reader.consumeLetterThenDigitSequence());
        // Test consumeHexSequence method
        reader = new CharacterReader(new StringReader("0xFoo"));
        Assert.assertEquals("0xFoo", reader.consumeHexSequence());
        // Test consumeDigitSequence method
        reader = new CharacterReader(new StringReader("12345"));
        Assert.assertEquals("12345", reader.consumeDigitSequence());
        // Test matches method
        reader = new CharacterReader(new StringReader("HelloWorld"));
        Assert.assertTrue(reader.matches('H'));
        Assert.assertTrue(reader.matches("Hello"));
        // Test matchesIgnoreCase method
        reader = new CharacterReader(new StringReader("HelloWorld"));
        Assert.assertTrue(reader.matchesIgnoreCase("HELLO"));
        Assert.assertTrue(reader.matchesIgnoreCase("hello"));
        // Test matchesAny method
        reader = new CharacterReader(new StringReader("HelloWorld"));
        Assert.assertTrue(reader.matchesAny('H', 'e', 'l', 'o', 'W', 'r'));
        Assert.assertFalse(reader.matchesAny('X'));
        // Test matchesAnySorted method
        reader = new CharacterReader(new StringReader("HelloWorld"));
        Assert.assertTrue(reader.matchesAnySorted('H', 'e', 'l', 'o', 'r', 'W'));
        Assert.assertFalse(reader.matchesAnySorted('X'));
        // Test matchesLetter method
        reader = new CharacterReader(new StringReader("HelloWorld"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter('H'));
        // Test matchesDigit method
        reader = new CharacterReader(new StringReader("HelloWorld"));
        Assert.assertFalse(reader.matchesDigit());
        reader = new CharacterReader(new StringReader("12345"));
        Assert.assertTrue(reader.matchesDigit());
        // Test matchConsume method
        reader = new CharacterReader(new StringReader("HelloWorld"));
        Assert.assertEquals("Hello", reader.matchConsume("He"));
        // Test matchConsumeIgnoreCase method
        reader = new CharacterReader(new StringReader("HelloWorld"));
        Assert.assertEquals("Hello", reader.matchConsumeIgnoreCase("he"));
        // Test containsIgnoreCase method
        reader = new CharacterReader(new StringReader("<html><head><title>Test</title>
</head><body><p>Hello World</p></body></html>"));
        Assert.assertTrue(reader.containsIgnoreCase("world"));
        Assert.assertTrue(reader.containsIgnoreCase("hello"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        // Test IOException when trying to read from an invalid Reader
        CharacterReader reader = new CharacterReader(new StringReader("InvalidReader"));
        reader.bufferUp();
    }

    @Test
    public void testCharacterReaderBufferUp() {
        // Test bufferUp method
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp();
        Assert.assertFalse(reader.isEmpty());
    }
}