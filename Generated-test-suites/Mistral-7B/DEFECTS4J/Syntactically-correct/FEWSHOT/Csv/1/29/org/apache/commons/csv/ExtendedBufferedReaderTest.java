package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("foo\nbar\nbaz".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

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
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("foo\nbar\nbaz".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        reader.read();
        reader.read();
        reader.read();

        assertEquals('f', reader.readAgain());
        assertEquals('o', reader.readAgain());
        assertEquals('o', reader.readAgain());

        reader.readLine();

        assertEquals('\n', reader.readAgain());
        assertEquals('b', reader.readAgain());
        assertEquals('a', reader.readAgain());
        assertEquals('r', reader.readAgain());
        assertEquals('\n', reader.readAgain());
        assertEquals('b', reader.readAgain());
        assertEquals('a', reader.readAgain());
        assertEquals('z', reader.readAgain());
        assertEquals('\n', reader.readAgain());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("foo\nbar\nbaz\n".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        assertEquals("foo", reader.readLine());
        assertEquals("bar", reader.readLine());
        assertEquals("baz", reader.readLine());
        assertNull(reader.readLine());
    }

    @Test
    public void testReadLineWithEolCharacter() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("foo\r\nbar\r\nbaz\r\n".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        assertEquals("foo", reader.readLine());
        assertEquals("bar", reader.readLine());
        assertEquals("baz", reader.readLine());
        assertNull(reader.readLine());
    }

    @Test
    public void testReadLineWithEmptyLine() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("foo\n\nbar\n".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        assertEquals("foo", reader.readLine());
        assertEquals("", reader.readLine());
        assertEquals("bar", reader.readLine());
    }

    @Test
    public void testReadLineWithComment() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("foo# This is a comment\nbar\nbaz\n".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        assertEquals("foo", reader.readLine());
        assertEquals("bar", reader.readLine());
        assertEquals("baz", reader.readLine());
        assertNull(reader.readLine());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("foo\nbar\nbaz\n".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        assertEquals(0, reader.getLineNumber());
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
        reader.readLine();
        assertEquals(2, reader.getLineNumber());
        reader.readLine();
        assertNull(reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("foo\nbar\nbaz".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        assertEquals('f', reader.lookAhead());
        reader.read();
        assertEquals('o', reader.lookAhead());
        reader.read();
        assertEquals('o', reader.lookAhead());
        reader.readLine();
        reader.read();
        assertEquals('b', reader.lookAhead());
        reader.read();
        assertEquals('a', reader.lookAhead());
        reader.readLine();
        reader.read();
        reader.read();
        assertEquals('b', reader.lookAhead());
        reader.read();
        assertEquals('a', reader.lookAhead());
        reader.readLine();
        reader.read();
        reader.read();
        reader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
    }

    @Test
    public void testLookAheadWithEmptyInputStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0][])));

        assertNull(reader.lookAhead());
    }
}