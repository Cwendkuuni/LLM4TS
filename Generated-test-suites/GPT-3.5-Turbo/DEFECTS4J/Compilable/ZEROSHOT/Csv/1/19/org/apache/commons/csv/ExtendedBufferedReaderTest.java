package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {
    
    private ExtendedBufferedReader reader;
    
    @Test
    public void testRead() throws IOException {
        StringReader sr = new StringReader("Hello, World!");
        reader = new ExtendedBufferedReader(sr);
        
        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals(',', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('W', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals('r', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('d', reader.read());
        Assert.assertEquals('!', reader.read());
        Assert.assertEquals(-1, reader.read());
    }
    
    @Test
    public void testReadAgain() throws IOException {
        StringReader sr = new StringReader("Hello, World!");
        reader = new ExtendedBufferedReader(sr);
        
        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('o', reader.read());
        
        Assert.assertEquals('o', reader.readAgain());
        Assert.assertEquals('o', reader.readAgain());
    }
    
    @Test
    public void testReadLine() throws IOException {
        StringReader sr = new StringReader("Hello\nWorld\n");
        reader = new ExtendedBufferedReader(sr);
        
        Assert.assertEquals("Hello", reader.readLine());
        Assert.assertEquals("World", reader.readLine());
        Assert.assertEquals(null, reader.readLine());
    }
    
    @Test
    public void testReadLineAgain() throws IOException {
        StringReader sr = new StringReader("Hello\nWorld\n");
        reader = new ExtendedBufferedReader(sr);
        
        Assert.assertEquals("Hello", reader.readLine());
        Assert.assertEquals("World", reader.readLine());
        
        Assert.assertEquals("World", reader.readLine());
        Assert.assertEquals("World", reader.readLine());
    }
    
    @Test
    public void testLookAhead() throws IOException {
        StringReader sr = new StringReader("Hello");
        reader = new ExtendedBufferedReader(sr);
        
        Assert.assertEquals('e', reader.lookAhead());
        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.lookAhead());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('l', reader.lookAhead());
        Assert.assertEquals('l', reader.read());
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        StringReader sr = new StringReader("Hello\nWorld");
        reader = new ExtendedBufferedReader(sr);
        
        Assert.assertEquals(0, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(1, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(1, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(2, reader.getLineNumber());
    }
}