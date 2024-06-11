package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReaderInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertEquals(input, characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithStringInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals(input, characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithReaderAndSizeInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());
        Assert.assertEquals(input, characterReader.toString());
    }

    @Test
    public void testBufferUpMethod() throws IOException {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        characterReader.bufferUp();
        Assert.assertEquals(input.charAt(0), characterReader.current());
        Assert.assertEquals(input.charAt(1), characterReader.consume());
        characterReader.bufferUp();
        Assert.assertEquals(input.charAt(2), characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpMethodWithIOException() throws IOException {
        StringReader reader = new StringReader("Hello World!");
        reader.mark(10);
        reader.skip(11);
        reader.reset();

        CharacterReader characterReader = new CharacterReader(reader, 100);
        characterReader.bufferUp();
    }

    @Test
    public void testPosMethod() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertEquals(0, characterReader.pos());
        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());
        characterReader.consume();
        Assert.assertEquals(2, characterReader.pos());
    }

    @Test
    public void testIsEmptyMethod() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consumeTo(' ');
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrentMethod() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertEquals(input.charAt(0), characterReader.current());
        characterReader.advance();
        Assert.assertEquals(input.charAt(1), characterReader.current());
    }

    @Test
    public void testConsumeMethod() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertEquals(input.charAt(0), characterReader.current());
        Assert.assertEquals(input.charAt(0), characterReader.consume());
        Assert.assertEquals(input.charAt(1), characterReader.current());
    }

    @Test
    public void testUnconsumeMethod() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals(input.charAt(0), characterReader.current());
    }

    @Test
    public void testAdvanceMethod() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        characterReader.advance();
        Assert.assertEquals(input.charAt(1), characterReader.current());
    }

    @Test
    public void testMarkMethod() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals(input.charAt(0), characterReader.current());
    }

    @Test
    public void testNextIndexOfMethodWithSingleCharInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertEquals(5, characterReader.nextIndexOf(' '));
    }

    @Test
    public void testNextIndexOfMethodWithMultiCharInput() {
        String input = "Hello World!";
        CharacterReader characterReaderd = new CharacterReader(new StringReader(input), input.length());

        Assert.assertEquals(4, characterReaderd.nextIndexOf("lod"));
    }

    @Test
    public void testConsumeToMethodWithSingleCharInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        String result = characterReader.consumeTo(' ');
        Assert.assertEquals("Hello", result);
    }

    @Test
    public void testConsumeToMethodWithMultiCharInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        String result = characterReader.consumeTo(" World!");
        Assert.assertEquals("Hello", result);
    }

    @Test
    public void testConsumeToAnyMethodWithSingleCharInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        String result = characterReader.consumeToAny(' ');
        Assert.assertEquals("Hello", result);
    }

    @Test
    public void testConsumeToAnyMethodWithMultipleCharsInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        String result = characterReader.consumeToAny(new char[]{' ', '!'});
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testConsumeTagNameMethod() {
        String input = "<html><head><title>Test</title></head><body>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        String result = characterReader.consumeTagName();
        Assert.assertEquals("html", result);
    }

    @Test
    public void testConsumeDataMethod() {
        String input = "&lt;script src=\"script.js\"&gt;";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        String result = characterReader.consumeData();
        Assert.assertEquals("&lt;script src=\"script.js\"", result);
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        String input = "HelloWorld";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        String result = characterReader.consumeLetterSequence();
        Assert.assertEquals("Hello", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        String input = "Hello123World";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        String result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", result);
    }

    @Test
    public void testConsumeHexSequenceMethod() {
        String input = "&xf00d";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        String result = characterReader.consumeHexSequence();
        Assert.assertEquals("&xf00d", result);
    }

    @Test
    public void testConsumeDigitSequenceMethod() {
        String input = "123456";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        String result = characterReader.consumeDigitSequence();
        Assert.assertEquals("123456", result);
    }

    @Test
    public void testMatchesMethodWithSingleCharInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertTrue(characterReader.matches('H'));
        Assert.assertFalse(characterReader.matches('!'));
    }

    @Test
    public void testMatchesMethodWithMultiCharInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertTrue(characterReader.matches("He"));
        Assert.assertFalse(characterReader.matches("!ld"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithSingleCharInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertTrue(characterReader.matchesIgnoreCase("h"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("!"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithMultiCharInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertTrue(characterReader.matchesIgnoreCase("He"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("!ld"));
    }

    @Test
    public void testMatchesAnyMethodWithSingleCharInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertTrue(characterReader.matchesAny('H', 'h'));
        Assert.assertFalse(characterReader.matchesAny('!'));
    }

    @Test
    public void testMatchesAnyMethodWithMultipleCharsInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertTrue(characterReader.matchesAny(new char[]{'H', 'h'}));
        Assert.assertFalse(characterReader.matchesAny(new char[]{'!', 'd'}));
    }

    @Test
    public void testMatchesLetterMethod() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigitMethod() {
        String input = "Hello123World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeMethodWithSingleCharInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertTrue(characterReader.matchConsume("He"));
        Assert.assertEquals(3, characterReader.pos());
    }

    @Test
    public void testMatchConsumeMethodWithMultiCharInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertTrue(characterReader.matchConsume("He"));
        Assert.assertEquals(3, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethodWithSingleCharInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("He"));
        Assert.assertEquals(3, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethodWithMultiCharInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("He"));
        Assert.assertEquals(3, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCaseMethod() {
        String input = "<html><head><title>Test</title></head><body>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());

        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("HEAD"));
        Assert.assertFalse(characterReader.containsIgnoreCase("BODY"));
    }
}