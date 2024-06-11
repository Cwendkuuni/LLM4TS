package org.apache.commons.csv;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        StringReader sr = new StringReader("test line 1\n test line 2\r\n test line 3");
        reader = new ExtendedBufferedReader(sr);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('t', reader.read());
        assertEquals('e', reader.read());
        assertEquals('s', reader.read());
        assertEquals('t', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('l', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('1', reader.read());
        assertEquals('\n', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('t', reader.read());
        assertEquals('e', reader.read());
        assertEquals('s', reader.read());
        assertEquals('t', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('l', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('2', reader.read());
        assertEquals('\r', reader.read());
        assertEquals('\n', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('t', reader.read());
        assertEquals('e', reader.read());
        assertEquals('s', reader.read());
        assertEquals('t', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('l', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('3', reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        reader.read();
        reader.read();
        reader.read();
        assertEquals('t', reader.readAgain());
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        assertEquals('e', reader.readAgain());
        reader.read();
        reader.read();
        reader.read();
        assertEquals('s', reader.readAgain());
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        char[] buffer = new char[10];
        int len = reader.read(buffer, 0, buffer.length);
        assertEquals(10, len);
        assertArrayEquals("test line 1".toCharArray(), buffer);

        len = reader.read(buffer, 0, buffer.length);
        assertEquals(10, len);
        assertArrayEquals(" tes".toCharArray(), buffer);

        len = reader.read(buffer, 0, buffer.length);
        assertEquals(4, len);
        assertArrayEquals("t li".toCharArray(), buffer);

        len = reader.read(buffer, 0, buffer.length);
        assertEquals(-1, len);
    }

    @Test
    public void testReadLine() throws IOException {
        String line = reader.readLine();
        assertEquals("test line 1", line);

        line = reader.readLine();
        assertEquals(" test line 2", line);

        line = reader.readLine();
        assertEquals(" test line 3", line);

        line = reader.readLine();
        assertNull(line);

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testLookAhead() throws IOException {
        int c = reader.lookAhead();
        assertEquals('t', c);

        reader.read();
        c = reader.lookAhead();
        assertEquals('e', c);

        reader.read();
        c = reader.lookAhead();
        assertEquals('s', c);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        int lineNumber = reader.getLineNumber();
        assertEquals(0, lineNumber);

        reader.readLine();
        lineNumber = reader.getLineNumber();
        assertEquals(1, lineNumber);

        reader.readLine();
        lineNumber = reader.getLineNumber();
        assertEquals(2, lineNumber);

        reader.readLine();
        lineNumber = reader.getLineNumber();
        assertEquals(3, lineNumber);
    }

}