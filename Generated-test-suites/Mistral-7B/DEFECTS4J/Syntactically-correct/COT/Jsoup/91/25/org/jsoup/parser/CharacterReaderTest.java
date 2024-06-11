package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

/**
 * Tests for CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        assertNotNull(reader);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        CharacterReader stringReader = new CharacterReader("Test String");

        assertEquals(reader, stringReader);
    }

    @Test
    public void testBufferUpMethod() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.consume();
        reader.consume();

        assertEquals(reader.pos(), 3);
        assertEquals(reader.current(), 's');
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpMethodWithIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Test String")) {
            @Override
            public void bufferUp() throws IOException {
                throw new IOException();
            }
        };
        reader.bufferUp();
    }

    @Test
    public void testPosMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        assertEquals(reader.pos(), 0);
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();

        assertEquals(reader.pos(), 3);
    }

    @Test
    public void testIsEmptyMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();

        assertFalse(reader.isEmpty());

        reader.consume();
        reader.consume();

        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrentMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        assertEquals(reader.current(), 'T');
        reader.consume();
        assertEquals(reader.current(), 'e');
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();

        assertEquals(reader.current(), 's');
    }

    @Test
    public void testConsumeMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        reader.consume();
        assertEquals(reader.current(), 'e');

        reader.consume();
        assertEquals(reader.current(), 's');

        reader.consume();
        assertEquals(reader.current(), 'T');

        reader.consume();
        assertEquals(reader.current(), '\n');

        reader.consume();
        assertNull(reader.current());
    }

    @Test
    public void testUnconsumeMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();

        reader.unconsume();
        assertEquals(reader.pos(), 3);
        reader.unconsume();
        assertEquals(reader.pos(), 2);
        reader.unconsume();
        assertEquals(reader.pos(), 1);
        reader.unconsume();
        assertEquals(reader.pos(), 0);
    }

    @Test
    public void testAdvanceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        reader.advance();
        assertEquals(reader.pos(), 1);
        reader.consume();
        reader.consume();

        assertEquals(reader.pos(), 3);
    }

    @Test
    public void testMarkMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        reader.mark();
        reader.consume();
        reader.consume();
        reader.consume();

        reader.rewindToMark();

        assertEquals(reader.pos(), 0);
        assertEquals(reader.current(), 'T');
    }

    @Test(expected = UncheckedIOException.class)
    public void testRewindToMarkMethodWithInvalidMark() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        reader.rewindToMark();
    }

    @Test
    public void testNextIndexOfMethodWithChar() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        assertEquals(reader.nextIndexOf('t'), 1);
        assertEquals(reader.nextIndexOf('z'), -1);
    }

    @Test
    public void testNextIndexOfMethodWithCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        assertEquals(reader.nextIndexOf("est"), 2);
        assertEquals(reader.nextIndexOf("Test"), 0);
    }

    @Test
    public void testConsumeToMethodWithChar() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        String consumed = reader.consumeTo('t');
        assertEquals(consumed, "Test ");

        assertEquals(reader.current(), 's');
    }

    @Test
    public void testConsumeToMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        String consumed = reader.consumeTo("est");
        assertEquals(consumed, "Test ");

        assertEquals(reader.current(), 's');
    }

    @Test
    public void testConsumeToAnyMethodWithCharArray() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        String consumed = reader.consumeToAny(new char[] {'s', 't'});
        assertEquals(consumed, "Test");

        assertEquals(reader.current(), ' ');
    }

    @Test
    public void testConsumeToAnySortedMethodWithCharArray() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        String consumed = reader.consumeToAnySorted(new char[] {'t', 's'});
        assertEquals(consumed, "Test");

        assertEquals(reader.current(), ' ');
    }

    @Test
    public void testConsumeDataMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Test & String"));
        String consumed = reader.consumeData();
        assertEquals(consumed, "Test ");

        assertEquals(reader.current(), '&');
    }

    @Test
    public void testConsumeTagNameMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title>"));
        String consumed = reader.consumeTagName();
        assertEquals(consumed, "title");

        assertEquals(reader.current(), '>');
    }

    @Test
    public void testConsumeToEndMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        String consumed = reader.consumeToEnd();
        assertEquals(consumed, "Test String");

        assertFalse(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Test Letters"));
        String consumed = reader.consumeLetterSequence();
        assertEquals(consumed, "Test");

        assertEquals(reader.current(), ' ');
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Test12345"));
        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals(consumed, "Test");

        assertEquals(reader.current(), '1');
    }

    @Test
    public void testMatchesMethodWithChar() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        assertTrue(reader.matches("est"));
        assertFalse(reader.matches("Test12345"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        assertTrue(reader.matchesIgnoreCase("est"));
        assertTrue(reader.matchesIgnoreCase("Test"));
    }

    @Test
    public void testMatchesAnyMethodWithCharArray() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        assertTrue(reader.matchesAny(new char[] {'s', 't'}));
        assertFalse(reader.matchesAny(new char[] {'z'}));
    }

    @Test
    public void testMatchesAnySortedMethodWithCharArray() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        assertTrue(reader.matchesAnySorted(new char[] {'s', 't'}));
        assertTrue(reader.matchesAnySorted(new char[] {'t', 's'}));
        assertFalse(reader.matchesAnySorted(new char[] {'z'}));
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigitMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Test12345"));

        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        assertTrue(reader.matchConsume("est"));
        assertFalse(reader.matchConsume("Test12345"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        assertTrue(reader.matchConsumeIgnoreCase("est"));
        assertTrue(reader.matchConsumeIgnoreCase("Test"));
    }

    @Test
    public void testContainsIgnoreCaseMethodWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title><body><div>Test12345</div></body></html>"));

        assertTrue(reader.containsIgnoreCase("test"));
        assertTrue(reader.containsIgnoreCase("Test"));
        assertTrue(reader.containsIgnoreCase("Test12345"));
    }

    @Test
    public void testToStringMethod() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));

        assertEquals(reader.toString(), "Test String");
    }
}