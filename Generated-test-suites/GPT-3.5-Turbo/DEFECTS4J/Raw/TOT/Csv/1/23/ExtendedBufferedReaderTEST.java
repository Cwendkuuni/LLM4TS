import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;
    
    @Before
    public void setUp() {
        StringReader stringReader = new StringReader("Hello\nWorld\r\nThis is a test");
        reader = new ExtendedBufferedReader(new BufferedReader(stringReader));
    }
    
    @Test
    public void testRead() throws IOException {
        int result = reader.read();
        Assert.assertEquals('H', (char) result);
        result = reader.readAgain();
        Assert.assertEquals('H', (char) result);
    }
    
    @Test
    public void testReadLine() throws IOException {
        String result = reader.readLine();
        Assert.assertEquals("Hello", result);
        int lineNumber = reader.getLineNumber();
        Assert.assertEquals(1, lineNumber);
    }
    
    @Test
    public void testReadLineEmptyLine() throws IOException {
        String result = reader.readLine();
        Assert.assertEquals("World", result);
        int lineNumber = reader.getLineNumber();
        Assert.assertEquals(2, lineNumber);
    }
    
    @Test
    public void testReadLineBlankLine() throws IOException {
        StringReader stringReader = new StringReader("\n");
        reader = new ExtendedBufferedReader(new BufferedReader(stringReader));
        String result = reader.readLine();
        Assert.assertEquals("", result);
        int lineNumber = reader.getLineNumber();
        Assert.assertEquals(1, lineNumber);
    }
    
    @Test
    public void testReadLineEndOfFile() throws IOException {
        reader.readLine(); // "Hello"
        reader.readLine(); // "World"
        String result = reader.readLine();
        Assert.assertNull(result);
        int lineNumber = reader.getLineNumber();
        Assert.assertEquals(3, lineNumber);
    }
    
    @Test
    public void testReadLineCRLineEnding() throws IOException {
        reader.readLine(); // "Hello"
        StringReader stringReader = new StringReader("World\rLine ending");
        reader = new ExtendedBufferedReader(new BufferedReader(stringReader));
        String result = reader.readLine();
        Assert.assertEquals("World", result);
        int lineNumber = reader.getLineNumber();
        Assert.assertEquals(2, lineNumber);
    }
    
    @Test
    public void testReadLineMixedLineEndings() throws IOException {
        reader.readLine(); // "Hello"
        StringReader stringReader = new StringReader("World\r\nLine ending");
        reader = new ExtendedBufferedReader(new BufferedReader(stringReader));
        String result = reader.readLine();
        Assert.assertEquals("World", result);
        int lineNumber = reader.getLineNumber();
        Assert.assertEquals(2, lineNumber);
    }
    
    @Test
    public void testReadLineEmptyFile() throws IOException {
        StringReader stringReader = new StringReader("");
        reader = new ExtendedBufferedReader(new BufferedReader(stringReader));
        String result = reader.readLine();
        Assert.assertNull(result);
        int lineNumber = reader.getLineNumber();
        Assert.assertEquals(-1, lineNumber);
    }
    
    @Test
    public void testReadLineNoLineBreakAtEndOfFile() throws IOException {
        StringReader stringReader = new StringReader("Hello\nWorld");
        reader = new ExtendedBufferedReader(new BufferedReader(stringReader));
        reader.readLine(); // "Hello"
        String result = reader.readLine();
        Assert.assertEquals("World", result);
        int lineNumber = reader.getLineNumber();
        Assert.assertEquals(2, lineNumber);
    }
    
    @Test
    public void testReadCharArray() throws IOException {
        char[] buf = new char[6];
        int result = reader.read(buf, 0, 6);
        Assert.assertEquals(6, result);
        String output = new String(buf);
        Assert.assertEquals("Hello\n", output);
        result = reader.readAgain();
        Assert.assertEquals('W', (char) result);
    }
    
    @Test
    public void testReadCharArrayEmptyBuffer() throws IOException {
        char[] buf = new char[0];
        int result = reader.read(buf, 0, 0);
        Assert.assertEquals(0, result);
        result = reader.readAgain();
        Assert.assertEquals('H', (char) result);
    }
    
    @Test
    public void testReadCharArrayIncompleteLine() throws IOException {
        char[] buf = new char[100];
        int result = reader.read(buf, 0, 100);
        Assert.assertEquals(15, result);
        String output = new String(buf, 0, 15);
        Assert.assertEquals("Hello\nWorld\r\nT", output);
        result = reader.readAgain();
        Assert.assertEquals('h', (char) result);
    }
    
    @Test
    public void testLookAhead() throws IOException {
        int result = reader.lookAhead();
        Assert.assertEquals('e', (char) result);
        result = reader.read();
        Assert.assertEquals('e', (char) result);
        result = reader.lookAhead();
        Assert.assertEquals('l', (char) result);
    }
}