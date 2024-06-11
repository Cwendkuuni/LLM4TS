package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader characterReader = new CharacterReader("");
        assertTrue(characterReader.isEmpty());
        assertEquals("", characterReader.toString());
    }

    @Test
    public void testCharacterReaderSimple() {
        CharacterReader characterReader = new CharacterReader("abc");
        assertEquals(0, characterReader.pos());
        assertEquals('a', characterReader.current());
        assertEquals('a', characterReader.consume());
        assertEquals(1, characterReader.pos());
        assertEquals('b', characterReader.current());
        assertEquals('b', characterReader.consume());
        assertEquals(2, characterReader.pos());
        assertEquals('c', characterReader.current());
        assertEquals('c', characterReader.consume());
        assertEquals(3, characterReader.pos());
        assertTrue(characterReader.isEmpty());
        assertEquals("", characterReader.toString());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader characterReader = new CharacterReader("abab");
        assertEquals(0, characterReader.pos());
        assertEquals('a', characterReader.current());
        characterReader.mark();
        assertEquals('a', characterReader.consume());
        assertEquals(1, characterReader.pos());
        assertEquals('b', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('a', characterReader.current());
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader characterReader = new CharacterReader("abcdefabcdef");
        assertEquals(6, characterReader.nextIndexOf('f'));
        assertEquals(-1, characterReader.nextIndexOf('g'));
        characterReader.advance();
        assertEquals(7, characterReader.nextIndexOf('f'));
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader characterReader = new CharacterReader("abcdefabcdef");
        assertEquals("bcde", characterReader.consumeAsString());
        assertEquals(6, characterReader.pos());
        assertEquals("abcdef", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader("abcdefabcdef");
        assertEquals("bcdef", characterReader.consumeTo('f').toString());
        assertEquals(11, characterReader.pos());
        assertEquals("abcdefabcdef", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader characterReader = new CharacterReader("abcd123efgh");
        assertEquals("123", characterReader.consumeToAny('1', '2', '3').toString());
        assertEquals(7, characterReader.pos());
        assertEquals("abcd123efgh", characterReader.toString());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader("abcdefabcdef");
        assertTrue(characterReader.matches('a'));
        assertTrue(characterReader.matches('e'));
        assertFalse(characterReader.matches('g'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("abcdefabcdef");
        assertTrue(characterReader.matchesIgnoreCase("e"));
        assertTrue(characterReader.matchesIgnoreCase("f"));
        assertFalse(characterReader.matchesIgnoreCase("G"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader("abcd123efgh");
        assertTrue(characterReader.matchesAny('1', '2', '3'));
        assertFalse(characterReader.matchesAny('4'));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("abcd<title>efgh</title>ijkl");
        assertTrue(characterReader.containsIgnoreCase("<title>"));
        assertTrue(characterReader.containsIgnoreCase("</title>"));
        assertFalse(characterReader.containsIgnoreCase("<head>"));
    }
}