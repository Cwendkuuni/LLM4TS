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
    public void testRead() throws IOException {
        String inputStr = "Hello\nWorld\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStr.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.read());
        // Check line counter
        Assert.assertEquals(1, reader.getLineNumber());

        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals('W', reader.read());
        // Check line counter
        Assert.assertEquals(2, reader.getLineNumber());

        Assert.assertEquals('o', reader.read());
        Assert.assertEquals('r', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('d', reader.read());
        // Check line counter
        Assert.assertEquals(3, reader.getLineNumber());

        Assert.assertEquals('\n', reader.read());
        // End of stream
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        // Check line counter
        Assert.assertEquals(3, reader.getLineNumber());

        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());

        reader.close();
    }

    @Test(expected = IOException.class)
    public void testReadWithNegativeLength() throws IOException {
        String inputStr = "Hello\nWorld\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStr.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        reader.read(new char[1], 1, -1);
    }

    @Test
    public void testLookAhead() throws IOException {
        String inputStr = "Hello\nWorld\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStr.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        Assert.assertEquals('H', reader.lookAhead());
        reader.mark(1);
        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('\n', reader.lookAhead());

        reader.reset();
        Assert.assertEquals('H', reader.lookAhead());
    }

    @Test(expected = IOException.class)
    public void testLookAheadAtEndOfStream() throws IOException {
        String inputStr = "Hello\nWorld\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStr.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        reader.mark(1);
        reader.readLine();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
        reader.reset();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String inputStr = "Hello\nWorld\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStr.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        String line1 = reader.readLine();
        String line2 = reader.readLine();

        Assert.assertEquals(1, reader.getLineNumber());
        Assert.assertEquals("Hello", line1);

        Assert.assertEquals(2, reader.getLineNumber());
        Assert.assertEquals("World", line2);

        Assert.assertEquals(3, reader.getLineNumber());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());
    }
}