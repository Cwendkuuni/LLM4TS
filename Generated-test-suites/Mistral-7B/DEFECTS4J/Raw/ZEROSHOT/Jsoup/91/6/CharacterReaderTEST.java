package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * CharacterReaderTest class to test the CharacterReader methods.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstruction() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertNotNull(characterReader);
        characterReader = new CharacterReader(new ByteArrayInputStream("Test Byte Array".getBytes()), 10);
        Assert.assertNotNull(characterReader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderInvalidInput() {
        try {
            new CharacterReader(null, 10);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("input can not be null", e.getMessage());
        }

        try {
            new CharacterReader(new StringReader("Test String"), -1);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("sz must be > 0", e.getMessage());
        }
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals(0, characterReader.pos());
        characterReader.bufferUp();
        Assert.assertEquals(0, characterReader.isEmptyNoBufferUp());
        characterReader.consume();
        Assert.assertEquals(1, characterReader.pos());
        characterReader.bufferUp();
        Assert.assertEquals(1, characterReader.bufLength);
        Assert.assertEquals(2, characterReader.pos());
        characterReader.rewindToMark();
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testCharacterReaderCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
        characterReader.bufferUp();
        Assert.assertEquals('T', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.consume();
        Assert.assertEquals('T', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('s', characterReader.current());
    }

    @Test
    public void testCharacterReaderUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('T', characterReader.current());
    }

    @Test
    public void testCharacterReaderAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.advance();
        Assert.assertEquals('T', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('T', characterReader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals(-1, characterReader.nextIndexOf('X'));
        Assert.assertEquals(0, characterReader.nextIndexOf('T'));
        Assert.assertEquals(1, characterReader.nextIndexOf('e'));
        Assert.assertEquals(5, characterReader.nextIndexOf('S'));
        characterReader.bufferUp();
        characterReader.consume();
        characterReader.consume();
        Assert.assertEquals(3, characterReader.nextIndexOf('t'));
    }

    @Test
    public void testCharacterReaderNextIndexOfSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("Test"));
        Assert.assertEquals(0, characterReader.nextIndexOf("Test", 5));
        characterReader.bufferUp();
        characterReader.consume();
        characterReader.consume();
        Assert.assertEquals(4, characterReader.nextIndexOf("es", 5));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        String value = characterReader.consumeTo('S');
        Assert.assertEquals("Test", value);
        characterReader.bufferUp();
        characterReader.consume();
        value = characterReader.consumeTo('g');
        Assert.assertEquals("est", value);
    }

    @Test
    public void testCharacterReaderConsumeToSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        String value = characterReader.consumeToSequence("es");
        Assert.assertEquals("Test", value);
        characterReader.bufferUp();
        characterReader.consume();
        characterReader.consume();
        value = characterReader.consumeToSequence("String");
        Assert.assertEquals("Test String", value);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        String value = characterReader.consumeToAny('X');
        Assert.assertEquals("", value);
        characterReader.bufferUp();
        characterReader.consume();
        value = characterReader.consumeToAny('e');
        Assert.assertEquals("Test", value);
        value = characterReader.consumeToAny('s');
        Assert.assertEquals("Test", value);
        value = characterReader.consumeToAny('t');
        Assert.assertEquals("Test", value);
        value = characterReader.consumeToAny('r');
        Assert.assertEquals("Test", value);
        value = characterReader.consumeToAny('i');
        Assert.assertEquals("Test", value);
        value = characterReader.consumeToAny('n');
        Assert.assertEquals("Test", value);
        value = characterReader.consumeToAny('g');
        Assert.assertEquals("Test", value);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test & < String"));
        String value = characterReader.consumeData();
        Assert.assertEquals("Test & <", value);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><title>Test</title>"));
        String value = characterReader.consumeTagName();
        Assert.assertEquals("html", value);
        value = characterReader.consumeTagName();
        Assert.assertEquals("title", value);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        String value = characterReader.consumeToEnd();
        Assert.assertEquals("Test String", value);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        String value = characterReader.consumeLetterSequence();
        Assert.assertEquals("Test", value);
        characterReader.bufferUp();
        characterReader.consume();
        value = characterReader.consumeLetterSequence();
        Assert.assertEquals("String", value);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123String"));
        String value = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Test", value);
        value = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("123", value);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test&#x61;&#x6C;&#x65;String"));
        String value = characterReader.consumeHexSequence();
        Assert.assertEquals("Test", value);
        value = characterReader.consumeHexSequence();
        Assert.assertEquals("abc", value);
        value = characterReader.consumeHexSequence();
        Assert.assertEquals("String", value);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123String"));
        String value = characterReader.consumeDigitSequence();
        Assert.assertEquals("123", value);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test Test Test"));
        Assert.assertTrue(characterReader.matches('T'));
        Assert.assertTrue(characterReader.matches(' '));
        Assert.assertTrue(characterReader.matches('e'));
        Assert.assertFalse(characterReader.matches('X'));
    }

    @Test
    public void testCharacterReaderMatchesSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test Test Test"));
        Assert.assertTrue(characterReader.matchesSequence("Te"));
        Assert.assertTrue(characterReader.matchesSequence("es"));
        Assert.assertTrue(characterReader.matchesSequence("Test"));
        Assert.assertFalse(characterReader.matchesSequence("TestX"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(characterReader.matchesAny('S'));
        Assert.assertTrue(characterReader.matchesAny('t'));
        Assert.assertTrue(characterReader.matchesAny('r'));
        Assert.assertTrue(characterReader.matchesAny('i'));
        Assert.assertTrue(characterReader.matchesAny('n'));
        Assert.assertTrue(characterReader.matchesAny('g'));
        Assert.assertFalse(characterReader.matchesAny('X'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(characterReader.matchesAnySorted('g'));
        Assert.assertTrue(characterReader.matchesAnySorted('i'));
        Assert.assertTrue(characterReader.matchesAnySorted('n'));
        Assert.assertTrue(characterReader.matchesAnySorted('r'));
        Assert.assertTrue(characterReader.matchesAnySorted('s'));
        Assert.assertTrue(characterReader.matchesAnySorted('t'));
        Assert.assertFalse(characterReader.matchesAnySorted('X'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.bufferUp();
        characterReader.consume();
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123String"));
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.bufferUp();
        characterReader.consume();
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsumeSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        boolean result = characterReader.matchConsumeSequence("Test");
        Assert.assertTrue(result);
        Assert.assertEquals(5, characterReader.pos());
        result = characterReader.matchConsumeSequence("String");
        Assert.assertTrue(result);
        Assert.assertEquals(11, characterReader.pos());
    }

    @Test
    public void testCharacterReaderMatchConsumeSequenceIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        boolean result = characterReader.matchConsumeSequenceIgnoreCase("test");
        Assert.assertTrue(result);
        Assert.assertEquals(5, characterReader.pos());
        result = characterReader.matchConsumeSequenceIgnoreCase("string");
        Assert.assertTrue(result);
        Assert.assertEquals(11, characterReader.pos());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test <title>String</title>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("title"));
        Assert.assertFalse(characterReader.containsIgnoreCase("Title"));
    }
}