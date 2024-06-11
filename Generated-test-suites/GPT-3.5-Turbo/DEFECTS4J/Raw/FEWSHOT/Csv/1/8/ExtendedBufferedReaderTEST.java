package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals('a', br.read());
        assertEquals('b', br.read());
        assertEquals('c', br.read());
        assertEquals(-1, br.read());
        
        br.close();
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals('a', br.read());
        assertEquals('b', br.read());
        assertEquals('b', br.readAgain());
        
        br.close();
    }

    @Test
    public void testReadCharArray() throws IOException {
        Reader reader = new StringReader("abc\ndef\n");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        char[] buf = new char[5];
        
        assertEquals(5, br.read(buf, 0, 5));
        assertEquals('a', buf[0]);
        assertEquals('b', buf[1]);
        assertEquals('c', buf[2]);
        assertEquals('\n', buf[3]);
        assertEquals('d', buf[4]);
        
        assertEquals(2, br.read(buf, 0, 5));
        assertEquals('e', buf[0]);
        assertEquals('f', buf[1]);
        assertEquals('\n', buf[2]);
        assertEquals('\0', buf[3]);
        assertEquals('\0', buf[4]);
        
        assertEquals(-1, br.read(buf, 0, 5));
        
        br.close();
    }
    
    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("abc\ndef\n");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals("abc", br.readLine());
        assertEquals("def", br.readLine());
        assertEquals(null, br.readLine());
        
        br.close();
    }
    
    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals('a', br.lookAhead());
        assertEquals('a', br.read());
        assertEquals('b', br.lookAhead());
        assertEquals('b', br.read());
        assertEquals('c', br.lookAhead());
        assertEquals('c', br.read());
        assertEquals(-1, br.lookAhead());
        assertEquals(-1, br.read());
        
        br.close();
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new StringReader("abc\ndef\n");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals(0, br.getLineNumber());
        br.read();
        assertEquals(0, br.getLineNumber());
        br.readLine();
        assertEquals(1, br.getLineNumber());
        br.readLine();
        assertEquals(2, br.getLineNumber());
        
        br.close();
    }
}