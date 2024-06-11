package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {
    
    @Test
    public void testRead() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        int c = bufferedReader.read();
        Assert.assertEquals('H', c);
    }
    
    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        bufferedReader.read();
        int c = bufferedReader.readAgain();
        Assert.assertEquals('H', c);
    }
    
    @Test
    public void testReadCharArray() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        char[] buf = new char[5];
        int len = bufferedReader.read(buf, 0, 5);
        Assert.assertEquals(5, len);
        Assert.assertEquals('H', buf[0]);
        Assert.assertEquals('e', buf[1]);
        Assert.assertEquals('l', buf[2]);
        Assert.assertEquals('l', buf[3]);
        Assert.assertEquals('o', buf[4]);
    }
    
    @Test
    public void testReadLine() throws IOException {
        StringReader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        String line = bufferedReader.readLine();
        Assert.assertEquals("Hello", line);
    }
    
    @Test
    public void testLookAhead() throws IOException {
        StringReader reader = new StringReader("Hello");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        int c = bufferedReader.lookAhead();
        Assert.assertEquals('H', c);
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        bufferedReader.readLine();
        int lineNumber = bufferedReader.getLineNumber();
        Assert.assertEquals(1, lineNumber);
    }
    
    // Edge cases and exception handling tests
    
    @Test
    public void testReadEmpty() throws IOException {
        StringReader reader = new StringReader("");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        int c = bufferedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, c);
    }
    
    @Test
    public void testReadAgainEmpty() throws IOException {
        StringReader reader = new StringReader("");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        int c = bufferedReader.readAgain();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, c);
    }
    
    @Test
    public void testReadCharArrayEmpty() throws IOException {
        StringReader reader = new StringReader("");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        char[] buf = new char[5];
        int len = bufferedReader.read(buf, 0, 5);
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, len);
    }
    
    @Test
    public void testReadLineEmpty() throws IOException {
        StringReader reader = new StringReader("");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        String line = bufferedReader.readLine();
        Assert.assertNull(line);
    }
    
    @Test
    public void testReadLineWithEmptyLine() throws IOException {
        StringReader reader = new StringReader("\n");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        String line = bufferedReader.readLine();
        Assert.assertEquals("", line);
    }
    
    @Test
    public void testReadLineEOF() throws IOException {
        StringReader reader = new StringReader("Hello");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        bufferedReader.readLine();
        String line = bufferedReader.readLine();
        Assert.assertNull(line);
    }
    
    @Test
    public void testLookAheadEOF() throws IOException {
        StringReader reader = new StringReader("Hello");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        bufferedReader.lookAhead();
        int c = bufferedReader.lookAhead();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, c);
    }
}