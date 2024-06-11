package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.io.IOException;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testExtendedBufferedReader() throws IOException {
        StringReader stringReader = new StringReader("Line 1\rLine 2\rLine 3");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(stringReader);

        // Test read
        int readChar = extendedBufferedReader.read();
        Assert.assertEquals('L', readChar);
        readChar = extendedBufferedReader.read();
        Assert.assertEquals('i', readChar);
        readChar = extendedBufferedReader.read();
        Assert.assertEquals(' ', readChar);
        readChar = extendedBufferedReader.read();
        Assert.assertEquals('\r', readChar);
        readChar = extendedBufferedReader.read();
        Assert.assertEquals('L', readChar);
        readChar = extendedBufferedReader.read();
        Assert.assertEquals('i', readChar);
        readChar = extendedBufferedReader.read();
        Assert.assertEquals(' ', readChar);
        readChar = extendedBufferedReader.read();
        Assert.assertEquals('\r', readChar);
        readChar = extendedBufferedReader.read();
        Assert.assertEquals('L', readChar);
        readChar = extendedBufferedReader.read();
        Assert.assertEquals('i', readChar);
        readChar = extendedBufferedReader.read();
        Assert.assertEquals(' ', readChar);
        readChar = extendedBufferedReader.read();
        Assert.assertEquals('\n', readChar);
        readChar = extendedBufferedReader.read(); // end of stream
        Assert.assertEquals(-1, readChar);

        // Test readAgain
        readChar = extendedBufferedReader.read();
        readChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('L', readChar);

        // Test readLine
        String readLine = extendedBufferedReader.readLine();
        Assert.assertEquals("Line 1\r", readLine);
        readLine = extendedBufferedReader.readLine();
        Assert.assertEquals("Line 2\r", readLine);
        readLine = extendedBufferedReader.readLine();
        Assert.assertEquals("Line 3", readLine);
        readLine = extendedBufferedReader.readLine(); // end of stream
        Assert.assertNull(readLine);

        // Test lookAhead
        int lookAheadChar = extendedBufferedReader.lookAhead();
        Assert.assertEquals(-1, lookAheadChar); // EOF

        // Test getLineNumber
        Assert.assertEquals(0, extendedBufferedReader.getLineNumber());
        extendedBufferedReader.readLine();
        Assert.assertEquals(1, extendedBufferedReader.getLineNumber());
        extendedBufferedReader.readLine();
        Assert.assertEquals(2, extendedBufferedReader.getLineNumber());
        extendedBufferedReader.readLine(); // end of stream
        Assert.assertEquals(-1, extendedBufferedReader.getLineNumber());
    }
}