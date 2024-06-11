package org.apache.commons.csv;

import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("Hello, world!");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        int result = extendedReader.read();
        int expected = 'H';
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLookAhead() throws IOException {
        StringReader reader = new StringReader("Hello, world!");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        int result = extendedReader.lookAhead();
        int expected = 'e';
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("Line 1\nLine 2\nLine 3");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        extendedReader.readLine();
        extendedReader.readLine();
        int result = extendedReader.getLineNumber();
        int expected = 2;
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testReadAgainEndOfStream() throws IOException {
        StringReader reader = new StringReader("Hello");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        extendedReader.read();
        int result = extendedReader.readAgain();
        int expected = ExtendedBufferedReader.END_OF_STREAM;
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetLineNumberEmptyFile() throws IOException {
        StringReader reader = new StringReader("");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        int result = extendedReader.getLineNumber();
        int expected = -1;
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetLineNumberOnlyEmptyLines() throws IOException {
        StringReader reader = new StringReader("\n\n\n");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        extendedReader.readLine();
        int result = extendedReader.getLineNumber();
        int expected = 4;
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetLineNumberMultipleLines() throws IOException {
        StringReader reader = new StringReader("Line 1\nLine 2\rLine 3\r\nLine 4");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        extendedReader.readLine();
        extendedReader.readLine();
        extendedReader.readLine();
        int result = extendedReader.getLineNumber();
        int expected = 4;
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetLineNumberNoLineTerminatorEndOfStream() throws IOException {
        StringReader reader = new StringReader("Line without terminator");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        extendedReader.readLine();
        int result = extendedReader.getLineNumber();
        int expected = 1;
        
        Assert.assertEquals(expected, result);
    }
}
