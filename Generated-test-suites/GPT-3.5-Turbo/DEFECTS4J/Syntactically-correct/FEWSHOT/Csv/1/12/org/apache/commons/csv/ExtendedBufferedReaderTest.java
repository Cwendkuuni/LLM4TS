import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExtendedBufferedReaderTest {
    
    @Test
    public void testRead() {
        // Test reading a single character
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("testing"));
        assertEquals('t', reader.read());
        
        // Test reading a line
        reader = new ExtendedBufferedReader(new StringReader("testing\n123"));
        assertEquals('t', reader.read());
        assertEquals('e', reader.read());
        assertEquals('s', reader.read());
        assertEquals('t', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('g', reader.read());
        assertEquals('\n', reader.read());
        assertEquals('1', reader.read());
        assertEquals('2', reader.read());
        assertEquals('3', reader.read());
        
        // Test reading empty input
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(-1, reader.read());
        
        // Test reading from empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(-1, reader.read());
    }
    
    @Test
    public void testReadAgain() {
        // Test reading a single character
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("t"));
        reader.read();
        assertEquals('t', reader.readAgain());
        
        // Test reading a line
        reader = new ExtendedBufferedReader(new StringReader("testing\n123"));
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        assertEquals('\n', reader.read());
        reader.read();
        reader.read();
        reader.read();
        assertEquals('3', reader.readAgain());
        
        // Test reading empty input
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(-1, reader.readAgain());
        
        // Test reading from empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(-1, reader.readAgain());
    }
    
    @Test
    public void testReadCharArray() {
        char[] buf = new char[10];
        
        // Test reading a single character
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("testing"));
        assertEquals(1, reader.read(buf, 0, 1));
        assertEquals('t', buf[0]);
        
        // Test reading a line
        reader = new ExtendedBufferedReader(new StringReader("testing\n123"));
        assertEquals(8, reader.read(buf, 0, 10));
        assertEquals('t', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('s', buf[2]);
        assertEquals('t', buf[3]);
        assertEquals('i', buf[4]);
        assertEquals('n', buf[5]);
        assertEquals('g', buf[6]);
        assertEquals('\n', buf[7]);
        assertEquals('1', buf[8]);
        assertEquals('2', buf[9]);
        
        // Test reading empty input
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(0, reader.read(buf, 0, 10));
        
        // Test reading from empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(-1, reader.read(buf, 0, 10));
    }
    
    @Test
    public void testReadLine() {
        // Test reading a single line
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("testing"));
        assertEquals("testing", reader.readLine());
        
        // Test reading multiple lines
        reader = new ExtendedBufferedReader(new StringReader("testing\n123\nabc"));
        assertEquals("testing", reader.readLine());
        assertEquals("123", reader.readLine());
        assertEquals("abc", reader.readLine());
        
        // Test reading empty input
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertNull(reader.readLine());
        
        // Test reading from empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertNull(reader.readLine());
    }
    
    @Test
    public void testLookAhead() {
        // Test looking ahead in the reader
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("testing"));
        assertEquals('e', reader.lookAhead());
        
        // Test looking ahead at end of stream
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(-1, reader.lookAhead());
    }
    
    @Test
    public void testGetLineNumber() {
        // Test getting the line number after reading a line
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("testing\n123"));
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
        
        // Test getting the line number after reading multiple lines
        reader = new ExtendedBufferedReader(new StringReader("testing\n123\nabc\nxyz"));
        reader.readLine();
        reader.readLine();
        assertEquals(2, reader.getLineNumber());
        
        // Test getting the line number with empty input
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(0, reader.getLineNumber());
        
        // Test getting the line number from an empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(0, reader.getLineNumber());
    }
}