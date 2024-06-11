package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals("A", reader.toString());
        Assert.assertEquals('A', reader.current());
        reader.consume();
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals("", reader.consumeAsString());
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABC");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals("ABC", reader.toString());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals('B', reader.current());
        Assert.assertEquals("BC", reader.consumeTo("BC"));
        Assert.assertEquals('C', reader.current());
        Assert.assertEquals("", reader.consumeToEnd());
        Assert.assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("ABC");
        Assert.assertEquals(1, reader.nextIndexOf('B'));
        Assert.assertEquals(2, reader.nextIndexOf('C'));
        Assert.assertEquals(-1, reader.nextIndexOf('D'));
        reader.consume();
        Assert.assertEquals(2, reader.nextIndexOf('C'));
        reader.consume();
        Assert.assertEquals(-1, reader.nextIndexOf('C'));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("ABABABC");
        Assert.assertEquals("BA", reader.consumeTo('B'));
        Assert.assertEquals("AB", reader.consumeTo('A'));
        Assert.assertEquals("C", reader.consumeToEnd());
        reader.mark();
        Assert.assertEquals("AB", reader.consumeTo('A'));
        reader.rewindToMark();
        Assert.assertEquals("AB", reader.consumeTo('A'));
    }

    @Test
    public void testCharacterReaderConsumeToSequence() {
        CharacterReader reader = new CharacterReader("ABABABC");
        Assert.assertEquals("BA", reader.consumeTo("BA"));
        reader.consume();
        Assert.assertEquals("AB", reader.consumeTo("AB"));
        Assert.assertEquals("C", reader.consumeToEnd());
        reader.mark();
        Assert.assertEquals("AB", reader.consumeTo("AB"));
        reader.rewindToMark();
        Assert.assertEquals("AB", reader.consumeTo("AB"));
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("ABCDABCDABCDABCD");
        Assert.assertEquals("CD", reader.consumeToAny(new char[]{'C', 'D'}));
        Assert.assertEquals("AB", reader.consumeToAny(new char[]{'A', 'B'}));
        Assert.assertEquals("", reader.consumeToAny(new char[]{'E', 'F'}));
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("ABCDABCDABCDABCD");
        Assert.assertEquals("CD", reader.consumeToAnySorted(new char[]{'C', 'D'}));
        Assert.assertEquals("AB", reader.consumeToAnySorted(new char[]{'A', 'B'}));
        Assert.assertEquals("", reader.consumeToAnySorted(new char[]{'E', 'F'}));
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader("&lt;script src=&quot;js.js&quot; type=&quot;text/javascript&gt;");
        Assert.assertEquals("&lt;script src=&quot;js.js&quot; type=&quot;text/javascript&gt;", reader.consumeData());
        reader.consume();
        Assert.assertEquals("script src=&quot;js.js&quot; type=&quot;text/javascript&gt;", reader.consumeData());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader("<title>Page Title</title>");
        Assert.assertEquals("title", reader.consumeTagName());
        reader.consumeLetter();
        reader.consumeLetter();
        Assert.assertEquals("tle", reader.consumeTagName());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        Assert.assertEquals("abcdef", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        Assert.assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("ABABABC");
        Assert.assertTrue(reader.matches('B'));
        Assert.assertTrue(reader.matches("BA"));
        Assert.assertFalse(reader.matches('D'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABABABC");
        Assert.assertTrue(reader.matchesIgnoreCase("ba"));
        Assert.assertFalse(reader.matchesIgnoreCase("d"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("ABCDABCDABCDABCD");
        Assert.assertTrue(reader.matchesAny(new char[]{'A', 'B'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'C', 'D'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'E', 'F'}));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("ABCDABCDABCDABCD");
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'C', 'D'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'A', 'B'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'E', 'F'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("123456");
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        Assert.assertEquals("ABCDEF", reader.toString());
    }
}