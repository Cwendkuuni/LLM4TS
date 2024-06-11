package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testConstructor() {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        Assert.assertNotNull(extendedBufferedReader);
    }

    @Test
    public void testRead() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.read();
        Assert.assertEquals('t', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('e', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('s', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\n', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\ntest\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.read();
        Assert.assertEquals('t', actual);

        int lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('e', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('s', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('t', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\n', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('t', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\n', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\nline\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        String actual = extendedBufferedReader.readLine();
        Assert.assertEquals("test", actual);

        actual = extendedBufferedReader.readLine();
        Assert.assertEquals("line", actual);

        actual = extendedBufferedReader.readLine();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\ntest\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('t', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('e', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('s', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('\n', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
    }

    @Test(expected = IOException.class)
    public void testLookAheadIOException() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('t', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('e', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('s', actual);

        extendedBufferedReader.mark(1);
        extendedBufferedReader.read(); // consuming one character
        Assert.assertThrows(IOException.class, () -> extendedBufferedReader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\nline1\nline2\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        Assert.assertEquals(0, extendedBufferedReader.getLineNumber());

        String line1 = extendedBufferedReader.readLine();
        Assert.assertEquals("test", line1);
        Assert.assertEquals(1, extendedBufferedReader.getLineNumber());

        String line2 = extendedBufferedReader.readLine();
        Assert.assertEquals("line1", line2);
        Assert.assertEquals(2, extendedBufferedReader.getLineNumber());

        String line3 = extendedBufferedReader.readLine();
        Assert.assertEquals("line2", line3);
        Assert.assertEquals(3, extendedBufferedReader.getLineNumber());

        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedBufferedReader.readLine());
        Assert.assertEquals(3, extendedBufferedReader.getLineNumber());
    }
}