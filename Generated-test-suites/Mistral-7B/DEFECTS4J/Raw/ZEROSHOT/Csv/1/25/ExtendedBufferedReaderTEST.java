package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws Exception {
        String input = "Hello\nWorld\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals('W', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals('r', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('d', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        Assert.assertEquals(10, reader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws Exception {
        String input = "Hello\nWorld\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        reader.read();
        Assert.assertEquals('H', reader.readAgain());
        Assert.assertEquals('e', reader.readAgain());
        Assert.assertEquals('l', reader.readAgain());
        Assert.assertEquals('l', reader.readAgain());
        Assert.assertEquals('o', reader.readAgain());
        Assert.assertEquals('\n', reader.readAgain());
        Assert.assertEquals('W', reader.readAgain());
        Assert.assertEquals('o', reader.readAgain());
        Assert.assertEquals('r', reader.readAgain());
        Assert.assertEquals('l', reader.readAgain());
        Assert.assertEquals('d', reader.readAgain());
        Assert.assertEquals('\n', reader.readAgain());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());

        Assert.assertEquals(10, reader.getLineNumber());
    }

    @Test
    public void testReadLine() throws Exception {
        String input = "Hello\nWorld\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        Assert.assertEquals("Hello", reader.readLine());
        Assert.assertEquals("World", reader.readLine());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());

        Assert.assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws Exception {
        String input = "Hello\nWorld\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        Assert.assertEquals('H', reader.lookAhead());
        Assert.assertEquals('e', reader.lookAhead());
        Assert.assertEquals('l', reader.lookAhead());
        Assert.assertEquals('l', reader.lookAhead());
        Assert.assertEquals('o', reader.lookAhead());
        Assert.assertEquals('\n', reader.lookAhead());
        Assert.assertEquals('W', reader.lookAhead());
        Assert.assertEquals('o', reader.lookAhead());
        Assert.assertEquals('r', reader.lookAhead());
        Assert.assertEquals('l', reader.lookAhead());
        Assert.assertEquals('d', reader.lookAhead());
        Assert.assertEquals('\n', reader.lookAhead());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());

        Assert.assertEquals(10, reader.getLineNumber());
    }

    @Test(expected = IOException.class)
    public void testNegativeLookAhead() throws Exception {
        String input = "Hello\nWorld\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        reader.mark(1);
        reader.read();
        reader.reset();
        reader.lookAhead();
    }
}