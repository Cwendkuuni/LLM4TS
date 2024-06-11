package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader(new StringReader("sample text for testing"));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
        reader.consumeToEnd();
        assertEquals(12, reader.pos());
    }

    @Test
    public void testCurrent() {
        assertEquals('\u0000', reader.current());
        reader.consume();
        assertEquals('s', reader.current());
        reader.consumeToEnd();
        assertEquals('\u0000', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        Reader readerWithException = new StringReader("") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        };
        CharacterReader charReader = new CharacterReader(readerWithException, 1);
        charReader.consume();
    }

    @Test
    public void testConsume() {
        reader.consume();
        assertEquals('s', reader.current());
        reader.consume();
        assertEquals('a', reader.current());
        reader.consumeToEnd();
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('\u0000', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.consume();
        reader.advance();
        assertEquals('a', reader.current());
    }

    @Test
    public void testMark() {
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('s', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        reader.consume();
        assertEquals(0, reader.nextIndexOf('s'));
        reader.consume();
        assertEquals(1, reader.nextIndexOf('s'));
        reader.consumeToEnd();
        assertEquals(-1, reader.nextIndexOf('z'));
        reader.advance();
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();

        assertEquals(11, reader.nextIndexOf("sample"));
        assertEquals(-1, reader.nextIndexOf("test"));
    }

    @Test
    public void testConsumeTo() {
        String sample = reader.consumeTo(' ');
        assertEquals("sample", sample);
        assertEquals(' ', reader.current());

        String text = reader.consumeTo("text");
        assertEquals("sample text", text);
        assertEquals('t', reader.current());

        reader.consume();
        String rest = reader.consumeToEnd();
        assertEquals("text", rest);
        assertEquals('\u0000', reader.current());
    }

    @Test
    public void testConsumeToAny() {
        String result = reader.consumeToAny(new char[]{'t', 'e', 'x', 't'});
        assertEquals("sampletext", result);
        assertEquals('\u0000', reader.current());

        result = reader.consumeToAny(new char[]{'a', 'b', 'c'});
        assertEquals("", result);
        assertEquals('\u0000', reader.current());
    }

    @Test
    public void testConsumeToAnySorted() {
        String result = reader.consumeToAnySorted(new char[]{'z', 'e', 'x', 'a', 'm', 'p', 'l', 'e'});
        assertEquals("sampletext", result);
        assertEquals('\u0000', reader.current());
    }

    @Test
    public void testConsumeData() {
        String result = reader.consumeData();
        assertEquals("sample", result);
        assertEquals('\u0000', reader.current());
    }

    @Test
    public void testConsumeTagName() {
        String result = reader.consumeTagName();
        assertEquals("sampletext", result);
        assertEquals('\u0000', reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        String result = reader.consumeToEnd();
        assertEquals("sampletext", result);
        assertEquals('\u0000', reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        String result = reader.consumeLetterSequence();
        assertEquals("sample", result);
        assertEquals('\u0000', reader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String result = reader.consumeLetterThenDigitSequence();
        assertEquals("sample1", result);
        assertEquals('\u0000', reader.current());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('s'));
        assertFalse(reader.matches('z'));

        String test = "sampletext";
        for (int i = 0; i < test.length(); i++) {
            assertTrue(reader.matches(test.charAt(i)));
        }
    }

    @Test
    public void testMatchesIgnoreCase() {
        String test = "SampleText";
        for (int i = 0; i < test.length(); i++) {
            assertTrue(reader.matchesIgnoreCase(test.charAt(i) + ""));
        }

        assertTrue(reader.matchesIgnoreCase("sample"));
        assertTrue(reader.matchesIgnoreCase("text"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('s'));
        assertFalse(reader.matchesAny('z'));

        char[] test = {'s', 'a', 'm', 'p', 'l', 'e', 't', 'x'};
        for (int i = 0; i < test.length; i++) {
            assertTrue(reader.matchesAny(test[i]));
        }
    }

    @Test
    public void testMatchesAnySorted() {
        char[] test = {'z', 'e', 'x', 'a', 'm', 'p', 'l', 'e', 's', 't'};
        assertTrue(reader.matchesAnySorted(test));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("sample"));
        assertEquals("text", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("sample"));
        assertEquals("text", reader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/style"));
    }
}