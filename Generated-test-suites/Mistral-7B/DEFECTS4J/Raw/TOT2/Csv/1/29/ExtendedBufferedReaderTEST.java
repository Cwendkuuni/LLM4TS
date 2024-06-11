package org.apache.commons.csv.test;

import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        // Test case for read() method
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));
        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('\n', reader.read()); // should increment line counter
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testLookAhead() throws IOException {
        // Test case for lookAhead() method
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcdefg"));
        int c = reader.lookAhead();
        Assert.assertEquals('a', c);
        Assert.assertEquals('a', reader.read());
        c = reader.lookAhead();
        Assert.assertEquals('b', c);
        Assert.assertEquals('b', reader.read());
        // ... repeating for rest of the characters
    }

    @Test
    public void testEmptyInput() throws IOException {
        // Test case for empty input string
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testOnlyWhitespaceInput() throws IOException {
        // Test case for input containing only whitespaces
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("   "));
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test(expected = IOException.class)
    public void testEOFBeforeFirstRead() throws IOException {
        // Test case for input stream returning EOF before the first call to read()
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        reader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        // This test should throw an IOException
        reader.read();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLength() throws IOException {
        // Test case for negative length in read(char[], int, int) method
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        char[] buf = new char[3];
        reader.read(buf, 0, -1);
    }

    @Test(expected = IllegalStateException.class)
    public void testReadAfterLookAhead() throws IOException {
        // Test case for calling read() after calling lookAhead()
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        int c = reader.lookAhead();
        Assert.assertEquals('a', c);
        reader.read(); // consume 'a'
        Assert.assertEquals('b', reader.read());
        c = reader.lookAhead();
        Assert.assertEquals('c', c);

        // This test should throw an IllegalStateException
        reader.read();
    }
}
//