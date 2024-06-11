package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.CharacterReader;

import java.util.Arrays;
import java.util.Locale;

/**
 * CharacterReaderTest class to test the CharacterReader class.
 */
public class CharacterReaderTest {

    @org.junit.Test
    public void testCharacterReaderConstructor() {
        String input = "TestInput";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input.toCharArray(), reader.input);
        assertEquals(input.length(), reader.length);
    }

    @org.junit.Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("TestInput");
        assertFalse(reader.isEmpty());
    }

    @org.junit.Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("TestInput");
        assertEquals(0, reader.pos());

        reader.consume();
        assertEquals(1, reader.pos());

        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @org.junit.Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("TestInput");
        assertEquals('\0', reader.current());

        reader.consume();
        assertEquals(reader.current(), reader.input[0]);

        reader.consume();
        assertEquals(reader.current(), reader.input[1]);
    }

    @org.junit.Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("TestInput");
        reader.consume();
        reader.consume();
        reader.consume();

        assertEquals(reader.current(), reader.input[2]);
        assertEquals(reader.pos(), 3);
    }

    @org.junit.Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("TestInput");
        reader.consume();
        reader.consume();
        reader.consume();
        reader.unconsume();

        assertEquals(reader.current(), reader.input[2]);
        assertEquals(reader.pos(), 3);

        reader.consume();
        assertEquals(reader.pos(), 4);
        assertEquals(reader.current(), reader.input[3]);
    }

    @org.junit.Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("TestInput");
        reader.advance();
        reader.advance();

        assertEquals(reader.pos(), 2);
        assertEquals(reader.current(), reader.input[1]);
    }

    @org.junit.Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("TestInput");
        reader.mark();

        reader.advance();
        reader.rewindToMark();

        assertEquals(reader.pos(), 0);
        assertEquals(reader.current(), reader.input[0]);
    }

    @org.junit.Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("TestInput");
        reader.consume();
        reader.consume();
        reader.consume();

        String str = reader.consumeAsString();
        assertEquals("Test", str);
        assertEquals(reader.pos(), 3);
    }

    @org.junit.Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("TestInput");

        assertEquals(-1, reader.nextIndexOf('Z'));
        assertEquals(1, reader.nextIndexOf('T'));
        assertEquals(2, reader.nextIndexOf('e'));
        assertEquals(3, reader.nextIndexOf('s'));
        assertEquals(4, reader.nextIndexOf('t'));
        assertEquals(5, reader.nextIndexOf('I'));
        assertEquals(6, reader.nextIndexOf('n'));
        assertEquals(7, reader.nextIndexOf('put'));
        assertEquals(8, reader.nextIndexOf(' '));
        assertEquals(9, reader.nextIndexOf('T'));

        reader.consume();
        reader.consume();
        reader.consume();

        assertEquals(-1, reader.nextIndexOf('Z'));
        assertEquals(10, reader.nextIndexOf('i'));
    }

    @org.junit.Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader reader = new CharacterReader("TestInputTestInput");

        assertEquals(-1, reader.nextIndexOf("ZZzz"));
        assertEquals(0, reader.nextIndexOf("Test"));
        assertEquals(12, reader.nextIndexOf("Input"));
    }

    @org.junit.Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("TestInput");

        String str = reader.consumeTo('t');
        assertEquals("Te", str);
        assertEquals(3, reader.pos());

        str = reader.consumeTo('s');
        assertEquals("Test", str);
        assertEquals(6, reader.pos());

        str = reader.consumeTo("put");
        assertEquals("TestInput", str);
        assertEquals(11, reader.pos());

        str = reader.consumeToEnd();
        assertEquals("TestInput", str);
        assertEquals(15, reader.pos());
    }

    @org.junit.Test
    public void testConsumeToWithEmptyCharSequence() {
        CharacterReader reader = new CharacterReader("TestInput");

        String str = reader.consumeTo("");
        assertEquals("", str);
        assertEquals(0, reader.pos());
    }

    @org.junit.Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("1234abcdABC");

        String str = reader.consumeToAny(new char[]{'1', '2'});
        assertEquals("12", str);
        assertEquals(3, reader.pos());

        str = reader.consumeToAny(new char[]{'a', 'b'});
        assertEquals("abc", str);
        assertEquals(6, reader.pos());

        str = reader.consumeToAny(new char[]{'A', 'C'});
        assertEquals("ABC", str);
        assertEquals(9, reader.pos());

        str = reader.consumeToAny(new char[0]);
        assertEquals("", str);
        assertEquals(0, reader.pos());
    }

    @org.junit.Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("1234abcdABC");

        String str = reader.consumeToAnySorted(new char[]{'1', '3', 'a'});
        assertEquals("123", str);
        assertEquals(4, reader.pos());

        str = reader.consumeToAnySorted(new char[]{'3', 'a', 'b'});
        assertEquals("3ab", str);
        assertEquals(7, reader.pos());

        str = reader.consumeToAnySorted(new char[]{'a', 'b', 'C'});
        assertEquals("abC", str);
        assertEquals(9, reader.pos());

        str = reader.consumeToAnySorted(new char[0]);
        assertEquals("", str);
        assertEquals(0, reader.pos());
    }

    @org.junit.Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<abc");

        String str = reader.consumeData();
        assertEquals("&<", str);
        assertEquals(3, reader.pos());
    }

    @org.junit.Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<html>\t<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n");

        String str = reader.consumeTagName();
        assertEquals("<html>", str);
        assertEquals(7, reader.pos());

        str = reader.consumeTagName();
        assertEquals("<head>", str);
        assertEquals(13, reader.pos());

        str = reader.consumeTagName();
        assertEquals("<title>", str);
        assertEquals(19, reader.pos());

        str = reader.consumeTagName();
        assertEquals("Test", str);
        assertEquals(23, reader.pos());

        str = reader.consumeTagName();
        assertEquals("<body>", str);
        assertEquals(29, reader.pos());

        str = reader.consumeTagName();
        assertEquals("", str);
        assertEquals(30, reader.pos());
    }

    @org.junit.Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("TestInput");

        String str = reader.consumeToEnd();
        assertEquals("TestInput", str);
        assertEquals(15, reader.pos());
    }

    @org.junit.Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("TestInput");

        String str = reader.consumeLetterSequence();
        assertEquals("Test", str);
        assertEquals(6, reader.pos());
    }

    @org.junit.Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("Test1234");

        String str = reader.consumeLetterThenDigitSequence();
        assertEquals("Test", str);
        assertEquals(4, reader.pos());

        str = reader.consumeLetterThenDigitSequence();
        assertEquals("1234", str);
        assertEquals(8, reader.pos());
    }

    @org.junit.Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("Test#%&/a\u0061\u0062");

        String str = reader.consumeHexSequence();
        assertEquals("#%&/", str);
        assertEquals(6, reader.pos());
    }

    @org.junit.Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("Test123456");

        String str = reader.consumeDigitSequence();
        assertEquals("123456", str);
        assertEquals(7, reader.pos());
    }

    @org.junit.Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("TestInput");

        assertTrue(reader.matches('e'));
        assertTrue(reader.matches("es"));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches("tst"));
    }

    @org.junit.Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("TestInput");

        assertTrue(reader.matchesIgnoreCase("es"));
        assertFalse(reader.matchesIgnoreCase("Z"));
        assertFalse(reader.matchesIgnoreCase("tst"));
        assertTrue(reader.matchesIgnoreCase("TeS"));
    }

    @org.junit.Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("TestInput");

        assertTrue(reader.matchesAny(new char[]{'e', 's'}));
        assertTrue(reader.matchesAny(new char[]{'e', 's', 't'}));
        assertFalse(reader.matchesAny(new char[]{'z'}));
    }

    @org.junit.Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("TestInput");

        assertTrue(reader.matchesAnySorted(new char[]{'e', 's', 't'}));
        assertTrue(reader.matchesAnySorted(new char[]{'s', 't', 'e'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));
    }

    @org.junit.Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("TestInput");

        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @org.junit.Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("TestInput");

        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

    @org.junit.Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("TestInput");

        assertTrue(reader.matchConsume("es"));
        assertEquals(4, reader.pos());

        assertFalse(reader.matchConsume("z"));
        assertEquals(4, reader.pos());

        assertTrue(reader.matchConsume("Test"));
        assertEquals(7, reader.pos());
    }

    @org.junit.Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("TestInput");

        assertTrue(reader.matchConsumeIgnoreCase("es"));
        assertEquals(4, reader.pos());

        assertFalse(reader.matchConsumeIgnoreCase("Z"));
        assertEquals(4, reader.pos());

        assertTrue(reader.matchConsumeIgnoreCase("TeS"));
        assertEquals(7, reader.pos());
    }

    @org.junit.Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body><p>Test contains ignore case</p></body></html>");

        assertTrue(reader.containsIgnoreCase("<title>"));
        assertTrue(reader.containsIgnoreCase("test"));
        assertFalse(reader.containsIgnoreCase("Z"));
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testCacheStringWithNullInput() {
        CharacterReader reader = new CharacterReader(null);
        reader.cacheString(0, 0);
    }
}