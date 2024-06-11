package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(new StringReader("a\nb\nc\nd"));
        assertEquals('a', ebr.read());
        assertEquals('n', ebr.read());
        assertEquals('b', ebr.read());
        assertEquals('\n', ebr.read()); // line counter incremented
        assertEquals('c', ebr.read());
        assertEquals('n', ebr.read());
        assertEquals('d', ebr.read());
        assertEquals('\n', ebr.read()); // line counter incremented
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(new StringReader("a\nb\nc\nd"));
        assertEquals('a', ebr.read());
        assertEquals('a', ebr.readAgain());
        assertEquals('n', ebr.read());
        assertEquals('n', ebr.readAgain());
        assertEquals('b', ebr.read());
        assertEquals('b', ebr.readAgain());
        assertEquals('\n', ebr.read());
        assertEquals('\n', ebr.readAgain());
        assertEquals('c', ebr.read());
        assertEquals('c', ebr.readAgain());
        assertEquals('d', ebr.read());
        assertEquals('d', ebr.readAgain());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(new StringReader("a\nb\nc\nd\ne\nf"));
        assertEquals("a", ebr.readLine());
        assertEquals("b", ebr.readLine());
        assertEquals("c", ebr.readLine());
        assertEquals("d", ebr.readLine());
        assertEquals("e", ebr.readLine());
        assertEquals("f", ebr.readLine());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readLine());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(new StringReader("a\nb\nc\nd\ne\nf"));
        assertEquals('a', ebr.lookAhead());
        assertEquals('n', ebr.lookAhead());
        assertEquals('b', ebr.lookAhead());
        assertEquals('c', ebr.lookAhead());
        assertEquals('d', ebr.lookAhead());
        assertEquals('e', ebr.lookAhead());
        assertEquals('f', ebr.lookAhead());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.lookAhead());
    }

    @Test
    public void testLineNumber() throws IOException {
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(new StringReader("a\nb\nc\nd\ne\nf"));
        assertEquals(0, ebr.getLineNumber());
        assertEquals("a", ebr.readLine());
        assertEquals(1, ebr.getLineNumber());
        assertEquals("b", ebr.readLine());
        assertEquals(2, ebr.getLineNumber());
        assertEquals("c", ebr.readLine());
        assertEquals(3, ebr.getLineNumber());
        assertEquals("d", ebr.readLine());
        assertEquals(4, ebr.getLineNumber());
        assertEquals("e", ebr.readLine());
        assertEquals(5, ebr.getLineNumber());
        assertEquals("f", ebr.readLine());
        assertEquals(6, ebr.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readLine());
        assertEquals(7, ebr.getLineNumber());
    }

    @Test
    public void testPeekAndReadLine() throws IOException {
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(new StringReader("a\nb\nc\nd\ne\nf"));
        String peek = ebr.readLine();
        assertEquals("a", peek);
        assertEquals("a", ebr.readLine());
        String peekAgain = ebr.readLine();
        assertEquals("b", peekAgain);
        assertEquals("b", ebr.readLine());
    }

    @Test
    public void testReadEmptyLine() throws IOException {
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());
        assertEquals(0, ebr.getLineNumber());
    }

    @Test
    public void testReadLineWithNullReader() {
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(null);
        assertNull(ebr.readLine());
    }
}