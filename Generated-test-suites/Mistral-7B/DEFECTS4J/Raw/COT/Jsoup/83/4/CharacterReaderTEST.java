package org.jsoup.parser; // import required packages here if necessary

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithInput() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithInputAndSize() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader, 10);
        assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"), "Hello World!".length());
        assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        assertTrue(characterReader.isEmpty());

        characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        characterReader.advance();
        assertEquals(2, characterReader.pos());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        assertEquals('H', characterReader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeFromEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        characterReader.consume();
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.consume();
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals(4, characterReader.nextIndexOf('l'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals(4, characterReader.nextIndexOf("ll"));
        assertEquals(-1, characterReader.nextIndexOf("zz"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("llo", characterReader.consumeTo('l'));
        assertEquals("World!", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("llo", characterReader.consumeTo("ll").toString());
        assertEquals("World!", characterReader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("World!", characterReader.consumeToAny(new char[]{'l', 'o'}));
        assertEquals("World!", characterReader.consumeToAnySorted(new char[]{'l', 'o'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("World!", characterReader.consumeToAnySorted(new char[]{'o', 'l'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;script&gt;alert('Hello World')&lt;/script&gt;"));
        assertEquals("<script>alert('Hello World')</script>", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello World</title></head><body>"));
        assertEquals("title", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello World</title></head><body>"));
        assertEquals("</body>", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123def"));
        assertEquals("abc", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123def"));
        assertEquals("abc123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(characterReader.matches('l'));
        assertTrue(characterReader.matches("ll"));
        assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(characterReader.matchesIgnoreCase("ll"));
        assertFalse(characterReader.matchesIgnoreCase("zz"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(characterReader.matchesAny('l', 'o'));
        assertFalse(characterReader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'o'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter('1'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit('3'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(characterReader.matchConsume("ll"));
        assertEquals("llo", characterReader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(characterReader.matchConsumeIgnoreCase("ll"));
        assertEquals("llo", characterReader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello World</title></head><body>"));
        assertTrue(characterReader.containsIgnoreCase("title"));
        assertFalse(characterReader.containsIgnoreCase("zzz"));
    }

}