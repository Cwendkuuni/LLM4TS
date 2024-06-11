package org.apache.commons.csv;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.CSVParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Line 1\nLine 2\nLine 3\n"));
        String line1 = reader.readLine();
        String line2 = reader.readLine();
        String line3 = reader.readLine();

        Assert.assertEquals("Line 1", line1);
        Assert.assertEquals("Line 2", line2);
        Assert.assertNull(reader.readLine());
        Assert.assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ABCDEFG"));
        int lookAhead1 = reader.lookAhead();
        int lookAhead2 = reader.lookAhead();

        Assert.assertEquals('A', (char) lookAhead1);
        Assert.assertEquals('B', (char) lookAhead2);

        reader.read(); // read A
        Assert.assertEquals('B', (char) lookAhead1);
        Assert.assertEquals('C', (char) lookAhead2);
    }

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ABCDEFG"));
        int read1 = reader.read();
        int read2 = reader.read();
        int read3 = reader.read();

        Assert.assertEquals('A', (char) read1);
        Assert.assertEquals('B', (char) read2);
        Assert.assertEquals('C', (char) read3);
        Assert.assertEquals(-1, reader.read());

        Assert.assertEquals('A', reader.readAgain());
        Assert.assertEquals('B', reader.readAgain());
        Assert.assertEquals('C', reader.readAgain());
        Assert.assertEquals(-1, reader.readAgain());
    }

    @Test(expected = IOException.class)
    public void testException() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        reader.readLine();
        reader.lookAhead(); // should throw an exception
    }
}