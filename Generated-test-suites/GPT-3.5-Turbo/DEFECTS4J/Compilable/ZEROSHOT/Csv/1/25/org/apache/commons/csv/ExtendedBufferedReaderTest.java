package org.apache.commons.csv;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        Reader stringReader = new StringReader("Line 1\nLine 2\r\nLine 3");
        reader = new ExtendedBufferedReader(stringReader);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals(' ', reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        reader.read();
        assertEquals('L', reader.readAgain());
        reader.read();
        assertEquals('i', reader.readAgain());
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        char[] buffer = new char[5];
        int len = reader.read(buffer, 0, 5);
        assertEquals(5, len);
        assertEquals('L', buffer[0]);
        assertEquals('i', buffer[1]);
        assertEquals('n', buffer[2]);
        assertEquals('e', buffer[3]);
        assertEquals(' ', buffer[4]);
    }

    @Test
    public void testReadLine() throws IOException {
        assertEquals("Line 1", reader.readLine());
        assertEquals("Line 2", reader.readLine());
        assertEquals("Line 3", reader.readLine());
        assertNull(reader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals('L', reader.lookAhead());
        reader.read();
        assertEquals('i', reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(0, reader.getLineNumber());
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
        reader.readLine();
        assertEquals(2, reader.getLineNumber());
    }
}