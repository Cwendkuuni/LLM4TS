package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * Test for CharacterReader class
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertNull(reader.consume());
    }

    @Test
    public void testCharacterReaderSingleChar() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals('A', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleChars() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals('A', reader.consume());
        assertEquals('B', reader.consume());
        assertEquals('C', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("ABC");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('A', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeSubstring() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        String consumed = reader.consumeTo("CD");
        assertEquals("CD", consumed);
        assertEquals('E', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeString() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        String consumed = reader.consumeTo("DEF");
        assertEquals("DEF", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeSingleChar() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        String consumed = reader.consumeTo('F');
        assertEquals("F", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeCharArray() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        String consumed = reader.consumeToAny(new char[]{'E', 'F'});
        assertEquals("EF", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeCharArraySorted() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        String consumed = reader.consumeToAnySorted(new char[]{'E', 'F'});
        assertEquals("EF", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader("&lt;abc&gt;def");
        String consumed = reader.consumeData();
        assertEquals("&lt;abc&gt;", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>");
        String consumed = reader.consumeTagName();
        assertEquals("title", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>");
        String consumed = reader.consumeToEnd();
        assertEquals("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeLetterSequence();
        assertEquals("abc", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("ab12cd3ef");
        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("ab12", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ab%12cd%ef");
        String consumed = reader.consumeHexSequence();
        assertEquals("%12", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("ab123cd4ef");
        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatchesChar() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        assertTrue(reader.matches('a'));
        reader.rewindToMark();
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
    }

    @Test
    public void testCharacterReaderMatchesString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("cd"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("ABCdEf");
        assertTrue(reader.matchesIgnoreCase("AbC"));
        assertFalse(reader.matchesIgnoreCase("ef"));
    }

    @Test
    public void testCharacterReaderMatchesAnyChar() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
        assertTrue(reader.matchesAny('c'));
        assertFalse(reader.matchesAny('d'));
    }

    @Test
    public void testCharacterReaderMatchesAnySortedChars() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c'}));
        assertFalse(reader.matchesAnySorted(new char[]{'d', 'a'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("1234");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsumeString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchConsume("ab"));
        assertEquals("cd", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("ABCdEf");
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertEquals("ef", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<div id=\"foo\">Bar</div>\n</body>\n</html>");
        assertTrue(reader.containsIgnoreCase("foo"));
        assertFalse(reader.containsIgnoreCase("BAR"));
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader reader = new CharacterReader("abcd");
        String str = reader.toString();
        assertEquals("abcd", str);
        assertTrue(reader.isEmpty());
    }
}