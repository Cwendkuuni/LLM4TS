
package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {
    @Test
    public void testRead() throws IOException {
        StringReader reader = new StringReader("This is a test");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        int result = extendedReader.read();
        assertEquals('T', result);
        
        result = extendedReader.read();
        assertEquals('h', result);
        
        result = extendedReader.read();
        assertEquals('i', result);
        
        // TODO: Add more test cases to cover other possible scenarios
        
        reader.close();
    }
    
    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("This is a test");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        int result = extendedReader.read();
        assertEquals('T', result);
        
        result = extendedReader.readAgain();
        assertEquals('T', result);
        
        extendedReader.read();
        extendedReader.read();
        extendedReader.read();
        extendedReader.read();
        extendedReader.read();
        extendedReader.read();
        
        result = extendedReader.readAgain();
        assertEquals('t', result);
        
        reader.close();
    }
    
    @Test
    public void testReadCharArray() throws IOException {
        StringReader reader = new StringReader("This is a test");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        char[] buf = new char[10];
        int result = extendedReader.read(buf, 0, 10);
        
        assertEquals(10, result);
        assertEquals('T', buf[0]);
        assertEquals('s', buf[4]);
        assertEquals('t', buf[9]);
        
        reader.close();
    }
    
    @Test
    public void testReadLine() throws IOException {
        StringReader reader = new StringReader("This is a test\nThis is another test");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        String line = extendedReader.readLine();
        assertEquals("This is a test", line);
        
        line = extendedReader.readLine();
        assertEquals("This is another test", line);
        
        reader.close();
    }
    
    @Test
    public void testLookAhead() throws IOException {
        StringReader reader = new StringReader("This is a test");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        int result = extendedReader.lookAhead();
        assertEquals('T', result);
        
        reader.close();
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("This is a test\nThis is another test");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        int lineNumber = extendedReader.getLineNumber();
        assertEquals(0, lineNumber);
        
        extendedReader.readLine();
        lineNumber = extendedReader.getLineNumber();
        assertEquals(1, lineNumber);
        
        reader.close();
    }
}
