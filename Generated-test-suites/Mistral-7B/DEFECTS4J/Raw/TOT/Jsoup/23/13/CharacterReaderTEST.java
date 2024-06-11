package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCharacterReaderSingleCharacterInput() {
        CharacterReader reader = new CharacterReader("A");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        reader.consume();
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharactersInput() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        reader.consume();
        Assert.assertEquals('b', reader.current());
        reader.consume();
        Assert.assertEquals('c', reader.current());
        reader.consume(); // consume end of input
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        Assert.assertEquals('a', reader.current());
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.advance();
        Assert.assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeSequence() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        String seq = "ab";
        String consumed = reader.consumeTo(seq);
        Assert.assertEquals(seq, consumed);
        Assert.assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeSequenceIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABcdABCD");
        String seq = "ab";
        String consumed = reader.consumeToIgnoreCase(seq);
        Assert.assertEquals(seq, consumed);
        Assert.assertEquals('D', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeAny() {
        CharacterReader reader = new CharacterReader("012345abcdef");
        char[] seq = {'0', '1', '2', '3', '4', '5'};
        String consumed = reader.consumeToAny(seq);
        Assert.assertEquals(new String(seq), consumed);
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(" abcdefghijklmnopqrstuvwxyz");
        String consumed = reader.consumeToEnd();
        String expected = reader.input().substring(reader.pos());
        Assert.assertEquals(expected, consumed);
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals(consumed, reader.consumeToLetterSequence());
        Assert.assertEquals(consumed.length(), reader.consumeLetterSequence().length());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0123456789ABCDEFabcdefABCDEF");
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals(consumed, reader.consumeToHexSequence());
        Assert.assertEquals(consumed.length(), reader.consumeHexSequence().length());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("0123456789");
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals(consumed, reader.consumeToDigitSequence());
        Assert.assertEquals(consumed.length(), reader.consumeDigitSequence().length());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("a"));
        Assert.assertFalse(reader.matches('b'));
        Assert.assertFalse(reader.matches("b"));
        Assert.assertFalse(reader.matches("bc"));
        Assert.assertFalse(reader.matches("ABC"));
        Assert.assertFalse(reader.matchesIgnoreCase("ABC"));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchConsume("a"));
        Assert.assertEquals('b', reader.current());
        Assert.assertFalse(reader.matchConsume("b"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCdefGHIJ");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ABC"));
        Assert.assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Assert.assertTrue(reader.containsIgnoreCase("ab"));
        Assert.assertTrue(reader.containsIgnoreCase("AB"));
    }
}