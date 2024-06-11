package org.jsoup.parser; // update the package name if necessary

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader reader = new StringReader("Hello World");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(reader, characterReader.getReader());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        String input = "Hello World";
        CharacterReader characterReader = new CharacterReader(input);
        assertEquals(new StringReader(input), characterReader.getReader());
        assertEquals(input.length(), characterReader.pos());
    }

    @Test
    public void testEmptyCharacterReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        assertTrue(characterReader.isEmpty());
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testCurrentChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals('H', characterReader.current());
        assertEquals(0, characterReader.pos());
        characterReader.advance();
        assertEquals('H', characterReader.current());
        assertEquals(1, characterReader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader characterReader = new CharacterReader(new IOException("Test IOException").getReader());
        // no need to call any methods here as IOException will be thrown in CharacterReader constructor
    }

    @Test
    public void testBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.bufferUp();
        assertEquals(3, characterReader.pos());
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.mark();
        characterReader.advance();
        assertEquals(1, characterReader.pos());
        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        assertEquals('H', characterReader.consume());
        assertEquals('e', characterReader.current());
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String str = characterReader.consumeTo('l');
        assertEquals("Hello", str);
        assertEquals('l', characterReader.current());
        assertEquals(6, characterReader.pos());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String str = characterReader.consumeToEnd();
        assertEquals("Hello World", str);
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123World"));
        String str = characterReader.consumeToAny(new char[]{'1', '2'});
        assertEquals("lloWorld", str);
        assertEquals('W', characterReader.current());
        assertEquals(9, characterReader.pos());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("HelloWorld123"));
        String str = characterReader.consumeToAnySorted(new char[]{'1', '2'});
        assertEquals("lloWorld", str);
        assertEquals('W', characterReader.current());
        assertEquals(9, characterReader.pos());
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&amp; < & &lt; &gt; null"));
        String str = characterReader.consumeData();
        assertEquals("&amp; < & &lt; &gt; null", str);
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<h1 class='title' id='home'>"));
        String str = characterReader.consumeTagName();
        assertEquals("h1", str);
        assertEquals('/', characterReader.current());
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123xyz"));
        String str = characterReader.consumeLetterSequence();
        assertEquals("abc", str);
        assertEquals('1', characterReader.current());
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123xyz"));
        String str = characterReader.consumeLetterThenDigitSequence();
        assertEquals("abc123", str);
        assertEquals('x', characterReader.current());
        assertEquals(8, characterReader.pos());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matches('H'));
        assertTrue(characterReader.matches("Ho"));
        assertFalse(characterReader.matches('@'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesIgnoreCase("hO"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesAny('l', 'o'));
        assertTrue(characterReader.matchesAny(new char[]{'l', 'o'}));
        assertFalse(characterReader.matchesAny('@'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'o'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter('!'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit('0'));
        assertFalse(characterReader.matchesDigit('!'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head></html>"));
        assertTrue(characterReader.matchConsume("title"));
        assertEquals(11, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head></html>"));
        assertTrue(characterReader.matchConsumeIgnoreCase("TITLE"));
        assertEquals(11, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head></html>"));
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("TITLE"));
        assertFalse(characterReader.containsIgnoreCase("/style"));
    }
}