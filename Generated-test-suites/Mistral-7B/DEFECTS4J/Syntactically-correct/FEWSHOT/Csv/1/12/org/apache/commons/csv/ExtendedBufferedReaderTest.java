package org.apache.commons.csv;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExtendedBufferedReaderTest {

    private Reader reader;
    private ExtendedBufferedReader extendedReader;

    @Before
    public void setUp() throws IOException {
        String input = "line1\r\nline2\r\nline3\r\nline4\r\n";
        reader = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        extendedReader = new ExtendedBufferedReader(reader);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('l', extendedReader.read());
        assertEquals('i', extendedReader.read());
        assertEquals('n', extendedReader.read());
        assertEquals('e', extendedReader.read());
        assertEquals('1', extendedReader.read());
        assertEquals('\n', extendedReader.read());
        assertEquals('l', extendedReader.read());
        assertEquals('i', extendedReader.read());
        assertEquals('n', extendedReader.read());
        assertEquals('e', extendedReader.read());
        assertEquals('2', extendedReader.read());
        assertEquals('\n', extendedReader.read());
        assertEquals('l', extendedReader.read());
        assertEquals('i', extendedReader.read());
        assertEquals('n', extendedReader.read());
        assertEquals('e', extendedReader.read());
        assertEquals('3', extendedReader.read());
        assertEquals('\n', extendedReader.read());
        assertEquals('l', extendedReader.read());
        assertEquals('i', extendedReader.read());
        assertEquals('n', extendedReader.read());
        assertEquals('e', extendedReader.read());
        assertEquals('4', extendedReader.read());
        assertEquals('\r', extendedReader.read());
        assertEquals('\n', extendedReader.read());
        assertEquals(-1, extendedReader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        assertEquals('l', extendedReader.read());
        assertEquals('l', extendedReader.readAgain());

        assertEquals('i', extendedReader.read());
        assertEquals('i', extendedReader.readAgain());

        assertEquals('n', extendedReader.read());
        assertEquals('n', extendedReader.readAgain());

        assertEquals('1', extendedReader.read());
        assertEquals('1', extendedReader.readAgain());

        assertEquals('\n', extendedReader.read());
        assertEquals('\n', extendedReader.readAgain());

        assertEquals(-1, extendedReader.read());
        assertEquals(-1, extendedReader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        assertEquals("line1", extendedReader.readLine());
        assertEquals("line2", extendedReader.readLine());
        assertEquals("line3", extendedReader.readLine());
        assertEquals("line4", extendedReader.readLine());
        assertEquals(null, extendedReader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals('l', extendedReader.lookAhead());
        assertEquals('l', extendedReader.read());

        assertEquals('i', extendedReader.lookAhead());
        assertEquals('i', extendedReader.read());

        assertEquals('n', extendedReader.lookAhead());
        assertEquals('n', extendedReader.read());

        assertEquals('1', extendedReader.lookAhead());
        assertEquals('1', extendedReader.read());

        assertEquals('\n', extendedReader.lookAhead());
        assertEquals('\n', extendedReader.read());

        assertEquals(-1, extendedReader.lookAhead());
        assertEquals(-1, extendedReader.read());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(0, extendedReader.getLineNumber());
        assertEquals("line1", extendedReader.readLine());
        assertEquals(1, extendedReader.getLineNumber());
        assertEquals("line2", extendedReader.readLine());
        assertEquals(2, extendedReader.getLineNumber());
        assertEquals("line3", extendedReader.readLine());
        assertEquals(3, extendedReader.getLineNumber());
        assertEquals("line4", extendedReader.readLine());
        assertEquals(4, extendedReader.getLineNumber());
        assertEquals(null, extendedReader.readLine());
        assertEquals(4, extendedReader.getLineNumber());
    }

    @Test
    public void testPeekLine() throws IOException {
        String expectedLine = "line1";
        String line = extendedReader.readLine();
        verify(reader, times(1)).readLine();
        assertEquals(expectedLine, line);

        String peekedLine = extendedReader.peekLine();
        verify(reader, times(2)).readLine();
        assertEquals(expectedLine, peekedLine);

        line = extendedReader.readLine();
        verify(reader, times(3)).readLine();
        assertEquals("line2", line);

        peekedLine = extendedReader.peekLine();
        verify(reader, times(4)).readLine();
        assertEquals("line2", peekedLine);

        line = extendedReader.readLine();
        verify(reader, times(5)).readLine();
        assertEquals("line3", line);

        peekedLine = extendedReader.peekLine();
        verify(reader, times(6)).readLine();
        assertEquals("line3", peekedLine);

        line = extendedReader.readLine();
        verify(reader, times(7)).readLine();
        assertEquals("line4", line);

        peekedLine = extendedReader.peekLine();
        verify(reader, times(8)).readLine();
        assertEquals("line4", peekedLine);

        line = extendedReader.readLine();
        verify(reader, times(9)).readLine();
        assertEquals(null, line);

        peekedLine = extendedReader.peekLine();
        verify(reader, times(10)).readLine();
        assertEquals(null, peekedLine);
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekLineAfterEndOfStream() throws IOException {
        extendedReader.readLine();
        extendedReader.readLine();
        extendedReader.readLine();
        extendedReader.readLine();
        extendedReader.readLine();
        extendedReader.readLine();
        extendedReader.readLine();
        extendedReader.readLine();
        extendedReader.readLine();
        extendedReader.readLine();
        extendedReader.readLine();

        String peekedLine = extendedReader.peekLine();
        fail();
    }
}