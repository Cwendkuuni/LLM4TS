package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testExtendedBufferedReader() {
        String input = "Line 1\rLine 2\rLine 3\rLine 4\rLine 5";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        // Test read method
        Assert.assertEquals('L', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals('e', reader.read());
        // End of line
        Assert.assertEquals('\r', reader.read());
        Assert.assertEquals(' ', reader.readAgain());
        Assert.assertEquals('1', reader.read());
        // End of line
        Assert.assertEquals('\r', reader.read());
        // End of stream
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        // Test readLine method
        Assert.assertEquals("Line 1", reader.readLine());
        Assert.assertEquals("Line 2", reader.readLine());
        Assert.assertEquals("Line 3", reader.readLine());
        Assert.assertEquals("Line 4", reader.readLine());
        Assert.assertEquals("Line 5", reader.readLine());
        Assert.assertEquals(null, reader.readLine());

        // Test lookAhead method
        Assert.assertEquals('L', reader.lookAhead());
        reader.read(); // Consume 'L'
        Assert.assertEquals('i', reader.lookAhead());

        // Test getLineNumber method
        Assert.assertEquals(5, reader.getLineNumber());
    }
}