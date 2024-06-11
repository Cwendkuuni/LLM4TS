package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExtendedBufferedReaderTest {
    @Test
    public void testRead() throws IOException {
        String input = "Hello World";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        
        assertEquals('H', reader.read());
        assertEquals('e', reader.read());
        assertEquals('l', reader.read());
        assertEquals('l', reader.read());
        assertEquals('o', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('W', reader.read());
        assertEquals('o', reader.read());
        assertEquals('r', reader.read());
        assertEquals('l', reader.read());
        assertEquals('d', reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        String input = "Hello World";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        
        assertEquals('o', reader.readAgain());
    }
    
    @Test
    public void testReadCharArray() throws IOException {
        String input = "Hello World";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        char[] buf = new char[5];
        
        assertEquals(5, reader.read(buf, 0, 5));
        assertEquals('H', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('l', buf[2]);
        assertEquals('l', buf[3]);
        assertEquals('o', buf[4]);
        
        assertEquals(5, reader.read(buf, 0, 5));
        assertEquals(' ', buf[0]);
        assertEquals('W', buf[1]);
        assertEquals('o', buf[2]);
        assertEquals('r', buf[3]);
        assertEquals('l', buf[4]);
        
        assertEquals(2, reader.read(buf, 0, 5));
        assertEquals('d', buf[0]);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, buf[1]);
    }
    
    @Test
    public void testReadArrayEmpty() throws IOException {
        String input = "";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        char[] buf = new char[5];
        
        assertEquals(-1, reader.read(buf, 0, 5));
    }
    
    @Test
    public void testReadArrayPartial() throws IOException {
        String input = "Hello";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        char[] buf = new char[10];
        
        assertEquals(5, reader.read(buf, 0, 10));
        assertEquals('H', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('l', buf[2]);
        assertEquals('l', buf[3]);
        assertEquals('o', buf[4]);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, buf[5]);
        
        assertEquals(-1, reader.read(buf, 0, 10));
    }
    
    @Test
    public void testReadLine() throws IOException {
        String input = "Hello\nWorld\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        
        assertEquals("Hello", reader.readLine());
        assertEquals("World", reader.readLine());
        assertNull(reader.readLine());
    }
    
    @Test
    public void testLookAhead() throws IOException {
        String input = "Hello";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        
        assertEquals('e', reader.lookAhead());
        assertEquals('H', reader.read());
        assertEquals('e', reader.lookAhead());
        assertEquals('e', reader.read());
        assertEquals('l', reader.lookAhead());
        assertEquals('l', reader.read());
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        String input = "Hello\nWorld\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        
        assertEquals(0, reader.getLineNumber());
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
        reader.readLine();
        assertEquals(2, reader.getLineNumber());
    }
}