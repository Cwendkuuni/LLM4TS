import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {
    
    // Test case for read() method
    @Test
    public void testRead() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader bufReader = new ExtendedBufferedReader(reader);
        
        int firstChar = bufReader.read();
        assertEquals('H', firstChar);
        
        int secondChar = bufReader.read();
        assertEquals('e', secondChar);
        
        int thirdChar = bufReader.read();
        assertEquals('l', thirdChar);
        
        int endChar = bufReader.read();
        assertEquals('d', endChar);
        
        int eofChar = bufReader.read();
        assertEquals(-1, eofChar);
    }
    
    // Test case for readAgain() method
    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader bufReader = new ExtendedBufferedReader(reader);
        
        int firstChar = bufReader.read();
        assertEquals(firstChar, bufReader.readAgain());
    }
    
    // Test case for read(char[] buf, int offset, int length) method
    @Test
    public void testReadCharArray() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader bufReader = new ExtendedBufferedReader(reader);
        
        char[] buf = new char[5];
        int len = bufReader.read(buf, 0, 5);
        assertEquals(5, len);
        
        String result = new String(buf);
        assertEquals("Hello", result);
        
        char[] buf2 = new char[10];
        int len2 = bufReader.read(buf2, 0, 10);
        assertEquals(5, len2);
        
        String result2 = new String(buf2);
        assertEquals(" World", result2);
    }
    
    // Test case for readLine() method
    @Test
    public void testReadLine() throws IOException {
        StringReader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader bufReader = new ExtendedBufferedReader(reader);
        
        String line1 = bufReader.readLine();
        assertEquals("Hello", line1);
        
        String line2 = bufReader.readLine();
        assertEquals("World", line2);
        
        String line3 = bufReader.readLine();
        assertNull(line3);
    }
    
    // Test case for lookAhead() method
    @Test
    public void testLookAhead() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader bufReader = new ExtendedBufferedReader(reader);
        
        int currentChar = bufReader.read();
        int lookAheadChar = bufReader.lookAhead();
        
        assertEquals(currentChar, lookAheadChar);
    }
    
    // Test case for getLineNumber() method
    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader bufReader = new ExtendedBufferedReader(reader);
        
        bufReader.readLine();
        int lineNumber = bufReader.getLineNumber();
        
        assertEquals(1, lineNumber);
    }
}