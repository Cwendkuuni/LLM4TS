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
    public void testExtendedBufferedReader() throws IOException {
        String testData = "Line1\rLine2\rLine3\rLine4\rLine5\rLine6\rLine7\nLine8";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(testData));

        // Test read method
        Assert.assertEquals('L', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('\r', reader.read()); // Line separator
        Assert.assertEquals('1', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('\r', reader.read()); // Line separator

        // Test readAgain method
        Assert.assertEquals('1', reader.readAgain());

        // Test read method with char array
        char[] buffer = new char[7];
        int readLength = reader.read(buffer, 0, 7);
        Assert.assertEquals(6, readLength);
        Assert.assertEquals('L', buffer[0]);
        Assert.assertEquals('i', buffer[1]);
        Assert.assertEquals('n', buffer[2]);
        Assert.assertEquals('e', buffer[3]);
        Assert.assertEquals(' ', buffer[4]);
        Assert.assertEquals('1', buffer[5]);

        // Test readLine method
        String line = reader.readLine();
        Assert.assertEquals("Line1\r", line);
        line = reader.readLine();
        Assert.assertEquals("Line2\r", line);
        line = reader.readLine();
        Assert.assertEquals("Line3\r", line);
        line = reader.readLine();
        Assert.assertEquals("Line4\r", line);
        line = reader.readLine();
        Assert.assertEquals("Line5\r", line);
        line = reader.readLine();
        Assert.assertEquals("Line6\r", line);
        line = reader.readLine();
        Assert.assertEquals("Line7\n", line);
        line = reader.readLine();
        Assert.assertEquals(null, line);

        // Test lookAhead method
        Assert.assertEquals('L', reader.lookAhead());
        reader.read(); // consume 'L'
        Assert.assertEquals('i', reader.lookAhead());

        // Test getLineNumber method
        Assert.assertEquals(8, reader.getLineNumber());
    }
}