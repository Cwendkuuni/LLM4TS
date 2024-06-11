package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.io.IOException;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.read());
        // Read newline
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals('W', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('d', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        String line = reader.readLine();
        Assert.assertEquals("Hello World", line);
        Assert.assertEquals('\n', reader.read());
        String line2 = reader.readLine();
        Assert.assertNull(line2);
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        Assert.assertEquals('H', reader.lookAhead());
        reader.read(); // consume 'H'
        Assert.assertEquals('e', reader.lookAhead());
        reader.read(); // consume 'e'
        Assert.assertEquals('\n', reader.lookAhead());
        reader.readLine(); // consume newline
        Assert.assertEquals('W', reader.lookAhead());
        reader.read(); // consume 'W'
        Assert.assertEquals('o', reader.lookAhead());
        Assert.assertEquals('l', reader.lookAhead());
        Assert.assertEquals('d', reader.lookAhead());
        Assert.assertEquals('\n', reader.lookAhead());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        reader.read(); // consume 'H'
        reader.read(); // consume 'e'
        reader.close();
        reader.read(); // should throw IOException
    }

    @Test
    public void testGetLineNumber() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        Assert.assertEquals(0, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(1, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(2, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.finish();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.read();
        Assert.assertEquals(3, reader.getLineNumber());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        Assert.assertEquals(3, reader.getLineNumber());
    }
}