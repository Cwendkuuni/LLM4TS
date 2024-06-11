package org.apache.commons.csv;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        reader = new ExtendedBufferedReader(new StringReader("test\ndata\n1234"));
    }

    @Test
    public void testRead() throws IOException {
        int result = reader.read();
        assertEquals('t', result);
    }

    @Test
    public void testReadAgain() throws IOException {
        reader.read();
        int result = reader.readAgain();
        assertEquals('t', result);
    }

    @Test
    public void testReadCharArray() throws IOException {
        char[] buf = new char[4];
        int result = reader.read(buf, 0, 4);
        assertEquals(4, result);
        assertArrayEquals(new char[] {'e', 's', 't', '\n'}, buf);
    }

    @Test
    public void testReadLine() throws IOException {
        String result = reader.readLine();
        assertEquals("test", result);
    }

    @Test
    public void testLookAhead() throws IOException {
        int result = reader.lookAhead();
        assertEquals('e', result);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        reader.readLine();
        int result = reader.getLineNumber();
        assertEquals(1, result);
    }
}