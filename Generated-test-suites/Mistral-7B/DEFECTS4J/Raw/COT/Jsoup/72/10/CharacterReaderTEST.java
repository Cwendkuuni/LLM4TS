package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    private static final char EOF = (char) -1;

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader input = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        String input = "Test String";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertNotNull(characterReader);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(characterReader.isEmpty());

        characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals(0, characterReader.pos());

        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());

        characterReader.consume();
        Assert.assertEquals(2, characterReader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpWithIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String")) {
            @Override
            void bufferUp() {
                throw new IOException("Test IOException");
            }
        };
        characterReader.consume();
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals('T', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('s', characterReader.current());
        Assert.assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals('T', characterReader.consume());
        Assert.assertEquals('e', characterReader.consume());
        Assert.assertEquals('s', characterReader.consume());
        Assert.assertEquals(EOF, characterReader.consume());

        characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        Assert.assertEquals('T', characterReader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());

        characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        Assert.assertEquals('T', characterReader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        Assert.assertEquals('T', characterReader.current());

        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        Assert.assertEquals('T', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
        Assert.assertEquals(1, characterReader.nextIndexOf('e'));
        Assert.assertEquals(6, characterReader.nextIndexOf('S'));

        characterReader.consume();
        Assert.assertEquals(5, characterReader.nextIndexOf('S'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test Test"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("abc"));
        Assert.assertEquals(0, characterReader.nextIndexOf("Test"));
        Assert.assertEquals(6, characterReader.nextIndexOf("Test"));

        characterReader.consume();
        Assert.assertEquals(5, characterReader.nextIndexOf("Test"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals("Test", characterReader.consumeTo(' '));
        Assert.assertEquals("String", characterReader.consumeToEnd());

        characterReader = new CharacterReader(new StringReader("Test Test Test"));
        Assert.assertEquals("Test Test", characterReader.consumeTo(' '));
        Assert.assertEquals("Test Test Test", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToWithEmptySequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals("", characterReader.consumeToAny());
        Assert.assertEquals("Test String", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals("String", characterReader.consumeToAny(
                's', 't', 'r', 'i', 'n', 'g'
        ));
        Assert.assertEquals("Test String", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals("Test", characterReader.consumeToAnySorted(
                's', 't', 'r', 'i', 'n', 'g'
        ));
        Assert.assertEquals("Test String", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test & < null"));
        Assert.assertEquals("Test ", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<title>Test Title</title>"));
        Assert.assertEquals("<title>", characterReader.consumeTagName());
        Assert.assertEquals("Test Title</title>", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals("Test String", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals("Test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123 String"));
        Assert.assertEquals("Test123", characterReader.consumeLetterThenDigitSequence());
        Assert.assertEquals(" String", characterReader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(characterReader.matches('t'));
        Assert.assertTrue(characterReader.matches("Test"));
        Assert.assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("test"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("Test"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(characterReader.matchesAny('t', 'e', 's'));
        Assert.assertFalse(characterReader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(characterReader.matchesAnySorted('t', 'e', 's'));
        Assert.assertFalse(characterReader.matchesAnySorted('z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123 String"));
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String Test String"));
        Assert.assertTrue(characterReader.matchConsume("Test"));
        Assert.assertEquals("String Test String", characterReader.consumeToEnd());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String Test String"));
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("test"));
        Assert.assertEquals("String Test String", characterReader.consumeToEnd());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test Title</title></head>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("title"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/head"));
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals("Test String", characterReader.toString());
    }
}