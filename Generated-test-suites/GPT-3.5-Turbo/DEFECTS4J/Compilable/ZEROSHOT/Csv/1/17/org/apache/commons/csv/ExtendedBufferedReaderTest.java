package org.apache.commons.csv;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {
    
    private ExtendedBufferedReader reader;
    
    @Before
    public void setup() {
        reader = new ExtendedBufferedReader(null);
    }
    
    @Test
    public void testRead() throws Exception {
        int result = reader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
    }
    
    @Test
    public void testReadAgain() {
        int result = reader.readAgain();
        assertEquals(ExtendedBufferedReader.UNDEFINED, result);
    }
    
    @Test
    public void testRead_withLengthZero() throws Exception {
        char[] buf = new char[0];
        int result = reader.read(buf, 0, 0);
        assertEquals(0, result);
    }
    
    @Test
    public void testRead_withLengthGreaterThanZero() throws Exception {
        char[] buf = new char[10];
        int result = reader.read(buf, 0, 10);
        assertEquals(10, result);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }
    
    @Test
    public void testReadLine() throws Exception {
        String result = reader.readLine();
        assertNull(result);
    }
    
    @Test
    public void testLookAhead() throws Exception {
        int result = reader.lookAhead();
        assertEquals(ExtendedBufferedReader.UNDEFINED, result);
    }
    
    @Test
    public void testGetLineNumber() {
        int result = reader.getLineNumber();
        assertEquals(0, result);
    }
}