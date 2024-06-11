
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
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.read());
        
        br.close();
    }
    
    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        br.read();
        br.read();
        br.read();
        
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.readAgain());
        
        br.close();
    }
    
    @Test
    public void testReadCharArray() throws IOException {
        Reader reader = new StringReader("abc\ndef");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        char[] buf = new char[3];
        int len = br.read(buf, 0, 3);
        
        assertEquals(3, len);
        assertEquals('a', buf[0]);
        assertEquals('b', buf[1]);
        assertEquals('c', buf[2]);
        
        len = br.read(buf, 0, 3);
        
        assertEquals(1, len);
        assertEquals('d', buf[0]);
        
        len = br.read(buf, 0, 3);
        
        assertEquals(1, len);
        assertEquals('e', buf[0]);
        
        br.close();
    }
    
    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("abc\ndef");
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
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.lookAhead());
        
        br.close();
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new StringReader("abc\ndef");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals(0, br.getLineNumber());
        
        br.readLine();
        assertEquals(1, br.getLineNumber());
        
        br.readLine();
        assertEquals(2, br.getLineNumber());
        
        br.readLine();
        assertEquals(2, br.getLineNumber());
        
        br.close();
    }
    
}

