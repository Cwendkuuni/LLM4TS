package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.CharacterReader.EOF;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderIsInitializedCorrectly() {
        CharacterReader characterReader = new CharacterReader("TestString");
        assertEquals(13, characterReader.length);
        assertEquals(0, characterReader.pos);
        assertEquals(0, characterReader.mark);
    }

    @Test
    public void testCharacterReaderIsEmptyInitially() {
        CharacterReader characterReader = new CharacterReader("TestString");
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderCanConsumeCharacters() {
        CharacterReader characterReader = new CharacterReader("TestString");
        characterReader.consume();
        characterReader.consume();
        assertFalse(characterReader.isEmpty());
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testCharacterReaderCanMarkAndRewind() {
        CharacterReader characterReader = new CharacterReader("TestString");
        characterReader.consume();
        characterReader.consume();
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testCharacterReaderCanConsumeString() {
        CharacterReader characterReader = new CharacterReader("TestString");
        String consumed = characterReader.consumeAsString();
        assertEquals("Test", consumed);
    }

    @Test
    public void testCharacterReaderCanFindNextIndexOfACertainCharacter() {
        CharacterReader characterReader = new CharacterReader("TestString");
        int index = characterReader.nextIndexOf('t');
        assertEquals(3, index);
    }

    @Test
    public void testCharacterReaderCanFindNextIndexOfASequence() {
        CharacterReader characterReader = new CharacterReader("TestString");
        int index = characterReader.nextIndexOf("est");
        assertEquals(3, index);
    }

    @Test
    public void testCharacterReaderCanConsumeSequenceUpToACertainCharacter() {
        CharacterReader characterReader = new CharacterReader("TestString");
        String consumed = characterReader.consumeTo('s');
        assertEquals("Test", consumed);
    }

    @Test
    public void testCharacterReaderCanConsumeSequenceUpToASequence() {
        CharacterReader characterReader = new CharacterReader("TestString");
        String consumed = characterReader.consumeTo("est");
        assertEquals("TestString", consumed);
    }

    @Test
    public void testCharacterReaderCanConsumeAnyOfASequenceOfCharacters() {
        CharacterReader characterReader = new CharacterReader("TestString");
        String consumed = characterReader.consumeToAny(new char[]{'s', 't'});
        assertEquals("Test", consumed);
    }

    @Test
    public void testCharacterReaderCanConsumeAnyOfASortedSequenceOfCharacters() {
        CharacterReader characterReader = new CharacterReader("TestString");
        String consumed = characterReader.consumeToAnySorted(new char[]{'s', 't'});
        assertEquals("Test", consumed);
    }

    @Test
    public void testCharacterReaderCanConsumeData() {
        CharacterReader characterReader = new CharacterReader("Test&String");
        String consumed = characterReader.consumeData();
        assertEquals("Test", consumed);
    }

    @Test
    public void testCharacterReaderCanConsumeTagName() {
        CharacterReader characterReader = new CharacterReader("<html><title>Test</title>");
        String consumed = characterReader.consumeTagName();
        assertEquals("title", consumed);
    }

    @Test
    public void testCharacterReaderCanConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("<html><title>Test</title>");
        String consumed = characterReader.consumeToEnd();
        assertEquals("<html><title>Test</title>", consumed);
    }

    @Test
    public void testCharacterReaderCanConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("TestString");
        String consumed = characterReader.consumeLetterSequence();
        assertEquals("Test", consumed);
    }

    @Test
    public void testCharacterReaderCanConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader("Test123String");
        String consumed = characterReader.consumeLetterThenDigitSequence();
        assertEquals("Test123", consumed);
    }

    @Test
    public void testCharacterReaderCanConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("Test&#x123;String");
        String consumed = characterReader.consumeHexSequence();
        assertEquals("Test&#x123;", consumed);
    }

    @Test
    public void testCharacterReaderCanConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("Test123String");
        String consumed = characterReader.consumeDigitSequence();
        assertEquals("3", consumed);
    }

    @Test
    public void testCharacterReaderMatchesACertainCharacter() {
        CharacterReader characterReader = new CharacterReader("TestString");
        assertTrue(characterReader.matches('T'));
    }

    @Test
    public void testCharacterReaderMatchesASequence() {
        CharacterReader characterReader = new CharacterReader("TestString");
        assertTrue(characterReader.matches("est"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCaseASequence() {
        CharacterReader characterReader = new CharacterReader("TestString");
        assertTrue(characterReader.matchesIgnoreCase("est"));
    }

    @Test
    public void testCharacterReaderMatchesAnyCharacter() {
        CharacterReader characterReader = new CharacterReader("TestString");
        assertTrue(characterReader.matchesAny('T'));
    }

    @Test
    public void testCharacterReaderMatchesAnySortedCharacters() {
        CharacterReader characterReader = new CharacterReader("TestString");
        assertTrue(characterReader.matchesAnySorted(new char[]{'s', 't'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader("TestString");
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader("Test123String");
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testCharacterReaderCanMatchConsumeASequence() {
        CharacterReader characterReader = new CharacterReader("TestString");
        assertTrue(characterReader.matchConsume("est"));
    }

    @Test
    public void testCharacterReaderCanMatchConsumeIgnoreCaseASequence() {
        CharacterReader characterReader = new CharacterReader("TestString");
        assertTrue(characterReader.matchConsumeIgnoreCase("est"));
    }

    @Test
    public void testCharacterReaderCanContainIgnoreCaseASequence() {
        CharacterReader characterReader = new CharacterReader("<html><title>Test</title>");
        assertTrue(characterReader.containsIgnoreCase("/title"));
    }

    @Test
    public void testCharacterReaderToStringReturnsCorrectString() {
        CharacterReader characterReader = new CharacterReader("TestString");
        assertEquals("TestString", characterReader.toString());
    }
}