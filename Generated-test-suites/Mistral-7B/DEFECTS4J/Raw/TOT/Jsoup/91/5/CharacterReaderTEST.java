package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue("Expected empty reader", reader.isEmpty());
        Assert.assertEquals("Expected current position to be 0", 0, reader.pos());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        Assert.assertFalse("Expected not empty", reader.isEmpty());
        Assert.assertEquals("Expected current position to be 0", 0, reader.pos());
        Assert.assertEquals("Expected current character to be 'a'", 'a', reader.current());
        reader.consume();
        Assert.assertEquals("Expected current position to be 1", 1, reader.pos());
        Assert.assertEquals("Expected current character to be 'a'", 'a', reader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertFalse("Expected not empty", reader.isEmpty());
        Assert.assertEquals("Expected current position to be 0", 0, reader.pos());
        Assert.assertEquals("Expected current character to be 'a'", 'a', reader.current());

        reader.consume();
        Assert.assertEquals("Expected current position to be 1", 1, reader.pos());
        Assert.assertEquals("Expected current character to be 'b'", 'b', reader.current());

        reader.consume();
        Assert.assertEquals("Expected current position to be 2", 2, reader.pos());
        Assert.assertEquals("Expected current character to be 'c'", 'c', reader.current());

        reader.consume();
        Assert.assertEquals("Expected current position to be 3", 3, reader.pos());
        Assert.assertEquals("Expected current character to be 'd'", 'd', reader.current());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals("Expected current position to be at mark", 2, reader.pos());
        Assert.assertEquals("Expected current character to be 'c'", 'c', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp(); // Trigger IOException
    }

    @Test
    public void testCharacterReaderNextIndexOfSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abab"));
        int index = reader.nextIndexOf('a');
        Assert.assertEquals("Expected index of 'a' to be 1", 1, index);

        index = reader.nextIndexOf('b');
        Assert.assertEquals("Expected index of 'b' to be 3", 3, index);

        index = reader.nextIndexOf('x');
        Assert.assertEquals("Expected index of 'x' to be -1", -1, index);
    }

    @Test
    public void testCharacterReaderNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcd"));
        int index = reader.nextIndexOf("ab");
        Assert.assertEquals("Expected index of 'ab' to be 0", 0, index);

        index = reader.nextIndexOf("cd");
        Assert.assertEquals("Expected index of 'cd' to be 4", 4, index);

        index = reader.nextIndexOf("xyz");
        Assert.assertEquals("Expected index of 'xyz' to be -1", -1, index);
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        String consumed = reader.consumeTo('c');
        Assert.assertEquals("Expected consumed string to be 'ab'", "ab", consumed);

        consumed = reader.consumeTo('e');
        Assert.assertEquals("Expected consumed string to be 'abcde'", "abcde", consumed);

        consumed = reader.consumeTo('x');
        Assert.assertEquals("Expected consumed string to be 'abcdef'", "abcdef", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789abcdef"));
        String consumed = reader.consumeToAny('0123456789');
        Assert.assertEquals("Expected consumed string to be '0123456789'", "0123456789", consumed);

        consumed = reader.consumeToAnySorted(new char[]{'1', '5', '7'});
        Assert.assertEquals("Expected consumed string to be '157'", "157", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("Expected consumed string to be 'abc'", "abc", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("xyz12345"));
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Expected consumed string to be 'xyz12345'", "xyz12345", consumed);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789ABCDEFabcdef"));
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("Expected consumed string to be '0123456789ABCDEFabcdef'", "0123456789ABCDEFabcdef", consumed);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("01234567890123456789"));
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("Expected consumed string to be '01234567890123456789'", "01234567890123456789", consumed);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789abcdefabcdef"));
        Assert.assertTrue("Expected 'a' to match", reader.matches('a'));
        Assert.assertTrue("Expected 'a' to match", reader.matches("a"));
        Assert.assertTrue("Expected 'a' to match", reader.matchesIgnoreCase("A"));

        Assert.assertFalse("Expected 'x' not to match", reader.matches('x'));
        Assert.assertFalse("Expected 'x' not to match", reader.matches("x"));
        Assert.assertFalse("Expected 'x' not to match", reader.matchesIgnoreCase("X"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertTrue("Expected 'a' to match any", reader.matchesAny('a'));
        Assert.assertTrue("Expected 'a' to match any, sorted", reader.matchesAnySorted(new char[]{'a', 'e', 'i'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertTrue("Expected 'a' to be a letter", reader.matchesLetter());
        Assert.assertTrue("Expected 'z' to be a letter", reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));
        Assert.assertTrue("Expected '0' to be a digit", reader.matchesDigit());
        Assert.assertTrue("Expected '9' to be a digit", reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertTrue("Expected 'a' to match and consume", reader.matchConsume("a"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        Assert.assertTrue("Expected 'A' to match and consume, ignoring case", reader.matchConsumeIgnoreCase("a"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test Title</title>"));
        Assert.assertTrue("Expected '<title>' to be present", reader.containsIgnoreCase("<title>"));
        Assert.assertTrue("Expected '</title>' to be present", reader.containsIgnoreCase("</title>"));
    }
}