package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for ExtendedBufferedReader
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testExtendedBufferedReader() throws Exception {
        StringReader reader = new StringReader("line1\rline2\rline3");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        // test read method
        Assert.assertEquals('l', extendedReader.read());
        Assert.assertEquals('i', extendedReader.read());
        Assert.assertEquals('n', extendedReader.read());
        Assert.assertEquals('e', extendedReader.read());
        Assert.assertEquals('\r', extendedReader.read());
        Assert.assertEquals('1', extendedReader.read());
        Assert.assertEquals('\r', extendedReader.read());
        Assert.assertEquals('l', extendedReader.read());
        Assert.assertEquals('i', extendedReader.read());
        Assert.assertEquals('n', extendedReader.read());
        Assert.assertEquals('e', extendedReader.read());
        Assert.assertEquals('\r', extendedReader.read());
        Assert.assertEquals('2', extendedReader.read());
        Assert.assertEquals('\r', extendedReader.read());
        Assert.assertEquals('l', extendedReader.read());
        Assert.assertEquals('i', extendedReader.read());
        Assert.assertEquals('n', extendedReader.read());
        Assert.assertEquals('e', extendedReader.read());
        Assert.assertEquals('\r', extendedReader.read());
        Assert.assertEquals('3', extendedReader.read());
        Assert.assertEquals('\r', extendedReader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedReader.read());

        // test readAgain method
        Assert.assertEquals('l', extendedReader.read());
        Assert.assertEquals('l', extendedReader.readAgain());
        Assert.assertEquals('\r', extendedReader.read());
        Assert.assertEquals('\r', extendedReader.readAgain());
        Assert.assertEquals('1', extendedReader.read());
        Assert.assertEquals('1', extendedReader.readAgain());
        Assert.assertEquals('\r', extendedReader.read());
        Assert.assertEquals('\r', extendedReader.readAgain());
        Assert.assertEquals('l', extendedReader.read());
        Assert.assertEquals('i', extendedReader.read());
        Assert.assertEquals('n', extendedReader.read());
        Assert.assertEquals('e', extendedReader.read());
        Assert.assertEquals('\r', extendedReader.read());
        Assert.assertEquals('2', extendedReader.read());
        Assert.assertEquals('2', extendedReader.readAgain());
        Assert.assertEquals('\r', extendedReader.read());
        Assert.assertEquals('\r', extendedReader.readAgain());
        Assert.assertEquals('l', extendedReader.read());
        Assert.assertEquals('i', extendedReader.read());
        Assert.assertEquals('n', extendedReader.read());
        Assert.assertEquals('e', extendedReader.read());
        Assert.assertEquals('\r', extendedReader.read());
        Assert.assertEquals('3', extendedReader.read());
        Assert.assertEquals('3', extendedReader.readAgain());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedReader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedReader.readAgain());

        // test readLine method
        Assert.assertEquals("line1\r", extendedReader.readLine());
        Assert.assertEquals("line2\r", extendedReader.readLine());
        Assert.assertEquals("line3", extendedReader.readLine());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedReader.readLine());

        // test lookAhead method
        Assert.assertEquals('l', extendedReader.lookAhead());
        Assert.assertEquals('l', extendedReader.lookAhead());
        Assert.assertEquals('\r', extendedReader.lookAhead());
        Assert.assertEquals('1', extendedReader.lookAhead());
        Assert.assertEquals('\r', extendedReader.lookAhead());
        Assert.assertEquals('l', extendedReader.lookAhead());
        Assert.assertEquals('i', extendedReader.lookAhead());
        Assert.assertEquals('n', extendedReader.lookAhead());
        Assert.assertEquals('e', extendedReader.lookAhead());
        Assert.assertEquals('\r', extendedReader.lookAhead());
        Assert.assertEquals('2', extendedReader.lookAhead());
        Assert.assertEquals('\r'
                , extendedReader.lookAhead());
        Assert.assertEquals('l', extendedReader.lookAhead());
        Assert.assertEquals('i', extendedReader.lookAhead());
        Assert.assertEquals('n', extendedReader.lookAhead());
        Assert.assertEquals('e', extendedReader.lookAhead());
        Assert.assertEquals('\r', extendedReader.lookAhead());
        Assert.assertEquals('3', extendedReader.lookAhead());
        Assert.assertEquals('\r', extendedReader.lookAhead());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedReader.lookAhead());
        Assert.assertEquals(ExtendedBufferedReader.UNDEFINED, extendedReader.lookAhead());

        // test getLineNumber method
        Assert.assertEquals(1, extendedReader.getLineNumber());
        Assert.assertEquals(3, extendedReader.getLineNumber());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedReader.getLineNumber());
    }
}