package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest.java
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Testing constructor with String input
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.toString());
        Assert.assertEquals(11, reader.pos());
        Assert.assertFalse(reader.isEmpty());

        // Testing consume method
        Assert.assertEquals('H', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals(9, reader.pos());
        Assert.assertFalse(reader.isEmpty());

        // Testing unconsume method
        reader.unconsume();
        Assert.assertEquals('e', reader.current());

        // Testing advance method
        reader.advance();
        Assert.assertEquals('l', reader.current());

        // Testing mark and rewindToMark method
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('e', reader.current());

        // Testing nextIndexOf method
        Assert.assertEquals(5, reader.nextIndexOf('l'));
        Assert.assertEquals(-1, reader.nextIndexOf('Z'));

        // Testing consumeTo method
        String text = reader.consumeTo(' ');
        Assert.assertEquals("llo", text);
        Assert.assertEquals(8, reader.pos());

        // Testing consumeToAny method
        String text2 = reader.consumeToAny(new char[]{'l', 'o'});
        Assert.assertEquals("llo", text2);

        // Testing consumeToAnySorted method
        String text3 = reader.consumeToAnySorted(new char[]{'l', 'o'});
        Assert.assertEquals("llo", text3);

        // Testing consumeData method
        String text4 = reader.consumeData();
        Assert.assertEquals("World", text4);

        // Testing consumeTagName method
        reader.consumeLetterSequence();
        Assert.assertEquals("lloWorld", reader.toString());
        String tagName = reader.consumeTagName();
        Assert.assertEquals("World", tagName);

        // Testing consumeToEnd method
        String remainingText = reader.consumeToEnd();
        Assert.assertEquals("", remainingText);
        Assert.assertEquals(11, reader.pos());

        // Testing exceptions
        try {
            CharacterReader reader2 = new CharacterReader(null);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            CharacterReader reader3 = new CharacterReader(new StringReader(""));
            reader3.consume();
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            CharacterReader reader4 = new CharacterReader(new StringReader(""));
            reader4.consumeToAny(new char[]{'l', 'o'});
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            CharacterReader reader5 = new CharacterReader(new StringReader(""));
            reader5.consumeToAnySorted(new char[]{'l', 'o'});
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }
}