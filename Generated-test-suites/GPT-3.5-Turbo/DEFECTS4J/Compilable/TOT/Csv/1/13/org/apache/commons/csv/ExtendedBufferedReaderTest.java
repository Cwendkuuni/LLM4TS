package org.apache.commons.csv;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        assertEquals('H', br.read());
        assertEquals('e', br.read());
        assertEquals('l', br.read());
        assertEquals('l', br.read());
        assertEquals('o', br.read());
        assertEquals(' ', br.read());
        assertEquals('W', br.read());
        assertEquals('o', br.read());
        assertEquals('r', br.read());
        assertEquals('l', br.read());
        assertEquals('d', br.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.read());

        br.close();
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        assertEquals('H', br.read());
        assertEquals('e', br.read());
        
        assertEquals('e', br.readAgain());

        br.close();
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        Reader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        char[] buffer = new char[5];
        assertEquals(5, br.read(buffer, 0, 5));
        assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o'}, buffer);

        assertEquals('\n', br.read());

        assertEquals(5, br.read(buffer, 0, 5));
        assertArrayEquals(new char[]{'W', 'o', 'r', 'l', 'd'}, buffer);

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.read());

        br.close();
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("Hello\nWorld\n");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        assertEquals("Hello", br.readLine());
        assertEquals("World", br.readLine());
        assertNull(br.readLine());

        br.close();
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("Hello");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        assertEquals('H', br.lookAhead());
        assertEquals('H', br.read());

        assertEquals('e', br.lookAhead());
        assertEquals('e', br.read());

        assertEquals('l', br.lookAhead());
        assertEquals('l', br.read());

        assertEquals('l', br.lookAhead());
        assertEquals('l', br.read());

        assertEquals('o', br.lookAhead());
        assertEquals('o', br.read());

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.lookAhead());

        br.close();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new StringReader("Hello\nWorld\n");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        assertEquals(0, br.getLineNumber());

        br.readLine();
        assertEquals(1, br.getLineNumber());

        br.readLine();
        assertEquals(2, br.getLineNumber());

        br.close();
    }

}