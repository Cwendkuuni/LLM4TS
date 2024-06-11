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
    public void testConstructors() {
        ExtendedBufferedReader reader1 = new ExtendedBufferedReader(new StringReader(""));
        ExtendedBufferedReader reader2 = new ExtendedBufferedReader(new StringReader("test"));

        Assert.assertNotNull(reader1);
        Assert.assertNotNull(reader2);
    }

    @Test
    public void testRead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));

        Assert.assertEquals('t', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('s', reader.read());
        Assert.assertEquals(-1, reader.read());

        Assert.assertEquals(-1, reader.read());
    }

    @Test
    public void testReadAgain() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));

        reader.read(); // 't'
        reader.mark(1);

        Assert.assertEquals('t', reader.readAgain());
    }

    @Test(expected = IllegalStateException.class)
    public void testReadAgainAfterEndOfStream() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));

        reader.read(); // 't'
        reader.read(); // 'e'
        reader.read(); // 's'
        reader.read(); // -1 (end of stream)
        reader.readAgain(); // should throw IllegalStateException
    }

    @Test
    public void testReadLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test\n"));

        Assert.assertEquals("test", reader.readLine());
        Assert.assertEquals(-1, reader.read());

        Assert.assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadLineMultiple() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test\nhello\nworld"));

        Assert.assertEquals("test", reader.readLine()); // line 1
        Assert.assertEquals("hello", reader.readLine()); // line 2
        Assert.assertEquals("world", reader.readLine()); // line 3
        Assert.assertEquals(-1, reader.read());

        Assert.assertEquals(3, reader.getLineNumber());
    }

    @Test(expected = NullPointerException.class)
    public void testReadLineNullReader() throws Exception {
        ExtendedBufferedReader reader = null;
        reader.readLine();
    }

    @Test
    public void testLookAhead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));

        reader.mark(1);
        Assert.assertEquals('t', reader.lookAhead());
        reader.reset();

        reader.read(); // 't'
        Assert.assertEquals('e', reader.lookAhead());
    }

    @Test(expected = IOException.class)
    public void testLookAheadEndOfStream() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));

        reader.read(); // 't'
        reader.read(); // 'e'
        reader.read(); // 's'
        reader.read(); // -1 (end of stream)
        reader.lookAhead(); // should throw IOException
    }

    @Test
    public void testGetLineNumber() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test\nhello\nworld"));

        Assert.assertEquals(0, reader.getLineNumber());
        reader.readLine(); // line 1
        Assert.assertEquals(1, reader.getLineNumber());
        reader.readLine(); // line 2
        Assert.assertEquals(2, reader.getLineNumber());
        reader.readLine(); // line 3
        Assert.assertEquals(3, reader.getLineNumber());
        Assert.assertEquals(-1, reader.read());

        Assert.assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testReadEmpty() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));

        Assert.assertEquals(-1, reader.read());
        Assert.assertEquals(-1, reader.getLineNumber());
    }
}