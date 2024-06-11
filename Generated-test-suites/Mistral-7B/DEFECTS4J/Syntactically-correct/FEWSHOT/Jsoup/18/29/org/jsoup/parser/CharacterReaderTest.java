package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class CharacterReaderTest {

    private final String input;
    private final CharacterReader reader;

    public CharacterReaderTest(String input) {
        this.input = input;
        this.reader = new CharacterReader(input);
    }

    @Parameterized.Parameters(name = "Input: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"abcdefgh"},
                {"abc\ndefgh"},
                {"\u0001\u0002\u0003"},
                {"--foo"},
                {"--bar--"},
                {"--foo--bar--"}
        });
    }

    @Test
    public void testNewInstance() {
        assertEquals(input, reader.input);
        assertEquals(input.length(), reader.length);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());

        reader.consume();
        assertFalse(reader.isEmpty());

        reader.rewindToMark();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());

        reader.consume();
        assertEquals(1, reader.pos());

        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCurrent() {
        assertEquals(input.charAt(0), reader.current());

        reader.consume();
        assertEquals(input.charAt(1), reader.current());

        reader.rewindToMark();
        assertEquals(input.charAt(0), reader.current());
    }

    @Test
    public void testConsume() {
        reader.consume();
        assertEquals(input.charAt(1), reader.current());

        reader.consume();
        assertEquals(input.charAt(2), reader.current());

        reader.rewindToMark();
        assertEquals(input.charAt(0), reader.current());

        reader.consumeTo('e');
        assertEquals("abcdef", reader.toString());
        assertEquals(6, reader.pos());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        assertEquals('d', reader.current());

        reader.unconsume();
        assertEquals('e', reader.current());

        reader.consume();
        assertEquals('f', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals(input.charAt(1), reader.current());

        reader.advance();
        assertEquals(input.charAt(2), reader.current());

        reader.advance(3);
        assertEquals(input.charAt(5), reader.current());

        reader.rewindToMark();
        reader.advance();
        assertEquals(input.charAt(1), reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals(input.charAt(0), reader.current());
    }

    @Test
    public void testRewindToMark() {
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals(input.charAt(0), reader.current());
    }

    @Test
    public void testConsumeAsString() {
        reader.consume(2);
        assertEquals("bc", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        reader.consume(2);
        assertEquals("cd", reader.consumeTo('d'));
        assertEquals("efg", reader.consumeTo("g"));
        assertEquals("", reader.consumeTo('z'));
        assertEquals(input, reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        reader.consume(2);
        assertEquals("cd", reader.consumeToAny('c', 'd'));
        assertEquals("efg", reader.consumeToAny('e', 'g'));
        assertEquals("", reader.consumeToAny('z', 'a'));
        assertEquals("", reader.consumeToAny('z'));
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
        reader.consume('d');
        assertEquals("efg", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("1234", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        reader.consume();
        assertTrue(reader.matches('d'));
        assertFalse(reader.matches('a'));

        reader.consume();
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('d'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        reader.consume();
        reader.consume();
        assertTrue(reader.matchesIgnoreCase("EF"));
        assertFalse(reader.matchesIgnoreCase("ab"));

        reader.consume();
        reader.consume();
        assertTrue(reader.matchesIgnoreCase("GH"));
        assertFalse(reader.matchesIgnoreCase("ef"));
    }

    @Test
    public void testMatchesAny() {
        reader.consume();
        assertTrue(reader.matchesAny('b', 'c'));
        assertFalse(reader.matchesAny('a'));

        reader.consume();
        assertTrue(reader.matchesAny('e', 'g'));
        assertFalse(reader.matchesAny('f'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        reader.consume('d');
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        reader.consume('1');
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        reader.consume(2);
        assertTrue(reader.matchConsume("cd"));
        assertFalse(reader.matchConsume("ab"));

        reader.consume();
        reader.consume();
        assertTrue(reader.matchConsume("ef"));
        assertFalse(reader.matchConsume("efg"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        reader.consume();
        reader.consume();
        assertTrue(reader.matchConsumeIgnoreCase("EF"));
        assertFalse(reader.matchConsumeIgnoreCase("ab"));

        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertTrue(reader.matchConsumeIgnoreCase("GH"));
        assertFalse(reader.matchConsumeIgnoreCase("ef"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(reader.containsIgnoreCase("ab"));
        assertTrue(reader.containsIgnoreCase("EF"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }
}