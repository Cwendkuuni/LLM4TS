package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;

/**
 * Tests for the ExtendedBufferedReader class
 */
public class ExtendedBufferedReaderTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Line1\nLine2\nLine3".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new ByteArrayReader(inputStream));
        Assert.assertNotNull(reader);
    }

    /**
     * Test read method with normal character
     */
    @Test
    public void testReadNormalCharacter() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Line1\nLine2\nLine3".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new ByteArrayReader(inputStream));
        int actual = reader.read();
        int expected = 'L';
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test read method with EOF
     */
    @Test
    public void testReadEndOfFile() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Line1\nLine2\nLine3".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new ByteArrayReader(inputStream));
        int actual = reader.read();
        int expected = 'L';
        for (int i = 0; i < 3; i++) {
            actual = reader.read();
            Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
        }
    }

    /**
     * Test read method with empty input
     */
    @Test
    public void testReadEmptyInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new ByteArrayReader(inputStream));
        int actual = reader.read();
        int expected = ExtendedBufferedReader.UNDEFINED;
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test readAgain method with normal character
     */
    @Test
    public void testReadAgainNormalCharacter() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Line1\nLine2\nLine3".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new ByteArrayReader(inputStream));
        int actual = reader.read();
        int expected = 'L';
        reader.readAgain();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test readAgain method with EOF
     */
    @Test
    public void testReadAgainEndOfFile() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Line1\nLine2\nLine3".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new ByteArrayReader(inputStream));
        int actual = reader.read();
        int expected = 'L';
        for (int i = 0; i < 3; i++) {
            actual = reader.read();
            Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
        }
        expected = ExtendedBufferedReader.END_OF_STREAM;
        Assert.assertEquals(expected, reader.readAgain());
    }

    /**
     * Test read method with array
     */
    @Test
    public void testReadArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Line1\nLine2\nLine3".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new ByteArrayReader(inputStream));
        char[] buffer = new char[5];
        int actual = reader.read(buffer);
        int expected = 4;
        Assert.assertEquals(expected, actual);
        Assert.assertEquals('L', buffer[0]);
        Assert.assertEquals('i', buffer[1]);
        Assert.assertEquals('n', buffer[2]);
        Assert.assertEquals('e', buffer[3]);
        Assert.assertEquals('\n', buffer[4]);
    }

    /**
     * Test readLine method with normal line
     */
    @Test
    public void testReadLineNormalLine() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Line1\nLine2\nLine3".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new ByteArrayReader(inputStream));
        String actual = reader.readLine();
        String expected = "Line1";
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(1, reader.getLineNumber());
    }

    /**
     * Test readLine method with empty line
     */
    @Test
    public void testReadLineEmptyLine() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Line1\n\nLine3".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new ByteArrayReader(inputStream));
        String actual = reader.readLine();
        String expected = null;
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(1, reader.getLineNumber());
    }

    /**
     * Test readLine method with EOF
     */
    @Test
    public void testReadLineEndOfFile() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Line1\nLine2\nLine3".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new ByteArrayReader(inputStream));
        String actual = reader.readLine();
        String expected = "Line1";
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(1, reader.getLineNumber());
        actual = reader.readLine();
        expected = "Line2";
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(2, reader.getLineNumber());
        actual = reader.readLine();
        expected = "Line3";
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(3, reader.getLineNumber());
        actual = reader.readLine();
        Assert.assertEquals(null, actual);
        Assert.assertEquals(3, reader.getLineNumber());
    }

    /**
     * Test lookAhead method with normal character
     */
    @Test
    public void testLookAheadNormalCharacter() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Line1\nLine2\nLine3".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new ByteArrayReader(inputStream));
        int actual = reader.lookAhead();
        int expected = 'L';
        reader.read();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test lookAhead method with EOF
     */
    @Test
    public void testLookAheadEndOfFile() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Line1\nLine2\nLine3".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new ByteArrayReader(inputStream));
        int actual = reader.lookAhead();
        int expected = 'L';
        for (int i = 0; i < 3; i++) {
            reader.read();
        }
        expected = ExtendedBufferedReader.END_OF_STREAM;
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test getLineNumber method
     */
    @Test
    public void testGetLineNumber() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Line1\nLine2\nLine3".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new ByteArrayReader(inputStream));
        int actual = reader.getLineNumber();
        int expected = 0;
        Assert.assertEquals(expected, actual);
        reader.readLine();
        actual = reader.getLineNumber();
        expected = 1;
        Assert.assertEquals(expected, actual);
        reader.readLine();
        reader.readLine();
        actual = reader.getLineNumber();
        expected = 3;
        Assert.assertEquals(expected, actual);
        reader.readLine();
        actual = reader.getLineNumber();
        Assert.assertEquals(3, reader.getLineNumber());
    }
}