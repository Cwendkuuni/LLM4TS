package org.apache.commons.csv;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testNormalRead() throws Exception {
        StringReader sr = new StringReader("Hello World\n");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(sr);

        Assert.assertEquals('H', ebr.read());
        Assert.assertEquals('e', ebr.read());
        // ... and so on

        StringWriter sw = new StringWriter();
        char[] buffer = new char[1024];
        int length = ebr.read(buffer, 0, buffer.length);
        sw.write(buffer, 0, length);
        Assert.assertEquals("Hello World\n", sw.toString());

        ebr.close();
        sr.close();
    }

    @Test
    public void testEmptyRead() throws Exception {
        StringReader sr = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(sr);

        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readAgain());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.lookAhead());
        Assert.assertEquals(-1, ebr.getLineNumber());

        ebr.close();
        sr.close();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws Exception {
        StringReader sr = new StringReader("Hello World\n");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(sr);

        ebr.mark(1);
        ebr.read();
        ebr.read();

        // simulate an IOException
        ebr.skip(Long.MAX_VALUE);

        ebr.close();
        sr.close();
    }

    @Test
    public void testLookAhead() throws Exception {
        StringReader sr = new StringReader("Hello World\n");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(sr);

        Assert.assertEquals('H', ebr.lookAhead());
        Assert.assertEquals('e', ebr.read());

        ebr.close();
        sr.close();
    }

    @Test
    public void testReadLine() throws Exception {
        StringReader sr = new StringReader("Hello\nWorld\n");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(sr);

        Assert.assertEquals("Hello", ebr.readLine());
        Assert.assertEquals("World", ebr.readLine());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readLine());

        ebr.close();
        sr.close();
    }
}