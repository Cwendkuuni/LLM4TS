package org.apache.commons.csv;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("123"));
        
        assertEquals('1', (char)reader.read());
        assertEquals('2', (char)reader.read());
        assertEquals('3', (char)reader.read());
        assertEquals(-1, reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("123"));
        
        reader.read();
        assertEquals('1', (char)reader.readAgain());
    }

    @Test
    public void testReadCharArray() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("12\n34\r56\r\n78\n"));
        
        char[] buf = new char[5];
        int len = reader.read(buf, 0, 5);
        
        assertEquals(5, len);
        assertEquals('1', buf[0]);
        assertEquals('2', buf[1]);
        assertEquals('\n', buf[2]);
        assertEquals('3', buf[3]);
        assertEquals('4', buf[4]);
        
        assertEquals(10, reader.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\r\nline3"));
        
        assertEquals("line1", reader.readLine());
        assertEquals("line2", reader.readLine());
        assertEquals("line3", reader.readLine());
        assertEquals(null, reader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("123"));
        
        assertEquals('1', (char)reader.lookAhead());
        assertEquals('1', (char)reader.read());
    }

}