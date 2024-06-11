package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() throws IOException {
        String input = "abcd1234efghABC5678";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // test constructor
        Assert.assertEquals(input.length(), reader.charBuf.length);
        Assert.assertEquals(input, reader.toString());

        // test empty
        Assert.assertTrue(reader.isEmpty());
        reader.bufferUp();
        Assert.assertFalse(reader.isEmpty());

        // test pos
        Assert.assertEquals(0, reader.pos());
        reader.advance();
        Assert.assertEquals(1, reader.pos());

        // test consume
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals(2, reader.pos());

        // test unconsume
        reader.unconsume();
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals(2, reader.pos());

        // test advance
        reader.advance();
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals(3, reader.pos());

        // test mark and rewind
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals(3, reader.pos());

        // test nextIndexOf
        Assert.assertEquals(3, reader.nextIndexOf('c'));
        Assert.assertEquals(7, reader.nextIndexOf('5'));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));

        // test nextIndexOf with CharSequence
        CharSequence seq = "123";
        Assert.assertEquals(5, reader.nextIndexOf(seq));
        Assert.assertEquals(8, reader.nextIndexOf("ABC"));
        Assert.assertEquals(-1, reader.nextIndexOf("XYZ"));

        // test consumeTo
        String consumed = reader.consumeTo('5');
        Assert.assertEquals("abcd", consumed);
        Assert.assertEquals(5, reader.pos());

        consumed = reader.consumeTo("ABC");
        Assert.assertEquals("efghABC", consumed);
        Assert.assertEquals(10, reader.pos());

        // test consumeToEnd
        consumed = reader.consumeToEnd();
        Assert.assertEquals("efghABC5678", consumed);
        Assert.assertEquals(17, reader.pos());
        Assert.assertTrue(reader.isEmpty());

        // test consumeLetterSequence
        consumed = reader.consumeLetterSequence();
        Assert.assertEquals("d1", consumed);
        Assert.assertEquals(12, reader.pos());

        consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("efg5", consumed);
        Assert.assertEquals(15, reader.pos());

        consumed = reader.consumeHexSequence();
        Assert.assertEquals("678", consumed);
        Assert.assertEquals(17, reader.pos());

        consumed = reader.consumeDigitSequence();
        Assert.assertEquals("5678", consumed);
        Assert.assertEquals(17, reader.pos());

        // test matches
        Assert.assertTrue(reader.matches('d'));
        Assert.assertTrue(reader.matches("efg"));
        Assert.assertFalse(reader.matches('z'));

        // test matchesIgnoreCase
        Assert.assertTrue(reader.matchesIgnoreCase("efg"));
        Assert.assertTrue(reader.matchesIgnoreCase("Efg"));
        Assert.assertFalse(reader.matchesIgnoreCase("XYZ"));

        // test matchesAny
        Assert.assertTrue(reader.matchesAny('d'));
        Assert.assertTrue(reader.matchesAny("efg"));
        Assert.assertTrue(reader.matchesAny(new char[]{'5', '6'}));
        Assert.assertFalse(reader.matchesAny('z'));

        // test matchesAnySorted
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'5', '6'}));
        Assert.assertTrue(reader.matchesAnySorted("efg".toCharArray()));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x', 'y'}));

        // test matchesLetter
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter('C'));
        Assert.assertFalse(reader.matchesLetter('5'));

        // test matchesDigit
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit('5'));
        Assert.assertFalse(reader.matchesDigit('C'));

        // test matchConsume
        String consumedMatch = reader.matchConsume("efg");
        Assert.assertEquals("efg", consumedMatch);
        Assert.assertEquals(12, reader.pos());

        consumedMatch = reader.matchConsumeIgnoreCase("efg");
        Assert.assertEquals("efg", consumedMatch);
        Assert.assertEquals(12, reader.pos());

        // test containsIgnoreCase
        Assert.assertTrue(reader.containsIgnoreCase("ABC"));
        Assert.assertTrue(reader.containsIgnoreCase("abc"));
        Assert.assertFalse(reader.containsIgnoreCase("XYZ"));

        // test toString
        Assert.assertEquals("efghABC5678", reader.toString());
    }
}