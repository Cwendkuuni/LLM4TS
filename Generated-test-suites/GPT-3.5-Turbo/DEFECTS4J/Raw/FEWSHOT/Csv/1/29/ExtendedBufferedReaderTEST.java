package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class ExtendedBufferedReaderTest {
    
    private ExtendedBufferedReader reader;
    
    @Before
    public void setUp() {
        Reader r = new StringReader("Hello, world!\nThis is a test\n");
        reader = new ExtendedBufferedReader(r);
    }
    
    @Test
    public void testRead() throws IOException {
        assertEquals((int) 'H', reader.read());
        assertEquals((int) 'e', reader.read());
        assertEquals((int) 'l', reader.read());
        assertEquals((int) 'l', reader.read());
        assertEquals((int) 'o', reader.read());
        assertEquals((int) ',', reader.read());
        assertEquals((int) ' ', reader.read());
        assertEquals((int) 'w', reader.read());
        assertEquals((int) 'o', reader.read());
        assertEquals((int) 'r', reader.read());
        assertEquals((int) 'l', reader.read());
        assertEquals((int) 'd', reader.read());
        assertEquals((int) '!', reader.read());
        assertEquals((int) '\n', reader.read());
        assertEquals(0, reader.getLineNumber());
    }
    
    @Test
    public void testReadAgain() throws IOException {
        reader.read();
        assertEquals((int) 'H', reader.readAgain());
    }
    
    @Test
    public void testReadLine() throws IOException {
        assertEquals("Hello, world!", reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals("This is a test", reader.readLine());
        assertEquals(2, reader.getLineNumber());
    }
    
    @Test
    public void testLookAhead() throws IOException {
        assertEquals((int) 'H', reader.lookAhead());
        assertEquals((int) 'H', reader.read());
        assertEquals((int) 'e', reader.lookAhead());
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(0, reader.getLineNumber());
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
        reader.readLine();
        assertEquals(2, reader.getLineNumber());
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        char[] buf = new char[10];
        int len = reader.read(buf, 0, 10);
        assertEquals(10, len);
        assertEquals("Hello, wor", new String(buf, 0, len));
        assertEquals(1, reader.getLineNumber());
        
        len = reader.read(buf, 0, 10);
        assertEquals(10, len);
        assertEquals("ld!\nThis i", new String(buf, 0, len));
        assertEquals(2, reader.getLineNumber());
    }
    
    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        char[] buf = new char[0];
        int len = reader.read(buf, 0, 0);
        assertEquals(0, len);
    }
    
    @Test
    public void testReadWithPartialBuffer() throws IOException {
        char[] buf = new char[10];
        int len = reader.read(buf, 0, 5);
        assertEquals(5, len);
        assertEquals("Hello", new String(buf, 0, len));
        assertEquals(1, reader.getLineNumber());
        
        len = reader.read(buf, 0, 10);
        assertEquals(5, len);
        assertEquals(", wor", new String(buf, 0, len));
        assertEquals(1, reader.getLineNumber());
    }
}