package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testConstructor() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test".getBytes())));
        Assert.assertNotNull(reader);
    }

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test\n".getBytes())));
        int result = reader.read();
        Assert.assertEquals(84, result); // 't' character as an integer
        Assert.assertEquals(10, reader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test".getBytes())));
        int result = reader.read();
        int resultAgain = reader.readAgain();
        Assert.assertEquals(-2, resultAgain); // UNDEFINED
        reader.read(); // consume '\n' character
        resultAgain = reader.readAgain();
        Assert.assertEquals(84, resultAgain); // 't' character as an integer
    }

    @Test
    public void testReadArray() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test\nhello\nworld".getBytes())));
        char[] buffer = new char[3];
        int result = reader.read(buffer, 0, 3);
        Assert.assertEquals(3, result);
        Assert.assertEquals("tes", new String(buffer));
        Assert.assertEquals(12, reader.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test\nline\n".getBytes())));
        String result = reader.readLine();
        Assert.assertEquals("test\n", result);
        Assert.assertEquals(1, reader.getLineNumber());
        result = reader.readLine();
        Assert.assertEquals("line\n", result);
        Assert.assertEquals(3, reader.getLineNumber());
    }

    @Test(expected = IOException.class)
    public void testLookAheadWithEOF() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test\n".getBytes())));
        reader.lookAhead();
        reader.read(); // consume '\n' character
        reader.lookAhead();
        reader.lookAhead(); // should throw IOException
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test\ntest\n".getBytes())));
        int result = reader.lookAhead();
        Assert.assertEquals(84, result); // 't' character as an integer
        reader.read(); // consume 't' character
        result = reader.lookAhead();
        Assert.assertEquals(84, result); // 't' character as an integer
        reader.read(); // consume 'e' character
        result = reader.lookAhead();
        Assert.assertEquals(83, result); // 's' character as an integer
    }

    @Test
    public void testGetLineNumber() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test\nline1\nline2\nline3\n".getBytes())));
        Assert.assertEquals(1, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(2, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(4, reader.getLineNumber());
    }

}