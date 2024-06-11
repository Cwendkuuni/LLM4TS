package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals(0, characterReader.pos());
        assertEquals(false, characterReader.isEmpty());
        assertEquals('t', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.consume();
        assertEquals('s', characterReader.current());
        characterReader.consume();
        assertEquals('t', characterReader.current());
        characterReader.consume();
        assertEquals(' ', characterReader.current());
        characterReader.consume();
        assertEquals(EOF, characterReader.current());
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals('t', characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderConstructorWithReaderThrowsIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test") {
            @Override
            public int read(char[] c, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }
        });
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        CharacterReader characterReaderWithString = new CharacterReader("test");
        assertEquals(characterReader.charBuf, characterReaderWithString.charBuf);
        assertEquals(characterReader.reader, characterReaderWithString.reader);
        assertEquals(characterReader.bufLength, characterReaderWithString.bufLength);
        assertEquals(characterReader.bufPos, characterReaderWithString.bufPos);
        assertEquals(characterReader.readerPos, characterReaderWithString.readerPos);
        assertEquals(characterReader.bufMark, characterReaderWithString.bufMark);
        assertEquals(characterReader.stringCache, characterReaderWithString.stringCache);
        assertEquals(0, characterReader.pos());
        assertEquals(false, characterReader.isEmpty());
        assertEquals('t', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.consume();
        assertEquals('s', characterReader.current());
        characterReader.consume();
        assertEquals('t', characterReader.current());
        characterReader.consume();
        assertEquals(' ', characterReader.current());
        characterReader.consume();
        assertEquals(EOF, characterReader.current());
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        assertEquals(2, characterReader.bufLength);
        assertEquals(2, characterReader.bufPos);
        assertEquals(4, characterReader.readerPos);
        assertEquals(0, characterReader.bufMark);
        assertEquals("es", characterReader.stringCache[0]);
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        assertEquals(2, characterReader.bufLength);
        assertEquals(2, characterReader.bufPos);
        assertEquals(4, characterReader.readerPos);
        assertEquals(0, characterReader.bufMark);
        assertEquals(2, characterReader.pos());
        characterReader.consume();
        assertEquals(3, characterReader.pos());
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals(2, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        assertEquals(false, characterReader.isEmpty());
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        assertEquals(true, characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.consume();
        characterReader.consume();
        assertEquals('s', characterReader.current());
        characterReader.consume();
        assertEquals('t', characterReader.current());
        characterReader.consume();
        assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testConsumeChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals('t', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals('s', characterReader.consume());
        assertEquals('t', characterReader.consume());
        assertEquals(' ', characterReader.consume());
        assertEquals(EOF, characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('s', characterReader.current());
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
        characterReader.unconsume();
        assertEquals('e', characterReader.current());
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.consume();
        characterReader.consume();
        characterReader.advance();
        assertEquals('s', characterReader.current());
        characterReader.advance();
        assertEquals('t', characterReader.current());
        characterReader.advance();
        assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.mark();
        characterReader.consume();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals(4, characterReader.nextIndexOf(' '));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("testtesttest"));
        assertEquals(2, characterReader.nextIndexOf("est"));
        assertEquals(-1, characterReader.nextIndexOf("zzz"));
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals("est", characterReader.consumeTo('s'));
        assertEquals("test", characterReader.consumeTo(EOF));
        assertEquals("", characterReader.consumeTo('z'));
    }

    @Test
    public void testConsumeToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("testtesttest"));
        assertEquals("est", characterReader.consumeTo("st"));
        assertEquals("testtest", characterReader.consumeTo(EOF));
        assertEquals("", characterReader.consumeTo("zzz"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test<html>"));
        assertEquals("test<", characterReader.consumeToAny(new char[]{' ','h','l'}));
        assertEquals("test<html>", characterReader.consumeToAny(new char[]{' ','h','l','m'}));
        assertEquals("test<html>", characterReader.consumeToAny(new char[]{' ','h','l','m','n'}));
        assertEquals("test<html>", characterReader.consumeToAny(new char[]{' '}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test<html>"));
        assertEquals("test<", characterReader.consumeToAnySorted(new char[]{'<','h','l','m','n',' '}));
        assertEquals("test<html>", characterReader.consumeToAnySorted(new char[]{'<','h','l','m','n',' ',' '}));
        assertEquals("test<html>", characterReader.consumeToAnySorted(new char[]{'<','h','l','m','n',' ','a'}));
        assertEquals("test<html>", characterReader.consumeToAnySorted(new char[]{' '}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&amp;&lt;html&gt;"));
        assertEquals("&amp;&lt;", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<tag name=\"attr\"></tag>"));
        assertEquals("<tag", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals("test", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcABC123"));
        assertEquals("abc", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123ABC123"));
        assertEquals("abc123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatchesChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matches('t'));
        assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("testtesttest"));
        assertTrue(characterReader.matches("est"));
        assertFalse(characterReader.matches("zzz"));
    }

    @Test
    public void testMatchesIgnoreCaseString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestTestTEST"));
        assertTrue(characterReader.matchesIgnoreCase("est"));
        assertFalse(characterReader.matchesIgnoreCase("ZZZ"));
    }

    @Test
    public void testMatchesAnyChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchesAny(new char[]{'t','e','s'}));
        assertFalse(characterReader.matchesAny(new char[]{'z','x','c'}));
    }

    @Test
    public void testMatchesAnySortedChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchesAnySorted(new char[]{'t','e','s'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'z','x','c'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefABCDEF"));
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("12345678901234567890"));
        assertFalse(characterReader.matchesLetter());
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("testtesttest"));
        assertTrue(characterReader.matchConsume("est"));
        assertFalse(characterReader.matchConsume("zzz"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestTestTEST"));
        assertTrue(characterReader.matchConsumeIgnoreCase("est"));
        assertFalse(characterReader.matchConsumeIgnoreCase("ZZZ"));
    }

    @Test
    public void testContainsIgnoreCaseString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body><p>test</p></body></html>"));
        assertTrue(characterReader.containsIgnoreCase("title"));
        assertTrue(characterReader.containsIgnoreCase("test"));
        assertFalse(characterReader.containsIgnoreCase("zzz"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderThrowsIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test") {
            @Override
            public int read(char[] c, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }
        });
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals("test", characterReader.toString());
    }
}