package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader reader = new StringReader("Hello World");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testBufferUpMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertEquals(5, characterReader.bufLength);
        assertEquals(0, characterReader.bufPos);
    }

    @Test
    public void testPosMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertEquals(0, characterReader.pos());
        characterReader.consume();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testIsEmptyMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        assertTrue(characterReader.isEmpty());
        characterReader.consume();
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testConsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertEquals('H', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals('l', characterReader.consume());
        assertEquals('l', characterReader.consume());
        assertEquals('o', characterReader.consume());
        assertEquals(' ', characterReader.consume());
        assertEquals('W', characterReader.consume());
        assertEquals('r', characterReader.consume());
        assertEquals('d', characterReader.consume());
        assertEquals(EOF, characterReader.consume());
        assertEquals(13, characterReader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testConsumeMethodWithIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReaderThrowingIOException("Hello World")); {
            try {
                characterReader.bufferUp();
                for (int i = 0; i < 10; i++) {
                    characterReader.consume();
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Test
    public void testAdvanceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        characterReader.advance();
        assertEquals('H', characterReader.current());
        characterReader.advance();
        assertEquals('e', characterReader.current());
        characterReader.advance();
        assertEquals('l', characterReader.current());
    }

    @Test
    public void testMarkMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testNextIndexOfMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertEquals(6, characterReader.nextIndexOf('W'));
    }

    @Test
    public void testNextIndexOfMethodWithEmptyString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertEquals(-1, characterReader.nextIndexOf(""));
    }

    @Test
    public void testNextIndexOfMethodWithMultiCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World HelloJsoup"));
        characterReader.bufferUp();
        assertEquals(11, characterReader.nextIndexOf("llJsoup"));
    }

    @Test
    public void testConsumeToMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertEquals("H", characterReader.consumeTo('H').trim());
    }

    @Test
    public void testConsumeToMethodWithEmptyString() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        assertEquals("", characterReader.consumeTo(Character.MIN_VALUE).trim());
    }

    @Test
    public void testConsumeToMethodWithMultipleChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertEquals("Hello ", characterReader.consumeTo(" World").trim());
    }

    @Test
    public void testConsumeToAnyMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertEquals("H", characterReader.consumeToAny('H').trim());
    }

    @Test
    public void testConsumeToAnyMethodWithMultipleChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertEquals("Hello", characterReader.consumeToAny(new char[]{'o', 'l', 'd'}).trim());
    }

    @Test
    public void testConsumeToAnySortedMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("123456789"));
        characterReader.bufferUp();
        assertEquals("123", characterReader.consumeToAnySorted(new char[]{'1', '2'}).trim());
    }

    @Test
    public void testConsumeToEndMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertEquals("Hello World", characterReader.consumeToEnd().trim());
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertEquals("Hello", characterReader.consumeLetterSequence().trim());
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123World456"));
        characterReader.bufferUp();
        assertEquals("Hello123", characterReader.consumeLetterThenDigitSequence().trim());
    }

    @Test
    public void testMatchesMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertTrue(characterReader.matches('H'));
        assertFalse(characterReader.matches('W'));
    }

    @Test
    public void testMatchesMethodWithMultiChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertTrue(characterReader.matches("ll"));
        assertFalse(characterReader.matches("oo"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertTrue(characterReader.matchesIgnoreCase("h"));
        assertFalse(characterReader.matchesIgnoreCase("W"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithMultiChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertTrue(characterReader.matchesIgnoreCase("ll"));
        assertFalse(characterReader.matchesIgnoreCase("oo"));
    }

    @Test
    public void testMatchesAnyMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertTrue(characterReader.matchesAny('H'));
        assertFalse(characterReader.matchesAny('W'));
    }

    @Test
    public void testMatchesAnyMethodWithMultiChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertTrue(characterReader.matchesAny(new char[]{'h', 'l'}));
        assertFalse(characterReader.matchesAny(new char[]{'o', 'w'}));
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigitMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123World456"));
        characterReader.bufferUp();
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit());
    }
}

class StringReaderThrowingIOException extends StringReader {
    public StringReaderThrowingIOException(String s) {
        super(s);
    }

    @Override
    public int read() throws IOException {
        throw new IOException();
    }
}