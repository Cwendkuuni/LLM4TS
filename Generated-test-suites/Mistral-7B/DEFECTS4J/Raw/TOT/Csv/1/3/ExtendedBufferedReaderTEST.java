package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for ExtendedBufferedReader.
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));
        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.read());
        // Test reading a newline character
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals('W', reader.read());
        // Test reading the end of the stream
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        // Test reading an empty line
        reader = new ExtendedBufferedReader(new StringReader("\n"));
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));

        String line1 = reader.readLine();
        Assert.assertEquals("Hello", line1);

        String line2 = reader.readLine();
        Assert.assertEquals("World", line2);

        // Test reading the end of the stream
        String line3 = reader.readLine();
        Assert.assertNull(line3);
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));

        // Test looking at the next character
        Assert.assertEquals('e', reader.lookAhead());
        // Test looking at the next character after a newline
        reader.readLine();
        Assert.assertEquals('W', reader.lookAhead());
        // Test looking at the next character at the end of the stream
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
    }

    @Test(expected = IOException.class)
    public void testNegativeLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));
        reader.mark(1);
        // Throw an exception by reading a character before mark
        reader.read();
        reader.reset();
        reader.lookAhead();
    }

    @Test
    public void testReadArray() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));

        char[] buffer = new char[6];
        int len = reader.read(buffer, 0, buffer.length);

        Assert.assertEquals(5, len);
        Assert.assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o'}, buffer, 0, 5);
    }

    @Test
    public void testReadArrayAtEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));
        reader.readLine();

        char[] buffer = new char[1];
        int len = reader.read(buffer, 0, buffer.length);
        Assert.assertEquals(0, len);

        char[] buffer2 = new char[1];
        int len2 = reader.read(buffer2, 0, buffer2.length);
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lastChar());
    }

    @Test
    public void testReadArrayNegativeOffset() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new ByteArrayInputStream(new byte[]{(byte)'H'}));

        char[] buffer = new char[1];
        int len = reader.read(buffer, -1, buffer.length);
        Assert.assertEquals(-1, len);
    }

    @Test
    public void testReadArrayNegativeLength() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));

        char[] buffer = new char[1];
        int len = reader.read(buffer, 0, -1);
        Assert.assertEquals(-1, len);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        Assert.assertEquals(1, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(2, reader.getLineNumber());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        Assert.assertEquals(2, reader.getLineNumber());
    }
}