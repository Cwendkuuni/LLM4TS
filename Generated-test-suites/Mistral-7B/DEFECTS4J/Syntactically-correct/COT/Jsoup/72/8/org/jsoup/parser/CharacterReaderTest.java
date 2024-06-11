package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader input = new StringReader("Hello World");
        CharacterReader reader = new CharacterReader(input);
        Assert.assertNotNull(reader.charBuf);
        Assert.assertNotNull(reader.reader);
        Assert.assertEquals(maxBufferLen, reader.charBuf.length);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        Assert.assertNotNull(reader.charBuf);
        Assert.assertNotNull(reader.reader);
        Assert.assertEquals(input.length(), reader.pos());
    }

    @Test
    public void testBufferUp() throws IOException {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertEquals(input.length(), reader.bufLength);
        Assert.assertEquals(0, reader.bufPos);
        Assert.assertEquals(0, reader.bufMark);
        Assert.assertEquals(readAheadLimit, reader.bufSplitPoint);
        Assert.assertEquals(input.length(), reader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpWithIOException() throws IOException {
        StringReader reader = new StringReader("");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.bufferUp();
    }

    @Test
    public void testPos() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertEquals(input.length(), reader.pos());
    }

    @Test
    public void testIsEmpty() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertFalse(reader.isEmpty());

        reader.advance();
        reader.bufferUp();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertEquals('H', reader.current());
    }

    @Test
    public void testConsume() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertEquals('H', reader.current());
        Assert.assertEquals('H', reader.consume());
        Assert.assertEquals('H', reader.current());
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testUnconsume() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('H', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
        Assert.assertEquals(12, reader.nextIndexOf(' '));
        Assert.assertEquals(13, reader.nextIndexOf('\n'));
    }

    @Test
    public void testNextIndexOfWithEmptySequence() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertEquals(-1, reader.nextIndexOf(""));
    }

    @Test
    public void testNextIndexOfWithNullSequence() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertEquals(-1, reader.nextIndexOf(null));
    }

    @Test
    public void testConsumeTo() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        String result = reader.consumeTo('l');
        Assert.assertEquals("Hello", result);
        Assert.assertEquals('l', reader.current());
    }

    @Test
    public void testConsumeToWithEmptySequence() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        String result = reader.consumeTo("");
        Assert.assertEquals("Hello World", reader.toString());
        Assert.assertEquals('W', reader.current());
    }

    @Test
    public void testConsumeToWithNullSequence() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        String result = reader.consumeTo(null);
        Assert.assertEquals("Hello World", reader.toString());
        Assert.assertEquals('W', reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        String result = reader.consumeToEnd();
        Assert.assertEquals("World", result);
        Assert.assertEquals('\n', reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        String result = reader.consumeLetterSequence();
        Assert.assertEquals("Hello", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String input = "Hello123World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", result);
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "H&x26;e&x20;l&x20;l&x20;o";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        String result = reader.consumeHexSequence();
        Assert.assertEquals("&x26;&x20;&x20;&x20;&x20;lo", result);
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "1234567890";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        String result = reader.consumeDigitSequence();
        Assert.assertEquals("1234567890", result);
    }

    @Test
    public void testMatches() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("ll"));
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertTrue(reader.matchesIgnoreCase("ll"));
        Assert.assertFalse(reader.matchesIgnoreCase("ZZ"));
    }

    @Test
    public void testMatchesAny() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertTrue(reader.matchesAny(new char[]{'l', 'e'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'e'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'z', 'l'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'z'}));
    }

    @Test
    public void testMatchesLetter() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit('5'));
    }

    @Test
    public void testMatchConsume() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertTrue(reader.matchConsume("ll"));
        Assert.assertEquals("lloWorld", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ll"));
        Assert.assertEquals("lloWorld", reader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "<html><head><title>Test</title></head></html>";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("TITLE"));
        Assert.assertFalse(reader.containsIgnoreCase("/body"));
    }

    @Test
    public void testToString() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(new StringReader(input), input.length());
        reader.bufferUp();
        String result = reader.toString();
        Assert.assertEquals(input, result);
    }
}