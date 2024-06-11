package org.apache.commons.csv;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;
    private StringReader stringReader;

    @Before
    public void setup() {
        String testString = "Hello World!\nThis is a test string.\n";
        stringReader = new StringReader(testString);
        reader = new ExtendedBufferedReader(stringReader);
    }

    @Test
    public void testRead() throws Exception {
        // Typical use case
        int result = reader.read();
        assertEquals('H', result);

        // Edge case: End of stream
        while (reader.read() != ExtendedBufferedReader.END_OF_STREAM) {
            // Consume all characters from the reader
        }
        assertNull(reader.read());
    }

    @Test
    public void testReadAgain() throws Exception {
        // Typical use case
        reader.read(); // Consume 'H'
        int result = reader.readAgain();
        assertEquals('H', result);

        // Edge case: End of stream
        while (reader.read() != ExtendedBufferedReader.END_OF_STREAM) {
            // Consume all characters from the reader
        }
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadCharArray() throws Exception {
        // Typical use case
        char[] buf = new char[5];
        int result = reader.read(buf, 0, buf.length);
        assertEquals(5, result);
        assertEquals("Hello", String.valueOf(buf));

        // Edge case: End of stream
        while (reader.read(buf, 0, buf.length) != -1) {
            // Consume all characters from the reader
        }
        assertEquals(-1, reader.read(buf, 0, buf.length));
    }

    @Test
    public void testReadLine() throws Exception {
        // Typical use case
        String line = reader.readLine();
        assertEquals("Hello World!", line);

        // Edge case: Empty line
        line = reader.readLine();
        assertEquals("", line);

        // Edge case: End of stream
        while (reader.readLine() != null) {
            // Consume all lines from the reader
        }
        assertNull(reader.readLine());
    }

    @Test
    public void testLookAhead() throws Exception {
        // Typical use case
        int result = reader.lookAhead();
        assertEquals('H', result);

        // Edge case: End of stream
        while (reader.lookAhead() != ExtendedBufferedReader.END_OF_STREAM) {
            // Consume all characters from the reader
            reader.read();
        }
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws Exception {
        // Typical use case
        assertEquals(0, reader.getLineNumber());
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
        reader.readLine();
        assertEquals(2, reader.getLineNumber());

        // Edge case: End of stream
        while (reader.readLine() != null) {
            // Consume all lines from the reader
        }
        assertEquals(-1, reader.getLineNumber());
    }
}