package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

import java.util.Arrays;
import java.util.Locale;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('h', reader.current());
        assertEquals('h', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('e', reader.current());

        reader.mark();
        reader.unconsume();
        reader.rewindToMark();
        assertEquals(1, reader.pos());
        assertEquals('h', reader.current());

        String str = reader.consumeAsString();
        assertEquals("he", str);

        int index = reader.nextIndexOf('l');
        assertEquals(3, index);
        assertEquals("ll", reader.consumeTo('l'));
        assertEquals("llo", reader.consumeTo("ll"));

        char[] arr = new char[]{'a', 'b', 'c'};
        String str2 = reader.consumeToAny(arr);
        assertEquals("bc", str2);

        String str3 = reader.consumeTagName();
        assertEquals("title", str3);

        reader.consumeToEnd();
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("hello world");
        String str1 = reader.cacheString(0, 5);
        String str2 = reader.cacheString(0, 5);
        assertEquals(str1, str2);

        reader.cacheString(0, 5); // hash collision
        String str3 = reader.cacheString(0, 5);
        assertNotSame(str1, str3);

        reader.cacheString(0, 6); // longer string
        String str4 = reader.cacheString(0, 5);
        assertNotSame(str1, str4);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("hello world");
        assertTrue(reader.matches('h'));
        assertFalse(reader.matches('x'));

        String str = "hello";
        assertTrue(reader.matches(str));
        assertFalse(reader.matches(str.toUpperCase(Locale.ENGLISH)));

        char[] arr = new char[]{'h', 'e', 'l', 'l'};
        assertTrue(reader.matchesAny(arr));
        assertFalse(reader.matchesAnySorted(new char[]{'l', 'e', 'h'}));
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("hello", reader.consumeLetterSequence());
        assertEquals("world", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("123hello456world789");
        assertEquals("123", reader.consumeLetterThenDigitSequence());
        assertEquals("hello", reader.consumeLetterSequence());
        assertEquals("456", reader.consumeLetterThenDigitSequence());
        assertEquals("world", reader.consumeToEnd());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("&nbsp;");
        assertEquals("nbsp", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertEquals("1234567890", reader.consumeDigitSequence());
    }
}