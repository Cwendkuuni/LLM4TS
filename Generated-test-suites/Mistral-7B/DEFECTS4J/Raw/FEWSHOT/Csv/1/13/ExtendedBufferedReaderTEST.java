package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExtendedBufferedReaderTest {

    private ByteArrayOutputStream output;
    private ByteArrayInputStream input;
    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        output = new ByteArrayOutputStream();
        input = new ByteArrayInputStream(output.toByteArray());
        reader = new ExtendedBufferedReader(new InputStreamReader(input));
    }

    @Test
    public void testRead() throws IOException {
        for (char c = (char) ExtendedBufferedReader.END_OF_STREAM; c != '\n';) {
            int read = reader.read();
            assertEquals(c, read);
            c = (char) reader.readAgain();
        }

        int read = reader.read();
        assertNull(read);
    }

    @Test
    public void testReadLine() throws IOException {
        output.write("line1\nline2\nline3\n".getBytes());

        String line1 = reader.readLine();
        assertEquals("line1", line1);

        String line2 = reader.readLine();
        assertEquals("line2", line2);

        String line3 = reader.readLine();
        assertEquals("line3", line3);

        int read = reader.read();
        assertNull(read);
    }

    @Test
    public void testPeek() throws IOException {
        for (char c = 'a'; c != '\n'; c++) {
            output.write(c);
            output.write(' ');
        }
        output.write('\n');

        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(output.toByteArray())));

        for (char c = 'a'; c != '\n';) {
            int peek = reader.lookAhead();
            assertEquals(c, (char) peek);
            int read = reader.read();
            assertEquals(c, read);
            c = (char) reader.readAgain();
        }

        int peek = reader.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, peek);

        int read = reader.read();
        assertNull(read);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLookAheadMark() throws IOException {
        reader.mark(-1);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        output.write("line1\nline2\nline3\n".getBytes());

        assertEquals(0, reader.getLineNumber());

        String line1 = reader.readLine();
        assertEquals(1, reader.getLineNumber());

        String line2 = reader.readLine();
        assertEquals(2, reader.getLineNumber());

        String line3 = reader.readLine();
        assertEquals(3, reader.getLineNumber());

        int read = reader.read();
        assertEquals(-1, reader.getLineNumber());
    }
}