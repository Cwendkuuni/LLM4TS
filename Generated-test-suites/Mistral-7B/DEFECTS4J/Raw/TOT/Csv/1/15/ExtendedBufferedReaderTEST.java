package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('W', reader.read());
        Assert.assertEquals('r', reader.read());
        Assert.assertEquals('d', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        String line = reader.readLine();
        Assert.assertEquals("Hello World", line);
        Assert.assertEquals('W', reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        Assert.assertEquals('H', reader.lookAhead());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals(' ', reader.read());
        reader.mark(1);
        Assert.assertEquals('W', reader.lookAhead());
        reader.reset();
        Assert.assertEquals('W', reader.lookAhead());
        reader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
    }

    @Test(expected = IOException.class)
    public void testNegativeBufferSize() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""), -1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullReader() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(null);
    }
}