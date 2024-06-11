package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * CharacterReaderTest class to test the CharacterReader class methods.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderCreation() {
        String input = "testInput";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertNotNull(characterReader);
        Assert.assertEquals(input, characterReader.toString());
    }

    @Test
    public void testIsEmpty() {
        String input = "";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.isEmpty());

        input = "testInput";
        characterReader = new CharacterReader(input);
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrentAndConsume() {
        String input = "abc";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals('a', characterReader.current());
        Assert.assertEquals('a', characterReader.consume());
        Assert.assertEquals('b', characterReader.current());
        Assert.assertEquals('b', characterReader.consume());
        Assert.assertEquals('c', characterReader.current());
        Assert.assertEquals('c', characterReader.consume());
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testUnconsumeAndAdvance() {
        String input = "abcd";
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('a', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('b', characterReader.current());
    }

    @Test
    public void testMarkRewindToMark() {
        String input = "abcd";
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('a', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        String input = "abcd";
        CharacterReader characterReader = new CharacterReader(input);
        String result = characterReader.consumeAsString();
        Assert.assertEquals("ab", result);

        characterReader.advance();
        result = characterReader.consumeAsString();
        Assert.assertEquals("cd", result);
    }

    @Test
    public void testNextIndexOf() {
        String input = "abbbbccc";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals(1, characterReader.nextIndexOf('b'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('e'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        String input = "abababcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        CharSequence seq = "ab";
        Assert.assertEquals(1, characterReader.nextIndexOf(seq));

        seq = "cd";
        Assert.assertEquals(7, characterReader.nextIndexOf(seq));
    }

    @Test
    public void testConsumeTo() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        String result = characterReader.consumeTo('d');
        Assert.assertEquals("abcd", result);

        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        result = characterReader.consumeTo('d');
        Assert.assertEquals("bc", result);
    }

    @Test
    public void testConsumeToString() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        String result = characterReader.consumeTo("cd");
        Assert.assertEquals("ab", result);

        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        result = characterReader.consumeTo("cd");
        Assert.assertEquals("b", result);
    }

    @Test
    public void testConsumeToAny() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        char[] chars = {'a', 'c', 'd'};
        String result = characterReader.consumeToAny(chars);
        Assert.assertEquals("abcd", result);

        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        result = characterReader.consumeToAny(chars);
        Assert.assertEquals("bc", result);
    }

    @Test
    public void testConsumeToAnySorted() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        char[] chars = {'d', 'c', 'a'};
        String result = characterReader.consumeToAnySorted(chars);
        Assert.assertEquals("dcdc", result);
    }

    @Test
    public void testConsumeData() {
        String input = "ab&cd&ef";
        CharacterReader characterReader = new CharacterReader(input);
        String result = characterReader.consumeData();
        Assert.assertEquals("ab&cd", result);
    }

    @Test
    public void testConsumeTagName() {
        String input = "  <tag> content ";
        CharacterReader characterReader = new CharacterReader(input);
        String result = characterReader.consumeTagName();
        Assert.assertEquals("tag", result);
    }

    @Test
    public void testConsumeToEnd() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        String result = characterReader.consumeToEnd();
        Assert.assertEquals("abcdabcdabcd", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "abcdEfgh";
        CharacterReader characterReader = new CharacterReader(input);
        String result = characterReader.consumeLetterSequence();
        Assert.assertEquals("abcd", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String input = "abcd123ef";
        CharacterReader characterReader = new CharacterReader(input);
        String result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abcd123", result);
    }

    @Test
    public void testMatches() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matches('d'));
        Assert.assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatchesString() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        String seq = "cd";
        Assert.assertTrue(characterReader.matches(seq));
        Assert.assertFalse(characterReader.matches(Arrays.asList("ab", "ef").toArray(new String[2])));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "abcdABCDabcd";
        CharacterReader characterReader = new CharacterReader(input);
        String seq = "CD";
        Assert.assertTrue(characterReader.matchesIgnoreCase(seq));
        Assert.assertTrue(characterReader.matchesIgnoreCase("Ab"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("ef"));
    }

    @Test
    public void testMatchesAny() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        char[] seq = {'d', 'e'};
        Assert.assertFalse(characterReader.matchesAny(seq));
        Assert.assertTrue(characterReader.matchesAny(Arrays.asList('a', 'b', 'c').toArray(new char[0])));
    }

    @Test
    public void testMatchesAnySorted() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        char[] seq = {'d', 'c', 'a'};
        Assert.assertTrue(characterReader.matchesAnySorted(seq));
        Assert.assertFalse(characterReader.matchesAnySorted(Arrays.asList('e', 'f').toArray(new char[0])));
    }

    @Test
    public void testMatchesLetter() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String input = "123abcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeString() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        String seq = "cd";
        Assert.assertTrue(characterReader.matchConsume(seq));
        Assert.assertFalse(characterReader.matchConsume(Arrays.asList("ab", "ef").toArray(new String[2])));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "abcdABCDabcd";
        CharacterReader characterReader = new CharacterReader(input);
        String seq = "CD";
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase(seq));
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("Ab"));
        Assert.assertFalse(characterReader.matchConsumeIgnoreCase("ef"));
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = " <title>My title </title> <style> css style </style>";
        CharacterReader characterReader = new CharacterReader(input);
        String seq = "</title>";
        Assert.assertTrue(characterReader.containsIgnoreCase(seq));
        Assert.assertTrue(characterReader.containsIgnoreCase("/STYLE"));
        Assert.assertFalse(characterReader.containsIgnoreCase("ef"));
    }

    @Test
    public void testtoString() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        String result = characterReader.toString();
        Assert.assertEquals(input, result);
    }

    @Test
    public void testCacheString() {
        String input = "abcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(input);
        String result = characterReader.cacheString(0, 3);
        Assert.assertEquals("ab", result);

        result = characterReader.cacheString(4, 3);
        Assert.assertEquals("cd", result);
    }
}