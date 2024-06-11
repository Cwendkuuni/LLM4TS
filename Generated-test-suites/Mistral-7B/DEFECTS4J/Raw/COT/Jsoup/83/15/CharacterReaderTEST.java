package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for CharacterReader.
 */
public final class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertNotNull(characterReader);
    }

    @Test
    public void testBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        Assert.assertEquals(13, characterReader.bufLength);
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionThrownByBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        try {
            characterReader.bufferUp();
            characterReader.bufferUp(); // This should throw an IOException, but it's wrapped in an UncheckedIOException
        } catch (IOException e) {
            throw new AssertionError("IOException should be wrapped in an UncheckedIOException");
        }
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(characterReader.isEmpty());

        characterReader.bufferUp(); // consume first character
        Assert.assertTrue(characterReader.isEmptyNoBufferUp());
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.mark();
        characterReader.bufferUp();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(6, characterReader.nextIndexOf('l'));
        Assert.assertEquals(7, characterReader.nextIndexOf('o'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(6, characterReader.nextIndexOf("ll"));
        Assert.assertEquals(7, characterReader.nextIndexOf("oo"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("xx"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeTo('l');
        Assert.assertEquals("He", consumed);

        consumed = characterReader.consumeTo("ll");
        Assert.assertEquals("Hello", consumed);

        consumed = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World", consumed);
    }

    @Test
    public void testConsumeToWithEmptyString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeTo("");
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeToAny(new char[]{'l', 'o'});
        Assert.assertEquals("Hello", consumed);

        consumed = characterReader.consumeToAnySorted(new char[]{'o', 'l'});
        Assert.assertEquals("Hello", consumed);
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;html&gt;"));
        String consumed = characterReader.consumeData();
        Assert.assertEquals("&lt;html", consumed);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html>"));
        String consumed = characterReader.consumeTagName();
        Assert.assertEquals("html", consumed);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World", consumed);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeLetterSequence();
        Assert.assertEquals("Hello", consumed);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123World"));
        String consumed = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", consumed);
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertTrue(characterReader.matches("ll"));
        Assert.assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("ll"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("world"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("X"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAny('l'));
        Assert.assertTrue(characterReader.matchesAny('o'));
        Assert.assertFalse(characterReader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'o'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'x', 'l'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123World"));
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.matchConsume("ll");
        Assert.assertEquals("ll", consumed);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.matchConsumeIgnoreCase("ll");
        Assert.assertEquals("ll", consumed);

        consumed = characterReader.matchConsumeIgnoreCase("World");
        Assert.assertEquals("World", consumed);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>My Title</title></head><body>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("body"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/header"));
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String toString = characterReader.toString();
        Assert.assertEquals("Hello World", toString);
    }
}