package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals("Hello World!", characterReader.toString());
        characterReader.advance();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertEquals("Hello World!", characterReader.toString());
        characterReader.advance();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testBufferUp() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.advance();
        characterReader.bufferUp();
        Assert.assertEquals('H', characterReader.current());
        characterReader.advance();
        characterReader.bufferUp();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionThrownWhenBufferUpFails() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.advance();
        characterReader.advance();
        characterReader.bufferUp();
        characterReader.advance(); // This should throw an IOException, but it's caught by UncheckedIOException
    }

    @Test
    public void testPos() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertEquals(0, characterReader.pos());
        characterReader.advance();
        characterReader.advance();
        Assert.assertEquals(2, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consume();
        characterReader.consume();
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsume() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertEquals('H', characterReader.consume());
        Assert.assertEquals('e', characterReader.consume());
        Assert.assertEquals(Character.EOF, characterReader.consume());
    }

    @Test
    public void testAdvance() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.advance();
        Assert.assertEquals('H', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertEquals(6, characterReader.nextIndexOf('W'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('X'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertEquals(6, characterReader.nextIndexOf("World"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("Xyz"));
    }

    @Test
    public void testConsumeTo() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeTo('W');
        Assert.assertEquals("Hello", result);
    }

    @Test
    public void testConsumeToWithCharSequence() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeTo("World");
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testConsumeToAny() {
        String input = "Hello World!<&>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeToAny(',', ';');
        Assert.assertEquals("Hello World!<&", result);
    }

    @Test
    public void testConsumeToAnySorted() {
        String input = "Hello World!<&>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeToAnySorted(new char[]{'<', '&'});
        Assert.assertEquals("Hello World!<&", result);
    }

    @Test
    public void testConsumeData() {
        String input = "& <null>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeData();
        Assert.assertEquals("&", result);
    }

    @Test
    public void testConsumeTagName() {
        String input = "<title>Test</title>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeTagName();
        Assert.assertEquals("title", result);
    }

    @Test
    public void testConsumeToEnd() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeLetterSequence();
        Assert.assertEquals("Hello", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String input = "Hello123World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", result);
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "&x0F&x0A";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeHexSequence();
        Assert.assertEquals("fx", result);
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "12345";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeDigitSequence();
        Assert.assertEquals("12345", result);
    }

    @Test
    public void testMatches() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertTrue(characterReader.matches('W'));
        Assert.assertTrue(characterReader.matches("World!"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertTrue(characterReader.matchesIgnoreCase("worLD!"));
    }

    @Test
    public void testMatchesAny() {
        String input = "Hello World!<&>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertTrue(characterReader.matchesAny('W', '<', '&'));
    }

    @Test
    public void testMatchesAnySorted() {
        String input = "Hello World!<&>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'<', '&'}));
    }

    @Test
    public void testMatchesLetter() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter('z'));
    }

    @Test
    public void testMatchesDigit() {
        String input = "12345";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit('5'));
    }

    @Test
    public void testMatchConsume() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.toString();
        Assert.assertEquals("Hello World!", result);
        characterReader.matchConsume("World");
        result = characterReader.toString();
        Assert.assertEquals("Hello ", result);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.toString();
        Assert.assertEquals("Hello World!", result);
        characterReader.matchConsumeIgnoreCase("World!");
        result = characterReader.toString();
        Assert.assertEquals("Hello ", result);
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "<title>Test</title>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("TITLE"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/xTitle"));
    }
}