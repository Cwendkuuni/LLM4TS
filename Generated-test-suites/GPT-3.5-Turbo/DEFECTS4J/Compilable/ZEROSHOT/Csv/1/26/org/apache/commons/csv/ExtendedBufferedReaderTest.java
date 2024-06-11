package org.apache.commons.csv;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setup() {
        String input = "This is a test\n" +
                       "Second line\n" +
                       "Third line";
        Reader stringReader = new StringReader(input);
        reader = new ExtendedBufferedReader(stringReader);
    }

    @Test
    public void testRead() throws IOException {
        int c1 = reader.read();
        int c2 = reader.read();

        assertEquals('T', c1);
        assertEquals('h', c2);
    }

    @Test
    public void testReadAgain() throws IOException {
        int c = reader.read();
        int lastChar = reader.readAgain();

        assertEquals('T', c);
        assertEquals('T', lastChar);
    }
    
    @Test
    public void testReadArray() throws IOException {
        char[] buffer = new char[10];
        int len = reader.read(buffer);

        assertEquals(10, len);
        assertEquals("This is a ", new String(buffer));

        len = reader.read(buffer);

        assertEquals(10, len);
        assertEquals("test\nSecon", new String(buffer));

        len = reader.read(buffer);

        assertEquals(3, len);
        assertEquals("d line\nThr", new String(buffer, 0, 3));
    }

    @Test
    public void testReadLine() throws IOException {
        String line1 = reader.readLine();
        String line2 = reader.readLine();

        assertEquals("This is a test", line1);
        assertEquals("Second line", line2);
    }

    @Test
    public void testLookAhead() throws IOException {
        int c1 = reader.lookAhead();
        int c2 = reader.read();

        assertEquals('T', c1);
        assertEquals('T', c2);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        reader.readLine(); // Read the first line

        int lineNumber = reader.getLineNumber();

        assertEquals(1, lineNumber);
    }
    
    @Test
    public void testReadLineNoLineTerminator() throws IOException {
        String input = "This is a test";
        Reader stringReader = new StringReader(input);
        reader = new ExtendedBufferedReader(stringReader);
        
        String line1 = reader.readLine();

        assertEquals("This is a test", line1);
    }
    
    @Test
    public void testReadLineEmptyLine() throws IOException {
        String input = "\n";
        Reader stringReader = new StringReader(input);
        reader = new ExtendedBufferedReader(stringReader);
        
        String line1 = reader.readLine();

        assertEquals("", line1);
    }
    
    @Test
    public void testReadLineEndOfStream() throws IOException {
        String input = "This is a test";
        Reader stringReader = new StringReader(input);
        reader = new ExtendedBufferedReader(stringReader);
        
        reader.readLine();
        String line2 = reader.readLine();

        assertEquals(null, line2);
    }
    
    @Test
    public void testGetLineNumberEmpty() throws IOException {
        int lineNumber = reader.getLineNumber();

        assertEquals(0, lineNumber);
    }
    
    @Test
    public void testGetLineNumberWithEmptyLines() throws IOException {
        reader.readLine();
        reader.readLine();
        reader.readLine();
        int lineNumber = reader.getLineNumber();

        assertEquals(3, lineNumber);
    }
}
