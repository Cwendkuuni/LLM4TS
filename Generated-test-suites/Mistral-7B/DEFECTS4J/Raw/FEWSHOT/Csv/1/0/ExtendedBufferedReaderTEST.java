package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("foo\nbar\nbaz\nqux"));
        int currentChar;

        currentChar = reader.read();
        assertEquals('f', currentChar);
        assertEquals(0, reader.getLineNumber());

        currentChar = reader.read();
        assertEquals('o', currentChar);
        assertEquals(0, reader.getLineNumber());

        currentChar = reader.read();
        assertEquals('o', currentChar);
        assertEquals(0, reader.getLineNumber());

        currentChar = reader.read();
        assertEquals('\n', currentChar);
        assertEquals(1, reader.getLineNumber());

        currentChar = reader.read();
        assertEquals('b', currentChar);
        assertEquals(1, reader.getLineNumber());

        currentChar = reader.read();
        assertEquals('a', currentChar);
        assertEquals(1, reader.getLineNumber());

        currentChar = reader.read();
        assertEquals('\n', currentChar);
        assertEquals(2, reader.getLineNumber());

        currentChar = reader.read();
        assertEquals('b', currentChar);
        assertEquals(2, reader.getLineNumber());

        currentChar = reader.read();
        assertEquals('a', currentChar);
        assertEquals(2, reader.getLineNumber());

        currentChar = reader.read();
        assertEquals('z', currentChar);
        assertEquals(2, reader.getLineNumber());

        currentChar = reader.read();
        assertEquals('\n', currentChar);
        assertEquals(3, reader.getLineNumber());

        currentChar = reader.read();
        assertEquals('q', currentChar);
        assertEquals(3, reader.getLineNumber());

        currentChar = reader.read();
        assertEquals('u', currentChar);
        assertEquals(3, reader.getLineNumber());

        currentChar = reader.read();
        assertEquals('x', currentChar);
        assertEquals(3, reader.getLineNumber());

        currentChar = reader.read();
        assertNull(currentChar);
        assertEquals(3, reader.getLineNumber());

        currentChar = reader.readAgain();
        assertEquals(lastChar, currentChar);
        assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testReadLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("foo\nbar\nbaz\nqux\n"));
        String line;

        line = reader.readLine();
        assertEquals("foo", line);
        assertEquals(1, reader.getLineNumber());
        assertEquals('f', reader.readAgain());

        line = reader.readLine();
        assertEquals("bar", line);
        assertEquals(2, reader.getLineNumber());
        assertEquals('\n', reader.readAgain());

        line = reader.readLine();
        assertEquals("baz", line);
        assertEquals(3, reader.getLineNumber());
        assertEquals('b', reader.readAgain());

        line = reader.readLine();
        assertEquals("qux", line);
        assertEquals(4, reader.getLineNumber());
        assertEquals('\n', reader.readAgain());

        line = reader.readLine();
        assertNull(line);
        assertEquals(4, reader.getLineNumber());
        assertEquals(END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testLookAhead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("foo\nbar\nbaz\nqux\n"));
        int lookAheadChar;

        lookAheadChar = reader.lookAhead();
        assertEquals('f', lookAheadChar);
        assertEquals(0, reader.getLineNumber());

        reader.read(); // read 'f'

        lookAheadChar = reader.lookAhead();
        assertEquals('o', lookAheadChar);
        assertEquals(0, reader.getLineNumber());

        lookAheadChar = reader.lookAhead();
        assertEquals('o', lookAheadChar);
        assertEquals(0, reader.getLineNumber());

        lookAheadChar = reader.lookAhead();
        assertEquals('\n', lookAheadChar);
        assertEquals(1, reader.getLineNumber());

        reader.readLine(); // read "foo"

        lookAheadChar = reader.lookAhead();
        assertEquals('b', lookAheadChar);
        assertEquals(1, reader.getLineNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLookAhead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("foo\nbar\nbaz\nqux\n"));
        reader.lookAhead(-1);
    }
}