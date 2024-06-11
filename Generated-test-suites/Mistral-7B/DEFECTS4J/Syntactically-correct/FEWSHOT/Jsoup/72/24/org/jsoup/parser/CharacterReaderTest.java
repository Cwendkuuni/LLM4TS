package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    private static final char EOF = (char) -1;

    @Test
    public void testNewInstance() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertEquals(0, cr.pos());
        assertEquals(false, cr.isEmpty());
        assertEquals(EOF, cr.current());
        assertEquals(EOF, cr.consume());
    }

    @Test
    public void testNewInstanceWithSize() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"), 5);
        assertEquals(0, cr.pos());
        assertEquals(false, cr.isEmpty());
        assertEquals(EOF, cr.current());
        assertEquals(EOF, cr.consume());
    }

    @Test
    public void testNewInstanceWithString() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"), "abcd".toCharArray());
        assertEquals(0, cr.pos());
        assertEquals(false, cr.isEmpty());
        assertEquals('a', cr.current());
        assertEquals('a', cr.consume());
    }

    @Test(expected = UncheckedIOException.class)
    public void testNewInstanceWithNullReader() {
        new CharacterReader(null, 100);
    }

    @Test
    public void testBufferUp() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        cr.bufferUp();
        assertEquals(4, cr.bufLength);
        assertEquals(0, cr.bufPos);
        assertEquals(0, cr.bufMark);
        assertEquals(EOF, cr.current());
        assertEquals(EOF, cr.consume());
        assertEquals(0, cr.pos());
        assertEquals(false, cr.isEmpty());
    }

    @Test
    public void testBufferUpWithConsumedChars() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        cr.bufferUp();
        cr.consume();
        cr.consume();
        cr.consume();
        cr.bufferUp();
        assertEquals(4, cr.bufLength);
        assertEquals(1, cr.bufPos);
        assertEquals(1, cr.bufMark);
        assertEquals('d', cr.current());
        assertEquals('d', cr.consume());
        assertEquals(3, cr.pos());
        assertEquals(false, cr.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertEquals(0, cr.pos());
        cr.consume();
        cr.consume();
        cr.consume();
        cr.consume();
        assertEquals(4, cr.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertEquals(false, cr.isEmpty());
        cr.consume();
        cr.consume();
        cr.consume();
        cr.consume();
        assertEquals(true, cr.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertEquals(EOF, cr.current());
        cr.consume();
        assertEquals('a', cr.current());
    }

    @Test
    public void testConsume() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        cr.consume();
        assertEquals('a', cr.current());
        cr.consume();
        assertEquals('b', cr.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        cr.consume();
        cr.unconsume();
        assertEquals('a', cr.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        cr.advance();
        assertEquals('b', cr.current());
    }

    @Test
    public void testMark() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        cr.mark();
        cr.advance();
        cr.rewindToMark();
        assertEquals('a', cr.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertEquals(-1, cr.nextIndexOf('z'));
        cr.consume();
        assertEquals(-1, cr.nextIndexOf('z'));
        cr.consume();
        assertEquals(2, cr.nextIndexOf('c'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertEquals(-1, cr.nextIndexOf("ef"));
        cr.consume();
        cr.consume();
        assertEquals(-1, cr.nextIndexOf("ef"));
        cr.consume();
        cr.consume();
        assertEquals(0, cr.nextIndexOf("cd"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertEquals("", cr.consumeTo('a'));
        cr.consume();
        cr.consume();
        assertEquals("bc", cr.consumeTo('c'));
        cr.consume();
        assertEquals("d", cr.consumeToEnd());
    }

    @Test
    public void testConsumeToWithSequence() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertEquals("", cr.consumeTo("ef"));
        cr.consume();
        cr.consume();
        assertEquals("cd", cr.consumeTo("cd"));
        cr.consume();
        cr.consume();
        assertEquals("d", cr.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertEquals("", cr.consumeToAny(new char[]{}));
        cr.consume();
        cr.consume();
        assertEquals("bc", cr.consumeToAny(new char[]{'b', 'c'}));
        cr.consume();
        cr.consume();
        assertEquals("d", cr.consumeToAny(new char[]{'d'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertEquals("", cr.consumeToAnySorted(new char[]{}));
        cr.consume();
        cr.consume();
        assertEquals("bc", cr.consumeToAnySorted(new char[]{ 'c', 'b'}));
        cr.consume();
        cr.consume();
        assertEquals("d", cr.consumeToAnySorted(new char[]{'d'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader cr = new CharacterReader(new StringReader("&<abcd"));
        assertEquals("&<", cr.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader cr = new CharacterReader(new StringReader("<title>\txml\nversion=\"1.0\"/>abcd"));
        assertEquals("\txml\nversion=\"1.0\"", cr.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertEquals("abcd", cr.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader cr = new CharacterReader(new StringReader("abcdefghijklm"));
        assertEquals("abc", cr.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader cr = new CharacterReader(new StringReader("abc123def456"));
        assertEquals("abc123", cr.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertTrue(cr.matches('a'));
        assertTrue(cr.matches('b'));
        assertTrue(cr.matches('c'));
        assertTrue(cr.matches('d'));
        assertFalse(cr.matches('e'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertTrue(cr.matches("ab"));
        assertTrue(cr.matches("cd"));
        assertFalse(cr.matches("ef"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader cr = new CharacterReader(new StringReader("ABcd"));
        assertTrue(cr.matchesIgnoreCase("ab"));
        assertTrue(cr.matchesIgnoreCase("cd"));
        assertTrue(cr.matchesIgnoreCase("ab"));
        assertFalse(cr.matchesIgnoreCase("EF"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertTrue(cr.matchesAny(new char[]{'a', 'b'}));
        assertTrue(cr.matchesAny(new char[]{'b', 'c'}));
        assertTrue(cr.matchesAny(new char[]{'d'}));
        assertFalse(cr.matchesAny(new char[]{'e'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertTrue(cr.matchesAnySorted(new char[]{'a', 'b', 'c', 'd'}));
        assertTrue(cr.matchesAnySorted(new char[]{'b', 'c', 'd', 'a'}));
        assertFalse(cr.matchesAnySorted(new char[]{'e'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertTrue(cr.matchesLetter());
        assertFalse(cr.matchesLetter());
        assertTrue(cr.matchesLetter());
        assertTrue(cr.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertFalse(cr.matchesDigit());
        assertFalse(cr.matchesDigit());
        assertFalse(cr.matchesDigit());
        assertTrue(cr.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader cr = new CharacterReader(new StringReader("abcd"));
        assertEquals("cd", cr.matchConsume("cd"));
        assertEquals("", cr.matchConsume("ef"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader cr = new CharacterReader(new StringReader("ABcd"));
        assertEquals("cd", cr.matchConsumeIgnoreCase("CD"));
        assertEquals("", cr.matchConsumeIgnoreCase("EF"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader cr = new CharacterReader(new StringReader("<title>\txml\nversion=\"1.0\"\n<meta charset=\"UTF-8\">\t<meta name=\"description\" content=\"Description for the document.\"/><meta name=\"keywords\" content=\"Java, Jsoup, XML, parser\">"));
        assertTrue(cr.containsIgnoreCase("/title"));
        assertTrue(cr.containsIgnoreCase("meta"));
        assertTrue(cr.containsIgnoreCase("description"));
        assertTrue(cr.containsIgnoreCase("content"));
        assertTrue(cr.containsIgnoreCase("java"));
        assertTrue(cr.containsIgnoreCase("jsoup"));
        assertTrue(cr.containsIgnoreCase("xml"));
        assertTrue(cr.containsIgnoreCase("parser"));
        assertFalse(cr.containsIgnoreCase("XMLElement"));
    }
}