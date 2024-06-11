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
    public void testCharacterReader_NormalCase() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello", reader.consumeTo(' '));
        Assert.assertEquals("World", reader.consumeToEnd());
        reader.mark();
        reader.rewindToMark();
        Assert.assertEquals("Hello", reader.consumeTo(' '));
        reader.unconsume();
        Assert.assertEquals('H', reader.current());
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReader_IOException_Case() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        reader.reader.mark(maxBufferLen);
        reader.reader.close();
        reader.reader.reset();
        reader.consume();
    }

    @Test
    public void testCharacterReader_Empty_Case() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_ConsumeToAny_Case() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcd123e<fgh>ijk"));
        Assert.assertEquals("abc", reader.consumeToAny(new char[]{'a', 'b', 'c'}));
        Assert.assertEquals("123", reader.consumeToAny(new char[]{'1', '2', '3'}));
        Assert.assertEquals("e<fgh>", reader.consumeToAnySorted(new char[]{'e', 'f', 'g', 'h'}));
        Assert.assertEquals("ijk", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_ConsumeToEnd_Case() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcd123e<fgh>ijk"));
        Assert.assertEquals("abcd123e<fgh>ijk", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_ConsumeLetterSequence_Case() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertEquals("abcdefghijklm", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReader_ConsumeLetterThenDigitSequence_Case() throws IOException {
        CharacterReader reader = new StringReader("abc123def456ghi789jkl");
        Assert.assertEquals("abc123def456", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReader_ConsumeHexSequence_Case() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789ABCDEFabcdef"));
        Assert.assertEquals("012345", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReader_ConsumeDigitSequence_Case() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("12345678901234567890123456"));
        Assert.assertEquals("1234567890", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReader_Matches_Case() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("orl"));
    }

    @Test
    public void testCharacterReader_MatchesIgnoreCase_Case() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesIgnoreCase("HELLO"));
        Assert.assertTrue(reader.matchesIgnoreCase("ORL"));
    }

    @Test
    public void testCharacterReader_MatchesAny_Case() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesAny(new char[]{'h', 'e'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'z'}));
    }

    @Test
    public void testCharacterReader_MatchesAnySorted_Case() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'h', 'e', 'l', 'o', 'r', 'w', 'W'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testCharacterReader_MatchesLetter_Case() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReader_MatchesDigit_Case() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(reader.matchesDigit());
        reader.advance();
        Assert.assertFalse(reader.matchesDigit());
        reader.consume('1');
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testCharacterReader_ToString_Case() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.toString());
    }
}