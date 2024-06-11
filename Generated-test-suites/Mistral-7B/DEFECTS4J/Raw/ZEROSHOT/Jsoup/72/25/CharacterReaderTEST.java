package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        char[] chars = new char[10];
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), chars.length);

        // Test constructor
        assertNotNull(reader);
        assertEquals(chars.length, reader.charBuf.length);
        assertNotNull(reader.reader);
        assertEquals(0, reader.bufLength);
        assertEquals(0, reader.bufSplitPoint);
        assertEquals(0, reader.bufPos);
        assertEquals(0, reader.readerPos);
        assertEquals(0, reader.bufMark);

        // Test isEmpty
        assertTrue(reader.isEmpty());

        // Test current and consume
        reader.bufferUp();
        reader.advance();
        reader.bufferUp();
        assertEquals('H', reader.current());
        assertEquals('H', reader.consume());
        assertEquals('e', reader.current());
        assertEquals('e', reader.consume());
        assertTrue(reader.isEmpty());

        // Test pos
        reader.mark();
        reader.advance();
        reader.advance();
        reader.consume();
        reader.rewindToMark();
        assertEquals(3, reader.pos());

        // Test nextIndexOf
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.consume();
        reader.bufferUp();
        reader.advance();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();

        int indexOfL = reader.nextIndexOf('l');
        int indexOfW = reader.nextIndexOf('w');
        int indexOfD = reader.nextIndexOf('d');

        assertEquals(11, indexOfL);
        assertEquals(4, indexOfW);
        assertEquals(-1, indexOfD);

        // Test consumeTo
        String strConsumed = reader.consumeTo('l');
        assertEquals("Hello Wo", strConsumed);

        // Test consumeTo with null character
        strConsumed = reader.consumeTo(CharacterReader.nullChar);
        assertEquals("Hello Wo", strConsumed);

        // Test consumeTo with empty string
        strConsumed = reader.consumeTo("");
        assertEquals("Hello Wo", strConsumed);

        // Test consumeTo with String
        String strExpected = "Hello Wo";
        strConsumed = reader.consumeTo("World");
        assertEquals(strExpected, strConsumed);

        // Test consumeToAny
        char[] charsToFind = new char[]{'l', 'w'};
        String strConsumedAny = reader.consumeToAny(charsToFind);
        assertEquals(strExpected, strConsumedAny);

        // Test consumeToAnySorted
        char[] charsToFindSorted = new char[]{'w', 'l'};
        strConsumedAny = reader.consumeToAnySorted(charsToFindSorted);
        assertEquals(strExpected, strConsumedAny);

        // Test consumeData
        String strConsumedData = reader.consumeData();
        assertEquals("Hello Wo", strConsumedData);

        // Test consumeTagName
        String strConsumedTagName = reader.consumeTagName();
        assertEquals("Hello Wo", strConsumedTagName);

        // Test consumeToEnd
        String strConsumedToEnd = reader.consumeToEnd();
        assertEquals("Hello World", strConsumedToEnd);

        // Test consumeLetterSequence
        String strConsumedLetterSequence = reader.consumeLetterSequence();
        assertEquals("Hello", strConsumedLetterSequence);

        // Test consumeLetterThenDigitSequence
        String strConsumedLetterThenDigitSequence = reader.consumeLetterThenDigitSequence();
        assertEquals("Hello0", strConsumedLetterThenDigitSequence);

        // Test consumeHexSequence
        String strConsumedHexSequence = reader.consumeHexSequence();
        assertEquals("HelloWorld", strConsumedHexSequence);

        // Test consumeDigitSequence
        String strConsumedDigitSequence = reader.consumeDigitSequence();
        assertEquals("12345", strConsumedDigitSequence);

        // Test matches
        assertTrue(reader.matches('l'));
        assertFalse(reader.matches('d'));

        // Test matches with String
        assertTrue(reader.matches("World"));

        // Test matchesIgnoreCase
        assertTrue(reader.matchesIgnoreCase("world"));

        // Test matchesAny
        assertTrue(reader.matchesAny('l'));
        assertTrue(reader.matchesAny('w'));
        assertFalse(reader.matchesAny('d'));

        // Test matchesAnySorted
        assertTrue(reader.matchesAnySorted(new char[]{'l', 'w'}));
        assertFalse(reader.matchesAnySorted(new char[]{'d'}));

        // Test matchesLetter
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('3'));

        // Test matchesDigit
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('5'));

        // Test matchConsume
        strConsumed = reader.matchConsume("World");
        assertEquals("World", strConsumed);

        // Test matchConsumeIgnoreCase
        strConsumed = reader.matchConsumeIgnoreCase("world");
        assertEquals("World", strConsumed);

        // Test containsIgnoreCase
        assertTrue(reader.containsIgnoreCase("</title>"));
        assertTrue(reader.containsIgnoreCase("</style>"));
        assertFalse(reader.containsIgnoreCase("</script>"));

        // Test toString
        assertEquals("Hello World", reader.toString());

        // Test cacheString
        String strCached = reader.cacheString(charBuf, stringCache, 0, 12);
        assertEquals("Hello World", strCached);

        // Test rangeEquals
        assertTrue(reader.rangeEquals(0, 12, "Hello World"));

        // Test bufferUp
        reader.bufferUp();
        assertEquals(0, reader.bufPos);
        assertEquals(12, reader.bufLength);
        assertEquals(12, reader.bufSplitPoint);
        assertEquals(0, reader.readerPos);
        assertEquals(0, reader.bufMark);

        // Test dispose
        reader.dispose();
        assertNull(reader.reader);

        // Test CharacterReader with Reader
        reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", reader.toString());

        // Test CharacterReader with String
        reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.toString());

        // Test CharacterReader with IOException
        try {
            reader = new CharacterReader(new java.io.FileReader("file.txt"), chars.length);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderWithUncheckedIOException() {
        CharacterReader reader = new CharacterReader(new java.io.FileReader("file.txt"), maxBufferLen);
    }
}