package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        // Test constructor with Reader and given size
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader, 10);

        // Basic functionality test
        Assert.assertEquals("Test String", characterReader.consumeToEnd());

        // Edge cases test
        Assert.assertEquals("", characterReader.consumeToEnd());
        characterReader.bufferUp(); // Test when buffer is empty
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());

        // Test exceptions
        StringReader readerWithException = new StringReader("Test Exception");
        try {
            new CharacterReader(readerWithException, 10);
            Assert.fail();
        } catch (UncheckedIOException e) {
            // Expected
        }
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        // Test constructor with String
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));

        // Basic functionality test
        Assert.assertEquals("Test String", characterReader.consumeToEnd());

        // Edge cases test
        Assert.assertEquals("", characterReader.consumeToEnd());
        characterReader.bufferUp(); // Test when buffer is empty
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testBufferUpMethod() {
        // Test bufferUp method with reader
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"), 10);

        // Basic functionality test
        Assert.assertEquals("Test", characterReader.consumeLetterSequence());
        characterReader.bufferUp();
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());

        // Edge cases test
        characterReader = new CharacterReader(new StringReader(""), 10);
        characterReader.bufferUp();
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());

        // Test exceptions
        StringReader readerWithException = new StringReader("Test Exception");
        characterReader = new CharacterReader(readerWithException, 10);
        characterReader.bufferUp();
        try {
            characterReader.consume();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testPosMethod() {
        // Test pos method
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));

        // Basic functionality test
        Assert.assertEquals(0, characterReader.pos());
        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());
        characterReader.consume();
        Assert.assertEquals(2, characterReader.pos());
        characterReader.rewindToMark();
        Assert.assertEquals(1, characterReader.pos());

        // Edge cases test
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        Assert.assertEquals(0, characterReader.pos());

        // Test exceptions
        characterReader = new CharacterReader(new StringReader("Test Exception"), 10);
        characterReader.advance();
        try {
            characterReader.pos();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testIsEmptyMethod() {
        // Test isEmpty method
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));

        // Basic functionality test
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consume();
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.advance();
        Assert.assertFalse(characterReader.isEmpty());

        // Edge cases test
        characterReader = new CharacterReader(new StringReader(""), 10);
        Assert.assertTrue(characterReader.isEmpty());
        characterReader.bufferUp();
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
        Assert.assertTrue(characterReader.isEmpty());

        // Test exceptions
        StringReader readerWithException = new StringReader("Test Exception");
        characterReader = new CharacterReader(readerWithException, 10);
        try {
            characterReader.isEmpty();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCurrentMethod() {
        // Test current method
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));

        // Basic functionality test
        Assert.assertEquals('T', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());

        // Edge cases test
        characterReader.bufferUp();
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());

        // Test exceptions
        StringReader readerWithException = new StringReader("Test Exception");
        characterReader = new CharacterReader(readerWithException, 10);
        try {
            characterReader.current();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    // Test other methods (consume, unconsume, advance, mark, rewindToMark, nextIndexOf, nextIndexOf, consumeTo, consumeTo, consumeToAny, consumeToAnySorted, consumeData, consumeTagName, consumeToEnd, consumeLetterSequence, consumeLetterThenDigitSequence, consumeHexSequence, consumeDigitSequence, matches, matchesIgnoreCase, matchesAny, matchesAnySorted, matchesLetter, matchesDigit, matchConsume, matchConsumeIgnoreCase, containsIgnoreCase, toString, cacheString, rangeEquals)
}