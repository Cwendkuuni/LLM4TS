package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals('d', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals('f', reader.consume());
        Assert.assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(5, reader.nextIndexOf('e'));
        Assert.assertEquals(-1, reader.nextIndexOf('g'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        Assert.assertEquals(0, reader.nextIndexOf("ab"));
        Assert.assertEquals(6, reader.nextIndexOf("cd"));
        Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("bcde", reader.consumeTo('e'));
        Assert.assertEquals("f", reader.consumeTo('f'));
        Assert.assertEquals("", reader.consumeTo('g'));
    }

    @Test
    public void testConsumeToWithSequence() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        Assert.assertEquals("ab", reader.consumeTo("ab"));
        Assert.assertEquals("cd", reader.consumeTo("cd"));
        Assert.assertEquals("", reader.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("bcde", reader.consumeToAny('b', 'e'));
        Assert.assertEquals("f", reader.consumeToAny('f'));
        Assert.assertEquals("", reader.consumeToAny('a', 'z'));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("bcde", reader.consumeToAnySorted('b', 'e'));
        Assert.assertEquals("f", reader.consumeToAnySorted('f'));
        Assert.assertEquals("", reader.consumeToAnySorted(new char[] {'a', 'z'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("& <null");
        Assert.assertEquals("&", reader.consumeData().trim());
        Assert.assertEquals("<", reader.consumeData().trim());
        Assert.assertEquals("null", reader.consumeData().trim());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("\t<title>\n<style>");
        Assert.assertEquals("\t<title>", reader.consumeTagName().trim());
        Assert.assertEquals("", reader.consumeTagName().trim());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        Assert.assertEquals("abcd", reader.consumeLetterSequence().trim());
        Assert.assertEquals("", reader.consumeLetterSequence().trim());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        Assert.assertEquals("abc123", reader.consumeLetterThenDigitSequence().trim());
        Assert.assertEquals("def456", reader.consumeLetterThenDigitSequence().trim());
        Assert.assertEquals("", reader.consumeLetterThenDigitSequence().trim());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("%#65%#6E");
        Assert.assertEquals("AB", reader.consumeHexSequence().trim());
        Assert.assertEquals("", reader.consumeHexSequence().trim());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123456");
        Assert.assertEquals("12345", reader.consumeDigitSequence().trim());
        Assert.assertEquals("6", reader.consumeDigitSequence().trim());
        Assert.assertEquals("", reader.consumeDigitSequence().trim());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("a"));
        Assert.assertFalse(reader.matches('b'));
        Assert.assertFalse(reader.matches("b"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("A"));
        Assert.assertFalse(reader.matchesIgnoreCase("b"));
        Assert.assertFalse(reader.matchesIgnoreCase("B"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c'));
        Assert.assertFalse(reader.matchesAny('d', 'a', 'b'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchesAnySorted('a', 'b', 'c'));
        Assert.assertFalse(reader.matchesAnySorted(new char[] {'d', 'a', 'b'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }
}