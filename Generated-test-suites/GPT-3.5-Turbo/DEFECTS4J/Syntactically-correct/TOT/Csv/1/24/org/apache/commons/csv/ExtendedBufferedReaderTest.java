import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() {
        // Typical use case
        // Test reading a single character
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello"));
        int result = reader.read();
        assertEquals('H', result);
        
        // Edge case
        // Test reading the last character
        result = reader.read();
        assertEquals('o', result);
        
        // Error scenario
        // Test reading from an empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        result = reader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
    }

    @Test
    public void testReadAgain() {
        // Typical use case
        // Test reading a single character
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello"));
        reader.read();
        int result = reader.readAgain();
        assertEquals('H', result);
        
        // Edge case
        // Test reading the last character
        reader.read();
        result = reader.readAgain();
        assertEquals('o', result);
        
        // Error scenario
        // Test reading from an empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        result = reader.readAgain();
        assertEquals(ExtendedBufferedReader.UNDEFINED, result);
    }

    @Test
    public void testReadArray() {
        // Typical use case
        // Test reading into a char array
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello"));
        char[] buf = new char[5];
        int result = reader.read(buf, 0, 5);
        assertEquals(5, result);
        assertEquals("Hello", new String(buf));
        
        // Edge case
        // Test reading with offset and length larger than the available characters
        reader = new ExtendedBufferedReader(new StringReader("Hello"));
        buf = new char[10];
        result = reader.read(buf, 5, 10);
        assertEquals(5, result);
        assertEquals('\u0000', buf[0]);
        assertEquals('\u0000', buf[1]);
        assertEquals('\u0000', buf[2]);
        assertEquals('\u0000', buf[3]);
        assertEquals('\u0000', buf[4]);
        assertEquals('H', buf[5]);
        assertEquals('e', buf[6]);
        assertEquals('l', buf[7]);
        assertEquals('l', buf[8]);
        assertEquals('o', buf[9]);
        
        // Error scenario
        // Test reading from an empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        buf = new char[5];
        result = reader.read(buf, 0, 5);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
    }

    @Test
    public void testReadLine() {
        // Typical use case
        // Test reading a single line
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        String line = reader.readLine();
        assertEquals("Hello", line);
        
        // Edge case
        // Test reading the last line
        reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        reader.readLine();
        line = reader.readLine();
        assertEquals("World", line);
        
        // Error scenario
        // Test reading from an empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        line = reader.readLine();
        assertNull(line);
    }

    @Test
    public void testLookAhead() {
        // Typical use case
        // Test looking ahead at the next character
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello"));
        int result = reader.lookAhead();
        assertEquals('H', result);
        
        // Edge case
        // Test looking ahead at the last character
        reader = new ExtendedBufferedReader(new StringReader("Hello"));
        reader.read();
        result = reader.lookAhead();
        assertEquals('e', result);
        
        // Error scenario
        // Test looking ahead from an empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        result = reader.lookAhead();
        assertEquals(ExtendedBufferedReader.UNDEFINED, result);
    }

    @Test
    public void testGetLineNumber() {
        // Typical use case
        // Test getting the line number
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        reader.readLine();
        int result = reader.getLineNumber();
        assertEquals(1, result);
        
        // Edge case
        // Test getting the line number after read
        reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        reader.readLine();
        reader.read();
        result = reader.getLineNumber();
        assertEquals(2, result);
        
        // Error scenario
        // Test getting the line number from an empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        result = reader.getLineNumber();
        assertEquals(-1, result);
    }

}