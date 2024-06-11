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
    public void testDefaultConstructor() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        Assert.assertNull(reader.readLine());
        Assert.assertEquals(-1, reader.getLineNumber());
    }

    @Test
    public void testRead() throws Exception {
        String input = "test\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        int read = reader.read();
        Assert.assertEquals('t', (char) read);
        Assert.assertEquals(0, reader.getLineNumber());
        read = reader.read();
        Assert.assertEquals('e', (char) read);
        Assert.assertEquals(0, reader.getLineNumber());
        read = reader.read();
        Assert.assertEquals('s', (char) read);
        Assert.assertEquals(0, reader.getLineNumber());
        read = reader.read();
        Assert.assertEquals('\n', (char) read);
        Assert.assertEquals(1, reader.getLineNumber());
        read = reader.read();
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testReadLine() throws Exception {
        String input = "test\nline1\nline2\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        String line1 = reader.readLine();
        Assert.assertEquals("test", line1);
        Assert.assertEquals(1, reader.getLineNumber());
        String line2 = reader.readLine();
        Assert.assertEquals("line1", line2);
        Assert.assertEquals(2, reader.getLineNumber());
        String line3 = reader.readLine();
        Assert.assertEquals("line2", line3);
        Assert.assertEquals(3, reader.getLineNumber());
        String line4 = reader.readLine();
        Assert.assertEquals(null, line4);
        Assert.assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws Exception {
        String input = "test\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        int lookAhead = reader.lookAhead();
        Assert.assertEquals(-1, lookAhead);
        int read = reader.read();
        Assert.assertEquals('t', (char) read);
        lookAhead = reader.lookAhead();
        Assert.assertEquals('e', (char) lookAhead);
        lookAhead = reader.lookAhead();
        Assert.assertEquals('s', (char) lookAhead);
        lookAhead = reader.lookAhead();
        Assert.assertEquals('\n', (char) lookAhead);
    }
}