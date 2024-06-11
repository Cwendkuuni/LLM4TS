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
        char[] expectedChars = {'h', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
        String expectedString = new String(expectedChars);

        CharacterReader reader = new CharacterReader(new StringReader(expectedString));

        // Test basic methods
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(expectedChars[0], reader.current());
        reader.consume();
        Assert.assertEquals(expectedChars[1], reader.current());
        reader.unconsume();
        Assert.assertEquals(expectedChars[0], reader.current());
        reader.advance();
        Assert.assertEquals(expectedChars[1], reader.current());
        reader.mark();
        reader.rewindToMark();
        Assert.assertEquals(expectedChars[0], reader.current());

        // Test nextIndexOf methods
        Assert.assertEquals(4, reader.nextIndexOf(' '));
        Assert.assertEquals(10, reader.nextIndexOf('W'));
        Assert.assertEquals(-1, reader.nextIndexOf('X'));
        Assert.assertEquals(11, reader.nextIndexOf("ld"));

        // Test consumeTo methods
        String consumed = reader.consumeTo(' ');
        Assert.assertEquals("hello ", consumed);
        consumed = reader.consumeTo('W');
        Assert.assertEquals("hello World!", reader.toString());
        Assert.assertEquals("W", reader.current().toString());

        // Test consume methods that read to end
        consumed = reader.consumeToEnd();
        Assert.assertEquals("World!", consumed);

        // Test consume methods that read specific sequences
        consumed = reader.consumeTagName();
        Assert.assertEquals("World", consumed);
        consumed = reader.consumeLetterSequence();
        Assert.assertEquals("ld", consumed);
        consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("ld1", consumed);
        consumed = reader.consumeHexSequence();
        Assert.assertEquals("ld", consumed);
        consumed = reader.consumeDigitSequence();
        Assert.assertEquals("1", consumed);

        // Test matches methods
        Assert.assertTrue(reader.matches(' '));
        Assert.assertTrue(reader.matches("ld"));
        Assert.assertTrue(reader.matches("World"));
        Assert.assertFalse(reader.matches("Xylophone"));
        Assert.assertFalse(reader.matchesIgnoreCase("xylophone"));

        // Test matchesAny methods
        Assert.assertTrue(reader.matchesAny(' '));
        Assert.assertTrue(reader.matchesAny("ld"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{' ', 'l'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x', 'z'}));

        // Test containsIgnoreCase method
        Assert.assertTrue(reader.containsIgnoreCase("ld"));
        Assert.assertTrue(reader.containsIgnoreCase("World"));

        // Test consuming all characters
        String allChars = reader.consumeToEnd();
        Assert.assertEquals("", reader.toString());
        Assert.assertEquals("World!", allChars);
    }
}