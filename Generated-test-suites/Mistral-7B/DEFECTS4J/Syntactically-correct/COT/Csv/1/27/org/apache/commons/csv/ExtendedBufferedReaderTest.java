package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testConstructor() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello world"));
        Assert.assertNotNull(reader);
    }

    @Test
    public void testRead() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello world"));
        int actual = reader.read();
        int expected = 'h';
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReadAgain() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello world"));
        reader.read(); // read 'h'
        int actual = reader.readAgain();
        int expected = 'h';
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReadWithCharArray() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello world"));
        char[] buffer = new char[5];
        int length = reader.read(buffer, 0, buffer.length);
        String actual = new String(buffer, 0, length);
        String expected = "hello";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReadLine() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello\nworld\n"));
        String actual = reader.readLine();
        String expected = "hello";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello world"));
        int actual = reader.lookAhead();
        int expected = 'e';
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLineNumber() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\nline3\n"));
        int actual = reader.getLineNumber();
        int expected = 1;
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testExceptionWhenReadPastEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello world"));
        reader.readLine();
        reader.readLine(); // read past the end of the stream
    }

    @Test(expected = IOException.class)
    public void testExceptionWhenNegativeLengthIsPassedToRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello world"));
        reader.read(new char[5], 5, -1);
    }
}