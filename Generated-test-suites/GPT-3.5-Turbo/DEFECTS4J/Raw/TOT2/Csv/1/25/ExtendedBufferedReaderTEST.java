package org.apache.commons.csv.test;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        StringReader reader = new StringReader("test\nstring\nfor\nreading\n");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        assertEquals('t', extendedBufferedReader.read());
        assertEquals('e', extendedBufferedReader.read());
        assertEquals(1, extendedBufferedReader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("test\nstring\nfor\nreading\n");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        // Reading to the end of stream
        while (extendedBufferedReader.read() != ExtendedBufferedReader.END_OF_STREAM) {
        }
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedBufferedReader.readAgain());
    }

    @Test
    public void testReadCharArray() throws IOException {
        StringReader reader = new StringReader("test\r\nstring\nfor\rreading\r\n");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        char[] buffer = new char[6];

        assertEquals(6, extendedBufferedReader.read(buffer, 0, buffer.length));
        assertEquals(2, extendedBufferedReader.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        StringReader reader = new StringReader("test\nstring\nfor\nreading\n");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        assertEquals("test", extendedBufferedReader.readLine());
        assertEquals(1, extendedBufferedReader.getLineNumber());
        
        assertEquals("string", extendedBufferedReader.readLine());
        assertEquals(2, extendedBufferedReader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        StringReader reader = new StringReader("test\nstring\nfor\nreading\n");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        assertEquals('t', extendedBufferedReader.lookAhead());
        assertEquals(1, extendedBufferedReader.getLineNumber());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("test\nstring\nfor\nreading\n");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        assertEquals(0, extendedBufferedReader.getLineNumber());
        extendedBufferedReader.readLine(); // Increment line number
        assertEquals(1, extendedBufferedReader.getLineNumber());
    }
}