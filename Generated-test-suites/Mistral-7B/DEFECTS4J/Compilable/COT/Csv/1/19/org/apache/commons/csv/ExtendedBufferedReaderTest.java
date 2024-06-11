package org.apache.commons.csv;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.read());
        // ... add more assertions for all other characters
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(-1, reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        reader.read();
        reader.read();
        int lastChar = reader.readAgain();
        Assert.assertEquals('H', lastChar);

        reader.close();
        lastChar = reader.readAgain();
        Assert.assertEquals(-1, lastChar);
    }

    @Test
    public void testReadCharArray() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        char[] buffer = new char[5];
        int count = reader.read(buffer, 0, buffer.length);
        Assert.assertArrayEquals(new String(buffer, 0, count).toCharArray(), "Hello".toCharArray());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(-1, reader.read());
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        String line = reader.readLine();
        Assert.assertEquals("Hello World", line);
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(-1, reader.read());
    }

    @Test(expected = IOException.class)
    public void testLookAheadThrowsIOException() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        reader.lookAhead();
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        int nextChar = reader.lookAhead();
        Assert.assertEquals('H', nextChar);

        reader.read(); // consume 'H'
        nextChar = reader.lookAhead();
        Assert.assertEquals('e', nextChar);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\n"));
        Assert.assertEquals(0, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(1, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(2, reader.getLineNumber());
        reader.close();
    }
}