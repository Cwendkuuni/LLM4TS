package org.apache.commons.csv;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;
import java.io.Reader;
import java.io.IOException;


public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("Hello World\nTesting\n123");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        
        assertEquals('H', extendedBufferedReader.read());
        assertEquals('e', extendedBufferedReader.read());
        assertEquals(0, extendedBufferedReader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("Hello World\nTesting\n123");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        
        assertEquals('H', extendedBufferedReader.read());
        assertEquals('e', extendedBufferedReader.read());
        assertEquals('e', extendedBufferedReader.readAgain());
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        Reader reader = new StringReader("Hello World\nTesting");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        char[] buf = new char[5];
        
        int len = extendedBufferedReader.read(buf, 0, 5);
        
        assertEquals(5, len);
        assertEquals('o', buf[4]);
        assertEquals(1, extendedBufferedReader.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("Hello World\nTesting");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        
        String line = extendedBufferedReader.readLine();
        
        assertEquals("Hello World", line);
        assertEquals(1, extendedBufferedReader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        
        assertEquals('e', extendedBufferedReader.lookAhead());
        assertEquals('H', extendedBufferedReader.read());  // make sure the character is not consumed
    }
}