package org.apache.commons.csv;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("foo\nbar\nbaz");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int actual1 = ebr.read();
        int actual2 = ebr.read();
        int actual3 = ebr.read();

        assertEquals('f', actual1);
        assertEquals('o', actual2);
        assertEquals('o', actual3);
        assertEquals(-1, ebr.read());
        assertEquals(-1, ebr.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("foo\nbar\nbaz");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int actual1 = ebr.read();
        int actual2 = ebr.readAgain();
        int actual3 = ebr.read();

        assertEquals('f', actual1);
        assertEquals('f', actual2);
        assertEquals('o', actual3);
        assertEquals(-1, ebr.read());
        assertEquals(-1, ebr.read());
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("foo\nbar\nbaz\n");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        String actual1 = ebr.readLine();
        String actual2 = ebr.readLine();
        String actual3 = ebr.readLine();

        assertEquals("foo", actual1);
        assertEquals("bar", actual2);
        assertEquals(null, actual3);
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("foo\nbar\nbaz");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int actual1 = ebr.lookAhead();
        int actual2 = ebr.lookAhead();
        int actual3 = ebr.lookAhead();

        assertEquals('f', actual1);
        assertEquals('o', actual2);
        assertEquals(-1, actual3);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new StringReader("foo\nbar\nbaz\n");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int actual1 = ebr.readLine().length();
        int actual2 = ebr.readLine().length();
        int actual3 = ebr.readLine() == null ? -1 : ebr.readLine().length();
        int actual4 = ebr.getLineNumber();

        assertEquals(3, actual4);
    }

    @Test(expected = IOException.class)
    public void testLookAheadWithNegativeMark() throws IOException {
        Reader reader = new StringReader("foo\nbar\nbaz");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        ebr.mark(10);
        ebr.lookAhead();
        ebr.reset();
    }
}