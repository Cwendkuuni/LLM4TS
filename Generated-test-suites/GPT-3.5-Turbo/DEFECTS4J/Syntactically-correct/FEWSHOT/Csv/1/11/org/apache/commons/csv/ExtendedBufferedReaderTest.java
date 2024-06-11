package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {
    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        assertEquals('a', ebr.read());
        assertEquals('b', ebr.read());
        assertEquals('c', ebr.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readAgain());
    }

    @Test
    public void testReadWithLineCounter() throws IOException {
        Reader reader = new StringReader("abc\ndef\nghi");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        assertEquals('a', ebr.read());
        assertEquals('b', ebr.read());
        assertEquals('c', ebr.read());
        assertEquals(1, ebr.getLineNumber());

        assertEquals('\n', ebr.read());
        assertEquals('d', ebr.read());
        assertEquals('e', ebr.read());
        assertEquals('f', ebr.read());
        assertEquals(2, ebr.getLineNumber());

        assertEquals('\n', ebr.read());
        assertEquals('g', ebr.read());
        assertEquals('h', ebr.read());
        assertEquals('i', ebr.read());
        assertEquals(3, ebr.getLineNumber());
    }

    @Test
    public void testReadWithLineCounterAndCRLF() throws IOException {
        Reader reader = new StringReader("abc\r\ndef\r\nghi");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        assertEquals('a', ebr.read());
        assertEquals('b', ebr.read());
        assertEquals('c', ebr.read());
        assertEquals(1, ebr.getLineNumber());

        assertEquals('\r', ebr.read());
        assertEquals('\n', ebr.read());
        assertEquals('d', ebr.read());
        assertEquals('e', ebr.read());
        assertEquals('f', ebr.read());
        assertEquals(2, ebr.getLineNumber());

        assertEquals('\r', ebr.read());
        assertEquals('\n', ebr.read());
        assertEquals('g', ebr.read());
        assertEquals('h', ebr.read());
        assertEquals('i', ebr.read());
        assertEquals(3, ebr.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("abc\ndef\nghi");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        assertEquals("abc", ebr.readLine());
        assertEquals("def", ebr.readLine());
        assertEquals("ghi", ebr.readLine());
        assertNull(ebr.readLine());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readAgain());
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        assertEquals('a', ebr.lookAhead());
        assertEquals('a', ebr.read());
        assertEquals('b', ebr.lookAhead());
        assertEquals('b', ebr.read());
        assertEquals('c', ebr.lookAhead());
        assertEquals('c', ebr.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.lookAhead());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readAgain());
    }

    @Test
    public void testReadCharArray() throws IOException {
        Reader reader = new StringReader("abc\ndef\nghi");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        char[] buf = new char[4];

        assertEquals(4, ebr.read(buf, 0, 4));
        assertEquals("abc\n", new String(buf));

        assertEquals(4, ebr.read(buf, 0, 4));
        assertEquals("def\n", new String(buf));

        assertEquals(3, ebr.read(buf, 0, 4));
        assertEquals("ghi", new String(buf, 0, 3));

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read(buf, 0, 4));
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readAgain());
    }

    @Test
    public void testReadCharArrayWithLineCounter() throws IOException {
        Reader reader = new StringReader("abc\r\ndef\r\nghi");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        char[] buf = new char[4];

        assertEquals(3, ebr.getLineNumber());
        assertEquals(4, ebr.read(buf, 0, 4));
        assertEquals(1, ebr.getLineNumber());
        assertEquals("abc\r", new String(buf));

        assertEquals(3, ebr.getLineNumber());
        assertEquals(4, ebr.read(buf, 0, 4));
        assertEquals(2, ebr.getLineNumber());
        assertEquals("\ndef", new String(buf));

        assertEquals(3, ebr.getLineNumber());
        assertEquals(3, ebr.read(buf, 0, 4));
        assertEquals(3, ebr.getLineNumber());
        assertEquals("ghi", new String(buf, 0, 3));

        assertEquals(3, ebr.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read(buf, 0, 4));
        assertEquals(3, ebr.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readAgain());
    }
}