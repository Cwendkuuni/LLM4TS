package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        String input = "This is a test for CharacterReader.\n" +
                "It should be able to read characters from various sources.\n" +
                "Input from a StringReader:\n" +
                "abcd\n" +
                "efgh\n" +
                "ijkl\n" +
                "mnop\n" +
                "qrst\n" +
                "uvwxyz\n";

        // Test CharacterReader with String input
        CharacterReader reader1 = new CharacterReader(new StringReader(input));
        Assert.assertEquals('a', reader1.current());
        reader1.consume();
        Assert.assertEquals('b', reader1.current());
        reader1.advance();
        Assert.assertEquals('b', reader1.current());
        reader1.mark();
        reader1.rewindToMark();
        Assert.assertEquals('b', reader1.current());
        Assert.assertEquals(5, reader1.pos());
        Assert.assertFalse(reader1.isEmpty());
        Assert.assertEquals('z', reader1.consume());
        Assert.assertTrue(reader1.isEmpty());
        reader1.advance();
        reader1.advance();
        Assert.assertEquals(12, reader1.pos());
        reader1.bufferUp();
        reader1.bufferUp();
        Assert.assertEquals(14, reader1.pos());

        // Test CharacterReader with Reader input
        Reader reader2 = new StringReader(input);
        CharacterReader reader3 = new CharacterReader(reader2);
        Assert.assertEquals('a', reader3.current());
        reader3.consume();
        Assert.assertEquals('b', reader3.current());
        reader3.advance();
        Assert.assertEquals('b', reader3.current());
        reader3.mark();
        reader3.rewindToMark();
        Assert.assertEquals('b', reader3.current());
        Assert.assertEquals(5, reader3.pos());
        Assert.assertFalse(reader3.isEmpty());
        Assert.assertEquals('z', reader3.consume());
        Assert.assertTrue(reader3.isEmpty());
        reader3.advance();
        reader3.advance();
        Assert.assertEquals(12, reader3.pos());
        reader3.bufferUp();
        reader3.bufferUp();
        Assert.assertEquals(14, reader3.pos());

        // Test CharacterReader with empty input
        CharacterReader reader4 = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader4.isEmpty());
        Assert.assertEquals(0, reader4.pos());
        reader4.bufferUp();
        Assert.assertEquals(0, reader4.pos());

        // Test CharacterReader with null input
        CharacterReader reader5 = new CharacterReader(null);
        Assert.assertNull(reader5);

        // Test CharacterReader with maxBufferLen input
        CharacterReader reader6 = new CharacterReader(new StringReader(input.repeat(1000)));
        Assert.assertEquals(CharacterReader.EOF, reader6.current());
        Assert.assertTrue(reader6.isEmpty());
        reader6.mark();
        reader6.rewindToMark();
        Assert.assertEquals(CharacterReader.EOF, reader6.current());
        Assert.assertTrue(reader6.isEmpty());
        reader6.advance();
        Assert.assertEquals(CharacterReader.EOF, reader6.current());
        Assert.assertTrue(reader6.isEmpty());
    }
}