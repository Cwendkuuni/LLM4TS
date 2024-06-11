package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithInput() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithInputAndSize() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"), 10);
        Assert.assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consumeTo('W');
        Assert.assertEquals("Hello", characterReader.toString());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionThrownWhenBufferingFails() {
        CharacterReader characterReader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] c, int off, int len) throws IOException {
                throw new IOException();
            }
        });

        characterReader.bufferUp();
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(0, characterReader.pos());

        characterReader.consume('H');
        Assert.assertEquals(1, characterReader.pos());

        characterReader.consumeTo('W');
        Assert.assertEquals(7, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(characterReader.isEmpty());

        characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', characterReader.current());

        characterReader.consume();
        Assert.assertEquals('H', characterReader.current());

        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consume();
        Assert.assertEquals('H', characterReader.current());

        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());

        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
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
        Assert.assertEquals('H', characterReader.current());

        characterReader.consume();
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
        Assert.assertEquals(1, characterReader.nextIndexOf('a'));
        Assert.assertEquals(5, characterReader.nextIndexOf('e'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("xyz"));
        Assert.assertEquals(1, characterReader.nextIndexOf("ab"));
        Assert.assertEquals(5, characterReader.nextIndexOf("ef"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello", characterReader.consumeTo('W').toString());
        Assert.assertEquals("World", characterReader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeToWithSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello", characterReader.consumeTo("World").toString());
        Assert.assertEquals("World", characterReader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World<></title>"));
        Assert.assertEquals("Hello World<></title>", characterReader.toString());
        Assert.assertEquals("World<></title>", characterReader.consumeToAny(new char[]{'<', '>', '/', ' '}).toString());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World<></title>"));
        Assert.assertEquals("Hello World<></title>", characterReader.toString());
        Assert.assertEquals("World<></title>", characterReader.consumeToAnySorted(new char[]{'<', '>', '/', ' '}).toString());
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;Hello World"));
        Assert.assertEquals("&lt;", characterReader.consumeData().toString());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<title>Hello World"));
        Assert.assertEquals("<title>", characterReader.consumeTagName().toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", characterReader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello", characterReader.consumeLetterSequence().toString());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123World"));
        Assert.assertEquals("Hello123", characterReader.consumeLetterThenDigitSequence().toString());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertTrue(characterReader.matches("ld"));
        Assert.assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("ld"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAny(new char[]{'l', 'd'}));
        Assert.assertFalse(characterReader.matchesAny(new char[]{'z'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'd'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter('2'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit('2'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("ld", characterReader.matchConsume("ld").toString());
        Assert.assertEquals("World", characterReader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("ld", characterReader.matchConsumeIgnoreCase("ld").toString());
        Assert.assertEquals("World", characterReader.toString());
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", characterReader.cacheString(characterReader.charBuf, characterReader.stringCache, 0, 26).toString());
        Assert.assertEquals("xyz", characterReader.cacheString(characterReader.charBuf, characterReader.stringCache, 25, 3).toString());
    }

    @Test
    public void testRangeEquals() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertTrue(characterReader.rangeEquals(0, 26, "abcdefghijklmnopqrstuvwxyz"));
        Assert.assertFalse(characterReader.rangeEquals(0, 26, "1234567890"));
    }
}