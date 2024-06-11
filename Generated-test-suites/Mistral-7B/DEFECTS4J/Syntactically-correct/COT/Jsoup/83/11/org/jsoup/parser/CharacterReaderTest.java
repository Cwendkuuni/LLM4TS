package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    /**
     * Test constructor with a Reader input.
     */
    @Test
    public void testCharacterReaderWithReaderInput() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", characterReader.toString());
    }

    /**
     * Test constructor with a String input.
     */
    @Test
    public void testCharacterReaderWithStringInput() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("Hello World", characterReader.toString());
    }

    /**
     * Test constructor with a Reader input and specified size.
     */
    @Test
    public void testCharacterReaderWithReaderInputAndSize() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"), 10);
        assertEquals("Hello World", characterReader.toString());
    }

    /**
     * Test pos() method.
     */
    @Test
    public void testPosMethod() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals(0, characterReader.pos());
        characterReader.advance();
        assertEquals(1, characterReader.pos());
    }

    /**
     * Test isEmpty() method with an empty reader.
     */
    @Test
    public void testIsEmptyMethodWithEmptyReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        assertTrue(characterReader.isEmpty());
    }

    /**
     * Test isEmpty() method with a non-empty reader.
     */
    @Test
    public void testIsEmptyMethodWithNonEmptyReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(characterReader.isEmpty());
    }

    /**
     * Test current() method.
     */
    @Test
    public void testCurrentMethod() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    /**
     * Test consume() method.
     */
    @Test
    public void testConsumeMethod() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.consume();
        assertEquals('l', characterReader.current());
    }

    /**
     * Test unconsume() method.
     */
    @Test
    public void testUnconsumeMethod() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    /**
     * Test advance() method.
     */
    @Test
    public void testAdvanceMethod() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.advance();
        characterReader.advance();
        assertEquals('l', characterReader.current());
    }

    /**
     * Test mark() method.
     */
    @Test
    public void testMarkMethod() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('H', characterReader.current());
    }

    /**
     * Test nextIndexOf() method with a single character.
     */
    @Test
    public void testNextIndexOfMethodWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals(6, characterReader.nextIndexOf('l'));
    }

    /**
     * Test nextIndexOf() method with a multiple characters sequence.
     */
    @Test
    public void testNextIndexOfMethodWithMultipleCharactersSequence() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals(4, characterReader.nextIndexOf("ell"));
    }

    /**
     * Test nextIndexOf() method with an empty sequence.
     */
    @Test
    public void testNextIndexOfMethodWithEmptySequence() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals(-1, characterReader.nextIndexOf(""));
    }

    /**
     * Test consumeTo() method with a single character.
     */
    @Test
    public void testConsumeToMethodWithSingleCharacter() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello", characterReader.consumeTo('l'));
    }

    /**
     * Test consumeTo() method with a multiple characters sequence.
     */
    @Test
    public void testConsumeToMethodWithMultipleCharactersSequence() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello Wo", characterReader.consumeTo("llo"));
    }

    /**
     * Test consumeTo() method with an empty sequence.
     */
    @Test
    public void testConsumeToMethodWithEmptySequence() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("Hello World", characterReader.consumeToEnd());
    }

    /**
     * Test consumeToAny() method with a single character.
     */
    @Test
    public void testConsumeToAnyMethodWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("World", characterReader.consumeToAny('l'));
    }

    /**
     * Test consumeToAny() method with multiple characters.
     */
    @Test
    public void testConsumeToAnyMethodWithMultipleCharacters() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("World", characterReader.consumeToAny(new char[]{'l', 'o'}));
    }

    /**
     * Test consumeData() method.
     */
    @Test
    public void testConsumeDataMethod() {
        CharacterReader characterReader = new CharacterReader("& <null>");
        assertEquals("& <", characterReader.consumeData());
    }

    /**
     * Test consumeTagName() method.
     */
    @Test
    public void testConsumeTagNameMethod() {
        CharacterReader characterReader = new CharacterReader(" <tag>");
        assertEquals("<tag>", characterReader.consumeTagName());
    }

    /**
     * Test consumeToEnd() method.
     */
    @Test
    public void testConsumeToEndMethod() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("Hello World", characterReader.consumeToEnd());
    }

    /**
     * Test consumeLetterSequence() method.
     */
    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader characterReader = new CharacterReader("HeLLo");
        assertEquals("HeLLo", characterReader.consumeLetterSequence());
    }

    /**
     * Test consumeLetterThenDigitSequence() method.
     */
    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader("He11lo");
        assertEquals("He11lo", characterReader.consumeLetterThenDigitSequence());
    }

    /**
     * Test consumeHexSequence() method.
     */
    @Test
    public void testConsumeHexSequenceMethod() {
        CharacterReader characterReader = new CharacterReader("&xa1;");
        assertEquals("&xa1;", characterReader.consumeHexSequence());
    }

    /**
     * Test consumeDigitSequence() method.
     */
    @Test
    public void testConsumeDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader("12345");
        assertEquals("12345", characterReader.consumeDigitSequence());
    }

    /**
     * Test matches() method with a single character.
     */
    @Test
    public void testMatchesMethodWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matches('e'));
    }

    /**
     * Test matches() method with a multiple characters sequence.
     */
    @Test
    public void testMatchesMethodWithMultipleCharactersSequence() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matches("ell"));
    }

    /**
     * Test matchesIgnoreCase() method with a single character.
     */
    @Test
    public void testMatchesIgnoreCaseMethodWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchesIgnoreCase('E'));
    }

    /**
     * Test matchesIgnoreCase() method with a multiple characters sequence.
     */
    @Test
    public void testMatchesIgnoreCaseMethodWithMultipleCharactersSequence() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchesIgnoreCase("ELl"));
    }

    /**
     * Test matchesAny() method with a single character.
     */
    @Test
    public void testMatchesAnyMethodWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchesAny('l'));
    }

    /**
     * Test matchesAny() method with multiple characters.
     */
    @Test
    public void testMatchesAnyMethodWithMultipleCharacters() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchesAny(new char[]{'l', 'o'}));
    }

    /**
     * Test matchesLetter() method.
     */
    @Test
    public void testMatchesLetterMethod() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchesLetter());
    }

    /**
     * Test matchesDigit() method.
     */
    @Test
    public void testMatchesDigitMethod() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertFalse(characterReader.matchesDigit());
    }

    /**
     * Test matchConsume() method.
     */
    @Test
    public void testMatchConsumeMethod() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchConsume("ello"));
    }

    /**
     * Test matchConsumeIgnoreCase() method.
     */
    @Test
    public void testMatchConsumeIgnoreCaseMethod() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertTrue(characterReader.matchConsumeIgnoreCase("ELlo"));
    }

    /**
     * Test containsIgnoreCase() method.
     */
    @Test
    public void testContainsIgnoreCaseMethod() {
        CharacterReader characterReader = new CharacterReader("Hello <title>World</title>");
        assertTrue(characterReader.containsIgnoreCase("<title>"));
    }
}