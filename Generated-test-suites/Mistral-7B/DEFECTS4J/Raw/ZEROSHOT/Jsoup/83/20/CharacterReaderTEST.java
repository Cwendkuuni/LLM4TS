package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test constructor with String input
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.toString());

        // Test constructor with Reader input
        StringReader readerInput = new StringReader("Hello World");
        CharacterReader readerFromReader = new CharacterReader(readerInput);
        assertEquals("Hello World", readerFromReader.toString());

        // Test constructor with Reader and size
        readerInput = new StringReader("Hello World");
        CharacterReader readerWithSize = new CharacterReader(readerInput, 5);
        assertEquals("Hello World", readerWithSize.toString());

        // Test isEmpty()
        reader = new CharacterReader("Hello World");
        assertFalse(reader.isEmpty());

        reader.consume();
        assertFalse(reader.isEmpty());

        reader.consumeTo('W');
        assertTrue(reader.isEmpty());

        // Test pos() and isEmptyNoBufferUp()
        reader = new CharacterReader("Hello World");
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmptyNoBufferUp());

        reader.consume();
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmptyNoBufferUp());

        reader.consumeTo('l');
        assertEquals(2, reader.pos());
        assertFalse(reader.isEmptyNoBufferUp());

        reader.consumeTo('l');
        assertEquals(3, reader.pos());
        assertTrue(reader.isEmptyNoBufferUp());

        reader.consume();
        assertEquals(4, reader.pos());
        assertFalse(reader.isEmptyNoBufferUp());

        // Test mark() and rewindToMark()
        reader.mark();
        reader.consumeTo('l');
        reader.rewindToMark();
        assertEquals(4, reader.pos());

        // Test nextIndexOf() with char
        reader = new CharacterReader("Hello World");
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(4, reader.nextIndexOf('l'));

        // Test nextIndexOf() with CharSequence
        reader = new CharacterReader("Hello World");
        assertEquals(-1, reader.nextIndexOf("abc"));
        assertEquals(4, reader.nextIndexOf("ll"));

        // Test consumeTo() with char
        reader = new CharacterReader("Hello World");
        assertEquals("ll", reader.consumeTo('l'));
        assertEquals("ldldldldldldldldldld", reader.toString());

        // Test consumeTo() with String
        reader = new CharacterReader("Hello World");
        assertEquals("ll", reader.consumeTo("ll"));
        assertEquals("ldldldldldldldldldld", reader.toString());

        // Test consumeToAny()
        reader = new CharacterReader("Hello World");
        assertEquals("rld", reader.consumeToAny(new char[]{'l', 'r'}));
        assertEquals("Hello World", reader.toString());

        // Test consumeToAnySorted()
        reader = new CharacterReader("Hello World");
        assertEquals("ldl", reader.consumeToAnySorted(new char[]{'l', 'd'}));
        assertEquals("Hello World", reader.toString());

        // Test consumeData()
        reader = new CharacterReader("&nbsp; &lt; &amp; &gt;");
        assertEquals("&nbsp; &amp; &gt;", reader.consumeData());
        assertEquals("", reader.toString());

        // Test consumeTagName()
        reader = new CharacterReader("        <tag name=\"value\">   <another tag />");
        assertEquals("tag", reader.consumeTagName());
        assertEquals(" name=\"value\">   <another tag />", reader.toString());

        // Test consumeLetterSequence()
        reader = new CharacterReader("abc123def");
        assertEquals("def", reader.consumeLetterSequence());
        assertEquals("abc123", reader.toString());

        // Test consumeLetterThenDigitSequence()
        reader = new CharacterReader("abc123def");
        assertEquals("123", reader.consumeLetterThenDigitSequence());
        assertEquals("abcdef", reader.toString());

        // Test consumeHexSequence()
        reader = new CharacterReader("&x0A&x0B");
        assertEquals("AB", reader.consumeHexSequence());
        assertEquals("", reader.toString());

        // Test consumeDigitSequence()
        reader = new CharacterReader("1234567890");
        assertEquals("0", reader.consumeDigitSequence());
        assertEquals("123456789", reader.toString());

        // Test matches() with char
        reader = new CharacterReader("Hello World");
        assertTrue(reader.matches('l'));
        assertFalse(reader.matches('z'));

        // Test matches() with String
        reader = new CharacterReader("Hello World");
        assertTrue(reader.matches("ll"));
        assertFalse(reader.matches("zz"));

        // Test matchesIgnoreCase() with String
        reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesIgnoreCase("ll"));
        assertFalse(reader.matchesIgnoreCase("ZZ"));

        // Test matchesAny() with char
        reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesAny('l'));
        assertFalse(reader.matchesAny('z'));

        // Test matchesAnySorted() with char[]
        reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesAnySorted(new char[]{'l', 'd'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z', 'l'}));

        // Test matchesLetter()
        reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());

        // Test matchConsume() with String
        reader = new CharacterReader("Hello World");
        assertEquals("ll", reader.matchConsume("ll"));
        assertEquals("ldldldldldldldldldld", reader.toString());

        // Test matchConsumeIgnoreCase() with String
        reader = new CharacterReader("Hello World");
        assertEquals("ll", reader.matchConsumeIgnoreCase("LL"));
        assertEquals("ldldldldldldldldldld", reader.toString());

        // Test containsIgnoreCase() with String
        reader = new CharacterReader("<html><head><title><![CDATA[My Title]]></title>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertFalse(reader.containsIgnoreCase("TITLE"));
        assertFalse(reader.containsIgnoreCase("CDATA"));

        // Test toString()
        reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.toString());
    }

    // Test for cacheString() method
    @Test
    public void testCacheString() {
        // Test cacheString with small strings
        CharacterReader reader = new CharacterReader("Hello World");
        String smallString = reader.cacheString(charBuf, stringCache, 0, 5);
        assertEquals("World", smallString);

        smallString = reader.cacheString(charBuf, stringCache, 5, 5);
        assertEquals("World", smallString);

        smallString = reader.cacheString(charBuf, stringCache, 0, 3);
        assertEquals("Hel", smallString);

        // Test cacheString with large strings
        reader = new CharacterReader("Hello World");
        String largeString = reader.cacheString(charBuf, stringCache, 0, 5000);
        assertEquals("World", largeString);

        largeString = reader.cacheString(charBuf, stringCache, 5000, 5000);
        assertEquals("World", largeString);

        largeString = reader.cacheString(charBuf, stringCache, 0, 4999);
        assertEquals("Hello", largeString);
    }
}