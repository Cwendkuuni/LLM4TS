package org.apache.commons.csv;

import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    // Test constructor with null Reader object
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullReader() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(null);
    }
    
    // Test readAgain() when no characters have been read yet
    @Test
    public void testReadAgainWithNoCharacters() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        int result = reader.readAgain();
        Assert.assertEquals(-2, result);
    }
    
    // Test readAgain() when the end of the stream was reached on the last read
    @Test
    public void testReadAgainWithEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));
        reader.read();
        int result = reader.readAgain();
        Assert.assertEquals(-1, result);
    }
    
    // Test readLine() when there are multiple lines in the input
    @Test
    public void testReadLineWithMultipleLines() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\nline3"));
        String line1 = reader.readLine();
        String line2 = reader.readLine();
        String line3 = reader.readLine();
        Assert.assertEquals("line1", line1);
        Assert.assertEquals("line2", line2);
        Assert.assertEquals("line3", line3);
    }
    
    // Test readLine() when there is a line with no characters
    @Test
    public void testReadLineWithEmptyLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\nline2"));
        String emptyLine = reader.readLine();
        String line2 = reader.readLine();
        Assert.assertEquals("", emptyLine);
        Assert.assertEquals("line2", line2);
    }
    
    // Test readLine() when the end of the stream is reached (null is returned)
    @Test
    public void testReadLineWithEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1"));
        reader.readLine();
        String result = reader.readLine();
        Assert.assertNull(result);
    }
    
    // Test lookAhead() when there are more characters in the reader
    @Test
    public void testLookAheadWithMoreCharacters() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));
        int result = reader.lookAhead();
        Assert.assertEquals('e', result);
    }
    
    // Test lookAhead() when the end of the stream is reached
    @Test
    public void testLookAheadWithEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("t"));
        reader.read();
        int result = reader.lookAhead();
        Assert.assertEquals(-1, result);
    }
    
    // Test getLineNumber() when no lines have been read yet
    @Test
    public void testGetLineNumberWithNoLinesRead() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        int lineNumber = reader.getLineNumber();
        Assert.assertEquals(0, lineNumber);
    }
    
    // Test getLineNumber() when multiple lines have been read
    @Test
    public void testGetLineNumberWithMultipleLinesRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\nline3"));
        reader.readLine();
        reader.readLine();
        int lineNumber = reader.getLineNumber();
        Assert.assertEquals(2, lineNumber);
    }
    
    // Test with all methods and scenarios
    @Test
    public void testAllMethods() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2"));
        
        // Test ExtendedBufferedReader constructor with null Reader object
        ExtendedBufferedReader nullReader = new ExtendedBufferedReader(null);
        
        // Test readAgain() when no characters have been read yet
        int readAgain1 = reader.readAgain();
        Assert.assertEquals(-2, readAgain1);
        
        // Test read()
        int read1 = reader.read();
        Assert.assertEquals('l', read1);
        
        // Test readAgain() when characters have been read
        int readAgain2 = reader.readAgain();
        Assert.assertEquals('l', readAgain2);
        
        // Test readLine() when there are multiple lines in the input
        String line1 = reader.readLine();
        String line2 = reader.readLine();
        Assert.assertEquals("ine1", line1);
        Assert.assertEquals("line2", line2);
        
        // Test lookAhead() when there are more characters in the reader
        int lookAhead = reader.lookAhead();
        Assert.assertEquals('i', lookAhead);
        
        // Test getLineNumber() when multiple lines have been read
        int lineNumber = reader.getLineNumber();
        Assert.assertEquals(2, lineNumber);
        
        // Test readLine() when the end of the stream is reached
        String endOfStream = reader.readLine();
        Assert.assertNull(endOfStream);
    }
}
