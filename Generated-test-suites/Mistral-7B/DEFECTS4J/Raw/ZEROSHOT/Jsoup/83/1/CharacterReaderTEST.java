package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        char[] expected = new char[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
        char[] input = new char[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'};

        CharacterReader characterReader = new CharacterReader(new StringReader(new String(input)));
        char[] result = new char[expected.length];

        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals("Unexpected character at position " + i, expected[i], characterReader.current());
            characterReader.consume();
            result[i] = characterReader.current();
            characterReader.consume();
        }

        Assert.assertArrayEquals("Unexpected characters remaining in the reader", expected, result);
        Assert.assertTrue("The reader did not consume all the input", characterReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        characterReader.reader.close();
        characterReader.consume();
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world!"));
        Assert.assertEquals("Unexpected index of 'l'", 3, characterReader.nextIndexOf('l'));
        Assert.assertEquals("Unexpected index of '!'", 11, characterReader.nextIndexOf('!'));
        Assert.assertEquals("Unexpected index of 'x'", -1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testCharacterReaderNextIndexOfSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world!"));
        Assert.assertEquals("Unexpected index of 'hello'", 0, characterReader.nextIndexOf("hello"));
        Assert.assertEquals("Unexpected index of 'world'", 6, characterReader.nextIndexOf("world"));
        Assert.assertEquals("Unexpected index of 'xyz'", -1, characterReader.nextIndexOf("xyz"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world!"));
        String expected = "ello";
        String result = characterReader.consumeTo('l');
        Assert.assertEquals("Unexpected result of consumeTo('l')", expected, result);
        Assert.assertEquals("Unexpected character after 'l'", 'w', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world!"));
        String expected = "hello world!";
        String result = characterReader.consumeToEnd();
        Assert.assertEquals("Unexpected result of consumeToEnd()", expected, result);
        Assert.assertTrue("The reader did not consume all the input", characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world! a b c d e"));
        String expected = "world! a b c d e";
        String result = characterReader.consumeToAny(' ');
        Assert.assertEquals("Unexpected result of consumeToAny(' ') ", expected, result);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world! a b c d e"));
        String expected = "a b c d e";
        String result = characterReader.consumeToAnySorted(new char[]{'b', 'c', 'd'});
        Assert.assertEquals("Unexpected result of consumeToAnySorted(new char[]{'b', 'c', 'd'})", expected, result);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;script src=&quot;js.js&quot;&gt;"));
        String expected = "&lt;script src=&quot;js.js&quot;";
        String result = characterReader.consumeData();
        Assert.assertEquals("Unexpected result of consumeData()", expected, result);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"));
        String expected = "<html>\n<head>\n<title>";
        String result = characterReader.consumeTagName();
        Assert.assertEquals("Unexpected result of consumeTagName()", expected, result);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String expected = "abcdefghijklmnopqrstuvwxyz";
        String result = characterReader.consumeLetterSequence();
        Assert.assertEquals("Unexpected result of consumeLetterSequence()", expected, result);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123def456"));
        String expected = "abc123def456";
        String result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Unexpected result of consumeLetterThenDigitSequence()", expected, result);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&x0F&x41&x1A"));
        String expected = "&#xF;&#x41;&#x1A;";
        String result = characterReader.consumeHexSequence();
        Assert.assertEquals("Unexpected result of consumeHexSequence()", expected, result);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("1234567890"));
        String expected = "1234567890";
        String result = characterReader.consumeDigitSequence();
        Assert.assertEquals("Unexpected result of consumeDigitSequence()", expected, result);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world!"));
        Assert.assertTrue("Unexpected result of matches('h')", characterReader.matches('h'));
        Assert.assertTrue("Unexpected result of matches('l')", characterReader.matches('l'));
        Assert.assertFalse("Unexpected result of matches('x')", characterReader.matches('x'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world!"));
        Assert.assertTrue("Unexpected result of matchesIgnoreCase('h')", characterReader.matchesIgnoreCase("h"));
        Assert.assertTrue("Unexpected result of matchesIgnoreCase('L')", characterReader.matchesIgnoreCase("L"));
        Assert.assertFalse("Unexpected result of matchesIgnoreCase('x')", characterReader.matchesIgnoreCase("x"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world!"));
        Assert.assertTrue("Unexpected result of matchesAny('l')", characterReader.matchesAny('l'));
        Assert.assertTrue("Unexpected result of matchesAny('w')", characterReader.matchesAny('w'));
        Assert.assertFalse("Unexpected result of matchesAny('x')", characterReader.matchesAny('x'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world!"));
        Assert.assertTrue("Unexpected result of matchesAnySorted('l')", characterReader.matchesAnySorted('l'));
        Assert.assertTrue("Unexpected result of matchesAnySorted('w')", characterReader.matchesAnySorted('w'));
        Assert.assertFalse("Unexpected result of matchesAnySorted('x')", characterReader.matchesAnySorted('x'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world!"));
        Assert.assertTrue("Unexpected result of matchesLetter()", characterReader.matchesLetter());
        Assert.assertFalse("Unexpected result of matchesLetter()", characterReader.matchesLetter('!'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world!123"));
        Assert.assertTrue("Unexpected result of matchesDigit()", characterReader.matchesDigit());
        Assert.assertFalse("Unexpected result of matchesDigit()", characterReader.matchesDigit('!'));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world!"));
        String expected = "ello";
        characterReader.matchConsume("ello");
        Assert.assertEquals("Unexpected character after 'l'", 'w', characterReader.current());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("HELLO WORLD!"));
        String expected = "HELLO";
        characterReader.matchConsumeIgnoreCase("hello");
        Assert.assertEquals("Unexpected character after 'L'", 'W', characterReader.current());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body></body></html>"));
        Assert.assertTrue("The reader should contain the substring '<title>'", characterReader.containsIgnoreCase("<title>"));
        Assert.assertTrue("The reader should contain the substring '</title>'", characterReader.containsIgnoreCase("</title>"));
        Assert.assertFalse("The reader should not contain the substring '<script>'", characterReader.containsIgnoreCase("<script>"));
    }
}