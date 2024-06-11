package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.test.util.TestUtil;
import org.junit.Test;

/**
 * Test cases for ExtendedBufferedReader class
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testConstructor() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test".getBytes())));
        assertNull(reader.getLineNumber());
    }

    @Test
    public void testRead() throws IOException {
        String input = "test\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        int actual = reader.read();
        assertEquals('t', (char) actual);
        int expectedLastChar = 't';
        int actualLastChar = reader.readAgain();
        assertEquals(expectedLastChar, actualLastChar);

        int actual2 = reader.read();
        assertEquals('\n', (char) actual2);
        int expectedLastChar2 = '\n';
        int actualLastChar2 = reader.readAgain();
        assertEquals(expectedLastChar2, actualLastChar2);

        int actual3 = reader.read();
        assertFalse(reader.hasNext());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual3);
    }

    @Test
    public void testReadStringArray() throws IOException {
        String input = "test\nline1\nline2";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        char[] buffer = new char[2];
        int length = reader.read(buffer, 0, buffer.length);
        assertTrue(length > 0);
        assertEquals("test", new String(buffer));

        length = reader.read(buffer, 0, buffer.length);
        assertTrue(length > 0);
        assertEquals("line1", new String(buffer));

        length = reader.read(buffer, 0, buffer.length);
        assertFalse(length > 0);
        assertNull(reader.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        String input = "test\nline1\nline2\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        String actual = reader.readLine();
        assertEquals("test", actual);
        assertEquals("line1", reader.readLine());
        assertEquals("line2", reader.readLine());
        assertNull(reader.readLine());

        assertEquals(3, reader.getLineNumber());
    }

    @Test(expected = IOException.class)
    public void testLookAhead() throws IOException {
        String input = "test\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        reader.lookAhead();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String input = "test\nline1\nline2\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        assertEquals(0, reader.getLineNumber());

        String actual1 = reader.readLine();
        assertEquals("test", actual1);
        assertEquals(1, reader.getLineNumber());

        String actual2 = reader.readLine();
        assertEquals("line1", actual2);
        assertEquals(2, reader.getLineNumber());

        String actual3 = reader.readLine();
        assertEquals("line2", actual3);
        assertEquals(3, reader.getLineNumber());

        String actual4 = reader.readLine();
        assertNull(actual4);
        assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testPeek() throws IOException {
        String input = "test\ntest\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        int actual = reader.lookAhead();
        assertEquals('t', (char) actual);

        String actualLine = reader.readLine();
        assertEquals("test", actualLine);

        int expectedLastChar = 't';
        int actualLastChar = reader.readAgain();
        assertEquals(expectedLastChar, actualLastChar);

        actual = reader.lookAhead();
        assertEquals('t', (char) actual);

        actualLine = reader.readLine();
        assertEquals("test", actualLine);

        assertFalse(reader.hasNext());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
    }

    @Test
    public void testReadLineWithLF() throws IOException {
        String input = "test\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        String actualLine = reader.readLine();
        assertEquals("test", actualLine);
        assertEquals('\n', reader.readAgain());
    }

    @Test
    public void testReadLineWithCRLF() throws IOException {
        String input = "test\r\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        String actualLine = reader.readLine();
        assertEquals("test", actualLine);
        assertEquals('\r', reader.readAgain());
        assertEquals('\n', reader.readAgain());
    }

    @Test
    public void testReadLineWithCR() throws IOException {
        String input = "test\r";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        String actualLine = reader.readLine();
        assertEquals("test", actualLine);
        assertEquals('\r', reader.readAgain());
    }

    @Test
    public void testReadLineWithEmptyLine() throws IOException {
        String input = "\r\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        String actualLine = reader.readLine();
        assertEquals(null, actualLine);
        assertEquals('\r', reader.readAgain());
        assertEquals('\n', reader.readAgain());
    }

    @Test
    public void testReadLineWithEmptyInput() throws IOException {
        String input = "";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        assertNull(reader.readLine());
    }
}