package org.jsoup.parser; // Make sure the test file is in the same package as the class under test

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test cases for CharacterReader class
 */
public final class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertArrayEquals("Hello World".toCharArray(), reader.charBuf, 0, "Hello World".length());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader = new CharacterReader("Hello World");
        Assert.assertArrayEquals("Hello World".toCharArray(), reader.charBuf, 0, "Hello World".length());
    }

    @Test
    public void testBufferUpMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        int pos = reader.pos();
        Assert.assertEquals("Buffer position should be 6 after bufferUp() for 'Hello World'", 6, pos);
    }

    @Test
    public void testIsEmptyMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse("isEmpty() should return false when there's still content to read", reader.isEmpty());

        reader.consumeToEnd();
        Assert.assertTrue("isEmpty() should return true when all content has been read", reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpMethodWithIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World")) {
            @Override
            public void bufferUp() {
                throw new IOException();
            }
        };

        reader.consume();
    }

    @Test
    public void testConsumeMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        // Consume all characters
        for (int i = 0; i < "Hello World".length(); i++) {
            char c = reader.consume();
            Assert.assertEquals("Expected character: " + (char) (i + 48), (char) (i + 48), c);
        }

        // Verify isEmpty() returns true after consuming all characters
        Assert.assertTrue("isEmpty() should return true after consuming all characters", reader.isEmpty());
    }

    @Test
    public void testNextIndexOfMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        int index = reader.nextIndexOf('o');
        Assert.assertEquals("Expected index 4 for character 'o'", 4, index);
    }

    @Test
    public void testNextIndexOfMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World Hello"));
        int index = reader.nextIndexOf("ll");
        Assert.assertEquals("Expected index 5 for substring 'll'", 5, index);
    }

    @Test
    public void testNextIndexOfMethodWithEmptyString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        int index = reader.nextIndexOf("");
        Assert.assertEquals("Expected index -1 for empty string", -1, index);
    }

    @Test
    public void testConsumeToMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String result = reader.consumeTo('o');
        Assert.assertEquals("Expected substring: 'Hello'", result, "Hello");
    }

    @Test
    public void testConsumeToMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World Hello"));
        String result = reader.consumeTo("ll");
        Assert.assertEquals("Expected substring: 'Hello World Hello ll'", result, "Hello World Hello ll");
    }

    @Test
    public void testConsumeToAnyMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("123456"));
        String result = reader.consumeToAny('5');
        Assert.assertEquals("Expected substring: '3456'", result, "3456");
    }

    @Test
    public void testConsumeToAnyMethodWithMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("123456"));
        String result = reader.consumeToAny(new char[]{'3', '5'});
        Assert.assertEquals("Expected substring: '35'", result, "35");
    }

    @Test
    public void testConsumeToEndMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String result = reader.consumeToEnd();
        Assert.assertEquals("Expected all content: 'Hello World'", result, "Hello World");
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String result = reader.consumeLetterSequence();
        Assert.assertEquals("Expected substring: 'Hello'", result, "Hello");
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello12345 World"));
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Expected substring: 'Hello12345'", result, "Hello12345");
    }

    @Test
    public void testMatchesMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue("Expected matches for character 'o'", reader.matches('o'));
        Assert.assertFalse("Expected no matches for character 'x'", reader.matches('x'));
    }

    @Test
    public void testMatchesMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue("Expected matches for substring 'll'", reader.matches("ll"));
        Assert.assertFalse("Expected no matches for substring 'kk'", reader.matches("kk"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue("Expected matches for substring 'll'", reader.matchesIgnoreCase("ll"));
        Assert.assertTrue("Expected matches for substring 'LL'", reader.matchesIgnoreCase("LL"));
        Assert.assertFalse("Expected no matches for substring 'kk'", reader.matchesIgnoreCase("kk"));
    }

    @Test
    public void testMatchesAnyMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue("Expected matches for character 'l'", reader.matchesAny('l'));
        Assert.assertFalse("Expected no matches for character 'x'", reader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySortedMethodWithArray() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Character[] arr = {'l', 'o'};
        Assert.assertTrue("Expected matches for characters ['l', 'o']", reader.matchesAnySorted(arr));
        Character[] arr2 = {'x', 'o'};
        Assert.assertFalse("Expected no matches for characters ['x', 'o']", reader.matchesAnySorted(arr2));
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue("Expected matches for character 'o'", reader.matchesLetter());
        Assert.assertFalse("Expected no matches for character 'x'", reader.matchesLetter());
    }

    @Test
    public void testMatchesDigitMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello12345 World"));
        Assert.assertTrue("Expected matches for character '5'", reader.matchesDigit());
        Assert.assertFalse("Expected no matches for character 'x'", reader.matchesDigit());
    }

    @Test
    public void testToStringMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String result = reader.toString();
        Assert.assertEquals("Expected content: 'Hello World'", result, "Hello World");
    }
}