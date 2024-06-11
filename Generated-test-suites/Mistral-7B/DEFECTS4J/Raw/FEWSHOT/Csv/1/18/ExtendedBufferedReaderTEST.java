package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc\ndef\n"));
        assertEquals('a', reader.read());
        assertEquals('b', reader.read());
        assertEquals('c', reader.read());
        assertEquals('\n', reader.read());
        assertEquals('\n', reader.read());
        assertEquals(-1, reader.read());
    }

    @Test
    public void testReadAgain() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc\ndef\n"));
        reader.mark(0);
        assertEquals('a', reader.read());
        assertEquals('a', reader.readAgain());
        reader.reset();
        assertEquals('b', reader.read());
        assertEquals('b', reader.readAgain());
        reader.reset();
        assertEquals('c', reader.read());
        assertEquals('c', reader.readAgain());
        reader.reset();
        assertEquals('\n', reader.read());
        assertEquals('\n', reader.readAgain());
        reader.reset();
        assertEquals(-1, reader.read());
        assertEquals(-1, reader.readAgain());
    }

    @Test
    public void testReadLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc\ndef\n"));
        assertEquals("abc", reader.readLine());
        assertEquals("def", reader.readLine());
        assertEquals(null, reader.readLine());
    }

    @Test
    public void testLineCounter() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc\ndef\n"));
        assertEquals(1, reader.getLineNumber());
        assertEquals("abc", reader.readLine());
        assertEquals(2, reader.getLineNumber());
        assertEquals("def", reader.readLine());
        assertEquals(3, reader.getLineNumber());
        assertEquals(null, reader.readLine());
        assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc\ndef\n"));
        assertEquals('a', reader.lookAhead());
        assertEquals('b', reader.lookAhead());
        assertEquals('c', reader.lookAhead());
        assertEquals('\n', reader.lookAhead());
        assertEquals('\n', reader.lookAhead());
        assertEquals(-1, reader.lookAhead());
    }

    @Test(expected = IOException.class)
    public void testMarkAndResetFail() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc\ndef\n"));
        reader.mark(0);
        reader.read();
        reader.reset();
    }

    @Test
    public void testMarkAndResetOk() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc\ndef\n"));
        reader.mark(0);
        reader.read();
        reader.reset();
        assertEquals('a', reader.read());
    }

    @Test
    public void testPeek() throws Exception {
        StringReader reader = new StringReader("abc\ndef\n");
        BufferedReader bufferedReader = new BufferedReader(reader);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(bufferedReader);
        assertEquals('a', extReader.read());
        assertEquals('a', extReader.peek());
        assertEquals('b', extReader.read());
        assertEquals('b', extReader.peek());
        assertEquals('c', extReader.read());
        assertEquals('\n', extReader.peek());
        assertEquals('\n', extReader.read());
        assertEquals(-1, extReader.read());
    }
}