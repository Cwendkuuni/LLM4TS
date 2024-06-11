package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("line1\rline2\rline3".getBytes());
        reader = new ExtendedBufferedReader(new InputStreamReader(inputStream, "UTF-8"));;;
    }

    @Test
    public void testRead() throws IOException {
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(i + ' ', reader.read());
        }
        Assert.assertEquals(-1, reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals(' ', reader.readAgain());
        Assert.assertEquals(-1, reader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        Assert.assertEquals("line1", reader.readLine());
        Assert.assertEquals("line2", reader.readLine());
        Assert.assertEquals("line3", reader.readLine());
        Assert.assertNull(reader.readLine());
    }

    @Test(expected = IOException.class)
    public void testReadLineWithNegativeLength() throws IOException {
        reader.readLine(-1);
    }

    @Test
    public void testLookAhead() throws IOException {
        Assert.assertEquals('l', reader.lookAhead());
        reader.read();
        reader.read();
        Assert.assertEquals('i', reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Assert.assertEquals(0, reader.getLineNumber());
        Assert.assertEquals("line1", reader.readLine());
        Assert.assertEquals(1, reader.getLineNumber());
        Assert.assertEquals("line2", reader.readLine());
        Assert.assertEquals(2, reader.getLineNumber());
        Assert.assertNull(reader.readLine());
        Assert.assertEquals(2, reader.getLineNumber());
    }
}