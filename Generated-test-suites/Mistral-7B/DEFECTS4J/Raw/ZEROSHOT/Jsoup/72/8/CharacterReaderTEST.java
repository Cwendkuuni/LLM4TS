package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() throws IOException {
        String input = "This is a test for CharacterReader.";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // test basic methods
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());

        // test advance and current
        reader.advance();
        Assert.assertEquals(EOF, reader.current());
        reader.advance();
        Assert.assertEquals('T', reader.current());
        Assert.assertEquals('T', reader.consume());

        // test consumeTo and consumeToEnd
        String consumed = reader.consumeTo(' ');
        Assert.assertEquals("This ", consumed);
        consumed = reader.consumeToEnd();
        Assert.assertEquals(input, consumed);

        // test nextIndexOf
        Assert.assertEquals(0, reader.nextIndexOf('T'));
        Assert.assertEquals(6, reader.nextIndexOf('s'));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));

        // test mark and rewindToMark
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('T', reader.current());

        // test consumeToAny and consumeToAnySorted
        String consumedAny = reader.consumeToAny(new char[]{' ', 's'});
        Assert.assertEquals("This ", consumedAny);
        consumedAny = reader.consumeToAnySorted(new char[]{'s', ' ', 'T'});
        Assert.assertEquals("This ", consumedAny);

        // test consumeData, consumeTagName and consumeToEnd
        String data = reader.consumeData();
        Assert.assertEquals("is a test for CharacterReader.", data);
        data = reader.consumeTagName();
        Assert.assertEquals("is a test ", data);
        data = reader.consumeToEnd();
        Assert.assertEquals(input, data);

        // test consumeLetterSequence and consumeLetterThenDigitSequence
        String sequence = reader.consumeLetterSequence();
        Assert.assertEquals("is a test", sequence);
        sequence = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("is a test0", sequence);

        // test matches and matchesIgnoreCase
        Assert.assertTrue(reader.matches('T'));
        Assert.assertTrue(reader.matches("is"));
        Assert.assertFalse(reader.matches("is not"));
        Assert.assertTrue(reader.matchesIgnoreCase("IS"));

        // test matchesAny and matchesAnySorted
        Assert.assertTrue(reader.matchesAny(new char[]{' ', 's'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'s', ' ', 'T'}));

        // test matchesLetter and matchesDigit
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesDigit());

        // test matchConsume and matchConsumeIgnoreCase
        String tagName = reader.matchConsume("is a ");
        Assert.assertEquals("test", tagName);
        tagName = reader.matchConsumeIgnoreCase("IS a ");
        Assert.assertEquals("test", tagName);

        // test containsIgnoreCase
        Assert.assertTrue(reader.containsIgnoreCase("is"));
        Assert.assertTrue(reader.containsIgnoreCase("a"));
        Assert.assertFalse(reader.containsIgnoreCase("test"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        String input = "This is a test for CharacterReader.";
        CharacterReader reader = new CharacterReader(new StringReader(input));
        reader.reader.close();
        reader.consume();
    }
}