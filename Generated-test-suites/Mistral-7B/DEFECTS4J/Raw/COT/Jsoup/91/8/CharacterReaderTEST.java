package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader readerInstance = new CharacterReader(reader);
        Assert.assertNotNull(readerInstance);
    }

    @Test
    public void testCharacterReaderConstructorWithSize() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader readerInstance = new CharacterReader(reader, 10);
        Assert.assertEquals(10, readerInstance.charBuf.length);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("Hello World!"), "Hello World!".length());
        Assert.assertEquals("Hello World!", readerInstance.toString());
    }

    @Test
    public void testBufferUp() throws IOException {
        Reader reader = new StringReader("ABCDEFG");
        CharacterReader readerInstance = new CharacterReader(reader);
        readerInstance.bufferUp();
        Assert.assertEquals(3, readerInstance.pos());
        Assert.assertEquals('C', readerInstance.current());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader readerInstance = new CharacterReader(new StringReader(""));
        Assert.assertTrue(readerInstance.isEmpty());
        readerInstance.consume();
        Assert.assertFalse(readerInstance.isEmpty());
        readerInstance.consumeToEnd();
        readerInstance.bufferUp();
        Assert.assertFalse(readerInstance.isEmpty());
    }

    @Test
    public void testConsume() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFG"));
        Assert.assertEquals('A', readerInstance.consume());
        Assert.assertEquals('B', readerInstance.consume());
        Assert.assertEquals('C', readerInstance.consume());
        Assert.assertEquals('D', readerInstance.consume());
        Assert.assertEquals('E', readerInstance.consume());
        Assert.assertEquals('F', readerInstance.consume());
        Assert.assertEquals('G', readerInstance.consume());
        Assert.assertEquals(CharacterReader.EOF, readerInstance.consume());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionWhenBufferingUp() throws IOException {
        Reader reader = new StringReader("ABCDEFG");
        CharacterReader readerInstance = new CharacterReader(reader);
        readerInstance.bufferUp();
        reader.mark(1);
        reader.close();
        readerInstance.bufferUp();
    }

    @Test
    public void testCurrent() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFG"));
        Assert.assertEquals('A', readerInstance.current());
        readerInstance.consume();
        Assert.assertEquals('B', readerInstance.current());
        readerInstance.unconsume();
        Assert.assertEquals('A', readerInstance.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFG"));
        readerInstance.advance();
        Assert.assertEquals('B', readerInstance.current());
        readerInstance.advance();
        Assert.assertEquals('C', readerInstance.current());
        readerInstance.advance();
        Assert.assertEquals('D', readerInstance.current());
    }

    @Test
    public void testMark() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFG"));
        readerInstance.mark();
        readerInstance.consume();
        readerInstance.rewindToMark();
        Assert.assertEquals('A', readerInstance.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testInvalidMark() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFG"));
        readerInstance.mark();
        readerInstance.consumeToEnd();
        readerInstance.rewindToMark();
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFGABCDEFGABCDEFG"));
        Assert.assertEquals(11, readerInstance.nextIndexOf('G'));
        Assert.assertEquals(-1, readerInstance.nextIndexOf('Z'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFGABCDEFGABCDEFG"));
        Assert.assertEquals(11, readerInstance.nextIndexOf("GF"));
        Assert.assertEquals(-1, readerInstance.nextIndexOf("PQ"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFGABCDEFGABCDEFG"));
        Assert.assertEquals("EFG", readerInstance.consumeTo('F'));
        Assert.assertEquals("DEFG", readerInstance.consumeTo('E'));
        Assert.assertEquals("", readerInstance.consumeTo('Z'));
    }

    @Test
    public void testConsumeToWithSequence() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFGABCDEFGABCDEFG"));
        Assert.assertEquals("GF", readerInstance.consumeTo("FG"));
        Assert.assertEquals("", readerInstance.consumeTo("PQ"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFGABCDEFGABCDEFG"));
        Assert.assertEquals("FG", readerInstance.consumeToAny(new char[]{'F', 'G'}));
        Assert.assertEquals("", readerInstance.consumeToAny(new char[]{'P', 'Q'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFGABCDEFGABCDEFG"));
        Assert.assertEquals("GF", readerInstance.consumeToAnySorted(new char[]{'F', 'G'}));
        Assert.assertEquals("", readerInstance.consumeToAnySorted(new char[]{'P', 'Q'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("&<nullABCDEFGABCDEFGABCDEFG"));
        Assert.assertEquals("ABCDEFGABCDEFG", readerInstance.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("<tag name=\"AttributeName\" >Content< /tag>"));
        Assert.assertEquals("tag", readerInstance.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFGABCDEFGABCDEFG"));
        Assert.assertEquals("ABCDEFGABCDEFG", readerInstance.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFGABCDEFGABCDEFG"));
        Assert.assertEquals("DEFGEFG", readerInstance.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABC123DEF4GHIJ567KLMNOPQRSTUVWXYZ"));
        Assert.assertEquals("DEF4GHILMNOPQRSTUVWXYZ", readerInstance.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFGABCDEFGABCDEFG"));
        Assert.assertTrue(readerInstance.matches('E'));
        Assert.assertTrue(readerInstance.matches("EF"));
        Assert.assertFalse(readerInstance.matches('Z'));
        Assert.assertFalse(readerInstance.matches("PQ"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFGABCDEFGABCDEFG"));
        Assert.assertTrue(readerInstance.matchesIgnoreCase("ef"));
        Assert.assertFalse(readerInstance.matchesIgnoreCase("PQ"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFGABCDEFGABCDEFG"));
        Assert.assertTrue(readerInstance.matchesAny(new char[]{'E', 'F'}));
        Assert.assertFalse(readerInstance.matchesAny(new char[]{'P', 'Q'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFGABCDEFGABCDEFG"));
        Assert.assertTrue(readerInstance.matchesAnySorted(new char[]{'E', 'F'}));
        Assert.assertFalse(readerInstance.matchesAnySorted(new char[]{'P', 'Q'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABCDEFGABCDEFGABCDEFG"));
        Assert.assertTrue(readerInstance.matchesLetter());
        Assert.assertFalse(readerInstance.matchesLetter());
        readerInstance.consumeLetterSequence();
        Assert.assertTrue(readerInstance.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader readerInstance = new CharacterReader(new StringReader("ABC123DEF4GHIJ567KLMNOPQRSTUVWXYZ"));
        Assert.assertFalse(readerInstance.matchesDigit());
        readerInstance.consumeLetterThenDigitSequence();
        Assert.assertTrue(readerInstance.matchesDigit());
    }
}