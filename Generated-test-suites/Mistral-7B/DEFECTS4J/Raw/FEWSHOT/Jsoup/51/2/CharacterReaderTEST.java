package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_emptyString() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_singleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals('a', reader.current());
        assertEquals("a", reader.consumeAsString());
        assertEquals("a", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_multipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        assertEquals("a", reader.consumeAsString());
        reader.consume();
        assertEquals('b', reader.current());
        assertEquals("ab", reader.consumeTo("b"));
        assertEquals("abc", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeTo_singleCharacter() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("b", reader.consumeTo('b').trim());
        assertEquals("cd", reader.consumeToEnd().trim());
        assertEquals('d', reader.current());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeTo_multipleCharacters() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("bc", reader.consumeTo("bc").trim());
        assertEquals("d", reader.consume().trim());
        assertEquals("cd", reader.consumeToEnd().trim());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeToAny() {
        CharacterReader reader = new CharacterReader("abracadabra");
        assertEquals("abra", reader.consumeToAny(new char[]{'a', 'b', 'r'}).trim());
        assertEquals("cadaba", reader.consumeToAny(new char[]{'c', 'a', 'd', 'a', 'b'}).trim());
        assertEquals("", reader.consumeToAny(new char[]{'z'}).trim());
        assertEquals("r", reader.current());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abracadabra");
        assertEquals("abra", reader.consumeToAnySorted(new char[]{'a', 'b', 'r'}).trim());
        assertEquals("cadaba", reader.consumeToAnySorted(new char[]{'c', 'a', 'd', 'a', 'b'}).trim());
        assertEquals("", reader.consumeToAnySorted(new char[]{'z'}).trim());
        assertEquals("r", reader.current());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeData() {
        CharacterReader reader = new CharacterReader("&amp;<foo>bar<baz>qux&quot;");
        assertEquals("&amp;<foo>", reader.consumeData().trim());
        assertEquals("<baz>qux&quot;", reader.consumeToEnd().trim());
        assertEquals('q', reader.current());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeTagName() {
        CharacterReader reader = new CharacterReader("<title>My Title</title>");
        assertEquals("<title>", reader.consumeTagName().trim());
        assertEquals("My Title</title>", reader.consumeToEnd().trim());
        assertEquals('>', reader.current());
        assertTrue(reader.isEmpty());
    }

}