package org.apache.commons.csv;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));
        assertEquals((int)'t', reader.read());
        assertEquals((int)'e', reader.read());
        assertEquals((int)'s', reader.read());
        assertEquals((int)'t', reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));
        reader.read();
        reader.read();
        assertEquals((int)'s', reader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2"));
        assertEquals("line1", reader.readLine());
        assertEquals("line2", reader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));
        assertEquals((int)'t', reader.lookAhead());
        assertEquals((int)'t', reader.read());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2"));
        assertEquals(0, reader.getLineNumber());
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
        reader.readLine();
        assertEquals(2, reader.getLineNumber());
    }
}