package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        String input = "Hello World <html><body><h1>Test</h1></body></html>";
        reader = new CharacterReader(new StringReader(input));
    }

    

    // Test constructor methods

    @Test
    public void testCharacterReaderWithInput() {
        CharacterReader charReader = new CharacterReader(reader);
        Assert.assertNotNull(charReader);
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderWithNullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderWithInputAndSize() {
        CharacterReader charReader = new CharacterReader(reader, 10);
        Assert.assertNotNull(charReader);
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderWithNullInputAndSize() {
        new CharacterReader(null, 10);
    }

    @Test
    public void testCharacterReaderWithInputAndSizeGreaterThanMaxBufferLen() {
        CharacterReader charReader = new CharacterReader(reader, maxBufferLen * 2);
        Assert.assertEquals(maxBufferLen, charReader.charBuf.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderWithInputAndSizeLessThanZero() {
        new CharacterReader(reader, -1);
    }

    // Test pos() method

    @Test
    public void testPosWhenNotEmpty() {
        int expectedPos = 0;
        int actualPos = reader.pos();
        Assert.assertEquals(expectedPos, actualPos);
    }

    @Test
    public void testPosWhenEmpty() {
        reader.bufferUp();
        int expectedPos = -1;
        int actualPos = reader.pos();
        Assert.assertEquals(expectedPos, actualPos);
    }

    // Test isEmpty() method

    @Test
    public void testIsEmptyWhenNotEmpty() {
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testIsEmptyWhenEmpty() {
        reader.bufferUp();
        Assert.assertTrue(reader.isEmpty());
    }

    // Test current() method

    @Test
    public void testCurrentWhenNotAtEnd() {
        char expectedChar = 'H';
        char actualChar = reader.current();
        Assert.assertEquals(expectedChar, actualChar);
    }

    @Test(expected = IllegalStateException.class)
    public void testCurrentWhenAtEnd() {
        reader.bufferUp();
        reader.consume();
        reader.current();
    }

    // Test consume() method

    @Test
    public void testConsumeWhenNotAtEnd() {
        char expectedChar = 'H';
        char actualChar = reader.consume();
        Assert.assertEquals(expectedChar, actualChar);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWhenAtEnd() {
        reader.bufferUp();
        reader.consume();
        reader.consume();
        char actualChar = reader.consume();
    }

    // Test unconsume() method

    @Test
    public void testUnconsume() {
        reader.bufferUp();
        reader.consume();
        reader.unconsume();
        char actualChar = reader.current();
        Assert.assertEquals('H', actualChar);
    }

    // Test advance() method

    @Test
    public void testAdvanceWhenNotAtEnd() {
        reader.advance();
        char expectedChar = 'e';
        char actualChar = reader.current();
        Assert.assertEquals(expectedChar, actualChar);
    }

    @Test(expected = IllegalStateException.class)
    public void testAdvanceWhenAtEnd() {
        reader.bufferUp();
        reader.advance();
    }

    // Test mark() method

    @Test
    public void testMark() {
        reader.mark();
        Assert.assertTrue(reader.markSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMarkUnsupportedOperationException() {
        StringReader stringReader = new StringReader("");
        CharacterReader characterReader = new CharacterReader(stringReader);
        characterReader.mark();
    }

    // Test rewindToMark() method

    @Test
    public void testRewindToMark() {
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        char expectedChar = 'H';
        char actualChar = reader.current();
        Assert.assertEquals(expectedChar, actualChar);
    }

    // Test nextIndexOf(char) method

    @Test
    public void testNextIndexOfCharWhenFound() {
        int expectedIndex = 7;
        int actualIndex = reader.nextIndexOf('l');
        Assert.assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testNextIndexOfCharWhenNotFound() {
        int expectedIndex = -1;
        int actualIndex = reader.nextIndexOf('x');
        Assert.assertEquals(expectedIndex, actualIndex);
    }

    // Test nextIndexOf(CharSequence) method

    @Test
    public void testNextIndexOfSequenceWhenFound() {
        String sequence = "html";
        int expectedIndex = 2;
        int actualIndex = reader.nextIndexOf(sequence);
        Assert.assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testNextIndexOfSequenceWhenNotFound() {
        String sequence = "xml";
        int expectedIndex = -1;
        int actualIndex = reader.nextIndexOf(sequence);
        Assert.assertEquals(expectedIndex, actualIndex);
    }

    // Test consumeTo(char) method

    @Test
    public void testConsumeToCharWhenFound() {
        String expectedString = "World";
        String actualString = reader.consumeTo(' ');
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testConsumeToCharWhenNotFound() {
        String expectedString = "";
        String actualString = reader.consumeTo(' ');
        Assert.assertEquals(expectedString, actualString);
    }

    // Test consumeTo(String) method

    @Test
    public void testConsumeToStringWhenFound() {
        String expectedString = "<html><body><h1>Test</h1>";
        String actualString = reader.consumeTo("<h1>");
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testConsumeToStringWhenNotFound() {
        String expectedString = "";
        String actualString = reader.consumeTo("<h1>");
        Assert.assertEquals(expectedString, actualString);
    }

    // Test consumeToAny(char[]) method

    @Test
    public void testConsumeToAnyWhenFound() {
        char[] expectedChars = {'<', 'h', '1'};
        String actualString = reader.consumeToAny(expectedChars);
        char[] actualChars = actualString.toCharArray();
        Assert.assertArrayEquals(expectedChars, actualChars);
    }

    @Test
    public void testConsumeToAnyWhenNotFound() {
        char[] expectedChars = {};
        String actualString = reader.consumeToAny(new char[0]);
        char[] actualChars = actualString.toCharArray();
        Assert.assertArrayEquals(expectedChars, actualChars);
    }

    // Test consumeToAnySorted(char[]) method

    @Test
    public void testConsumeToAnySortedWhenFound() {
        char[] expectedChars = {'<', 'h'};
        String actualString = reader.consumeToAnySorted(new char[]{'h', '<'});
        char[] actualChars = actualString.toCharArray();
        Assert.assertArrayEquals(expectedChars, actualChars);
    }

    @Test
    public void testConsumeToAnySortedWhenNotFound() {
        char[] expectedChars = {};
        String actualString = reader.consumeToAnySorted(new char[]{'>', '<'});
        char[] actualChars = actualString.toCharArray();
        Assert.assertArrayEquals(expectedChars, actualChars);
    }

    // Test consumeData() method

    @Test
    public void testConsumeDataWhenFound() {
        String expectedString = "Hello World <html><body><h1>Test</h1></body></html>";
        String actualString = reader.consumeData();
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testConsumeDataWhenNotFound() {
        String expectedString = "";
        String actualString = reader.consumeData();
        Assert.assertEquals(expectedString, actualString);
    }

    // Test consumeTagName() method

    @Test
    public void testConsumeTagNameWhenFound() {
        String expectedString = "html";
        String actualString = reader.consumeTagName();
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testConsumeTagNameWhenNotFound() {
        String expectedString = "";
        String actualString = reader.consumeTagName();
        Assert.assertEquals(expectedString, actualString);
    }

    // Test consumeToEnd() method

    @Test
    public void testConsumeToEnd() {
        String expectedString = "</html>";
        String actualString = reader.consumeToEnd();
        Assert.assertEquals(expectedString, actualString);
    }

    // Test consumeLetterSequence() method

    @Test
    public void testConsumeLetterSequenceWhenFound() {
        String expectedString = "Hello";
        String actualString = reader.consumeLetterSequence();
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testConsumeLetterSequenceWhenNotFound() {
        String expectedString = "";
        String actualString = reader.consumeLetterSequence();
        Assert.assertEquals(expectedString, actualString);
    }

    // Test consumeLetterThenDigitSequence() method

    @Test
    public void testConsumeLetterThenDigitSequenceWhenFound() {
        String expectedString = "Hello123";
        String actualString = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testConsumeLetterThenDigitSequenceWhenNotFound() {
        String expectedString = "";
        String actualString = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expectedString, actualString);
    }

    // Test matches(char) method

    @Test
    public void testMatchesCharWhenMatches() {
        char expectedChar = 'H';
        boolean actualBoolean = reader.matches(expectedChar);
        Assert.assertTrue(actualBoolean);
    }

    @Test
    public void testMatchesCharWhenNotMatches() {
        char expectedChar = 'X';
        boolean actualBoolean = reader.matches(expectedChar);
        Assert.assertFalse(actualBoolean);
    }

    // Test matches(String) method

    @Test
    public void testMatchesStringWhenMatches() {
        String expectedString = "Hello";
        boolean actualBoolean = reader.matches(expectedString);
        Assert.assertTrue(actualBoolean);
    }

    @Test
    public void testMatchesStringWhenNotMatches() {
        String expectedString = "World";
        boolean actualBoolean = reader.matches(expectedString);
        Assert.assertFalse(actualBoolean);
    }

    @Test
    public void testMatchesStringIgnoreCaseWhenMatches() {
        String expectedString = "hello";
        boolean actualBoolean = reader.matchesIgnoreCase(expectedString);
        Assert.assertTrue(actualBoolean);
    }

    @Test
    public void testMatchesStringIgnoreCaseWhenNotMatches() {
        String expectedString = "World";
        boolean actualBoolean = reader.matchesIgnoreCase(expectedString);
        Assert.assertFalse(actualBoolean);
    }

    // Test matchesAny(char[]) method

    @Test
    public void testMatchesAnyCharArrayWhenMatches() {
        char[] expectedChars = {'H', 'W'};
        boolean actualBoolean = reader.matchesAny(expectedChars);
        Assert.assertTrue(actualBoolean);
    }

    @Test
    public void testMatchesAnyCharArrayWhenNotMatches() {
        char[] expectedChars = {'X', 'Z'};
        boolean actualBoolean = reader.matchesAny(expectedChars);
        Assert.assertFalse(actualBoolean);
    }

    // Test matchesAnySorted(char[]) method

    @Test
    public void testMatchesAnySortedCharArrayWhenMatches() {
        char[] expectedChars = {'H', 'h'};
        boolean actualBoolean = reader.matchesAnySorted(expectedChars);
        Assert.assertTrue(actualBoolean);
    }

    @Test
    public void testMatchesAnySortedCharArrayWhenNotMatches() {
        char[] expectedChars = {'X', 'Z'};
        boolean actualBoolean = reader.matchesAnySorted(expectedChars);
        Assert.assertFalse(actualBoolean);
    }

    // Test matchesLetter() method

    @Test
    public void testMatchesLetterWhenMatches() {
        char expectedChar = 'H';
        boolean actualBoolean = reader.matchesLetter();
        Assert.assertTrue(actualBoolean);
    }

    @Test
    public void testMatchesLetterWhenNotMatches() {
        char expectedChar = '!';
        boolean actualBoolean = reader.matchesLetter();
        Assert.assertFalse(actualBoolean);
    }

    // Test matchesDigit() method

    @Test
    public void testMatchesDigitWhenMatches() {
        char expectedChar = '3';
        boolean actualBoolean = reader.matchesDigit();
        Assert.assertTrue(actualBoolean);
    }

    @Test
    public void testMatchesDigitWhenNotMatches() {
        char expectedChar = 'X';
        boolean actualBoolean = reader.matchesDigit();
        Assert.assertFalse(actualBoolean);
    }

    // Test matchConsume(String) method

    @Test
    public void testMatchConsumeStringWhenMatches() {
        String expectedString = "Hello";
        boolean actualBoolean = reader.matchConsume(expectedString);
        Assert.assertTrue(actualBoolean);
    }

    @Test
    public void testMatchConsumeStringWhenNotMatches() {
        String expectedString = "World";
        boolean actualBoolean = reader.matchConsume(expectedString);
        Assert.assertFalse(actualBoolean);
    }

    // Test matchConsumeIgnoreCase(String) method

    @Test
    public void testMatchConsumeIgnoreCaseStringWhenMatches() {
        String expectedString = "hello";
        boolean actualBoolean = reader.matchConsumeIgnoreCase(expectedString);
        Assert.assertTrue(actualBoolean);
    }

    @Test
    public void testMatchConsumeIgnoreCaseStringWhenNotMatches() {
        String expectedString = "World";
        boolean actualBoolean = reader.matchConsumeIgnoreCase(expectedString);
        Assert.assertFalse(actualBoolean);
    }

    // Test containsIgnoreCase(String) method

    @Test
    public void testContainsIgnoreCaseStringWhenMatches() {
        String expectedString = "</title>";
        boolean actualBoolean = reader.containsIgnoreCase(expectedString);
        Assert.assertTrue(actualBoolean);
    }

    @Test
    public void testContainsIgnoreCaseStringWhenNotMatches() {
        String expectedString = "</style>";
        boolean actualBoolean = reader.containsIgnoreCase(expectedString);
        Assert.assertFalse(actualBoolean);
    }

    // Test toString() method

    @Test
    public void testToString() {
        String expectedString = "Hello World <html><body><h1>Test</h1></body></html>";
        String actualString = reader.toString();
        Assert.assertEquals(expectedString, actualString);
    }

    
}