package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(stream));

        assertEquals('f', reader.read());
        assertEquals('o', reader.read());
        assertEquals('o', reader.read());
        assertEquals('\n', reader.read());
        assertEquals('b', reader.read());
        assertEquals('a', reader.read());
        assertEquals('r', reader.read());
        assertEquals('\n', reader.read());
        assertEquals('b', reader.read());
        assertEquals('a', reader.read());
        assertEquals('z', reader.read());
        assertEquals('\n', reader.read());
        assertEquals('q', reader.read());
        assertEquals('u', reader.read());
        assertEquals('x', reader.read());
        assertEquals('\n', reader.read());
        assertEquals(-1, reader.read());

        assertEquals(-1, reader.read());
        assertEquals(-1, reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(stream));

        assertEquals('f', reader.read());
        assertEquals('f', reader.readAgain());

        assertEquals('o', reader.read());
        assertEquals('o', reader.readAgain());

        assertEquals('\n', reader.read());
        assertEquals('\n', reader.readAgain());

        // EOF
        assertEquals(-1, reader.read());
        assertEquals(-1, reader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(stream));

        assertEquals("foo", reader.readLine());
        assertEquals("bar", reader.readLine());
        assertEquals("baz", reader.readLine());
        assertEquals("qux", reader.readLine());
        assertEquals(null, reader.readLine());

        assertEquals('f', reader.read());
        assertEquals('o', reader.read());
        assertEquals('o', reader.read());
        assertEquals('\n', reader.read());
        // reset
        reader.mark(1);
        reader.reset();
        assertEquals('f', reader.read());
        assertEquals('o', reader.read());
        assertEquals('o', reader.read());
        assertEquals('\n', reader.read());
    }

    @Test
    public void testLookAhead() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(stream));

        assertEquals('f', reader.read());

        assertEquals('o', reader.lookAhead());
        assertEquals('o', reader.lookAhead());
        assertEquals('\n', reader.lookAhead());
        assertEquals('b', reader.lookAhead());

        assertEquals(-1, reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(stream));

        assertEquals(0, reader.getLineNumber());
        assertEquals("foo", reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals("bar", reader.readLine());
        assertEquals(2, reader.getLineNumber());
        assertEquals("baz", reader.readLine());
        assertEquals(3, reader.getLineNumber());
        assertEquals("qux", reader.readLine());
        assertEquals(4, reader.getLineNumber());
        assertEquals(null, reader.readLine());
        assertEquals(4, reader.getLineNumber());
    }

    @Test
    public void testReadWithMarkAndReset() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(stream));

        reader.mark(1);
        assertEquals('f', reader.read());
        reader.reset();
        assertEquals('f', reader.read());

        reader.mark(1);
        assertEquals('o', reader.read());
        reader.reset();
        assertEquals('o', reader.read());

        reader.mark(1);
        assertEquals('\n', reader.read());
        reader.reset();
        assertEquals('\n', reader.read());
    }

    @Test
    public void testReadWithMarkButNoReset() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(stream));

        reader.mark(1);
        assertEquals('f', reader.read());
        reader.read(); // consume 'o'

        assertEquals('o', reader.read());
        assertEquals('o', reader.read());

        reader.mark(1);
        assertEquals('\n', reader.read());
        assertEquals('b', reader.read());

        assertEquals(-1, reader.read());
        assertEquals(-1, reader.read());
    }
}