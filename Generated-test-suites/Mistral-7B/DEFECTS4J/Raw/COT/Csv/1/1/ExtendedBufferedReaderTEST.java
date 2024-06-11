package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testConstructor() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        Assert.assertNotNull(reader);
    }

    @Test
    public void testRead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        int actual = reader.read();
        Assert.assertEquals('a', actual);
    }

    @Test
    public void testReadAgain() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        reader.read();
        int actual = reader.readAgain();
        Assert.assertEquals('a', actual);
    }

    @Test
    public void testReadWithCharArray() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcd\nefgh"));
        char[] buffer = new char[5];
        int actual = reader.read(buffer, 0, 5);
        Assert.assertEquals(4, actual);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, buffer);
    }

    @Test
    public void testReadLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc\ndef\rghi\njkl"));
        String actual = reader.readLine();
        Assert.assertEquals("abc\ndef", actual);
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals('\r', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals('g', reader.read());
    }

    @Test
    public void testLookAhead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcd"));
        int actual = reader.lookAhead();
        Assert.assertEquals('a', actual);
    }

    @Test
    public void testGetLineNumber() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcd\nefgh\nijkl"));
        Assert.assertEquals(0, reader.getLineNumber());
        String line = reader.readLine();
        Assert.assertEquals(1, reader.getLineNumber());
        Assert.assertEquals("abcd", line);
        line = reader.readLine();
        Assert.assertEquals(2, reader.getLineNumber());
        Assert.assertEquals("efgh", line);
        line = reader.readLine();
        Assert.assertEquals(3, reader.getLineNumber());
        Assert.assertEquals("ijkl", line);
        Assert.assertEquals(-1, reader.getLineNumber());
    }

    @Test(expected = IOException.class)
    public void testReadWithException() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        reader.read(); // Read successfully
        reader.read(); // Throws exception
    }

    @Test
    public void testReadLineWithComment() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("--comment\nabcd"));
        String line = reader.readLine();
        Assert.assertEquals("abcd", line);
        Assert.assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadLineWithEmptyLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\n"));
        String line = reader.readLine();
        Assert.assertEquals(null, line);
        Assert.assertEquals(0, reader.getLineNumber());
    }
}