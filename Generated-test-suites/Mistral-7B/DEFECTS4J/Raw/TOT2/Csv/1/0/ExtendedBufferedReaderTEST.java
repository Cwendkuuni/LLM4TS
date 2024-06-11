package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));
        int result = reader.read();
        assertEquals('t', result);
    }

    @Test
    public void testRead_edgeCasesAndExceptions() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));

        // Empty input
        assertEquals(-1, reader.read());
        reader.close();
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(-1, reader.read());
        reader.close();

        // Reaching EOF
        reader = new ExtendedBufferedReader(new StringReader("test"));
        int result = reader.read();
        assertEquals(-1, result);
        reader.close();

        // Negative length
        reader = new ExtendedBufferedReader(new StringReader("test"));
        try {
            reader.read(new char[1], 0, -1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        reader.close();
    }

    @Test
    public void testReadAgain() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));
        int result = reader.read();
        assertEquals('t', result);

        result = reader.readAgain();
        assertEquals('t', result);

        reader.close();
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test\nline"));
        String result = reader.readLine();
        assertEquals("test", result);
    }

    @Test
    public void testReadLine_edgeCasesAndExceptions() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test\nline"));

        // Empty input
        assertEquals(null, reader.readLine());
        reader.close();
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(null, reader.readLine());
        reader.close();

        // Reaching EOF
        reader = new ExtendedBufferedReader(new StringReader("test\nline"));
        String result = reader.readLine();
        assertEquals("line", result);
        reader.close();

        // Input with only line terminator
        reader = new ExtendedBufferedReader(new StringReader("\n"));
        assertEquals(null, reader.readLine());
        reader.close();
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));
        int result = reader.lookAhead();
        assertEquals('t', result);

        reader.close();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test\nline1\ntest2\nline3"));

        assertEquals(1, reader.getLineNumber());
        assertEquals("test", reader.readLine());
        assertEquals(2, reader.getLineNumber());
        assertEquals("line1", reader.readLine());
        assertEquals(3, reader.getLineNumber());
        assertEquals("test2", reader.readLine());
        assertEquals(4, reader.getLineNumber());
        assertEquals("line3", reader.readLine());
        assertEquals(-1, reader.readLine());

        reader.close();
    }

    @Test
    public void testClose() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test\nline1\ntest2\nline3"));

        reader.close();

        try {
            reader.read();
            fail();
        } catch (IOException e) {
            // Expected exception
        }

        reader = new ExtendedBufferedReader(new StringReader("test\nline1\ntest2\nline3"));
        reader.close();
        reader.close();
    }
}