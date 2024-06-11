package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

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
    }
    
    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals('H', br.read());
        assertEquals('e', br.read());
        assertEquals('l', br.read());
        assertEquals('l', br.read());
        assertEquals('o', br.read());
        
        assertEquals('o', br.readAgain());
    }
    
    @Test
    public void testReadCharArray() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        char[] buf = new char[5];
        assertEquals(5, br.read(buf, 0, 5));
        assertEquals('H', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('l', buf[2]);
        assertEquals('l', buf[3]);
        assertEquals('o', buf[4]);
        
        assertEquals(6, br.read(buf, 0, 6));
        assertEquals(' ', buf[0]);
        assertEquals('W', buf[1]);
        assertEquals('o', buf[2]);
        assertEquals('r', buf[3]);
        assertEquals('l', buf[4]);
        assertEquals('d', buf[5]);

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.read(buf, 0, 5));
    }
    
    @Test
    public void testReadLine() throws IOException {
        String input = "Hello\nWorld\n";
        Reader reader = new StringReader(input);
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals("Hello", br.readLine());
        assertEquals("World", br.readLine());
        assertNull(br.readLine());
    }
    
    @Test
    public void testLookAhead() throws IOException {
        String input = "Hello World";
        Reader reader = new StringReader(input);
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals('H', br.lookAhead());
        
        br.read();
        assertEquals('e', br.lookAhead());
        
        br.read();
        assertEquals('l', br.lookAhead());
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        String input = "Hello\nWorld\n";
        Reader reader = new StringReader(input);
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals(0, br.getLineNumber());
        
        br.readLine();
        assertEquals(1, br.getLineNumber());
        
        br.readLine();
        assertEquals(2, br.getLineNumber());
    }
}