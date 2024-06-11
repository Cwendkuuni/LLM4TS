package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Tests for {@link ExtendedBufferedReader}.
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testReadMethod() throws IOException {
        String testData = "Hello, World!\n" +
                "This is a test.\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        // Test reading normal characters
        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('H', reader.readAgain());
        Assert.assertEquals('e', reader.readAgain());
        Assert.assertEquals('l', reader.readAgain());
        Assert.assertEquals('l', reader.readAgain());
        Assert.assertEquals('o', reader.readAgain());
        // Test reaching EOS
        Assert.assertEquals(-1, reader.read());
        // Test reading line terminator
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(-1, reader.read());
    }

    @Test
    public void testReadLineMethod() throws IOException {
        String testData = "Line 1\n" +
                "Line 2\n" +
                "Line 3\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        String line1 = reader.readLine();
        Assert.assertEquals("Line 1", line1);
        Assert.assertEquals('\\n', reader.lastChar);

        String line2 = reader.readLine();
        Assert.assertEquals("Line 2", line2);
        Assert.assertEquals('\\r', reader.lastChar);

        String line3 = reader.readLine();
        Assert.assertEquals("Line 3", line3);
        Assert.assertEquals(-1, reader.lastChar);

        Assert.assertEquals(-1, reader.readLine());
    }

    @Test
    public void testReadLineWithEmptyLine() throws IOException {
        String testData = "Line 1\n" +
                "\n" +
                "Line 3\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        String line1 = reader.readLine();
        Assert.assertEquals("Line 1", line1);
        Assert.assertEquals('\\n', reader.lastChar);

        String line2 = reader.readLine();
        Assert.assertEquals(null, line2);
        Assert.assertEquals(-1, reader.lastChar);

        String line3 = reader.readLine();
        Assert.assertEquals("Line 3", line3);
        Assert.assertEquals('\\n', reader.lastChar);
    }

    @Test(expected = IOException.class)
    public void testLookAheadMethodWithNegativeOffset() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("Test".getBytes())));
        reader.lookAhead(-1);
    }

    @Test
    public void testLookAheadMethod() throws IOException {
        String testData = "Hello, World!\n" +
                "This is a test.\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        int lookAheadChar = reader.lookAhead();
        Assert.assertEquals('H', lookAheadChar);
        Assert.assertEquals('e', reader.read());

        lookAheadChar = reader.lookAhead();
        Assert.assertEquals('e', lookAheadChar);
        Assert.assertEquals('l', reader.read());

        lookAheadChar = reader.lookAhead();
        Assert.assertEquals('l', lookAheadChar);
        Assert.assertEquals('l', reader.read());

        lookAheadChar = reader.lookAhead();
        Assert.assertEquals('o', lookAheadChar);
        Assert.assertEquals(' ', reader.read());

        lookAheadChar = reader.lookAhead();
        Assert.assertEquals(-1, lookAheadChar);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String testData = "Line 1\n" +
                "Line 2\n" +
                "Line 3\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        Assert.assertEquals(0, reader.getLineNumber());

        String line1 = reader.readLine();
        Assert.assertEquals(1, reader.getLineNumber());
        Assert.assertEquals("Line 1", line1);
        Assert.assertEquals('\\n', reader.lastChar);

        String line2 = reader.readLine();
        Assert.assertEquals(2, reader.getLineNumber());
        Assert.assertEquals("Line 2", line2);
        Assert.assertEquals('\\r', reader.lastChar);

        String line3 = reader.readLine();
        Assert.assertEquals(3, reader.getLineNumber());
        Assert.assertEquals("Line 3", line3);
        Assert.assertEquals(-1, reader.lastChar);

        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.getLineNumber());
    }
}