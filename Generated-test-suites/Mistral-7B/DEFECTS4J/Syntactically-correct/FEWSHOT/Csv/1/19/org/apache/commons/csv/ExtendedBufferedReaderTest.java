package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

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
    }

    @Test
    public void testReadAgain() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        reader.mark(0);
        assertEquals('f', reader.read());
        assertEquals('f', reader.readAgain());

        reader.mark(1);
        assertEquals('o', reader.read());
        assertEquals('o', reader.readAgain());

        reader.mark(2);
        assertEquals('\n', reader.read());
        assertEquals('\n', reader.readAgain());

        reader.mark(3);
        assertEquals('b', reader.read());
        assertEquals('b', reader.readAgain());

        reader.mark(4);
        assertEquals('a', reader.read());
        assertEquals('a', reader.readAgain());

        reader.mark(5);
        assertEquals('r', reader.read());
        assertEquals('r', reader.readAgain());

        reader.mark(6);
        assertEquals('\n', reader.read());
        assertEquals('\n', reader.readAgain());

        reader.mark(7);
        assertEquals('b', reader.read());
        assertEquals('b', reader.readAgain());

        reader.mark(8);
        assertEquals('a', reader.read());
        assertEquals('a', reader.readAgain());

        reader.mark(9);
        assertEquals('z', reader.read());
        assertEquals('z', reader.readAgain());

        reader.mark(10);
        assertEquals('\n', reader.read());
        assertEquals('\n', reader.readAgain());

        reader.mark(11);
        assertEquals('q', reader.read());
        assertEquals('q', reader.readAgain());

        reader.mark(12);
        assertEquals('u', reader.read());
        assertEquals('u', reader.readAgain());

        reader.mark(13);
        assertEquals('x', reader.read());
        assertEquals('x', reader.readAgain());

        reader.mark(14);
        assertEquals('\n', reader.read());
        assertEquals(-1, reader.readAgain());

        reader.reset();
        assertEquals(-1, reader.read());
    }

    @Test
    public void testReadLine() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        assertEquals("foo", reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals('b', reader.read());
        assertEquals('a', reader.read());
        assertEquals('r', reader.read());
        assertEquals('\n', reader.read());
        assertEquals(2, reader.getLineNumber());
        assertEquals("baz", reader.readLine());
        assertEquals(3, reader.getLineNumber());
        assertEquals('q', reader.read());
        assertEquals('u', reader.read());
        assertEquals('x', reader.read());
        assertEquals('\n', reader.read());
        assertEquals(4, reader.getLineNumber());
        assertEquals(null, reader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        assertEquals('f', reader.lookAhead());
        assertEquals('f', reader.read());

        assertEquals('o', reader.lookAhead());
        assertEquals('o', reader.read());

        reader.mark(1);
        assertEquals('\n', reader.lookAhead());
        assertEquals('\n', reader.read());

        reader.mark(2);
        assertEquals('b', reader.lookAhead());
        assertEquals('b', reader.read());

        reader.mark(3);
        assertEquals('a', reader.lookAhead());
        assertEquals('a', reader.read());

        reader.mark(4);
        assertEquals('r', reader.lookAhead());
        assertEquals('r', reader.read());

        reader.mark(5);
        assertEquals('\n', reader.lookAhead());
        assertEquals('\n', reader.read());

        reader.mark(6);
        assertEquals('b', reader.lookAhead());
        assertEquals('b', reader.read());

        reader.mark(7);
        assertEquals('a', reader.lookAhead());
        assertEquals('a', reader.read());

        reader.mark(8);
        assertEquals('z', reader.lookAhead());
        assertEquals('z', reader.read());

        reader.mark(9);
        assertEquals('\n', reader.lookAhead());
        assertEquals(-1, reader.read());

        reader.reset();
        assertEquals(-1, reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String input = "foo\nbar\nbaz\nqux\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        assertEquals(0, reader.getLineNumber());
        assertEquals("foo", reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals("baz", reader.readLine());
        assertEquals(3, reader.getLineNumber());
        assertEquals(null, reader.readLine());
    }

    @Test
    public void testNullReader() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(null);
        assertNull(reader);
    }
}