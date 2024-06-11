package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

import java.util.Arrays;
import java.util.Locale;

public class CharacterReaderTest {

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.current());
        assertEquals(CharacterReader.EOF, reader.consume());

        reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.current());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());

        reader = new CharacterReader("");
        assertEquals("", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());

        reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(6, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('d'));

        reader = new CharacterReader("abc");
        assertEquals(-1, reader.nextIndexOf('d'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("abcabc");
        String seq = "bc";
        assertEquals(2, reader.nextIndexOf(seq));
        String seq2 = "cd";
        assertEquals(-1, reader.nextIndexOf(seq2));

        reader = new CharacterReader("abc");
        assertEquals(-1, reader.nextIndexOf(seq2));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("cdefg", reader.consumeTo('g'));
        assertEquals("abc", reader.consumeTo('b'));
        assertEquals("", reader.consumeTo('z'));

        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("cdefg", reader.consumeTo("g").toString());
        assertEquals("abc", reader.consumeTo("b").toString());
        assertEquals("", reader.consumeTo("z").toString());

        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeTo("z").toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("defg", reader.consumeToAny(new char[]{'d', 'e', 'f', 'g'}));
        assertEquals("abc", reader.consumeToAny(new char[]{'a', 'b', 'c'}));
        assertEquals("", reader.consumeToAny(new char[]{'z'}));

        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeToAny(new char[]{'z'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("g", reader.consumeToAnySorted(new char[]{'g', 'f', 'e', 'd'}));
        assertEquals("a", reader.consumeToAnySorted(new char[]{'a', 'b', 'c'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'z'}));

        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeToAnySorted(new char[]{'z'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<abcdefg");
        assertEquals("&<", reader.consumeData().toString());

        reader = new CharacterReader("abcdefg");
        assertEquals("", reader.consumeData().toString());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(" <title>My Title</title>");
        assertEquals("title", reader.consumeTagName().toString());

        reader = new CharacterReader("<title>My Title</title>");
        assertEquals("", reader.consumeTagName().toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abcdefg", reader.consumeToEnd().toString());

        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("defg", reader.consumeLetterSequence().toString());

        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeLetterSequence().toString());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        assertEquals("def456", reader.consumeLetterThenDigitSequence().toString());

        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeLetterThenDigitSequence().toString());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));

        reader = new CharacterReader("abc");
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertTrue(reader.matches("abc"));
        assertFalse(reader.matches("def"));

        reader = new CharacterReader("abc");
        assertFalse(reader.matches("def"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertTrue(reader.matchesIgnoreCase("AbC"));
        assertFalse(reader.matchesIgnoreCase("Def"));

        reader = new CharacterReader("abc");
        assertFalse(reader.matchesIgnoreCase("Def"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny(new char[]{'a', 'b', 'c'}));
        assertFalse(reader.matchesAny(new char[]{'d'}));

        reader = new CharacterReader("abc");
        assertFalse(reader.matchesAny(new char[]{'z'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));

        reader = new CharacterReader("abc");
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());

        reader = new CharacterReader("123");
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesLetter());

        reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchConsumeString() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertTrue(reader.matchConsume("abc"));

        reader = new CharacterReader("abc");
        assertFalse(reader.matchConsume("def"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertTrue(reader.matchConsumeIgnoreCase("AbC"));

        reader = new CharacterReader("abc");
        assertFalse(reader.matchConsumeIgnoreCase("Def"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>My Title</title></head><body>...</body></html>");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/BODY"));
        assertFalse(reader.containsIgnoreCase("/header"));
    }

    @Test(expected = NullPointerException.class)
    public void testConsumeWithNullInput() {
        CharacterReader reader = new CharacterReader(null);
    }
}