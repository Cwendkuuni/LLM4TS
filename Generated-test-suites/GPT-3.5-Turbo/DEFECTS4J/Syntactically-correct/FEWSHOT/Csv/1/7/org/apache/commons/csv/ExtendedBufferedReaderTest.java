package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {
    @Test
    public void testRead() throws IOException {
        StringReader reader = new StringReader("hello world");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        
        int result = ebr.read();
        
        assertEquals('h', result);
        assertEquals(1, ebr.getLineNumber());
    }
    
    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("hello world");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        
        int result = ebr.read();
        int again = ebr.readAgain();
        
        assertEquals('h', result);
        assertEquals('h', again);
    }
    
    @Test
    public void testRead_withLineTerminator() throws IOException {
        StringReader reader = new StringReader("hello\nworld");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        
        int result = ebr.read();
        
        assertEquals('h', result);
        assertEquals(1, ebr.getLineNumber());
        
        result = ebr.read();
        
        assertEquals('e', result);
        assertEquals(1, ebr.getLineNumber());
        
        result = ebr.read();
        
        assertEquals('l', result);
        assertEquals(1, ebr.getLineNumber());
        
        result = ebr.read();
        
        assertEquals('l', result);
        assertEquals(1, ebr.getLineNumber());
        
        result = ebr.read();
        
        assertEquals('o', result);
        assertEquals(1, ebr.getLineNumber());
        
        result = ebr.read();
        
        assertEquals('\n', result);
        assertEquals(2, ebr.getLineNumber());
        
        result = ebr.read();
        
        assertEquals('w', result);
        assertEquals(2, ebr.getLineNumber());
    }
    
    @Test
    public void testRead_withLookAhead() throws IOException {
        StringReader reader = new StringReader("hello world");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        
        int result = ebr.lookAhead();
        
        assertEquals('h', result);
        
        result = ebr.read();
        
        assertEquals('h', result);
        assertEquals(1, ebr.getLineNumber());
        
        result = ebr.read();
        
        assertEquals('e', result);
        assertEquals(1, ebr.getLineNumber());
    }
    
    @Test
    public void testReadLine() throws IOException {
        StringReader reader = new StringReader("hello\nworld");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        
        String result = ebr.readLine();
        
        assertEquals("hello", result);
        assertEquals(1, ebr.getLineNumber());
        
        result = ebr.readLine();
        
        assertEquals("world", result);
        assertEquals(2, ebr.getLineNumber());
    }
    
    @Test
    public void testReadLine_EOF() throws IOException {
        StringReader reader = new StringReader("hello world");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        
        String result = ebr.readLine();
        
        assertEquals("hello world", result);
        assertEquals(1, ebr.getLineNumber());
        
        result = ebr.readLine();
        
        assertNull(result);
        assertEquals(-1, ebr.readAgain());
    }
}