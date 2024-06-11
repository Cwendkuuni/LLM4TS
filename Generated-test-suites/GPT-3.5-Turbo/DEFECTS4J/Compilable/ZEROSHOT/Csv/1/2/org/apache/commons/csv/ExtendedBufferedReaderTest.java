package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("Hello world");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        int result = br.read();

        Assert.assertEquals('H', result);
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("Hello world");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        br.read();
        int result = br.readAgain();

        Assert.assertEquals('H', result);
    }

    @Test
    public void testReadArray() throws IOException {
        Reader reader = new StringReader("Hello world");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        char[] buf = new char[5];
        int result = br.read(buf, 0, 5);

        Assert.assertEquals(5, result);
        Assert.assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o'}, buf);
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("Hello\nworld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        String result = br.readLine();

        Assert.assertEquals("Hello", result);
        Assert.assertEquals(1, br.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("Hello world");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        int result = br.lookAhead();

        Assert.assertEquals('H', result);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new StringReader("Hello\nworld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        br.readLine();
        int result = br.getLineNumber();

        Assert.assertEquals(1, result);
    }
}