package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderInstance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderInstanceWithSize() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"), 10);
        Assert.assertNotNull(characterReader);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        CharacterReader characterReader = new CharacterReader(null, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"), 10);
        characterReader.mark();
        characterReader.rewindToMark();
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        int pos = characterReader.pos();
        Assert.assertEquals(0, pos);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals('T', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals('T', characterReader.consume());
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('T', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals(-1, characterReader.nextIndexOf('Z'));
        Assert.assertEquals(1, characterReader.nextIndexOf('e'));
    }

    @Test
    public void testNextIndexOfWithSeq() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("Test"));
        Assert.assertEquals(4, characterReader.nextIndexOf("String"));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("Test String", consumed);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        String consumed = characterReader.consumeTo('t');
        Assert.assertEquals("Test", consumed);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        String consumed = characterReader.consumeToAny(new char[]{'s', 't', 'r', 'i', 'n', 'g'});
        Assert.assertEquals("Test", consumed);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        String consumed = characterReader.consumeToAnySorted(new char[]{'s', 't', 'r', 'i', 'n', 'g'});
        Assert.assertEquals("Test", consumed);
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test & String"));
        String consumed = characterReader.consumeData();
        Assert.assertEquals("Test ", consumed);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html>"));
        String consumed = characterReader.consumeTagName();
        Assert.assertEquals("html", consumed);
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String") {
            @Override
            public void close() throws IOException {
                throw new IOException("Test IOException");
            }
        });
        characterReader.consume();
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        String consumed = characterReader.consumeLetterSequence();
        Assert.assertEquals("Test", consumed);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123 String"));
        String consumed = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Test123", consumed);
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(characterReader.matches('t'));
        Assert.assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("string"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("TEST"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(characterReader.matchesAny('t', 's'));
        Assert.assertFalse(characterReader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'s', 't'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123 String"));
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test<String>"));
        Assert.assertTrue(characterReader.matchConsume("Test"));
        Assert.assertEquals("String", characterReader.consumeToEnd());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test<String>"));
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("test"));
        Assert.assertEquals("String", characterReader.consumeToEnd());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test String</title></head>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("title"));
        Assert.assertFalse(characterReader.containsIgnoreCase("test"));
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals("Test String", characterReader.toString());
    }
}