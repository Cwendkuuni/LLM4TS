package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExtendedBufferedReaderTest {
    @Test
    public void testRead() throws Exception {
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals('a', br.read());
        assertEquals('b', br.read());
        assertEquals('c', br.read());
        assertEquals(-1, br.read());
        
        br.close();
    }
    
    @Test
    public void testReadAgain() throws Exception {
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals('a', br.read());
        assertEquals('b', br.read());
        assertEquals('b', br.readAgain());
        
        br.close();
    }
    
    @Test
    public void testReadArray() throws Exception {
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        char[] buf = new char[2];
        assertEquals(2, br.read(buf, 0, 2));
        assertEquals('a', buf[0]);
        assertEquals('b', buf[1]);
        
        assertEquals(1, br.read(buf, 0, 2));
        assertEquals('c', buf[0]);
        
        assertEquals(-1, br.read(buf, 0, 2));
        
        br.close();
    }
    
    @Test
    public void testReadLine() throws Exception {
        Reader reader = new StringReader("abc\ndef\n");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals("abc", br.readLine());
        assertEquals("def", br.readLine());
        assertEquals(null, br.readLine());
        
        br.close();
    }
    
    @Test
    public void testLookAhead() throws Exception {
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals('a', br.lookAhead());
        assertEquals('a', br.read());
        assertEquals('b', br.lookAhead());
        
        br.close();
    }
    
    @Test
    public void testGetLineNumber() throws Exception {
        Reader reader = new StringReader("abc\ndef\ng");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals(0, br.getLineNumber());
        br.readLine();
        assertEquals(1, br.getLineNumber());
        br.readLine();
        assertEquals(2, br.getLineNumber());
        
        br.close();
    }
}