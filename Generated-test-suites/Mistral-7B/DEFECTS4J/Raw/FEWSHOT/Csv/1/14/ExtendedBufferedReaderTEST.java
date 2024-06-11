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
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int readChar;

        for (int i = 0; i < input.length(); i++) {
            readChar = extReader.read();
            assertEquals(input.charAt(i), (char) readChar);
        }

        readChar = extReader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, readChar);
    }

    @Test
    public void testReadAgain() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int readChar;
        char expectedChar;

        for (int i = 0; i < input.length(); i++) {
            readChar = extReader.read();
            expectedChar = (char) readChar;

            readChar = extReader.readAgain();
            assertEquals(expectedChar, (char) readChar);
        }

        readChar = extReader.read();
        readChar = extReader.readAgain();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, readChar);
    }

    @Test
    public void testReadLine() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        String line;

        line = extReader.readLine();
        assertEquals("foo", line);

        line = extReader.readLine();
        assertEquals("bar", line);

        line = extReader.readLine();
        assertEquals("baz", line);

        line = extReader.readLine();
        assertEquals("qux", line);

        line = extReader.readLine();
        assertNull(line);
    }

    @Test
    public void testLookAhead() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int lookAheadChar;
        int readChar;

        lookAheadChar = extReader.lookAhead();
        assertEquals('\n', lookAheadChar);

        readChar = extReader.read();
        assertEquals('f', (char) readChar);

        lookAheadChar = extReader.lookAhead();
        assertEquals('o', (char) lookAheadChar);

        lookAheadChar = extReader.lookAhead();
        assertEquals('\n', lookAheadChar);

        readChar = extReader.read();
        assertEquals('b', (char) readChar);

        lookAheadChar = extReader.lookAhead();
        assertEquals('a', (char) lookAheadChar);

        lookAheadChar = extReader.lookAhead();
        assertEquals('\n', lookAheadChar);

        readChar = extReader.read();
        assertEquals('b', (char) readChar);

        lookAheadChar = extReader.lookAhead();
        assertEquals('a', (char) lookAheadChar);

        lookAheadChar = extReader.lookAhead();
        assertEquals('\n', lookAheadChar);

        readChar = extReader.read();
        assertEquals('z', (char) readChar);

        lookAheadChar = extReader.lookAhead();
        assertEquals('a', (char) lookAheadChar);

        lookAheadChar = extReader.lookAhead();
        assertEquals('\n', lookAheadChar);

        readChar = extReader.read();
        assertEquals('q', (char) readChar);

        lookAheadChar = extReader.lookAhead();
        assertEquals('u', (char) lookAheadChar);

        lookAheadChar = extReader.lookAhead();
        assertEquals('\n', lookAheadChar);

        readChar = extReader.read();
        assertEquals('x', (char) readChar);

        lookAheadChar = extReader.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lookAheadChar);

        readChar = extReader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, readChar);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int lineNumber;

        lineNumber = extReader.getLineNumber();
        assertEquals(0, lineNumber);

        extReader.readLine();

        lineNumber = extReader.getLineNumber();
        assertEquals(1, lineNumber);

        extReader.readLine();

        lineNumber = extReader.getLineNumber();
        assertEquals(2, lineNumber);

        extReader.readLine();

        lineNumber = extReader.getLineNumber();
        assertEquals(3, lineNumber);

        extReader.readLine();

        lineNumber = extReader.getLineNumber();
        assertEquals(4, lineNumber);

        extReader.readLine();

        lineNumber = extReader.getLineNumber();
        assertEquals(-1, lineNumber);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int readLength;
        char[] buffer = new char[3];

        readLength = extReader.read(buffer, 0, buffer.length);
        assertEquals(3, readLength);
        assertEquals("foo", new String(buffer));

        readLength = extReader.read(buffer, 0, buffer.length);
        assertEquals(3, readLength);
        assertEquals("bar", new String(buffer));

        readLength = extReader.read(buffer, 0, buffer.length);
        assertEquals(3, readLength);
        assertEquals("baz", new String(buffer));

        readLength = extReader.read(buffer, 0, buffer.length);
        assertEquals(3, readLength);
        assertEquals("qux", new String(buffer));

        readLength = extReader.read(buffer, 0, buffer.length);
        assertEquals(-1, readLength);

        readLength = extReader.read(buffer, 0, 3);
        assertEquals(3, readLength);
        assertEquals("qux", new String(buffer));

        readLength = extReader.read(buffer, 1, 2);
        assertEquals(2, readLength);
        assertEquals("xy", new String(buffer));

        readLength = extReader.read(buffer, 3, 3);
        assertEquals(-1, readLength);

        readLength = extReader.read(buffer, 0, 1);
        assertEquals(1, readLength);
        assertEquals('x', buffer[0]);

        readLength = extReader.read(buffer, 2, 1);
        assertEquals(1, readLength);
        assertEquals('x', buffer[2]);
    }

    @Test
    public void testReadWithOffsetAndLengthAtEOF() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        char[] buffer = new char[3];

        int readLength = extReader.read(buffer, 0, buffer.length);
        assertEquals(3, readLength);
        assertEquals("foo", new String(buffer));

        readLength = extReader.read(buffer, 0, buffer.length);
        assertEquals(3, readLength);
        assertEquals("bar", new String(buffer));

        readLength = extReader.read(buffer, 0, buffer.length);
        assertEquals(3, readLength);
        assertEquals("baz", new String(buffer));

        readLength = extReader.read(buffer, 0, buffer.length);
        assertEquals(3, readLength);
        assertEquals("qux", new String(buffer));

        readLength = extReader.read(buffer, 0, buffer.length);
        assertEquals(-1, readLength);

        readLength = extReader.read(buffer, 0, 3);
        assertEquals(-1, readLength);

        readLength = extReader.read(buffer, 0, 1);
        assertEquals(-1, readLength);

        readLength = extReader.read(buffer, 1, 1);
        assertEquals(-1, readLength);

        readLength = extReader.read(buffer, 2, 1);
        assertEquals(-1, readLength);
    }

    @Test
    public void testReadWithOffsetAndNegativeLength() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        char[] buffer = new char[3];

        int readLength = extReader.read(buffer, 2, 3);
        assertEquals(-1, readLength);
    }

    @Test
    public void testLookAheadAtEOF() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int lookAheadChar;

        lookAheadChar = extReader.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lookAheadChar);

        lookAheadChar = extReader.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lookAheadChar);
    }

    @Test
    public void testReadWithMark() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int readChar;
        int markChar;

        extReader.mark(1);

        readChar = extReader.read();
        assertEquals('f', (char) readChar);

        extReader.reset();

        readChar = extReader.read();
        assertEquals('f', (char) readChar);

        extReader.mark(1);

        readChar = extReader.read();
        assertEquals('b', (char) readChar);

        extReader.reset();

        readChar = extReader.read();
        assertEquals('b', (char) readChar);

        extReader.mark(1);

        readChar = extReader.read();
        assertEquals('a', (char) readChar);

        extReader.reset();

        readChar = extReader.read();
        assertEquals('a', (char) readChar);

        extReader.mark(1);

        readChar = extReader.read();
        assertEquals('z', (char) readChar);

        extReader.reset();

        readChar = extReader.read();
        assertEquals('q', (char) readChar);

        extReader.mark(1);

        readChar = extReader.read();
        assertEquals('u', (char) readChar);

        extReader.reset();

        readChar = extReader.read();
        assertEquals('x', (char) readChar);

        extReader.mark(1);

        readChar = extReader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, readChar);

        extReader.reset();

        readChar = extReader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, readChar);
    }
}