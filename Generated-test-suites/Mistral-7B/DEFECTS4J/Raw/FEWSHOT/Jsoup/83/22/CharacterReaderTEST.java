package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        try {
            CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
            assertEquals("abcdefghijklmnopqrstuvwxyz", reader.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testReaderNull() {
        try {
            CharacterReader reader = new CharacterReader(null, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReaderUnsupportedMark() {
        try {
            CharacterReader reader = new CharacterReader(new StringReader("abc"), 10);
            reader.mark();
            reader.reset();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        byte[] buffer = new byte[1024];
        reader.read(buffer);
        assertEquals(1024, reader.pos());
        reader.bufferUp();
        assertEquals(1024, reader.bufLength);
        assertEquals(0, reader.bufPos);
        assertEquals(0, reader.readerPos);
        assertEquals(1024, reader.bufSplitPoint);
        reader.consume();
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
        reader.consume();
        reader.advance();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertFalse(reader.isEmpty());
        reader.consume();
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(EOF, reader.consume());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String input = reader.consumeTo('d');
        assertEquals("abc", input);
        String input2 = reader.consumeTo('z');
        assertEquals("abcdefghijklmnopqrstuvwxyz", input2);
        String input3 = reader.consumeToEnd();
        assertEquals("abcdefghijklmnopqrstuvwxyz", input3);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String input = reader.consumeToAny(new char[]{'d', 'e'});
        assertEquals("abc", input);
        String input2 = reader.consumeToAny(new char[]{'z'});
        assertEquals("defghijklmnopqrstuvwxyz", input2);
        String input3 = reader.consumeToAny(new char[]{'a', 'b', 'c'});
        assertEquals("abcdefghijklmnopqrstuvwxyz", input3);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String input = reader.consumeToAnySorted(new char[]{'z', 'd'});
        assertEquals("defghijklmnopqrstuvwxyz", input);
        String input2 = reader.consumeToAnySorted(new char[]{'a', 'b', 'c'});
        assertEquals("abcdefghijklmnopqrstuvwxyz", input2);
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        String input = reader.consumeData();
        assertEquals("&<", input);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader(" \t<html>\r\f<head>\n<title>Test</title>\n</head>\n<body>\n"));
        String input = reader.consumeTagName();
        assertEquals("html", input);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String input = reader.consumeToEnd();
        assertEquals("abcdefghijklmnopqrstuvwxyz", input);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        String input = reader.consumeLetterSequence();
        assertEquals("abcdefghijklmnopqrstuvwxyz", input);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456ghi789"));
        String input = reader.consumeLetterThenDigitSequence();
        assertEquals("abc123", input);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("e"));
        assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('e'));
        assertTrue(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matchesAnySorted('a'));
        assertTrue(reader.matchesAnySorted('e'));
        assertTrue(reader.matchesAnySorted('z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String input = reader.matchConsume("def");
        assertEquals("def", input);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String input = reader.matchConsumeIgnoreCase("Def");
        assertEquals("def", input);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("TITLE"));
        assertFalse(reader.containsIgnoreCase("/body"));
    }
}