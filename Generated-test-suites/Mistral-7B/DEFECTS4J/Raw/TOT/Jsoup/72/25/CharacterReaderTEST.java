package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest.java
 */
public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader(new StringReader("This is a test string."));
    }

    @Test
    public void testPos() {
        Assert.assertEquals(0, reader.pos());
        reader.consume();
        Assert.assertEquals(1, reader.pos());
        reader.consumeToEnd();
        Assert.assertEquals(13, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Assert.assertEquals('T', reader.current());
        reader.consume();
        Assert.assertEquals('h', reader.current());
    }

    @Test
    public void testConsume() {
        char c = reader.consume();
        Assert.assertEquals('T', c);
        Assert.assertEquals('h', reader.current());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        Assert.assertEquals('h', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('T', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        Assert.assertEquals(6, reader.nextIndexOf('s'));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfSequence() {
        Assert.assertEquals(6, reader.nextIndexOf("is"));
        Assert.assertEquals(-1, reader.nextIndexOf("isnot"));
    }

    @Test
    public void testConsumeTo() {
        String str = reader.consumeTo('.');
        Assert.assertEquals("This is a ", str);
    }

    @Test
    public void testConsumeToEnd() {
        String str = reader.consumeToEnd();
        Assert.assertEquals("This is a test string.", str);
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp();
        reader.reader.close();
        reader.bufferUp();
    }

    @Test
    public void testConsumeLetterSequence() {
        String str = reader.consumeLetterSequence();
        Assert.assertEquals("is", str);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String str = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("is5", str);
    }

    @Test
    public void testConsumeHexSequence() {
        String str = reader.consumeHexSequence();
        Assert.assertEquals("is", str);
    }

    @Test
    public void testConsumeDigitSequence() {
        String str = reader.consumeDigitSequence();
        Assert.assertEquals("5", str);
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(reader.matches('t'));
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesSequence() {
        Assert.assertTrue(reader.matches("is"));
        Assert.assertFalse(reader.matches("isnot"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertTrue(reader.matchesIgnoreCase("is"));
        Assert.assertTrue(reader.matchesIgnoreCase("IS"));
        Assert.assertFalse(reader.matchesIgnoreCase("isnot"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(reader.matchesAny('s', 'i'));
        Assert.assertFalse(reader.matchesAny('z', 'i'));
    }

    @Test
    public void testMatchesAnySorted() {
        Assert.assertTrue(reader.matchesAnySorted('i', 's'));
        Assert.assertFalse(reader.matchesAnySorted('z', 'i'));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit('5'));
    }

    @Test
    public void testMatchConsumeSequence() {
        reader.matchConsume("is");
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals(' '
                , reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCaseSequence() {
        reader.matchConsumeIgnoreCase("Is");
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals(' '
                , reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(reader.containsIgnoreCase("is"));
        Assert.assertFalse(reader.containsIgnoreCase("isnot"));
    }
}