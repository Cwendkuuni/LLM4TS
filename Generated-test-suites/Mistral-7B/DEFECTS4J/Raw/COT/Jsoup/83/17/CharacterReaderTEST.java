package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        int pos = characterReader.pos();
        Assert.assertEquals(2, pos);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(characterReader.isEmpty());

        characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        char current = characterReader.current();
        Assert.assertEquals('W', current);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume(); // should throw an exception
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        characterReader.consume();
        char current = characterReader.current();
        Assert.assertEquals('W', current);
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        characterReader.consume();
        characterReader.unconsume();
        char current = characterReader.current();
        Assert.assertEquals('W', current);
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        int pos = characterReader.pos();
        Assert.assertEquals(2, pos);
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.mark();
        characterReader.advance();
        characterReader.advance();
        int pos = characterReader.pos();
        Assert.assertEquals(2, pos);
        characterReader.rewindToMark();
        int previousPos = characterReader.pos();
        Assert.assertEquals(0, previousPos);
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        int indexOfChar = characterReader.nextIndexOf('o');
        Assert.assertEquals(4, indexOfChar);

        int indexOfString = characterReader.nextIndexOf("llo");
        Assert.assertEquals(2, indexOfString);

        int indexOfEmpty = characterReader.nextIndexOf("");
        Assert.assertEquals(-1, indexOfEmpty);

        int indexOfNull = characterReader.nextIndexOf(null);
        Assert.assertEquals(-1, indexOfNull);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String str = characterReader.consumeTo('l');
        Assert.assertEquals("Hello", str);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String str = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World", str);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String str = characterReader.consumeToAny(',', '.', '!');
        Assert.assertEquals("Hello World", str);

        characterReader = new CharacterReader(new StringReader("Hello, World!"));
        str = characterReader.consumeToAny(',', '.', '!');
        Assert.assertEquals(", World!", str);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String str = characterReader.consumeToAnySorted('a', 'z', '!');
        Assert.assertEquals("Hello World", str);

        characterReader = new CharacterReader(new StringReader("apple, banana, cherry"));
        str = characterReader.consumeToAnySorted('p', 'q');
        Assert.assertEquals("apple, banana", str);
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&amp; < &lt; <null>"));
        String str = characterReader.consumeData();
        Assert.assertEquals("&amp; < &lt;", str);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        String str = characterReader.consumeTagName();
        Assert.assertEquals("html", str);

        characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        str = characterReader.consumeTagName();
        Assert.assertEquals("head", str);

        characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        str = characterReader.consumeTagName();
        Assert.assertEquals("title", str);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcde"));
        String str = characterReader.consumeLetterSequence();
        Assert.assertEquals("abcde", str);

        characterReader = new CharacterReader(new StringReader("abcdefg"));
        str = characterReader.consumeLetterSequence();
        Assert.assertEquals("abcde", str);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123def456"));
        String str = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abc123", str);

        characterReader = new CharacterReader(new StringReader("abc123def456"));
        str = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("def456", str);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("\\u0041\\u0042\\u0043"));
        String str = characterReader.consumeHexSequence();
        Assert.assertEquals("ABC", str);

        characterReader = new CharacterReader(new StringReader("\\u0041\\u0042\\u0043"));
        str = characterReader.consumeHexSequence();
        Assert.assertEquals("", str);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("123456"));
        String str = characterReader.consumeDigitSequence();
        Assert.assertEquals("123456", str);

        characterReader = new CharacterReader(new StringReader("123456"));
        str = characterReader.consumeDigitSequence();
        Assert.assertEquals("", str);
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertTrue(characterReader.matches("ll"));
        Assert.assertFalse(characterReader.matches('a'));
        Assert.assertFalse(characterReader.matches("llo"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("llo"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("world"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("hello"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAny('l', 'o'));
        Assert.assertFalse(characterReader.matchesAny('a'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'o', 'l'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'a'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter('1'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String str = characterReader.matchConsume("llo");
        Assert.assertEquals("llo", str);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String str = characterReader.matchConsumeIgnoreCase("llo");
        Assert.assertEquals("llo", str);

        characterReader = new CharacterReader(new StringReader("Hello World"));
        str = characterReader.matchConsumeIgnoreCase("world");
        Assert.assertEquals("world", str);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("title"));

        characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/head"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullReader() {
        new CharacterReader(null);
    }
}