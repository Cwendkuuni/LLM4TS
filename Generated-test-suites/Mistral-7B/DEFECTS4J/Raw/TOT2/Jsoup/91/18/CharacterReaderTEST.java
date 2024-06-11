package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderWithDefaultConstructor() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(CharacterReader.EOF, reader.current());
        Assert.assertTrue(reader.matches(CharacterReader.EOF));
        reader.advance();
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('t', reader.current());
        Assert.assertTrue(reader.matches('t'));
        reader.consume();
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('e', reader.current());
        reader.mark();
        reader.rewindToMark();
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('e', reader.current());
        reader.consume();
        reader.consume();
        Assert.assertEquals(4, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithCustomConstructor() {
        CharacterReader reader = new CharacterReader(new StringReader("test"), 10);
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(CharacterReader.EOF, reader.current());
        Assert.assertTrue(reader.matches(CharacterReader.EOF));
        reader.bufferUp();
        reader.advance();
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('t', reader.current());
        Assert.assertTrue(reader.matches('t'));
        reader.consume();
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('e', reader.current());
        reader.mark();
        reader.rewindToMark();
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('e', reader.current());
        reader.consume();
        reader.consume();
        Assert.assertEquals(4, reader.pos());
        reader.bufferUp();
        reader.consumeTo(CharacterReader.EOF);
        Assert.assertEquals(4, reader.pos());
        reader.consume();
        Assert.assertEquals(5, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionThrownWhenBufferUpFails() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.pos = -1; // set pos to an invalid value
        reader.bufferUp();
    }

    @Test
    public void testConsumeToWithSmallSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("testtesttest"));
        String result = reader.consumeTo('t');
        Assert.assertEquals("est", result);
    }

    @Test
    public void testConsumeToWithBigSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("testtesttest"));
        String result = reader.consumeTo("est");
        Assert.assertEquals("test", result);
    }

    @Test
    public void testConsumeToAnyNullChar() {
        CharacterReader reader = new CharacterReader(new StringReader("test<html"));
        String result = reader.consumeToAny(CharacterReader.nullChar);
        Assert.assertEquals("test<", result);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdababcd"));
        String result = reader.consumeToAnySorted(new char[]{'a', 'b', 'c'});
        Assert.assertEquals("ab", result);
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("test&lt;html&gt;test"));
        String result = reader.consumeData();
        Assert.assertEquals("test<html", result);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title>"));
        String result = reader.consumeTagName();
        Assert.assertEquals("html", result);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("testtesttest"));
        String result = reader.consumeToEnd();
        Assert.assertEquals("testtesttest", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("testLetterSequence"));
        String result = reader.consumeLetterSequence();
        Assert.assertEquals("LetterSequence", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("test123LetterThenDigitSequence"));
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("LetterThenDigitSequence", result);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("test#x32#x41#x42"));
        String result = reader.consumeHexSequence();
        Assert.assertEquals("2AB", result);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("test123456"));
        String result = reader.consumeDigitSequence();
        Assert.assertEquals("6", result);
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("test123"));
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("testtest"));
        Assert.assertTrue(reader.matches("est"));
        Assert.assertTrue(reader.matches("test"));
        Assert.assertTrue(reader.matches("es"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("testtest"));
        Assert.assertTrue(reader.matchesIgnoreCase("Est"));
        Assert.assertTrue(reader.matchesIgnoreCase("test"));
        Assert.assertTrue(reader.matchesIgnoreCase("es"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("testtesttest"));
        Assert.assertTrue(reader.matchesAny('t'));
        Assert.assertTrue(reader.matchesAny('e'));
        Assert.assertTrue(reader.matchesAny('s'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("testtesttest"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'s', 'e', 't'}));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("testtesttest"));
        reader.matchConsume("est");
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("testtesttest"));
        reader.matchConsumeIgnoreCase("EST");
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title></html>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/TITLE"));
    }
}