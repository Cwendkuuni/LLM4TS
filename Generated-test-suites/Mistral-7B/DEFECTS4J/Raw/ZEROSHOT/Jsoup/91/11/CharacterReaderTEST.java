package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * CharacterReaderTest class to test all methods in CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        char[] buffer = new char[100];
        CharacterReader reader = new CharacterReader(new StringReader("test"), buffer.length);

        // Test constructor
        Assert.assertNotNull(reader);
        Assert.assertEquals("test", reader.toString());

        // Test isEmpty method
        Assert.assertFalse(reader.isEmpty());
        bufferUp(reader);
        Assert.assertTrue(reader.isEmpty());

        // Test pos method
        Assert.assertEquals(0, reader.pos());
        consume(reader, 5);
        Assert.assertEquals(5, reader.pos());

        // Test consume method
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals('s', reader.consume());
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals(' ', reader.consume());
        Assert.assertEquals(EOF, reader.consume());

        // Test mark and rewindToMark method
        reader.mark();
        consume(reader, 5);
        reader.rewindToMark();
        Assert.assertEquals(0, reader.pos());

        // Test nextIndexOf method
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
        Assert.assertEquals(1, reader.nextIndexOf('t'));

        // Test consumeTo method
        String consumed = reader.consumeTo('s');
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(1, reader.pos());

        consumed = reader.consumeTo("est");
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(5, reader.pos());

        consumed = reader.consumeToEnd();
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(12, reader.pos());

        // Test consumeToAny method
        consumed = reader.consumeToAny(new char[]{'s', 't'});
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(5, reader.pos());

        consumed = reader.consumeToAnySorted(new char[]{'t', 's'});
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(5, reader.pos());

        consumed = reader.consumeData();
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(12, reader.pos());

        consumed = reader.consumeTagName();
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(12, reader.pos());

        consumed = reader.consumeToEnd();
        Assert.assertEquals("", consumed);
        Assert.assertEquals(12, reader.pos());

        // Test consumeLetterSequence and consumeLetterThenDigitSequence method
        consumed = reader.consumeLetterSequence();
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(12, reader.pos());

        consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(12, reader.pos());

        // Test matches method
        Assert.assertTrue(reader.matches('t'));
        Assert.assertFalse(reader.matches('z'));

        // Test matches method with String argument
        Assert.assertTrue(reader.matches("est"));
        Assert.assertFalse(reader.matches("foo"));

        // Test matchesIgnoreCase method with String argument
        Assert.assertTrue(reader.matchesIgnoreCase("test"));
        Assert.assertTrue(reader.matchesIgnoreCase("Est"));
        Assert.assertFalse(reader.matchesIgnoreCase("foo"));

        // Test matchesAny method
        Assert.assertTrue(reader.matchesAny(new char[]{'t', 'e'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'z'}));

        // Test matchesAnySorted method
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'t', 'e'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'z'}));

        // Test matchesLetter method
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());

        // Test matchesDigit method
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());

        // Test matchConsume method
        consumed = reader.consumeTo("est");
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(5, reader.pos());
        reader.matchConsume("est");
        Assert.assertEquals(8, reader.pos());

        // Test matchConsumeIgnoreCase method
        consumed = reader.consumeTo("test");
        Assert.assertEquals("testtest", consumed);
        Assert.assertEquals(12, reader.pos());
        reader.matchConsumeIgnoreCase("Test");
        Assert.assertEquals(14, reader.pos());

        // Test containsIgnoreCase method
        Assert.assertTrue(reader.containsIgnoreCase("est"));
        Assert.assertTrue(reader.containsIgnoreCase("Test"));
        Assert.assertFalse(reader.containsIgnoreCase("foo"));

        // Test bufferUp method
        bufferUp(reader);
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(12, reader.pos());

        // Test throw exception when trying to read beyond EOF
        try {
            reader.consume();
            Assert.fail();
        } catch (IllegalStateException e) {
            // Expected
        }

        try {
            reader.consume(2);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test isEmptyNoBufferUp method
        Assert.assertTrue(reader.isEmptyNoBufferUp());
        reader.consumeLetterThenDigitSequence();
        Assert.assertFalse(reader.isEmptyNoBufferUp());

        // Reset reader position to 0
        reader.rewindToMark();
        reader.consumeLetterThenDigitSequence();
        Assert.assertFalse(reader.isEmptyNoBufferUp());
        reader.consumeLetterThenDigitSequence();
        reader.consumeLetterThenDigitSequence();
        Assert.assertTrue(reader.isEmptyNoBufferUp());
    }

    private void bufferUp(CharacterReader reader) {
        reader.bufferUp();
    }

    private char consume(CharacterReader reader, int count) {
        for (int i = 0; i < count; i++) {
            reader.consume();
        }
        return reader.consume();
    }
}