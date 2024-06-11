package org.junit.tests; // replace with your package name if necessary

import static org.junit.Assert.*;

import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.CharacterReader.TokeniserState;

import java.io.StringReader;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        assertEquals("Test String", characterReader.toString());
        assertEquals(13, characterReader.pos());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Test String");
        assertEquals("Test String", characterReader.toString());
        assertEquals(13, characterReader.pos());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testBufferUpMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.bufferUp();
        assertEquals(14, characterReader.pos());
        assertEquals(13, characterReader.readerPos);
        assertFalse(characterReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpMethodWithIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String")) {
            @Override
            public void bufferUp() {
                throw new IOException("Test IOException");
            }
        };
        characterReader.bufferUp();
    }

    @Test
    public void testIsEmptyMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        assertTrue(characterReader.isEmpty());
        characterReader.bufferUp();
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrentMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        assertEquals('T', characterReader.current());
        characterReader.advance();
        assertEquals('e', characterReader.current());
        characterReader.bufferUp();
        assertEquals('S', characterReader.current());
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.consume();
        assertEquals('T', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.bufferUp();
        assertEquals('S', characterReader.current());
    }

    @Test
    public void testUnconsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvanceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMarkMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testRewindToMarkMethodWithInvalidMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.rewindToMark();
    }

    @Test
    public void testNextIndexOfMethodWithChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals(4, characterReader.nextIndexOf('S'));
        assertEquals(-1, characterReader.nextIndexOf('X'));
    }

    @Test
    public void testNextIndexOfMethodWithCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals(5, characterReader.nextIndexOf("String"));
        assertEquals(-1, characterReader.nextIndexOf("TestX"));
    }

    @Test
    public void testConsumeToMethodWithChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals("String", characterReader.consumeTo('S'));
        assertEquals("", characterReader.consumeTo('X'));
    }

    @Test
    public void testConsumeToMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals("String", characterReader.consumeTo("String"));
        assertEquals("", characterReader.consumeTo("TestX"));
    }

    @Test
    public void testConsumeToAnyMethodWithChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals("Test", characterReader.consumeToAny(new char[]{'T', 'e', 's'}));
        assertEquals("", characterReader.consumeToAny(new char[]{'X'}));
    }

    @Test
    public void testConsumeToAnySortedMethodWithChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals("Test", characterReader.consumeToAnySorted(new char[]{'e', 's', 't', 'T'}));
        assertEquals("", characterReader.consumeToAnySorted(new char[]{'X'}));
    }

    @Test
    public void testConsumeDataMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals("& < null", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagNameMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"));
        assertEquals("title", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEndMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals("String", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals("Test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123String"));
        assertEquals("Test123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatchesMethodWithChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertTrue(characterReader.matches('S'));
        assertFalse(characterReader.matches('X'));
    }

    @Test
    public void testMatchesMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertTrue(characterReader.matches("String"));
        assertFalse(characterReader.matches("TestX"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertTrue(characterReader.matchesIgnoreCase("string"));
        assertTrue(characterReader.matchesIgnoreCase("TestString"));
        assertFalse(characterReader.matchesIgnoreCase("TestX"));
    }

    @Test
    public void testMatchesAnyMethodWithChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertTrue(characterReader.matchesAny(new char[]{'S', 'T'}));
        assertFalse(characterReader.matchesAny(new char[]{'X'}));
    }

    @Test
    public void testMatchesAnySortedMethodWithChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertTrue(characterReader.matchesAnySorted(new char[]{'S', 'T'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'X'}));
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigitMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123String"));
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringString"));
        assertTrue(characterReader.matchConsume("String"));
        assertEquals(6, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringstring"));
        assertTrue(characterReader.matchConsumeIgnoreCase("String"));
        assertEquals(6, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>"));
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("/p"));
    }
}