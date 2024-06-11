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
    public void setUp() {
        String input = "Line 1\rLine 2\rLine 3\rLine 4\rLine 5";
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
    }

    /**
     * Test read method
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        int actual;
        int expected;

        // Read normal characters
        for (int i = 0; i < 5; i++) {
            actual = reader.read();
            expected = (i == 4) ? ExtendedBufferedReader.END_OF_STREAM : i + 48; // convert ASCII code to int
            Assert.assertEquals("Unexpected character returned", expected, actual);
        }

        // Read end of stream
        actual = reader.read();
        Assert.assertEquals("Unexpected character returned", ExtendedBufferedReader.END_OF_STREAM, actual);
    }

    /**
     * Test readAgain method
     * @throws IOException
     */
    @Test
    public void testReadAgain() throws IOException {
        int current;
        int lookAhead;

        // Read and save first character
        current = reader.read();

        // Check readAgain returns the same character
        lookAhead = reader.lookAhead();
        reader.mark(1);
        reader.reset();
        Assert.assertEquals("Unexpected character returned", current, reader.read());

        // Check readAgain returns end of stream after reading all characters
        for (int i = 0; i < 5; i++) {
            reader.read();
        }
        Assert.assertEquals("Unexpected character returned", ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    /**
     * Test readLine method
     * @throws IOException
     */
    @Test
    public void testReadLine() throws IOException {
        String expected;
        String actual;

        // Test reading lines with correct line terminator
        actual = reader.readLine();
        Assert.assertEquals("Unexpected line returned", "Line 1\r", actual);
        actual = reader.readLine();
        Assert.assertEquals("Unexpected line returned", "Line 2\r", actual);
        actual = reader.readLine();
        Assert.assertEquals("Unexpected line returned", "Line 3\r", actual);
        actual = reader.readLine();
        Assert.assertEquals("Unexpected line returned", "Line 4\r", actual);
        actual = reader.readLine();
        Assert.assertEquals("Unexpected line returned", "Line 5\r", actual);
        Assert.assertEquals("Unexpected end of stream reached", ExtendedBufferedReader.END_OF_STREAM, reader.readLine());

        // Test reading lines with incorrect line terminator
        reader.mark(0);
        reader.readLine();
        int peekChar = reader.lookAhead();
        reader.reset();
        Assert.assertNotEquals("Unexpected character returned, expected CR or LF", '\r', peekChar);
        Assert.assertNotEquals("Unexpected character returned, expected CR or LF", '\n', peekChar);
    }

    /**
     * Test lookAhead method
     * @throws IOException
     */
    @Test
    public void testLookAhead() throws IOException {
        int expected;
        int actual;

        // Test looking ahead to the same character as read
        expected = reader.read();
        actual = reader.lookAhead();
        Assert.assertEquals("Unexpected character returned", expected, actual);

        // Test looking ahead to next character
        reader.mark(0);
        reader.read();
        actual = reader.lookAhead();
        reader.reset();
        Assert.assertEquals("Unexpected character returned", (reader.read() + 1), actual);

        // Test looking ahead to end of stream
        for (int i = 0; i < 5; i++) {
            reader.read();
        }
        Assert.assertEquals("Unexpected character returned", ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
    }

    /**
     * Test getLineNumber method
     * @throws IOException
     */
    @Test
    public void testGetLineNumber() throws IOException {
        int expected;
        int actual;

        // Test reading lines and verifying line numbers
        actual = reader.getLineNumber();
        expected = 0;
        Assert.assertEquals("Unexpected line number returned", expected, actual);

        actual = reader.readLine();
        expected++;
        Assert.assertEquals("Unexpected line number returned", expected, reader.getLineNumber());

        actual = reader.readLine();
        expected++;
        Assert.assertEquals("Unexpected line number returned", expected, reader.getLineNumber());

        // Test reading to end of stream and verifying line number
        for (int i = 0; i < 3; i++) {
            actual = reader.readLine();
        }
        actual = reader.getLineNumber();
        expected += 3;
        Assert.assertEquals("Unexpected line number returned", expected, actual);
        Assert.assertEquals("Unexpected character returned", ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }
}