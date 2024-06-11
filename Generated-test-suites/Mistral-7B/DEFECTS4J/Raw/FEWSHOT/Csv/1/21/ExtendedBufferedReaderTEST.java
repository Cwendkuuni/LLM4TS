package org.apache.commons.csv;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("foo\nbar\n");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int ch1 = extReader.read();
        int ch2 = extReader.read();
        int ch3 = extReader.read();
        int ch4 = extReader.read();
        int ch5 = extReader.read();

        assertEquals('f', ch1);
        assertEquals('o', ch2);
        assertEquals('o', ch3);
        assertEquals('\n', ch4);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ch5);
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("foo\nbar\n");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int ch1 = extReader.read();
        int ch2 = extReader.readAgain();
        int ch3 = extReader.readAgain();
        int ch4 = extReader.readAgain();
        int ch5 = extReader.readAgain();

        assertEquals('f', ch1);
        assertEquals('o', ch2);
        assertEquals('o', ch3);
        assertEquals('\n', ch4);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ch5);
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("foo\nbar\nbaz\n");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        String line1 = extReader.readLine();
        String line2 = extReader.readLine();
        String line3 = extReader.readLine();

        assertEquals("foo", line1);
        assertEquals("bar", line2);
        assertEquals("baz", line3);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extReader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("foo\nbar\nbaz\n");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int ch1 = extReader.lookAhead();
        int ch2 = extReader.lookAhead();
        int ch3 = extReader.lookAhead();

        assertEquals('f', ch1);
        assertEquals('o', ch2);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ch3);

        extReader.readLine();

        int ch4 = extReader.lookAhead();
        int ch5 = extReader.lookAhead();

        assertEquals('b', ch4);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ch5);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new StringReader("foo\nbar\nbaz\n");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        String line1 = extReader.readLine();
        String line2 = extReader.readLine();
        String line3 = extReader.readLine();

        assertEquals(3, extReader.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extReader.readLine());
        assertEquals(4, extReader.getLineNumber());
    }

    @Test(expected = IllegalStateException.class)
    public void testLookAheadAfterEOFLine() throws IOException {
        Reader reader = new StringReader("foo\n");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        extReader.readLine();
        int ch1 = extReader.lookAhead();

    }

    @Test
    public void testLookAheadAfterEmptyLine() throws IOException {
        Reader reader = new StringReader("");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int ch1 = extReader.lookAhead();

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ch1);
    }
}