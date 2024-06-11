package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setup() {
        String input = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(input));
    }

    @Test
    public void testNewInstance() {
        CharacterReader reader1 = new CharacterReader(null);
        CharacterReader reader2 = new CharacterReader(new StringReader(""));
        CharacterReader reader3 = new CharacterReader(new StringReader("Hello, World!"));

        Assert.assertNotNull(reader1);
        Assert.assertNotNull(reader2);
        Assert.assertNotNull(reader3);
    }

    @Test
    public void testNewInstanceWithInvalidInput() {
        StringReader invalidInput = new StringReader(null);
        Assert.assertThrows(NullPointerException.class, () -> new CharacterReader(invalidInput));
    }

    @Test
    public void testNewInstanceWithLargeInput() {
        StringReader largeInput = new StringReader(new String(new char[maxBufferLen * 2]));
        Assert.assertThrows(IllegalArgumentException.class, () -> new CharacterReader(largeInput));
    }

    @Test
    public void testReaderPosition() {
        Assert.assertEquals(0, characterReader.pos());
        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());
        characterReader.unconsume();
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(characterReader.isEmpty());
        characterReader.consume();
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consumeToEnd();
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsume() {
        Assert.assertEquals('T', characterReader.consume());
        Assert.assertEquals('h', characterReader.consume());
        Assert.assertEquals('i', characterReader.consume());
        Assert.assertEquals(CharacterReader.EOF, characterReader.consume());
    }

    @Test
    public void testConsumeTo() {
        String expected = "is a test for CharacterReader class.";
        String result = characterReader.consumeTo(' ');
        Assert.assertEquals(expected.substring(0, 7), result);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "This is a test for CharacterReader class.";
        String result = characterReader.consumeToEnd();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeToAny() {
        characterReader.consumeLetterSequence();
        Assert.assertEquals("is a test for CharacterReader class.", characterReader.consumeToAny(Array.asList(' ', '.').toArray(new char[0])));
        Assert.assertEquals("CharacterReader class.", characterReader.consumeToAny(new char[]{'.'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("class.", characterReader.consumeToAnySorted(Array.asList(' ', '.', 'c', 'l', 'a', 's', 's').toArray(new char[0])));
        Assert.assertEquals("is a test for CharacterReader", characterReader.consumeToAnySorted(Array.asList(' ', '.', 'i', 's', 'a', ' ', 't', 'e', 's', 't', ' ', 'f', 'o', 'r', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', 'Reader').toArray(new char[0])));
    }

    @Test
    public void testConsumeData() {
        String expected = "&, <, null";
        String result = characterReader.consumeData();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeTagName() {
        String expected = "\t\n\r\f ";
        String result = characterReader.consumeTagName();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "is a test for CharacterReader class.";
        String result = characterReader.consumeLetterSequence();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = "is a test for CharacterReader class.";
        String result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "is a test for CharacterReader class.";
        String result = characterReader.consumeHexSequence();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "is a test for CharacterReader class.";
        String result = characterReader.consumeDigitSequence();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatches() {
        characterReader.consumeLetterSequence();
        Assert.assertTrue(characterReader.matches('s'));
        Assert.assertTrue(characterReader.matches("is a test for CharacterReader class."));
        Assert.assertFalse(characterReader.matches('@'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        characterReader.consumeLetterSequence();
        Assert.assertTrue(characterReader.matchesIgnoreCase("IS A TEST FOR CHARACTERREADER CLASS."));
        Assert.assertFalse(characterReader.matchesIgnoreCase("@"));
    }

    @Test
    public void testMatchesAny() {
        characterReader.consumeLetterThenDigitSequence();
        Assert.assertTrue(characterReader.matchesAny(' ', 's', 't'));
        Assert.assertFalse(characterReader.matchesAny('@'));
    }

    @Test
    public void testMatchesAnySorted() {
        characterReader.consumeLetterThenDigitSequence();
        Assert.assertTrue(characterReader.matchesAnySorted(Array.asList(' ', 's', 't').toArray(new char[0])));
        Assert.assertFalse(characterReader.matchesAnySorted(Array.asList('@')));
    }

    @Test
    public void testMatchesLetter() {
        characterReader.consumeLetterSequence();
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        characterReader.consumeLetterThenDigitSequence();
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        characterReader.consumeLetterSequence();
        String expected = "is a test for CharacterReader class.";
        String result = characterReader.matchConsume("is a test for CharacterReader class.");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        characterReader.consumeLetterSequence();
        String expected = "IS A TEST FOR CHARACTERREADER CLASS.";
        String result = characterReader.matchConsumeIgnoreCase("IS A TEST FOR CHARACTERREADER CLASS.");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testContainIgnoreCase() {
        characterReader.consumeLetterSequence();
        Assert.assertTrue(characterReader.containsIgnoreCase("<title>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("<style>"));
        Assert.assertFalse(characterReader.containsIgnoreCase("@"));
    }

    @Test(expected = IOException.class)
    public void testIOException() {
        StringReader invalidReader = new StringReader(null);
        CharacterReader invalidCharacterReader = new CharacterReader(invalidReader);
        invalidCharacterReader.consume();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        StringReader largeInput = new StringReader(new String(new char[maxBufferLen * 2]));
        CharacterReader largeCharacterReader = new CharacterReader(largeInput);
    }

    @Test
    public void testToString() {
        String expected = "This is a test for CharacterReader class.";
        String result = characterReader.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCacheString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String result = characterReader.cacheString(new char[0], new String[10], 0, 10);
        Assert.assertEquals("abcdefg", result);

        characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        result = characterReader.cacheString(new char[0], new String[10], 4, 10);
        Assert.assertEquals("efghij", result);
    }
}