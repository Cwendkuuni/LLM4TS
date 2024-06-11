package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.CharacterReader.EOF;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        String input = "abcdefg";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("abcdefg", characterReader.toString());
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        String input = "";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        String input = "abc";
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.advance();
        Assert.assertEquals('a', characterReader.current());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCurrentEndOfInput() {
        String input = "abc";
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.advance();
        characterReader.advance();
        characterReader.current();
    }

    @Test
    public void testConsume() {
        String input = "abc";
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.advance();
        char c = characterReader.consume();
        Assert.assertEquals('a', c);
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeEndOfInput() {
        String input = "abc";
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.advance();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
    }

    @Test
    public void testUnconsume() {
        String input = "abc";
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.advance();
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('a', characterReader.current());
    }

    @Test
    public void testAdvance() {
        String input = "abc";
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.advance();
        characterReader.advance();
        Assert.assertEquals('c', characterReader.current());
    }

    @Test
    public void testMark() {
        String input = "abc";
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        Assert.assertEquals('a', characterReader.current());
    }

    @Test
    public void testRewindToMark() {
        String input = "abc";
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.advance();
        characterReader.advance();
        characterReader.mark();
        characterReader.rewindToMark();
        Assert.assertEquals('a', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        String input = "abc";
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.advance();
        String s = characterReader.consumeAsString();
        Assert.assertEquals("a", s);
        characterReader.advance();
        s = characterReader.consumeAsString();
        Assert.assertEquals("b", s);
        characterReader.consume();
        s = characterReader.consumeAsString();
        Assert.assertEquals("c", s);
    }

    @Test
    public void testNextIndexOf() {
        String input = "abccabc";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals(1, characterReader.nextIndexOf('b'));
        Assert.assertEquals(3, characterReader.nextIndexOf('c'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        String input = "abccabcab";
        CharacterReader characterReader = new CharacterReader(input);
        CharSequence seq = "ba";
        Assert.assertEquals(1, characterReader.nextIndexOf(seq));
        CharSequence seq2 = "cd";
        Assert.assertEquals(-1, characterReader.nextIndexOf(seq2));
    }

    @Test
    public void testConsumeTo() {
        String input = "abccabc";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("b", characterReader.consumeTo('c'));
        Assert.assertEquals("c", characterReader.current());
        Assert.assertEquals("abc", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("abcd", characterReader.consumeToAny('a', 'd'));
        Assert.assertEquals("cd", characterReader.consumeToAny('c', 'd'));
        Assert.assertEquals("", characterReader.consumeToAny('z'));
    }

    @Test
    public void testConsumeToAnySorted() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("abcd", characterReader.consumeToAnySorted('a', 'd'));
        Assert.assertEquals("cd", characterReader.consumeToAnySorted('c', 'd'));
        Assert.assertEquals("", characterReader.consumeToAnySorted('z'));
    }

    @Test
    public void testConsumeData() {
        String input = "abc&def<null>";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("abc", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        String input = "   <title>Test</title>";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("<title>", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        String input = "abcdefg";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("abcdefg", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "abcdefg";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("abc", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String input = "abc123def456";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("abc123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "abcd%e1%e2%e3ef";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("abcd%e1%e2%e3", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "1234567890";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("123456", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        String input = "abcd";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matches('d'));
        Assert.assertTrue(characterReader.matches("d"));
        Assert.assertFalse(characterReader.matches('e'));
        Assert.assertFalse(characterReader.matches("e"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "ABCdEf";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matchesIgnoreCase("D"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("d"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("ef"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("x"));
    }

    @Test
    public void testMatchesAny() {
        String input = "abcd";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matchesAny('a', 'd'));
        Assert.assertTrue(characterReader.matchesAny("a", "d"));
        Assert.assertFalse(characterReader.matchesAny('e', 'd'));
        Assert.assertFalse(characterReader.matchesAny("e", "d"));
    }

    @Test
    public void testMatchesAnySorted() {
        String input = "abcd";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matchesAnySorted('a', 'd'));
        Assert.assertTrue(characterReader.matchesAnySorted("a", "d"));
        Assert.assertFalse(characterReader.matchesAnySorted('e', 'd'));
        Assert.assertFalse(characterReader.matchesAnySorted("e", "d"));
    }

    @Test
    public void testMatchesLetter() {
        String input = "abcd";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String input = "1234";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String input = "abcd";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("d", characterReader.matchConsume("d"));
        Assert.assertFalse(characterReader.matchConsume("e"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "ABCdEf";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("D", characterReader.matchConsumeIgnoreCase("D"));
        Assert.assertEquals("d", characterReader.matchConsumeIgnoreCase("d"));
        Assert.assertFalse(characterReader.matchConsumeIgnoreCase("X"));
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "<html><head><title>Test</title></head><body>";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/TITLE"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/body"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/BODY"));
    }
}