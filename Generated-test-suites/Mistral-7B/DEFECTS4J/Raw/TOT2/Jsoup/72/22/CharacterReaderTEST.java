package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        String testInput = "This is a test for CharacterReader.\n" +
                "It should be able to read characters from various inputs.\n" +
                "Let's test some methods.";
        reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(testInput.getBytes())));
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(reader);
    }

    @Test
    public void testPos() {
        Assert.assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        reader.advance();
        Assert.assertEquals(3, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(reader.isEmpty());
        reader.consume();
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Assert.assertEquals('T', reader.current());
        reader.consume();
        Assert.assertEquals('h', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        Reader input = new InputStreamReader(new ByteArrayInputStream(new byte[0]));
        CharacterReader reader = new CharacterReader(input, 10);
        reader.bufferUp();
        reader.reader.close();
        reader.consume();
    }

    @Test
    public void testConsume() {
        reader.consume();
        Assert.assertEquals('T', reader.current());
        reader.consume();
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
        Assert.assertEquals(6, reader.nextIndexOf(' '));
        Assert.assertEquals(-1, reader.nextIndexOf('Z'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        Assert.assertEquals(11, reader.nextIndexOf("is a test for"));
        Assert.assertEquals(-1, reader.nextIndexOf("test for CharacterReader"));
    }

    @Test
    public void testConsumeTo() {
        String expected = "This is a test for CharacterReader.";
        Assert.assertEquals(expected, reader.consumeTo('.'));
        Assert.assertEquals("It should be able to read characters from various inputs.\nLet's test some methods.", reader.consumeTo('\n'));
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        expected += "\nIt's working.";
        Assert.assertEquals(expected, reader.consumeToEnd());
    }

    @Test
    public void testConsumeToWithSequence() {
        String expected = "It should be able to read characters from various inputs.\nLet's test some methods.";
        Assert.assertEquals(expected, reader.consumeTo("Let's test some methods."));
    }

    @Test
    public void testConsumeToAny() {
        Assert.assertEquals("This is a test for CharacterReader.\n", reader.consumeToAny('.'));
        Assert.assertEquals("This is a test for CharacterReader.\nIt should be able to read characters from various inputs.\n", reader.consumeToAnySorted(new char[]{' ', '\n'}));
    }

    @Test
    public void testConsumeData() {
        String expected = "&, <, null";
        Assert.assertEquals(expected, reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        String expected = "\t\n\r\f ";
        Assert.assertEquals(expected, reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "It's working.";
        Assert.assertEquals(expected, reader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(reader.matches('h'));
        Assert.assertTrue(reader.matches("CharacterReader"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertTrue(reader.matchesIgnoreCase("characterreader"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(reader.matchesAny(new char[]{'h', 't'}));
    }

    @Test
    public void testMatchesAnySorted() {
        Assert.assertTrue(reader.matchesAnySorted(new char[]{' ', '\n'}));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String testString = "CharacterReader.";
        reader.matchConsume(testString);
        Assert.assertEquals(testString.length(), reader.pos() - reader.readerPos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String testString = "characterreader.";
        reader.matchConsumeIgnoreCase(testString);
        Assert.assertEquals(testString.length(), reader.pos() - reader.readerPos());
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/style"));
    }
}