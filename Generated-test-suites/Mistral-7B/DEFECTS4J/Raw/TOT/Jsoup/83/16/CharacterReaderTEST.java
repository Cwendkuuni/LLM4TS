package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

public final class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        String input = "This is a test for CharacterReader class.";
        reader = new CharacterReader(new StringReader(input));
    }

    @Test
    public void testReaderInitialization() {
        Assert.assertNotNull(reader);
        Assert.assertNotNull(reader.charBuf);
        Assert.assertNotNull(reader.reader);
        Assert.assertEquals(maxBufferLen, reader.charBuf.length);
    }

    @Test
    public void testPos() {
        int initialPosition = reader.pos();
        reader.consume();
        reader.consume();
        Assert.assertEquals(initialPosition + 2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        char initialChar = reader.current();
        reader.consume();
        Assert.assertEquals(initialChar, reader.current());
        reader.consume();
        Assert.assertEquals(EOF, reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        StringReader readerInput = new StringReader("Test input");
        CharacterReader reader = new CharacterReader(readerInput, 1);
        reader.bufferUp();
        reader.reader.close();
        reader.consume();
    }

    @Test
    public void testConsume() {
        char initialChar = reader.current();
        reader.consume();
        Assert.assertEquals(initialChar, reader.current());
        reader.consume();
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        Assert.assertEquals(reader.current(), reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        Assert.assertEquals(reader.pos(), reader.pos() + 1);
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals(reader.pos(), reader.pos() - 1);
    }

    @Test
    public void testNextIndexOf() {
        reader.consumeTo('t');
        Assert.assertEquals(reader.nextIndexOf('t'), 1);
        reader.consumeToEnd();
        Assert.assertEquals(-1, reader.nextIndexOf('t'));
    }

    @Test
    public void testNextIndexOfWithString() {
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        Assert.assertEquals(reader.nextIndexOf("hextest"), 11);
        reader.consumeToEnd();
        Assert.assertEquals(-1, reader.nextIndexOf("hextest"));
    }

    @Test
    public void testConsumeTo() {
        String str = reader.consumeTo('e');
        Assert.assertEquals("his", str);
        reader.consumeToEnd();
        Assert.assertEquals(EOF, reader.current());

        String str2 = reader.consumeTo("extit");
        Assert.assertEquals("hisisit", str2);
        reader.consumeToEnd();
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeToAny() {
        String str = reader.consumeToAny(',', '.');
        Assert.assertEquals("his,is.it", str);
        reader.consumeToEnd();
        Assert.assertEquals(EOF, reader.current());

        String str2 = reader.consumeToAnySorted('a', 'z');
        Assert.assertEquals("hisisit", str2);
    }

    @Test
    public void testConsumeTagName() {
        String str = reader.consumeTagName();
        Assert.assertEquals("This", str);
        reader.consumeToEnd();
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeData() {
        String str = reader.consumeData();
        Assert.assertEquals("This is a test for CharacterReader class.", str);
        reader.consumeToEnd();
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(reader.matches('t'));
        reader.consumeToEnd();
        Assert.assertFalse(reader.matches('t'));
    }

    @Test
    public

    void testMatchesIgnoreCase() {
        Assert.assertTrue(reader.matchesIgnoreCase("is"));
        reader.consumeToEnd();
        Assert.assertFalse(reader.matchesIgnoreCase("is"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(reader.matchesAny('h', 'i'));
        reader.consumeToEnd();
        Assert.assertFalse(reader.matchesAny('h', 'i'));
    }

    @Test
    public void testMatchesAnySorted() {
        Assert.assertTrue(reader.matchesAnySorted('h', 'i'));
        reader.consumeToEnd();
        Assert.assertFalse(reader.matchesAnySorted('h', 'i'));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(reader.matchesLetter());
        reader.consumeToEnd();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertTrue(reader.matchesDigit());
        reader.consumeToEnd();
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String str = reader.consumeTo('t');
        Assert.assertEquals("his", str);
        Assert.assertTrue(reader.matchConsume("is"));
        Assert.assertEquals("it", reader.consumeToEnd());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String str = reader.consumeTo('t');
        Assert.assertEquals("his", str);
        Assert.assertTrue(reader.matchConsumeIgnoreCase("is"));
        Assert.assertEquals("it", reader.consumeToEnd());
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(reader.containsIgnoreCase("is"));
        reader.consumeToEnd();
        Assert.assertFalse(reader.containsIgnoreCase("is"));
    }
}