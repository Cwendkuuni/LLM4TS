package org.apache.commons.csv;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        StringReader reader = new StringReader("Hello World!");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        int result = br.read();

        assertEquals('H', result);
    }
    
    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("Hello World!");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        br.read();
        int result = br.readAgain();
        
        assertEquals('H', result);
    }
    
    @Test
    public void testRead_withMultipleCharacters() throws IOException {
        StringReader reader = new StringReader("Hello World!");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        char[] buf = new char[5];
        int result = br.read(buf, 0, 5);

        assertEquals(5, result);
    }
    
    @Test
    public void testRead_withEmptyBuffer() throws IOException {
        StringReader reader = new StringReader("Hello World!");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        char[] buf = new char[0];
        int result = br.read(buf, 0, 0);

        assertEquals(0, result);
    }

    @Test
    public void testReadLine() throws IOException {
        StringReader reader = new StringReader("Hello\nWorld!");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        String result = br.readLine();

        assertEquals("Hello", result);
    }
    
    @Test
    public void testReadLine_withEmptyLine() throws IOException {
        StringReader reader = new StringReader("\nWorld!");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        String result = br.readLine();

        assertEquals("", result);
    }

    @Test
    public void testLookAhead() throws IOException {
        StringReader reader = new StringReader("Hello World!");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        int result = br.lookAhead();

        assertEquals('e', result);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("Hello\nWorld!\n");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        br.readLine();
        int result = br.getLineNumber();

        assertEquals(1, result);
    }
}