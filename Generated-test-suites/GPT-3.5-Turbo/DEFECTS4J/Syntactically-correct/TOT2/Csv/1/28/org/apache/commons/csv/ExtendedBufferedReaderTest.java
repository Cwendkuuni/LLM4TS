import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        String testString = "Hello World\nTesting";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(testString));
        
        assertEquals('H', (char) reader.read());
        assertEquals('e', (char) reader.read());
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws IOException {
        String testString = "Hello World\nTesting";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(testString));
        
        reader.read();
        reader.read();
        assertEquals('e', (char) reader.readAgain());
    }

    @Test
    public void testReadCharArray() throws IOException {
        String testString = "Hello World\r\nTesting";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(testString));
        
        char[] buffer = new char[12];
        reader.read(buffer, 0, 12);
        
        assertEquals('T', buffer[8]);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        String testString = "Hello World\nTesting";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(testString));
        
        assertEquals("Hello World", reader.readLine());
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        String testString = "Hello World\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(testString));
        
        assertEquals('e', (char) reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String testString = "Line 1\nLine 2\nLine 3";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(testString));
        
        reader.readLine();
        reader.readLine();
        assertEquals(2, reader.getLineNumber());
    }
}