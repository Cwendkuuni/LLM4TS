import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;
    
    @Before
    public void setup() {
        Reader r = new StringReader("This is a test\nfor the ExtendedBufferedReader class");
        reader = new ExtendedBufferedReader(r);
    }
    
    @Test
    public void testRead() throws IOException {
        int expected = 'T';
        int actual = reader.read();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReadAgain() throws IOException {
        int expected = 'T';
        reader.read();
        int actual = reader.readAgain();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReadMultipleChars() throws IOException {
        char[] buf = new char[10];
        int expected = 10;
        int actual = reader.read(buf, 0, 10);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReadLine() throws IOException {
        String expected = "This is a test";
        String actual = reader.readLine();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReadLineAtEOF() throws IOException {
        reader.readLine();
        String expected = null;
        String actual = reader.readLine();
        assertNull(actual);
    }
    
    @Test
    public void testLookAhead() throws IOException {
        int expected = 'h';
        int actual = reader.lookAhead();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        int expected = 0;
        int actual = reader.getLineNumber();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetLineNumberAfterReadLine() throws IOException {
        reader.readLine();
        int expected = 1;
        int actual = reader.getLineNumber();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetLineNumberAfterReadMultipleChars() throws IOException {
        char[] buf = new char[10];
        reader.read(buf, 0, 10);
        int expected = 1;
        int actual = reader.getLineNumber();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetLineNumberAfterRead() throws IOException {
        reader.read();
        int expected = 1;
        int actual = reader.getLineNumber();
        assertEquals(expected, actual);
    }
}