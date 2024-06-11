package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2"));
        Assert.assertEquals('l', reader.read()); // first character of line1
        Assert.assertEquals('i', reader.read()); // second character of line1
        Assert.assertEquals('\n', reader.read()); // line separator between line1 and line2
        Assert.assertEquals('l', reader.read()); // first character of line2
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read()); // end of stream

        Assert.assertEquals(2, reader.getLineNumber()); // two lines read
    }

    @Test
    public void testReadAgain() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2"));
        Assert.assertEquals('l', reader.read()); // first character of line1
        Assert.assertEquals('i', reader.read()); // second character of line1
        Assert.assertEquals('n', reader.readAgain()); // last character of line1
        Assert.assertEquals('\n', reader.read()); // line separator between line1 and line2
        Assert.assertEquals('l', reader.read()); // first character of line2
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read()); // end of stream

        Assert.assertEquals(2, reader.getLineNumber()); // two lines read
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\nline3\n"));
        Assert.assertEquals("line1", reader.readLine());
        Assert.assertEquals("line2", reader.readLine());
        Assert.assertEquals("line3", reader.readLine());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());

        Assert.assertEquals(3, reader.getLineNumber()); // three lines read
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2"));
        Assert.assertEquals('l', reader.read()); // first character of line1
        int next = reader.lookAhead(); // look ahead at the next character
        Assert.assertEquals('i', next); // next character is the first character of line1
        Assert.assertEquals('i', reader.read()); // read the next character to confirm
        Assert.assertEquals('\n', reader.read()); // line separator between line1 and line2
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read()); // end of stream

        Assert.assertEquals(2, reader.getLineNumber()); // two lines read
    }

    @Test
    public void testEmptyStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read()); // end of stream
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.getLineNumber()); // no lines read
    }

    @Test
    public void testMalformedLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\rline2")); // malformed line with CR instead of LF
        Assert.assertEquals('l', reader.read()); // first character of line1
        Assert.assertEquals('i', reader.read()); // second character of line1
        Assert.assertEquals('\r', reader.read()); // malformed line separator
        Assert.assertEquals('l', reader.read()); // first character of line2
        int next = reader.lookAhead(); // look ahead at the next character
        Assert.assertEquals('w', next); // next character is the first character of line2
        Assert.assertEquals('i', reader.read()); // read the next character to confirm
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read()); // end of stream

        Assert.assertEquals(2, reader.getLineNumber()); // two lines read
    }

    @Test
    public void testBufferedInputStream() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("line1\nline2\nline3\n".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new BufferedReader(input));
        Assert.assertEquals("line1", reader.readLine());
        Assert.assertEquals("line2", reader.readLine());
        Assert.assertEquals("line3", reader.readLine());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());

        Assert.assertEquals(3, reader.getLineNumber()); // three lines read
    }
}