package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader input = new StringReader("Hello World!");
        CharacterReader reader = new CharacterReader(input);
        assertEquals("Hello World!", reader.toString());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertEquals("Hello World!", reader.toString());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testBufferUp() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        reader.bufferUp();
        assertEquals(testInput, reader.toString());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertEquals(testInput.length(), reader.pos());
    }

    @Test
    public void testIsEmpty() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertEquals('B', reader.current());
        reader.consume();
        assertEquals('B', reader.current());
        reader.consume();
        assertEquals('u', reader.current());
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertEquals('B', reader.consume());
        assertEquals('u', reader.current());
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testUnconsume() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        reader.unconsume();
        assertEquals("Buffer Up Test", reader.toString());
        assertEquals('u', reader.current());
    }

    @Test
    public void testAdvance() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        reader.advance();
        assertEquals('u', reader.current());
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        reader.advance();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMark() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        reader.mark();
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.rewindToMark();
        assertEquals("Buffer Up", reader.toString());
        assertEquals('B', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput)) {
            @Override
            void bufferUp() {
                throw new IOException("Test IOException");
            }
        };
        reader.consume();
    }

    @Test
    public void testNextIndexOf() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertEquals(testInput.indexOf('T'), reader.nextIndexOf('T'));
        assertEquals(-1, reader.nextIndexOf('Z'));
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertEquals(-1, reader.nextIndexOf('Z'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertEquals(testInput.indexOf("Test"), reader.nextIndexOf("Test"));
        assertEquals(-1, reader.nextIndexOf("ABC"));
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertEquals(-1, reader.nextIndexOf("ABC"));
    }

    @Test
    public void testConsumeTo() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertEquals("Test", reader.consumeTo('T').trim());
        assertEquals("Test Up", reader.consumeTo(' ').trim());
        assertEquals("Buffer Up", reader.consumeTo('>').trim());
        assertEquals("", reader.consumeTo('Z').trim());
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertEquals("", reader.consumeTo('Z').trim());
    }

    @Test
    public void testConsumeToWithSequence() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertEquals("Test", reader.consumeTo("Test").trim());
        assertEquals("Test Up", reader.consumeTo(" ").trim());
        assertEquals("Buffer Up", reader.consumeTo(">").trim());
        assertEquals("", reader.consumeTo("Z").trim());
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertEquals("", reader.consumeTo("Z").trim());
    }

    @Test
    public void testConsumeToAny() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertEquals("Test ", reader.consumeToAny(new char[]{'T', ' '}).trim());
        assertEquals("Buffer Up Test", reader.consumeToAnySorted(new char[]{' ', 'T', '>'}).trim());
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertEquals("", reader.consumeToAny(new char[]{'Z'}).trim());
    }

    @Test
    public void testConsumeToAnySorted() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertEquals("Test", reader.consumeToAnySorted(new char[]{'T'}).trim());
        assertEquals("Buffer Up Test", reader.consumeToAnySorted(new char[]{' ', 'T'}).trim());
        assertEquals("Buffer Up Test", reader.consumeToAnySorted(new char[]{' ', 'T', '>'}).trim());
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertEquals("", reader.consumeToAnySorted(new char[]{'Z'}).trim());
    }

    @Test
    public void testConsumeData() {
        String testInput = "&lt; &amp; &gt; &quot; &apos; null &lt;!DOCTYPE html &gt;";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertEquals("&lt;&amp;&gt;&quot;&apos;null&lt;!DOCTYPE html &gt;", reader.consumeData().trim());
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertEquals("", reader.consumeData().trim());
    }

    @Test
    public void testConsumeTagName() {
        String testInput = "<title>Test</title><style>Test</style>";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertEquals("title", reader.consumeTagName().trim());
        assertEquals("style", reader.consumeTagName().trim());
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertEquals("", reader.consumeTagName().trim());
    }

    @Test
    public void testMatchingMethods() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertTrue(reader.matches('B'));
        assertTrue(reader.matches('u'));
        assertTrue(reader.matches('B', 1));
        assertTrue(reader.matches('u', 1));
        assertTrue(reader.matches("Bu"));
        assertTrue(reader.matches("Bu", 1));
        assertTrue(reader.matches("Bu", 2));
        assertFalse(reader.matches('Z'));
        assertFalse(reader.matches("Zu"));
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertFalse(reader.matches('Z'));
        assertFalse(reader.matches("Zu"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertTrue(reader.matchesIgnoreCase("B"));
        assertTrue(reader.matchesIgnoreCase("u"));
        assertTrue(reader.matchesIgnoreCase("BU"));
        assertTrue(reader.matchesIgnoreCase("BU", 1));
        assertTrue(reader.matchesIgnoreCase("Bu", 1));
        assertTrue(reader.matchesIgnoreCase("bu", 1));
        assertFalse(reader.matchesIgnoreCase("Z"));
        assertFalse(reader.matchesIgnoreCase("Zu"));
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertFalse(reader.matchesIgnoreCase("Z"));
        assertFalse(reader.matchesIgnoreCase("Zu"));
    }

    @Test
    public void testMatchesAny() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertTrue(reader.matchesAny('B', 'u'));
        assertTrue(reader.matchesAny('B', 'u', 'u'));
        assertTrue(reader.matchesAny('B', 'u', 'u', 1));
        assertTrue(reader.matchesAny('B', 'u', 'u', 2));
        assertFalse(reader.matchesAny('Z'));
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertFalse(reader.matchesAny('Z'));
    }

    @Test
    public void testMatchesAnySorted() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertTrue(reader.matchesAnySorted('B', 'u'));
        assertTrue(reader.matchesAnySorted('B', 'u', 'u'));
        assertTrue(reader.matchesAnySorted('B', 'u', 'u', 1));
        assertTrue(reader.matchesAnySorted('B', 'u', 'u', 2));
        assertFalse(reader.matchesAnySorted('Z'));
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertFalse(reader.matchesAnySorted('Z'));
    }

    @Test
    public void testMatchesLetter() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertTrue(reader.matchesLetter());
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String testInput = "Buffer Up Test";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));
        assertTrue(reader.matchesDigit());
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.consumeTo('>');
        reader.consumeToEnd();
        assertFalse(reader.matchesDigit());
    }
}