package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);

        assertEquals("Hello World!", characterReader.toString());
        assertNotNull(characterReader.reader);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World!");

        assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testBufferUpMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.bufferUp();

        assertEquals(13, characterReader.bufLength);
        assertEquals(0, characterReader.bufPos);
        assertEquals(0, characterReader.readerPos);
        assertEquals(0, characterReader.bufMark);
        assertEquals(10, characterReader.bufSplitPoint);
    }

    @Test
    public void testIsEmptyMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertFalse(characterReader.isEmpty());

        characterReader.bufferUp();
        characterReader.advance();

        assertTrue(characterReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpMethodWithIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });

        characterReader.bufferUp();
    }

    @Test
    public void testCurrentMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertEquals('H', characterReader.current());
        assertEquals('H', characterReader.consume());

        characterReader.advance();

        assertEquals('H', characterReader.current());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testConsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertEquals('H', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals('l', characterReader.consume());

        characterReader.rewindToMark();

        assertEquals('H', characterReader.consume());
    }

    @Test
    public void testAdvanceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.advance();
        characterReader.advance();

        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMarkMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();

        assertEquals('H', characterReader.consume());
    }

    @Test
    public void testNextIndexOfMethodWithChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertEquals(6, characterReader.nextIndexOf('o'));
        assertEquals(-1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfMethodWithCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertEquals(6, characterReader.nextIndexOf("ll"));
        assertEquals(-1, characterReader.nextIndexOf("xx"));
    }

    @Test
    public void testConsumeToMethodWithChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        String result = characterReader.consumeTo('o');

        assertEquals("Hello", result);
    }

    @Test
    public void testConsumeToMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        String result = characterReader.consumeTo("ll");

        assertEquals("Hello World", result);
    }

    @Test
    public void testConsumeToAnyMethodWithCharArray() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        String result = characterReader.consumeToAny(new char[]{'l', 'o'});

        assertEquals("Hello", result);
    }

    @Test
    public void testConsumeToAnySortedMethodWithCharArray() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        String result = characterReader.consumeToAnySorted(new char[]{'l', 'o'});

        assertEquals("Hello", result);
    }

    @Test
    public void testConsumeDataMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&<null"));

        String result = characterReader.consumeData();

        assertEquals("&<", result);
    }

    @Test
    public void testConsumeTagNameMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("/html/head"));

        String result = characterReader.consumeTagName();

        assertEquals("/html/head", result);
    }

    @Test
    public void testConsumeToEndMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        String result = characterReader.consumeToEnd();

        assertEquals("Hello World!", result);
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("HelloWorld"));

        String result = characterReader.consumeLetterSequence();

        assertEquals("Hello", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123World"));

        String result = characterReader.consumeLetterThenDigitSequence();

        assertEquals("Hello123", result);
    }

    @Test
    public void testConsumeHexSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&#x61;&#x6D;&#x6F;&#x61;&#x6C;&#x6C;&#x65;&#x6E;" +
                "&#x63;&#x61;&#x6C;&#x63;&#x68;&#x65;"));

        String result = characterReader.consumeHexSequence();

        assertEquals("abcdefg", result);
    }

    @Test
    public void testConsumeDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("12345678901234567890"));

        String result = characterReader.consumeDigitSequence();

        assertEquals("12345678901234567890", result);
    }

    @Test
    public void testMatchesMethodWithChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertTrue(characterReader.matches('o'));
        assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatchesMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertTrue(characterReader.matches("ll"));
        assertFalse(characterReader.matches("xx"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertTrue(characterReader.matchesIgnoreCase("ll"));
        assertFalse(characterReader.matchesIgnoreCase("XX"));
    }

    @Test
    public void testMatchesAnyMethodWithCharArray() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertTrue(characterReader.matchesAny(new char[]{'o', 'l'}));
        assertFalse(characterReader.matchesAny(new char[]{'x', 'x'}));
    }

    @Test
    public void testMatchesAnySortedMethodWithCharArray() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'o'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'x', 'x'}));
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchesDigitMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertFalse(characterReader.matchesLetter());
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertTrue(characterReader.matchConsume("ll"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertTrue(characterReader.matchConsumeIgnoreCase("ll"));
    }

    @Test
    public void testContainsIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello World</title></head></html>"));

        assertTrue(characterReader.containsIgnoreCase("title"));
        assertFalse(characterReader.containsIgnoreCase("xx"));
    }

    @Test
    public void testToStringMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        assertEquals("Hello World!", characterReader.toString());
    }
}