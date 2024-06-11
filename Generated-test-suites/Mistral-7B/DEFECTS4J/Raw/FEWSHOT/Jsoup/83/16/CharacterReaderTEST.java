package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test constructor with Reader
        Reader reader = new StringReader("");
        CharacterReader cr = new CharacterReader(reader);
        assertTrue(cr.isEmpty());
        assertEquals(0, cr.pos());

        // Test constructor with String
        String input = "Hello World";
        CharacterReader cr2 = new CharacterReader(input);
        assertEquals("Hello World", cr2.toString());
        assertEquals(11, cr2.pos());

        // Test constructor with Reader and int
        Reader reader2 = new StringReader("12345");
        CharacterReader cr3 = new CharacterReader(reader2, 10);
        assertEquals(10, cr3.charBuf.length);
        assertEquals(0, cr3.pos());
        assertEquals(0, cr3.bufPos);
        assertEquals(0, cr3.bufMark);
        assertEquals(0, cr3.bufLength);
        assertEquals(0, cr3.bufSplitPoint);
        assertEquals(0, cr3.readerPos);
        assertTrue(cr3.isEmpty());

        // Test bufferUp
        reader2.mark(reader2.available());
        reader2.skip(2);
        cr3.bufferUp();
        assertEquals(2, cr3.readerPos);
        assertEquals(2, cr3.bufLength);
        assertEquals(2, cr3.bufSplitPoint);
        assertEquals(0, cr3.bufPos);
        assertEquals(0, cr3.bufMark);
        assertArrayEquals(new char[]{'1', '2'}, cr3.charBuf);

        // Test pos() and isEmpty()
        reader2.reset();
        cr3.bufferUp();
        cr3.advance();
        cr3.advance();
        assertTrue(cr3.isEmptyNoBufferUp());
        assertEquals(2, cr3.pos());

        // Test current() and consume()
        reader2.mark(reader2.available());
        cr3.bufferUp();
        assertEquals('1', cr3.current());
        assertEquals('1', cr3.consume());
        assertEquals('2', cr3.current());
        assertEquals(1, cr3.pos());
        assertEquals(0, cr3.bufPos);
        assertEquals(2, cr3.readerPos);

        // Test unconsume()
        cr3.unconsume();
        assertEquals('2', cr3.current());

        // Test advance()
        cr3.advance();
        assertEquals('2', cr3.current());
        assertEquals(2, cr3.pos());

        // Test mark() and rewindToMark()
        cr3.mark();
        cr3.advance();
        assertEquals('2', cr3.current());
        cr3.rewindToMark();
        assertEquals('2', cr3.current());

        // Test nextIndexOf()
        assertEquals(-1, cr3.nextIndexOf('Z'));
        cr3.bufferUp();
        assertEquals(1, cr3.nextIndexOf('2'));

        // Test nextIndexOf() with CharSequence
        String seq = "abcd";
        assertEquals(-1, cr3.nextIndexOf(seq));
        cr3.bufferUp();
        cr3.consumeTo("ab");
        assertEquals(2, cr3.nextIndexOf(seq));

        // Test consumeTo() and consumeToAny()
        assertEquals("1", cr3.consumeTo(' '));
        assertEquals("12", cr3.consumeTo('3'));
        assertEquals("123", cr3.consumeToAny(new char[]{'3', '4'}));
        assertEquals("1234", cr3.consumeToAny(new char[]{'3', '4', '5'}));

        // Test consumeTagName()
        assertEquals("title", cr3.consumeTagName());
        assertEquals("script", cr3.consumeTagName());

        // Test consumeLetterSequence() and consumeLetterThenDigitSequence()
        assertEquals("JavaTest", cr3.consumeLetterSequence());
        assertEquals("Java123Test", cr3.consumeLetterThenDigitSequence());

        // Test consumeHexSequence() and consumeDigitSequence()
        assertEquals("F0F0", cr3.consumeHexSequence());
        assertEquals("123456", cr3.consumeDigitSequence());

        // Test matches() and matchesIgnoreCase()
        assertTrue(cr3.matches('2'));
        assertTrue(cr3.matchesIgnoreCase("abcd"));
        assertFalse(cr3.matchesIgnoreCase("ABCD"));

        // Test matchesAny() and matchesAnySorted()
        assertTrue(cr3.matchesAny(new char[]{'2', '3'}));
        assertTrue(cr3.matchesAnySorted(new char[]{'3', '2'}));
        assertFalse(cr3.matchesAny(new char[]{'Z', '2'}));

        // Test matchesLetter() and matchesDigit()
        assertTrue(cr3.matchesLetter());
        assertTrue(cr3.matchesDigit());

        // Test matchConsume() and matchConsumeIgnoreCase()
        assertTrue(cr3.matchConsume("ab"));
        assertTrue(cr3.matchConsumeIgnoreCase("Ab"));
        assertFalse(cr3.matchConsumeIgnoreCase("AB"));

        // Test containsIgnoreCase()
        assertTrue(cr3.containsIgnoreCase("SCRIPT"));
        assertTrue(cr3.containsIgnoreCase("script"));
        assertFalse(cr3.containsIgnoreCase("SCRIPTS"));

        // Test toString()
        assertEquals("123", cr3.toString());

        // Test cacheString()
        CharacterReader cr4 = new CharacterReader(reader2);
        String str1 = cr4.consumeTo("123");
        assertEquals("123", str1);
        String str2 = CharacterReader.cacheString(cr4.charBuf, CharacterReader.stringCache, 0, cr4.pos());
        assertEquals("123", str1);
        assertEquals("123", str2);
        assertFalse(str1 == str2);
    }
}