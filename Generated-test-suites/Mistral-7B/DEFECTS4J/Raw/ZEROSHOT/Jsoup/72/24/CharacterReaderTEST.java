package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderCreation() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertNotNull(characterReader);
        CharacterReader characterReader1 = new CharacterReader(new StringReader(""));
        Assert.assertNotNull(characterReader1);
        CharacterReader characterReader2 = new CharacterReader(new StringReader(""));
        Assert.assertSame(characterReader1, characterReader2);
        CharacterReader characterReader3 = new CharacterReader(new StringReader("Test"));
        Assert.assertNotSame(characterReader1, characterReader3);
    }

    @Test
    public void testCharacterReaderPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(0, characterReader.pos());
        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());
        characterReader.consume();
        Assert.assertEquals(2, characterReader.pos());
        characterReader.rewindToMark();
        Assert.assertEquals(1, characterReader.pos());
        characterReader.consumeTo('l');
        Assert.assertEquals(5, characterReader.pos());
        characterReader.rewindToMark();
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(characterReader.isEmpty());
        characterReader.advance();
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consume();
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consumeToEnd();
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('o', characterReader.current());
        characterReader.consume();
        Assert.assertEquals(' ', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('W', characterReader.current());
        characterReader.consumeToEnd();
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', characterReader.consume());
        Assert.assertEquals('e', characterReader.current());
        Assert.assertEquals('l', characterReader.consume());
        Assert.assertEquals('l', characterReader.consume());
        Assert.assertEquals('o', characterReader.consume());
        Assert.assertEquals(' ', characterReader.consume());
        Assert.assertEquals('W', characterReader.consume());
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
        characterReader.rewindToMark();
        Assert.assertEquals('l', characterReader.consume());
        Assert.assertEquals('o', characterReader.consume());
        Assert.assertEquals(' ', characterReader.consume());
        Assert.assertEquals('W', characterReader.consume());
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testCharacterReaderUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testCharacterReaderAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        Assert.assertEquals('H', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCharacterReaderSetPosition() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.reader.seek(5);
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.mark();
        characterReader.advance();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(-1, characterReader.nextIndexOf('x'));
        Assert.assertEquals(7, characterReader.nextIndexOf('W'));
        Assert.assertEquals(5, characterReader.nextIndexOf(' '));
        characterReader.consumeTo('l');
        Assert.assertEquals(5, characterReader.nextIndexOf('l'));
        characterReader.consumeToEnd();
        Assert.assertEquals(-1, characterReader.nextIndexOf('l'));
    }

    @Test
    public void testCharacterReaderNextIndexOfSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("test"));
        characterReader.consumeTo('l');
        Assert.assertEquals(4, characterReader.nextIndexOf("ll"));
        characterReader.consumeToEnd();
        Assert.assertEquals(-1, characterReader.nextIndexOf("ll"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello", characterReader.consumeTo(' '));
        Assert.assertEquals("World", characterReader.consumeToEnd());
        characterReader.consumeTo('l');
        Assert.assertEquals("llo", characterReader.consumeTo('o'));
        characterReader.consumeToEnd();
        Assert.assertEquals("", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello", characterReader.consumeToSequence(" "));
        Assert.assertEquals("World", characterReader.consumeToEnd());
        characterReader.consumeTo('l');
        Assert.assertEquals("llo", characterReader.consumeToSequence("lo"));
        characterReader.consumeToEnd();
        Assert.assertEquals("", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("World", characterReader.consumeToAny('W'));
        Assert.assertEquals("o World", characterReader.consumeToAny('o'));
        Assert.assertEquals("Hello World", characterReader.consumeToAny());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("World", characterReader.consumeToAnySorted('W'));
        Assert.assertEquals("World", characterReader.consumeToAnySorted('o'));
        Assert.assertEquals("Hello World", characterReader.consumeToAnySorted());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;script src=\"script.js\"&gt;"));
        Assert.assertEquals("<script src=\"script.js\"", characterReader.consumeData());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<title>Test</title>"));
        Assert.assertEquals("title", characterReader.consumeTagName());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<title>Test</title>"));
        Assert.assertEquals("</title>", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello", characterReader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello1234World"));
        Assert.assertEquals("Hello1234", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&#x61;"));
        Assert.assertEquals("a", characterReader.consumeHexSequence());
        characterReader.consumeToEnd();
        Assert.assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("12345"));
        Assert.assertEquals("12345", characterReader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        // matches 'H'
        Assert.assertTrue(characterReader.matches('H'));
        // matches 'e'
        Assert.assertTrue(characterReader.matches('e'));
        // does not match 'x'
        Assert.assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testCharacterReaderMatchesSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        // matches "Hello"
        Assert.assertTrue(characterReader.matchesSequence("Hello"));
        // does not match "Hello World"
        Assert.assertFalse(characterReader.matchesSequence("Hello World"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        // matches 'l'
        Assert.assertTrue(characterReader.matchesAny('l'));
        // matches 'o'
        Assert.assertTrue(characterReader.matchesAny('o'));
        // matches 'W'
        Assert.assertTrue(characterReader.matchesAny('W'));
        // does not match 'x'
        Assert.assertFalse(characterReader.matchesAny('x'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        // matches 'W'
        Assert.assertTrue(characterReader.matchesAnySorted('W'));
        // matches 'o'
        Assert.assertTrue(characterReader.matchesAnySorted('o'));
        // matches 'l'
        Assert.assertTrue(characterReader.matchesAnySorted('l'));
        // does not match 'x'
        Assert.assertFalse(characterReader.matchesAnySorted('x'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        // matches 'H'
        Assert.assertTrue(characterReader.matchesLetter());
        // matches 'e'
        Assert.assertTrue(characterReader.matchesLetter());
        // matches 'l'
        Assert.assertTrue(characterReader.matchesLetter());
        // matches 'o'
        Assert.assertTrue(characterReader.matchesLetter());
        // matches 'W'
        Assert.assertTrue(characterReader.matchesLetter());
        // does not match 'x'
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("12345"));
        // matches '1'
        Assert.assertTrue(characterReader.matchesDigit());
        // matches '2'
        Assert.assertTrue(characterReader.matchesDigit());
        // matches '3'
        Assert.assertTrue(characterReader.matchesDigit());
        // matches '4'
        Assert.assertTrue(characterReader.matchesDigit());
        // matches '5'
        Assert.assertTrue(characterReader.matchesDigit());
        // does not match 'x'
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsumeSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        // matches "Hello"
        Assert.assertTrue(characterReader.matchConsumeSequence("Hello"));
        // does not match "World"
        Assert.assertFalse(characterReader.matchConsumeSequence("World"));
    }

    @Test
    public void testCharacterReaderMatchConsumeSequenceIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        // matches "hello"
        Assert.assertTrue(characterReader.matchConsumeSequenceIgnoreCase("hello"));
        // does not match "World"
        Assert.assertFalse(characterReader.matchConsumeSequenceIgnoreCase("World"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<title>Test</title>"));
        // matches "</title>"
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        // matches "</title>"
        Assert.assertTrue(characterReader.containsIgnoreCase("title"));
        // does not match "Test"
        Assert.assertFalse(characterReader.containsIgnoreCase("Test"));
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", characterReader.toString());
        characterReader.consumeToEnd();
        Assert.assertEquals("", characterReader.toString());
    }
}