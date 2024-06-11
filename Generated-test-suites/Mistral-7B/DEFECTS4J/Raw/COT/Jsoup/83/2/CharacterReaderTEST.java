package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testBufferUpMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertEquals(6, reader.pos());
    }

    @Test
    public void testIsEmptyMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(reader.isEmpty());

        reader.advance();
        Assert.assertFalse(reader.isEmpty());

        reader.consume();
        reader.consume();
        reader.consume();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testPosMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(0, reader.pos());

        reader.advance();
        Assert.assertEquals(1, reader.pos());

        reader.consume();
        reader.consume();
        reader.consume();
        reader.advance();
        Assert.assertEquals(4, reader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionThrownWhenBufferUpFails() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        reader.reader.reset();
        reader.reader.close();
        reader.bufferUp();
    }

    @Test
    public void testConsumeMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', reader.current());
        Assert.assertEquals('H', reader.consume());
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String sequence = reader.consumeLetterSequence();
        Assert.assertEquals("ello", sequence);
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello123World"));
        String sequence = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("ello123", sequence);
    }

    @Test
    public void testConsumeHexSequenceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("&nbsp;"));
        String sequence = reader.consumeHexSequence();
        Assert.assertEquals("nbsp", sequence);
    }

    @Test
    public void testConsumeDigitSequenceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        String sequence = reader.consumeDigitSequence();
        Assert.assertEquals("12345", sequence);
    }

    @Test
    public void testConsumeToMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String sequence = reader.consumeTo(' ');
        Assert.assertEquals("Hello", sequence);
    }

    @Test
    public void testConsumeToStringMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String sequence = reader.consumeTo("World");
        Assert.assertEquals("Hello", sequence);
    }

    @Test
    public void testConsumeToAnyMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld<!DOCTYPE html>"));
        String sequence = reader.consumeToAny(new char[]{'W', 'o', 'r', 'l'});
        Assert.assertEquals("HelloWorld", sequence);
    }

    @Test
    public void testConsumeToAnySortedMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld<!DOCTYPE html>"));
        String sequence = reader.consumeToAnySorted(new char[]{'l', 'o', 'r', 'l', 'W'});
        Assert.assertEquals("HelloWorld", sequence);
    }

    @Test
    public void testConsumeDataMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("&amp;<!DOCTYPE html>"));
        String sequence = reader.consumeData();
        Assert.assertEquals("&amp;", sequence);
    }

    @Test
    public void testConsumeTagNameMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        String sequence = reader.consumeTagName();
        Assert.assertEquals("htmlheadtitle", sequence);
    }

    @Test
    public void testConsumeToEndMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String sequence = reader.consumeToEnd();
        Assert.assertEquals("Hello World", sequence);
    }

    @Test
    public void testMatchesMethodWithSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matches('o'));
        Assert.assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matches("ll"));
        Assert.assertFalse(reader.matches("lll"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesIgnoreCase('L'));
        Assert.assertFalse(reader.matchesIgnoreCase('x'));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesIgnoreCase("ll"));
        Assert.assertFalse(reader.matchesIgnoreCase("lll"));
    }

    @Test
    public void testMatchesAnyMethodWithSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesAny('l', 'o'));
        Assert.assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnyMethodWithArray() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesAny(new char[]{'l', 'o'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'x'}));
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigitMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeMethodWithSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.matchConsume('o');
        Assert.assertEquals(2, reader.pos());
    }

    @Test
    public void testMatchConsumeMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.matchConsume("ll");
        Assert.assertEquals(5, reader.pos());
    }

    @Test
    public void testContainsIgnoreCaseMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("HEAD"));
        Assert.assertFalse(reader.containsIgnoreCase("/head"));
    }
}