package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertNotNull(reader);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertNotNull(reader);
    }

    @Test
    public void testCharacterReaderConstructorWithReaderAndSize() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), 10);
        Assert.assertNotNull(reader);
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionThrownWhenBufferUpFails() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), 10);
        reader.bufferUp();
        reader.reader.close();
        reader.bufferUp();
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(0, reader.pos());
        reader.consume();
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.consume();
        Assert.assertEquals('e', reader.current());
        reader.consume();
        Assert.assertEquals('l', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('H', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(-1, reader.nextIndexOf('x'));
        Assert.assertEquals(6, reader.nextIndexOf('l'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(6, reader.nextIndexOf("ll"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("ello", reader.consumeTo('l'));
        Assert.assertEquals("ld", reader.consumeTo('d'));
        Assert.assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeToWithSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("llo", reader.consumeTo("ll"));
        Assert.assertEquals("ld", reader.consumeTo("d"));
        Assert.assertEquals("", reader.consumeTo("z"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("World", reader.consumeToAny(new char[]{'l', 'o', 'r', 'd'}));
        Assert.assertEquals("", reader.consumeToAnySorted(new char[]{'z', 'd'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("World", reader.consumeToAnySorted(new char[]{'l', 'o', 'r', 'd'}));
        Assert.assertEquals("", reader.consumeToAnySorted(new char[]{'z', 'd'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;html&gt;<head>"));
        Assert.assertEquals("<html", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head>"));
        Assert.assertEquals("html", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("World", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello123 World456"));
        Assert.assertEquals("Hello123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matches('e'));
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matches("ef"));
        Assert.assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesIgnoreCase("ef"));
        Assert.assertTrue(reader.matchesIgnoreCase("ef"));
        Assert.assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesAny('e', 'f'));
        Assert.assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'e', 'f'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter('1'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsumeSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchConsume("ef"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ef"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("EF"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertTrue(reader.containsIgnoreCase("TITLE"));
        Assert.assertFalse(reader.containsIgnoreCase("body"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals("abcdef", reader.toString());
    }
}