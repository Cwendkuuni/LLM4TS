import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.CharacterReaderTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest extends CharacterReader { // Extending the class for easier access to private methods and fields

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", this.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", this.toString());
    }

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals('a', reader.consume());
    }

    @Test
    public void testConsumeMethodWithMultiCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionThrownWhenBufferFailsToFill() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp();
        reader.consume();
    }

    @Test
    public void testMarkAndRewindMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeToMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        String result = reader.consumeTo('b');
        Assert.assertEquals("a", result);
    }

    @Test
    public void testConsumeToMethodWithMultiCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        String result = reader.consumeTo('c');
        Assert.assertEquals("a", result);
        result = reader.consumeTo('c');
        Assert.assertEquals("bc", result);
    }

    @Test
    public void testConsumeToMethodWithEndOfString() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        String result = reader.consumeToEnd();
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testConsumeToMethodWithEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        String result = reader.consumeToEnd();
        Assert.assertEquals("", result);
    }

    @Test
    public void testConsumeToAnyMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        String result = reader.consumeToAny('b');
        Assert.assertEquals("a", result);
    }

    @Test
    public void testConsumeToAnyMethodWithMultiCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String result = reader.consumeToAny('d');
        Assert.assertEquals("abcd", result);
        result = reader.consumeToAny('c');
        Assert.assertEquals("abcd", result);
        result = reader.consumeToAny('a');
        Assert.assertEquals("bc", result);
    }

    @Test
    public void testConsumeToEndMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        String result = reader.consumeToEnd();
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("abcabc"));
        String result = reader.consumeLetterSequence();
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123abc"));
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abc123", result);
    }

    @Test
    public void testConsumeHexSequenceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("abc#x123abc"));
        String result = reader.consumeHexSequence();
        Assert.assertEquals("x123", result);
    }

    @Test
    public void testConsumeDigitSequenceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123abc"));
        String result = reader.consumeDigitSequence();
        Assert.assertEquals("123", result);
    }

    @Test
    public void testMatchesMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertFalse(reader.matches('b'));
    }

    @Test
    public void testMatchesMethodWithMultiCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches("ac"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertFalse(reader.matchesIgnoreCase("B"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithMultiCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
        Assert.assertFalse(reader.matchesIgnoreCase("C"));
    }

    @Test
    public void testMatchesAnyMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchesAny('a'));
        Assert.assertTrue(reader.matchesAny('b'));
        Assert.assertTrue(reader.matchesAny('c'));
        Assert.assertFalse(reader.matchesAny('d'));
    }

    @Test
    public void testMatchesAnyMethodWithMultiCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchesAny("ab"));
        Assert.assertTrue(reader.matchesAny("ac"));
        Assert.assertFalse(reader.matchesAny("de"));
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("123abc"));
        Assert.assertFalse(reader.matchesLetter());
        reader.advance();
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigitMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("123abc"));
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        String result = reader.matchConsume("a");
        Assert.assertEquals("a", result);
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testMatchConsumeMethodWithMultiCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        String result = reader.matchConsume("ab");
        Assert.assertEquals("ab", result);
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethodWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String result = reader.matchConsumeIgnoreCase("a");
        Assert.assertEquals("a", result);
        Assert.assertEquals('A', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethodWithMultiCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String result = reader.matchConsumeIgnoreCase("Ab");
        Assert.assertEquals("Ab", result);
        Assert.assertEquals('C', reader.current());
    }

    @Test
    public void testContainsIgnoreCaseMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title></html>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("title"));
    }

    @Test
    public void testToStringMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals("abc", reader.toString());
    }
}