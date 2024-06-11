package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.nextIndexOf('a'));
        Assert.assertEquals("", reader.consumeTo('a'));
        Assert.assertEquals("", reader.consumeToEnd());
        Assert.assertEquals("", reader.consumeLetterSequence());
        Assert.assertEquals("", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("", reader.consumeHexSequence());
        Assert.assertEquals("", reader.consumeDigitSequence());
        Assert.assertFalse(reader.matches('a'));
        Assert.assertFalse(reader.matches("ab"));
        Assert.assertFalse(reader.matchesAny('b', 'c'));
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchConsume("ab"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("Ab"));
        Assert.assertFalse(reader.containsIgnoreCase("ab"));
        Assert.assertEquals("", reader.toString());
    }

    @Test
    public void testSingleCharacterReader() {
        CharacterReader reader = new CharacterReader("ab");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.nextIndexOf('a'));
        Assert.assertEquals("a", reader.consumeAsString());
        Assert.assertEquals("a", reader.consumeTo('a').trim());
        Assert.assertEquals("b", reader.consumeTo('b').trim());
        Assert.assertEquals("ab", reader.consumeToEnd());
        Assert.assertEquals("a", reader.consumeLetterSequence());
        Assert.assertEquals("a", reader.consumeLetterThenDigitSequence().substring(0, 1));
        Assert.assertEquals("0x61", reader.consumeHexSequence()); // hex for lowercase 'a'
        Assert.assertEquals("1", reader.consumeDigitSequence());
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("a"));
        Assert.assertTrue(reader.matchesAny('a', 'a'));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchConsume("a"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("a"));
        Assert.assertTrue(reader.containsIgnoreCase("a"));
        Assert.assertEquals("a", reader.toString());
    }

    @Test
    public void testScanStringWithMultipleInstances() {
        CharacterReader reader = new CharacterReader("aaabbbcccc");
        Assert.assertFalse(reader.isEmpty());

        Assert.assertEquals(0, reader.nextIndexOf('a'));
        Assert.assertEquals("a", reader.consumeAsString());
        Assert.assertEquals("a", reader.consumeTo('a').trim());
        Assert.assertEquals("a", reader.consumeLetterSequence());
        Assert.assertEquals("aa", reader.consumeLetterThenDigitSequence().substring(0, 2));
        Assert.assertEquals("0x610x61", reader.consumeHexSequence()); // hex for lowercase 'a'
        Assert.assertEquals("2", reader.consumeDigitSequence());

        // check nextInstanceOf with different characters
        Assert.assertEquals(3, reader.nextIndexOf('b'));
        Assert.assertEquals("b", reader.consumeTo('b').trim());
        Assert.assertEquals("bb", reader.consumeLetterThenDigitSequence().substring(0, 2));
        Assert.assertEquals("0x620x62", reader.consumeHexSequence());
        Assert.assertEquals("3", reader.consumeDigitSequence());

        // check nextInstanceOf with different sequences
        Assert.assertEquals(5, reader.nextIndexOf("cc"));
        Assert.assertEquals("cc", reader.consumeTo("cc").trim());
        Assert.assertEquals("ccc", reader.consumeLetterThenDigitSequence().substring(0, 3));
        Assert.assertEquals("0x630x630x63", reader.consumeHexSequence());
        Assert.assertEquals("3", reader.consumeDigitSequence());
    }

    @Test
    public void testScanStringWithSubSequences() {
        CharacterReader reader = new CharacterReader("aaabbbccc123ABC");
        Assert.assertFalse(reader.isEmpty());

        // test scan for single character
        Assert.assertEquals(0, reader.nextIndexOf('a'));
        Assert.assertEquals("a", reader.consumeTo('a').trim());
        Assert.assertEquals("a", reader.consumeLetterSequence());
        Assert.assertEquals("a", reader.toString().substring(0, 1));

        // test scan for sub-sequence
        Assert.assertEquals(4, reader.nextIndexOf("AB"));
        Assert.assertEquals("AB", reader.consumeTo("AB").trim());
        Assert.assertEquals("AB", reader.consumeLetterThenDigitSequence().substring(0, 2));
        Assert.assertEquals("0x410x42", reader.consumeHexSequence());
        Assert.assertEquals("", reader.consumeDigitSequence());

        // test scan for sequence with mixed case
        Assert.assertEquals(7, reader.nextIndexOf("AbC"));
        Assert.assertEquals("AbC", reader.consumeTo("AbC").trim());
        Assert.assertEquals("AbC", reader.consumeLetterThenDigitSequence().substring(0, 3));
        Assert.assertEquals("0x410x620x43", reader.consumeHexSequence());
        Assert.assertEquals("", reader.consumeDigitSequence());
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLengthInput() {
        new CharacterReader("abcdef".substring(0, -1));
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcabc");
        reader.mark();
        reader.consumeLetterSequence();
        reader.rewindToMark();
        Assert.assertEquals("a", reader.consumeLetterSequence().trim());
    }

    @Test
    public void testMarkAndConsume() {
        CharacterReader reader = new CharacterReader("abcabc");
        reader.mark();
        reader.consumeLetterSequence();
        reader.consume('b');
        reader.rewindToMark();
        Assert.assertEquals("ab", reader.consumeLetterThenDigitSequence().substring(0, 2));
    }

    @Test
    public void testMarkAndAdvance() {
        CharacterReader reader = new CharacterReader("abcabc");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals("a", reader.consumeLetterSequence().trim());
    }
}