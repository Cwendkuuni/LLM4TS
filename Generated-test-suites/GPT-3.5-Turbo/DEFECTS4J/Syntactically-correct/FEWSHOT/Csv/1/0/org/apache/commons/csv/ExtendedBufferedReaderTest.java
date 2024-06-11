
package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {
    
    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        int ch = br.read();
        assertEquals('H', ch);
        
        ch = br.read();
        assertEquals('e', ch);
        
        ch = br.read();
        assertEquals('l', ch);
        
        ch = br.read();
        assertEquals('l', ch);
        
        ch = br.read();
        assertEquals('o', ch);
        
        ch = br.read();
        assertEquals(' ', ch);
        
        ch = br.read();
        assertEquals('W', ch);
        
        ch = br.read();
        assertEquals('o', ch);
        
        ch = br.read();
        assertEquals('r', ch);
        
        ch = br.read();
        assertEquals('l', ch);
        
        ch = br.read();
        assertEquals('d', ch);
        
        ch = br.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ch);
        
        br.close();
    }
    
    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("Hello");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        int ch = br.read();
        assertEquals('H', ch);
        
        ch = br.readAgain();
        assertEquals('H', ch);
        
        ch = br.read();
        assertEquals('e', ch);
        
        ch = br.readAgain();
        assertEquals('e', ch);
        
        br.close();
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        Reader reader = new StringReader("Hello World\nTest");
        char[] buf = new char[5];
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        int len = br.read(buf, 0, 5);
        assertEquals(5, len);
        assertEquals('H', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('l', buf[2]);
        assertEquals('l', buf[3]);
        assertEquals('o', buf[4]);
        
        len = br.read(buf, 0, 5);
        assertEquals(5, len);
        assertEquals(' ', buf[0]);
        assertEquals('W', buf[1]);
        assertEquals('o', buf[2]);
        assertEquals('r', buf[3]);
        assertEquals('l', buf[4]);
        
        len = br.read(buf, 0, 5);
        assertEquals(4, len);
        assertEquals('d', buf[0]);
        assertEquals('\n', buf[1]);
        assertEquals('T', buf[2]);
        assertEquals('e', buf[3]);
        assertEquals('s', buf[4]);
        
        len = br.read(buf, 0, 5);
        assertEquals(-1, len);
        
        br.close();       
    }
    
    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("Hello World\nTest");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        String line = br.readLine();
        assertEquals("Hello World", line);
        
        line = br.readLine();
        assertEquals("Test", line);
        
        line = br.readLine();
        assertNull(line);
        
        br.close();
    }
    
    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("Hello");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        int ch = br.lookAhead();
        assertEquals('H', ch);
        
        ch = br.read();
        assertEquals('H', ch);
        
        ch = br.lookAhead();
        assertEquals('e', ch);
        
        ch = br.read();
        assertEquals('e', ch);
        
        br.close();
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        int line = br.getLineNumber();
        assertEquals(0, line);
        
        br.readLine();
        line = br.getLineNumber();
        assertEquals(1, line);
        
        br.readLine();
        line = br.getLineNumber();
        assertEquals(2, line);
        
        br.readLine();
        line = br.getLineNumber();
        assertEquals(2, line);
        
        br.close();     
    }
}

