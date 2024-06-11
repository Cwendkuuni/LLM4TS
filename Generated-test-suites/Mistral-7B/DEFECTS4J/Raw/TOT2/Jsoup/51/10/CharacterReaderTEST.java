package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertNull(reader.consume());
    }

    @Test
    public void testSimpleReader() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.consume());
        reader.unconsume();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeString() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeTo('f');
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals('f', reader.current());
    }

    @Test
    public void testConsumeSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeTo("def");
        Assert.assertEquals("abcdef", consumed);
        Assert.assertEquals(' ', reader.current());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeNullSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consumeTo(null);
    }

    @Test
    public void testConsumeChar() {
        CharacterReader reader = new CharacterReader("abcdef");
        char consumed = reader.consume();
        Assert.assertEquals('a', consumed);
        consumed = reader.consume();
        Assert.assertEquals('b', consumed);
        consumed = reader.consume();
        Assert.assertEquals('c', consumed);
    }

    @Test
    public void testConsumeEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("abcdef", consumed);
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        String str1 = reader.cacheString(0, 3);
        String str2 = reader.cacheString(3, 3);
        String str3 = reader.cacheString(6, 3);
        String str4 = reader.cacheString(0, 7);
        Assert.assertEquals("abc", str1);
        Assert.assertEquals("abcd", str2);
        Assert.assertEquals("abcd", str3);
        Assert.assertEquals("abcdabcd", str4);
    }

    @Test
    public void testInvalidInput() {
        CharacterReader reader = new CharacterReader("\uD800\uDC00"); // surrogate pair
        Assert.assertFalse(reader.isEmpty());
        reader.consume(); // consume first surrogate
        try {
            reader.consume(); // should throw an exception
                // JUnit should not allow the test to continue if an exception is thrown
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}