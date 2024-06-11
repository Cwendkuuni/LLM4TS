package org.jsoup.parser; // replace with your package name if different

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyCharacterReader() {
        CharacterReader characterReader = new CharacterReader("");
        Assert.assertTrue(characterReader.isEmpty());
        Assert.assertEquals("", characterReader.toString());
    }

    @Test
    public void testCharacterReaderWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader("a");
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals("a", characterReader.consumeAsString());
        Assert.assertEquals('a', characterReader.current());
        Assert.assertEquals('a', characterReader.consume());
        characterReader.rewindToMark();
        Assert.assertEquals('a', characterReader.current());
    }

    @Test
    public void testNextIndexOfSimpleChar() {
        CharacterReader characterReader = new CharacterReader("abcabc");
        Assert.assertEquals(1, characterReader.nextIndexOf('b'));
    }

    @Test
    public void testNextIndexOfEmptySequence() {
        CharacterReader characterReader = new CharacterReader("abc");
        Assert.assertEquals(-1, characterReader.nextIndexOf(Character.DELIMITER));
    }

    @Test(expected = AssertionError.class)
    public void testNextIndexOfInvalidCharacter() {
        CharacterReader characterReader = new CharacterReader("abc");
        characterReader.nextIndexOf((char) 0xFFFF);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("abcdef");
        String expected = characterReader.consumeToEnd();
        Assert.assertEquals("abcdef", expected);
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader characterReader = new CharacterReader("abcdef");
        Assert.assertEquals("abc", characterReader.consumeTo('c'));
    }

    @Test
    public void testConsumeToString() {
        CharacterReader characterReader = new CharacterReader("abcdef");
        Assert.assertEquals("abc", characterReader.consumeTo("bc"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader("abcd123abcd");
        Assert.assertEquals("123", characterReader.consumeToAny('1', '3'));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader("abcd123abcd");
        Assert.assertEquals("123", characterReader.consumeToAnySorted(new char[]{'1', '3'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader("abc&def");
        Assert.assertEquals("abc", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader("<title>Hello World</title>");
        Assert.assertEquals("<title>", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("HelloWorld");
        Assert.assertEquals("Hello", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader("Hello12345");
        Assert.assertEquals("Hello123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("&x0FFF;");
        Assert.assertEquals("FFF", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("12345");
        Assert.assertEquals("12345", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatchesSimpleChar() {
        CharacterReader characterReader = new CharacterReader("abc");
        Assert.assertTrue(characterReader.matches('b'));
        Assert.assertFalse(characterReader.matches('a'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader characterReader = new CharacterReader("abcabc");
        Assert.assertTrue(characterReader.matches("ab"));
        Assert.assertFalse(characterReader.matches("ac"));
    }

    @Test
    public void testMatchesIgnoreCaseString() {
        CharacterReader characterReader = new CharacterReader("abcabc");
        Assert.assertTrue(characterReader.matchesIgnoreCase("Ab"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("Ac"));
    }

    @Test
    public void testMatchesAnySingleChar() {
        CharacterReader characterReader = new CharacterReader("abc");
        Assert.assertTrue(characterReader.matchesAny('a'));
        Assert.assertTrue(characterReader.matchesAny('b'));
        Assert.assertFalse(characterReader.matchesAny('c'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader("abc");
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'a', 'b'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'b', 'a'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader("abc");
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter()); // false since ' ' is not a letter
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader("123");
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit()); // false since ' ' is not a digit
    }

    @Test
    public void testMatchConsumeSimpleChar() {
        CharacterReader characterReader = new CharacterReader("abc");
        Assert.assertTrue(characterReader.matchConsume('b'));
        Assert.assertEquals("", characterReader.toString());
    }

    @Test
    public void testMatchConsumeString() {
        CharacterReader characterReader = new CharacterReader("abcabc");
        Assert.assertTrue(characterReader.matchConsume("ab"));
        Assert.assertEquals("c", characterReader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        CharacterReader characterReader = new CharacterReader("abcabc");
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("Ab"));
        Assert.assertEquals("c", characterReader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("<html><head><title>Test</title></head></html>");
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/HEAD"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/body"));
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader("abc");
        Assert.assertEquals("abc", characterReader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader characterReader = new CharacterReader("abcdefg");
        String str1 = characterReader.cacheString(0, 5);
        String str2 = characterReader.cacheString(5, 3);
        Assert.assertEquals("abcde", str1);
        Assert.assertEquals("fgh", str2);
    }

    @Test
    public void testRangeEquals() {
        CharacterReader characterReader = new CharacterReader("abcdefg");
        Assert.assertTrue(characterReader.rangeEquals(0, 5, "abcde"));
        Assert.assertFalse(characterReader.rangeEquals(0, 5, "abcdefg"));
        Assert.assertFalse(characterReader.rangeEquals(5, 3, "abcdefg"));
    }
}