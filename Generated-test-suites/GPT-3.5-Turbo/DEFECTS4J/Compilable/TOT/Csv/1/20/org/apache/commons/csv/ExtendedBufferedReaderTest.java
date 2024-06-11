package org.apache.commons.csv;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {
    
    private ExtendedBufferedReader reader;
    
    @Before
    public void setUp() {
        BufferedReader br = new BufferedReader(new StringReader("Test String\nAnother Test\n"));
        reader = new ExtendedBufferedReader(br);
    }
    
    @Test
    public void testRead() throws IOException {
        // Typical use case
        int result1 = reader.read();
        assertEquals((int)'T', result1);
        
        // Edge case: end of stream
        int result2 = reader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result2);
        
        reader.close();
    }
    
    @Test
    public void testReadAgain() throws IOException {
        // Typical use case
        reader.read();
        int result1 = reader.readAgain();
        assertEquals((int)'T', result1);
        
        // Edge case: no character read yet
        int result2 = reader.readAgain();
        assertEquals(ExtendedBufferedReader.UNDEFINED, result2);
        
        reader.close();
    }
    
    @Test
    public void testReadCharArray() throws IOException {
        // Typical use case
        char[] buf = new char[5];
        int result1 = reader.read(buf, 0, 5);
        String str1 = new String(buf);
        assertEquals(5, result1);
        assertEquals("Test ", str1);
        
        // Edge case: not enough characters to fill the buffer
        char[] buf2 = new char[10];
        int result2 = reader.read(buf2, 0, 10);
        String str2 = new String(buf2);
        assertEquals(8, result2);
        assertEquals("String\nAno", str2);
        
        reader.close();
    }
    
    @Test
    public void testReadLine() throws IOException {
        // Typical use case
        String result1 = reader.readLine();
        assertEquals("Test String", result1);
        
        // Edge case: empty line
        String result2 = reader.readLine();
        assertEquals("", result2);
        
        // Edge case: end of stream
        String result3 = reader.readLine();
        assertNull(result3);
        
        reader.close();
    }
    
    @Test
    public void testLookAhead() throws IOException {
        // Typical use case
        int result1 = reader.lookAhead();
        assertEquals((int)'e', result1);
        
        // Edge case: end of stream
        reader.readLine();
        int result2 = reader.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result2);
        
        reader.close();
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        // Typical use case
        reader.readLine();
        int result1 = reader.getLineNumber();
        assertEquals(1, result1);
        
        // Edge case: no line read yet
        int result2 = reader.getLineNumber();
        assertEquals(0, result2);
        
        reader.close();
    }
}