package org.apache.commons.csv;
import org.junit.*;
import java.io.StringReader;
import java.io.IOException;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        
        int result = ebr.read();
        
        assertEquals('H', result);
    }
    
    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        
        int result = ebr.read();
        result = ebr.readAgain();
        
        assertEquals('H', result);
    }
    
    @Test
    public void testRead_charArrayNotNull() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        char[] buf = new char[5];
        
        int result = ebr.read(buf, 0, 5);
        
        assertEquals(5, result);
        assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o'}, buf);
    }
    
    @Test
    public void testRead_charArrayEmpty() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        char[] buf = new char[0];
        
        int result = ebr.read(buf, 0, 0);
        
        assertEquals(0, result);
        assertArrayEquals(new char[0], buf);
    }
    
    @Test
    public void testRead_charArrayWithLineTerminators() throws IOException {
        StringReader reader = new StringReader("Hello\n\rWorld");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        char[] buf = new char[10];
        
        int result = ebr.read(buf, 0, 10);
        
        assertEquals(10, result);
        assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o', '\n', '\r', 'W', 'o', 'r'}, buf);
    }
    
    @Test
    public void testRead_charArrayWithPartialLineTerminator() throws IOException {
        StringReader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        char[] buf = new char[10];
        
        int result = ebr.read(buf, 0, 10);
        
        assertEquals(10, result);
        assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o', '\n', 'W', 'o', 'r', 'l'}, buf);
    }
    
    @Test
    public void testRead_charArrayWithIncompleteLineTerminator() throws IOException {
        StringReader reader = new StringReader("Hello");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        char[] buf = new char[10];
        
        int result = ebr.read(buf, 0, 10);
        
        assertEquals(5, result);
        assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000'}, buf);
    }
    
    @Test
    public void testReadLine() throws IOException {
        StringReader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        
        String result = ebr.readLine();
        
        assertEquals("Hello", result);
    }
    
    @Test
    public void testReadLine_eof() throws IOException {
        StringReader reader = new StringReader("Hello");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        ebr.readLine();
        
        String result = ebr.readLine();
        
        assertNull(result);
    }
    
    @Test
    public void testLookAhead() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        ebr.read();
        
        int result = ebr.lookAhead();
        
        assertEquals('e', result);
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("Hello\n\nWorld");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        ebr.readLine();
        
        int result = ebr.getLineNumber();
        
        assertEquals(1, result);
    }
}