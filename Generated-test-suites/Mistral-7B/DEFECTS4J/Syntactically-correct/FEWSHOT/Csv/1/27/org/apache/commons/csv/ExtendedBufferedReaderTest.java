package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        int ch;
        int lineNumber = 0;

        for (int i = 0; i < 10; i++) {
            ch = extendedReader.read();
            lineNumber += (ch == '\n' ? 1 : 0);
            assertEquals(i < input.length() ? (char) (i < input.length() ? input.charAt(i) : ExtendedBufferedReader.END_OF_STREAM) : ExtendedBufferedReader.END_OF_STREAM, (char) ch);
        }

        assertEquals(4, lineNumber);
    }

    @Test
    public void testReadAgain() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        int ch;
        int lineNumber = 0;

        for (int i = 0; i < 11; i++) {
            ch = extendedReader.read();
            lineNumber += (ch == '\n' ? 1 : 0);
            if (i < input.length()) {
                assertEquals(input.charAt(i), (char) ch);
            } else {
                assertEquals(ExtendedBufferedReader.END_OF_STREAM, (char) ch);
            }

            assertEquals(ch, extendedReader.readAgain());
        }

        assertEquals(5, lineNumber);
    }

    @Test
    public void testReadLine() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        String line;

        for (int i = 0; i < 4; i++) {
            line = extendedReader.readLine();
            assertEquals("foo", line);
            assertEquals(1, extendedReader.getLineNumber());
            assertNotEquals('\n', extendedReader.readAgain());
        }

        line = extendedReader.readLine();
        assertEquals("bar", line);
        assertEquals(2, extendedReader.getLineNumber());
        assertEquals('\n', extendedReader.readAgain());

        line = extendedReader.readLine();
        assertEquals("baz", line);
        assertEquals(3, extendedReader.getLineNumber());
        assertEquals('\n', extendedReader.readAgain());

        line = extendedReader.readLine();
        assertNull(line);
        assertEquals(4, extendedReader.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedReader.readAgain());
    }

    @Test
    public void testLookAhead() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        int ch;

        for (int i = 0; i < 4; i++) {
            ch = extendedReader.lookAhead();
            assertEquals(i < input.length() ? input.charAt(i) : ExtendedBufferedReader.END_OF_STREAM, (char) ch);
        }

        ch = extendedReader.lookAhead();
        assertEquals('\n', (char) ch);

        ch = extendedReader.read();
        assertEquals('\n', (char) ch);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, (char) extendedReader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        for (int i = 0; i < 5; i++) {
            int lineNumber = extendedReader.getLineNumber();
            assertEquals(i + 1, lineNumber);
        }

        int lineNumber = extendedReader.getLineNumber();
        assertEquals(6, lineNumber);
    }
}