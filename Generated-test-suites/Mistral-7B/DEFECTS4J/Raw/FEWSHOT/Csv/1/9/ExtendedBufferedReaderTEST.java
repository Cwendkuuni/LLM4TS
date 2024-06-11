package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        String input = "foo\nbar\nbaz\nqux";
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
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        assertEquals(10, reader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws IOException {
        String input = "foo\nbar\nbaz\nqux";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        for (int i = 0; i < 11; i++) {
            int c = reader.read();
            assertEquals(i < 10 ? i : ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
        }

        assertNull(reader.readAgain());
    }

    @Test
    public void testLookAhead() throws IOException {
        String input = "foo\nbar\nbaz\nqux";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        assertEquals('f', reader.lookAhead());
        assertEquals('o', reader.lookAhead());
        assertEquals('\n', reader.lookAhead());
        assertEquals('b', reader.lookAhead());
        assertEquals('a', reader.lookAhead());
        assertEquals('r', reader.lookAhead());
        assertEquals('\n', reader.lookAhead());
        assertEquals('b', reader.lookAhead());
        assertEquals('a', reader.lookAhead());
        assertEquals('z', reader.lookAhead());
        assertEquals('q', reader.lookAhead());
        assertEquals('u', reader.lookAhead());
        assertEquals('x', reader.lookAhead());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
        assertNull(reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String input = "foo\nbar\nbaz\nqux";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        assertEquals(0, reader.getLineNumber());
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
        reader.readLine();
        reader.readLine();
        reader.readLine();
        reader.readLine();
        reader.readLine();
        reader.readLine();
        assertEquals(9, reader.getLineNumber());
        reader.readLine();
        assertEquals(10, reader.getLineNumber());
        reader.readLine();
        assertEquals(-1, reader.getLineNumber());
    }

    @Test
    public void testMixedLineEndings() throws IOException {
        String input = "foo\r\nbar\r\nbaz\r\nqux\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        assertEquals('f', reader.read());
        assertEquals('\r');
        assertEquals('o', reader.read());
        assertEquals('\n');
        assertEquals('b', reader.read());
        assertEquals('\r');
        assertEquals('\n');
        assertEquals('b', reader.read());
        assertEquals('\r');
        assertEquals('\n');
        assertEquals('z', reader.read());
        assertEquals('\r');
        assertEquals('\n');
        assertEquals('q', reader.read());
        assertEquals('u', reader.read());
        assertEquals('x', reader.read());
        assertEquals('\n');
        assertEquals(12, reader.getLineNumber());
    }

    @Test
    public void testReadLineWithComment() throws IOException {
        String input = "foo\n#comment\nbaz\nqux";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        assertEquals("foo", reader.readLine());
        assertEquals("baz", reader.readLine());
        assertEquals(2, reader.getLineNumber());

        String comment = reader.readLine();
        assertNotNull(comment);
        assertEquals("#comment", comment);
        assertEquals(3, reader.getLineNumber());

        assertNull(reader.readLine());
        assertEquals(3, reader.getLineNumber());
    }

    @Test(expected = IOException.class)
    public void testLookAheadWithNoCharacters() throws IOException {
        String input = "";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        reader.lookAhead();
    }
}