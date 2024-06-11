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
        Reader r = new StringReader("This is a test string");
        reader = new ExtendedBufferedReader(r);
    }
    
    @Test
    public void testRead() throws IOException {
        assertEquals(84, reader.read());
    }
    
    @Test
    public void testReadAgain() throws IOException {
        assertEquals(UNDEFINED, reader.readAgain());
        reader.read();
        assertEquals(84, reader.readAgain());
    }
    
    @Test
    public void testReadCharArray() throws IOException {
        char[] buf = new char[5];
        assertEquals(5, reader.read(buf, 0, 5));
    }
    
    @Test
    public void testReadLine() throws IOException {
        assertEquals("This is a test string", reader.readLine());
    }
    
    @Test
    public void testLookAhead() throws IOException {
        assertEquals(84, reader.lookAhead());
    }
    
    @Test
    public void testGetLineNumber() {
        assertEquals(0, reader.getLineNumber());
    }
}