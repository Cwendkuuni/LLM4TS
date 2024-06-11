package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        String testString = "foo\nbar\nbaz\nqux";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        int readChar;
        int expectedLine = 0;

        // First read
        readChar = extendedReader.read();
        assertEquals('f', readChar);
        assertEquals(expectedLine, extendedReader.getLineNumber());

        readChar = extendedReader.read();
        assertEquals('o', readChar);
        assertEquals(expectedLine, extendedReader.getLineNumber());

        readChar = extendedReader.read();
        assertEquals('o', readChar);
        assertEquals(expectedLine, extendedReader.getLineNumber());

        readChar = extendedReader.read();
        assertEquals('\n', readChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        // Second read
        readChar = extendedReader.read();
        assertEquals('b', readChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readChar = extendedReader.read();
        assertEquals('a', readChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readChar = extendedReader.read();
        assertEquals('\n', readChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readChar = extendedReader.read();
        assertEquals('b', readChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        // Third read
        readChar = extendedReader.read();
        assertEquals('a', readChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readChar = extendedReader.read();
        assertEquals('z', readChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readChar = extendedReader.read();
        assertEquals('\n', readChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        // Fourth read
        readChar = extendedReader.read();
        assertEquals('q', readChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readChar = extendedReader.read();
        assertEquals('u', readChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readChar = extendedReader.read();
        assertEquals('x', readChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readChar = extendedReader.read();
        assertNull(readChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        assertEquals(-1, extendedReader.read());
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        String testString = "foo\nbar\nbaz\nqux";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        int lookAheadChar;
        int expectedLine = 0;

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('\n', lookAheadChar);
        assertEquals(expectedLine, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('f', lookAheadChar);
        assertEquals(expectedLine, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('o', lookAheadChar);
        assertEquals(expectedLine, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('o', lookAheadChar);
        assertEquals(expectedLine, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('\n', lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('b', lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('a', lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('\n', lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('b', lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('a', lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('\n', lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('b', lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('a', lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('z', lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('\n', lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('q', lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('u', lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals('x', lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertNull(lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        lookAheadChar = extendedReader.lookAhead();
        assertEquals(-1, lookAheadChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws IOException {
        String testString = "foo\nbar\nbaz\nqux";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        int readAgainChar;
        int expectedLine = 0;

        // First read
        readAgainChar = extendedReader.read();
        assertEquals('f', readAgainChar);
        assertEquals(expectedLine, extendedReader.getLineNumber());

        readAgainChar = extendedReader.readAgain();
        assertEquals('f', readAgainChar);
        assertEquals(expectedLine, extendedReader.getLineNumber());

        // Second read
        readAgainChar = extendedReader.read();
        assertEquals('o', readAgainChar);
        assertEquals(expectedLine, extendedReader.getLineNumber());

        readAgainChar = extendedReader.readAgain();
        assertEquals('o', readAgainChar);
        assertEquals(expectedLine, extendedReader.getLineNumber());

        readAgainChar = extendedReader.read();
        assertEquals('\n', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.readAgain();
        assertEquals('\n', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        // Third read
        readAgainChar = extendedReader.read();
        assertEquals('b', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.readAgain();
        assertEquals('b', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.read();
        assertEquals('a', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.readAgain();
        assertEquals('a', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.read();
        assertEquals('\n', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.readAgain();
        assertEquals('\n', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.read();
        assertEquals('b', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.readAgain();
        assertEquals('b', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.read();
        assertEquals('a', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.readAgain();
        assertEquals('a', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.read();
        assertEquals('z', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.readAgain();
        assertEquals('z', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.read();
        assertEquals('\n', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.readAgain();
        assertEquals('\n', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.read();
        assertEquals('q', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.readAgain();
        assertEquals('q', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.read();
        assertEquals('u', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.readAgain();
        assertEquals('u', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.read();
        assertEquals('x', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.readAgain();
        assertEquals('x', readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readAgainChar = extendedReader.read();
        assertNull(readAgainChar);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        assertEquals(-1, extendedReader.read());
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());
    }

    @Test
    public void testReadArray() throws IOException {
        String testString = "foo\nbar\nbaz\nqux";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        char[] charArray = new char[6];

        int readChars;
        int expectedLine = 0;

        // First read
        readChars = extendedReader.read(charArray, 0, charArray.length);
        assertEquals(5, readChars);
        assertEquals('f', charArray[0]);
        assertEquals('o', charArray[1]);
        assertEquals('o', charArray[2]);
        assertEquals('o', charArray[3]);
        assertEquals(' ', charArray[4]);
        assertEquals(expectedLine, extendedReader.getLineNumber());

        // Second read
        readChars = extendedReader.read(charArray, 1, charArray.length - 1);
        assertEquals(4, readChars);
        assertEquals('a', charArray[0]);
        assertEquals('r', charArray[1]);
        assertEquals('b', charArray[2]);
        assertEquals('\n', charArray[3]);
        assertEquals('\n', extendedReader.read());
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        // Third read
        readChars = extendedReader.read(charArray, 0, charArray.length);
        assertEquals(3, readChars);
        assertEquals('b', charArray[0]);
        assertEquals('a', charArray[1]);
        assertEquals('z', charArray[2]);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        // Fourth read
        readChars = extendedReader.read(charArray, 1, charArray.length - 1);
        assertEquals(3, readChars);
        assertEquals('q', charArray[0]);
        assertEquals('u', charArray[1]);
        assertEquals('x', charArray[2]);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readChars = extendedReader.read(charArray, 0, charArray.length);
        assertEquals(-1, readChars);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        String testString = "foo\nbar\nbaz\nqux";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        String readLine;
        int expectedLine = 0;

        // First read
        readLine = extendedReader.readLine();
        assertEquals("foo", readLine);
        assertEquals(expectedLine, extendedReader.getLineNumber());

        readLine = extendedReader.readLine();
        assertEquals("bar", readLine);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readLine = extendedReader.readLine();
        assertEquals("baz", readLine);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readLine = extendedReader.readLine();
        assertEquals("qux", readLine);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        readLine = extendedReader.readLine();
        assertNull(readLine);
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());

        assertEquals(-1, extendedReader.read());
        assertEquals(expectedLine + 1, extendedReader.getLineNumber());
    }
}