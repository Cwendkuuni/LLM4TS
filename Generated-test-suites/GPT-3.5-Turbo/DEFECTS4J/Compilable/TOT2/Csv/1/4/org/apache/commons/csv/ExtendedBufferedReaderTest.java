package org.apache.commons.csv;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        StringReader stringReader = new StringReader("Test\nstring");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);

        int result = reader.read();
        
        assertEquals('T', result);
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws IOException {
        StringReader stringReader = new StringReader("Test\nstring");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);

        reader.read();
        int result = reader.readAgain();
        
        assertEquals('T', result);
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadCharArray() throws IOException {
        StringReader stringReader = new StringReader("Test\r\nstring");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);
        char[] buf = new char[5];

        int result = reader.read(buf, 0, 5);
        
        assertEquals(5, result);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        StringReader stringReader = new StringReader("Test\r\nstring");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);

        String result = reader.readLine();
        
        assertEquals("Test", result);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        StringReader stringReader = new StringReader("Test\rstring");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);

        int result = reader.lookAhead();
        
        assertEquals('r', result);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        StringReader stringReader = new StringReader("Test\nstring\nnewline");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);
        reader.readLine();

        int result = reader.getLineNumber();
        
        assertEquals(1, result);
    }

}