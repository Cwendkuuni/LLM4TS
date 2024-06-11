package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    private CharacterReader reader;
    private char[] testInput = new char[]{'a', 'b', 'c', 'd'};

    @Before
    public void setUp() {
        reader = new CharacterReader(new String(testInput));
    }

    @After
    public void tearDown() {
        reader.close();
    }

    @Test
    public void testNewInstance() {
        CharacterReader reader1 = new CharacterReader(new StringReader(""));
        CharacterReader reader2 = new CharacterReader(new StringReader(""));
        CharacterReader reader3 = new CharacterReader(new StringReader("abcd"));

        assertEquals("Expected both readers to have the same position after creation", 0, reader1.pos());
        assertEquals("Expected both readers to have the same position after creation", 0, reader2.pos());
        assertEquals("Expected both readers to have the same position after creation", 0, reader3.pos());

        assertEquals("Expected both readers to read the same character", 'a', reader3.current());
        assertEquals("Expected both readers to read the same character", 'a', reader3.consume());

        reader3.mark();
        reader3.rewindToMark();

        assertEquals("Expected the character reader to be at the same position after mark and rewind", 0, reader3.pos());
        assertFalse("Expected the character reader to not have consumed the character after mark and rewind", reader3.isEmptyNoBufferUp());
        assertEquals("Expected the character reader to read the same character after mark and rewind", 'a', reader3.current());

        reader3.consume();
        reader3.advance();

        assertEquals("Expected the character reader to have consumed one character and moved to the next position", 1, reader3.pos());
        assertFalse("Expected the character reader to not have consumed the second character", reader3.isEmptyNoBufferUp());
        assertEquals("Expected the character reader to read the second character", 'b', reader3.current());

        reader3.consume();
        reader3.consume();
        reader3.consume();

        assertEquals("Expected the character reader to have consumed three characters and reached the end of the input", 3, reader3.pos());
        assertTrue("Expected the character reader to have reached the end of the input", reader3.isEmptyNoBufferUp());

        reader3.mark();
        reader3.rewindToMark();

        assertEquals("Expected the character reader to have returned to the previous position", 1, reader3.pos());
        assertFalse("Expected the character reader to not have consumed the second character", reader3.isEmptyNoBufferUp());
        assertEquals("Expected the character reader to read the second character", 'b', reader3.current());

        reader1.close();
        reader2.close();
        reader3.close();
    }

    @Test
    public void testNextIndexOf() {
        reader.bufferUp();

        assertEquals("Expected nextIndexOf to return -1 for a character not in the input", -1, reader.nextIndexOf('z'));
        assertEquals("Expected nextIndexOf to return the index of the first occurrence of a character", 0, reader.nextIndexOf('a'));
        assertEquals("Expected nextIndexOf to return the index of the first occurrence of a character", 1, reader.nextIndexOf('b'));
        assertEquals("Expected nextIndexOf to return the index of the first occurrence of a character", 3, reader.nextIndexOf('d'));

        reader.mark();
        reader.rewindToMark();

        assertEquals("Expected nextIndexOf to return the same index after mark and rewind", 0, reader.nextIndexOf('a'));
        assertEquals("Expected nextIndexOf to return the same index after mark and rewind", 1, reader.nextIndexOf('b'));
        assertEquals("Expected nextIndexOf to return the same index after mark and rewind", 3, reader.nextIndexOf('d'));
    }

    @Test
    public void testNextIndexOfSequence() {
        reader.bufferUp();

        assertEquals("Expected nextIndexOf to return -1 for a non-existent sequence", -1, reader.nextIndexOf("abcd"));
        assertEquals("Expected nextIndexOf to return the index of the first occurrence of a sequence", 0, reader.nextIndexOf("ab"));
        assertEquals("Expected nextIndexOf to return the index of the first occurrence of a sequence", 2, reader.nextIndexOf("cd"));

        reader.mark();
        reader.rewindToMark();

        assertEquals("Expected nextIndexOf to return the same index after mark and rewind", 0, reader.nextIndexOf("ab"));
        assertEquals("Expected nextIndexOf to return the same index after mark and rewind", 2, reader.nextIndexOf("cd"));
    }

    @Test(expected = IllegalStateException.class)
    public void testReadPastEnd() {
        reader.bufferUp();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();

        // This should throw an IllegalStateException as the reader has been moved past the end of the input
        reader.consume();
    }
}