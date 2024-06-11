package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {
    
    private ExtendedBufferedReader reader;
    
    @Before
    public void setup() {
        // Create a reader with a test string
        reader = new ExtendedBufferedReader(new StringReader("This is a test string.\nSecond line."));
    }
    
    @Test
    public void testRead() throws IOException {
        int ch1 = reader.read();
        int ch2 = reader.read();
        
        Assert.assertEquals('T', ch1);
        Assert.assertEquals('h', ch2);
    }
    
    @Test
    public void testReadAgain() throws IOException {
        reader.read();
        int ch = reader.readAgain();
        
        Assert.assertEquals('T', ch);
    }
    
    @Test
    public void testReadCharArray() throws IOException {
        char[] buf = new char[5];
        reader.read(buf, 0, 5);
        
        Assert.assertEquals("This ", new String(buf));
    }
    
    @Test
    public void testReadLine() throws IOException {
        String line1 = reader.readLine();
        String line2 = reader.readLine();
        
        Assert.assertEquals("This is a test string.", line1);
        Assert.assertEquals("Second line.", line2);
    }
    
    @Test
    public void testLookAhead() throws IOException {
        int ch1 = reader.lookAhead();
        int ch2 = reader.read();
        
        Assert.assertEquals('h', ch1);
        Assert.assertEquals('T', ch2);
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        reader.readLine();
        int lineNumber = reader.getLineNumber();
        
        Assert.assertEquals(1, lineNumber);
    }
}