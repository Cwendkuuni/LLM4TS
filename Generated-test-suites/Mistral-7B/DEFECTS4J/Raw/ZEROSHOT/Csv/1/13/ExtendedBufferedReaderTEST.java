package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        String input = "Line1\rLine2\nLine3\rLine4\nLine5";
        reader = new ExtendedBufferedReader(new StringReader(input));
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals(' ' , reader.read());
        assertEquals('n', reader.read());
        assertEquals('\r', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('L', reader.read());
        assertEquals('e', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('1', reader.read());
        assertEquals('\n', reader.read());
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals('\r', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('3', reader.read());
        assertEquals('\r', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals('\r', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('5', reader.read());
        assertEquals('\n', reader.read());
        assertEquals('\r', reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('n', reader.read());
        assertEquals('\r', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('L', reader.read());
        assertEquals('e', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('1', reader.read());
        assertEquals('\n', reader.read());
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals('\r', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('3', reader.read());
        assertEquals('\r', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals('\r', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('5', reader.read());
        assertEquals('\n', reader.read());
        assertEquals('\r', reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        assertEquals('L', reader.readAgain());
        assertEquals('i', reader.readAgain());
        assertEquals(' ', reader.readAgain());
        assertEquals('L', reader.readAgain());
        assertEquals('i', reader.readAgain());
        assertEquals('n', reader.readAgain());
        assertEquals(' ', reader.readAgain());
        assertEquals('L', reader.readAgain());
        assertEquals('i', reader.readAgain());
        assertEquals('n', reader.readAgain());
        assertEquals(' ', reader.readAgain());
        assertEquals('5', reader.readAgain());
        assertEquals('\n', reader.readAgain());
        assertEquals('\r', reader.readAgain());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        assertEquals("Line1\r", reader.readLine());
        assertEquals("Line2\n", reader.readLine());
        assertEquals("Line3\r", reader.readLine());
        assertEquals("Line4\n", reader.readLine());
        assertEquals("Line5", reader.readLine());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals('\r', reader.lookAhead());
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('\r', reader.lookAhead());
        assertEquals(' ', reader.read());
        assertEquals('L', reader.read());
        assertEquals('e', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('1', reader.read());
        assertEquals('\n', reader.read());
        assertEquals('\n', reader.lookAhead());
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals('\r', reader.lookAhead());
        assertEquals(' ', reader.read());
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals('\r', reader.lookAhead());
        assertEquals(' ', reader.read());
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals('\r', reader.lookAhead());
        assertEquals(' ', reader.read());
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('5', reader.read());
        assertEquals('\n', reader.read());
        assertEquals('\r', reader.lookAhead());
        assertEquals('\r', reader.read());
        assertEquals('\n', reader.lookAhead());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(0, reader.getLineNumber());
        assertEquals(1, reader.readLine() == null ? 0 : reader.getLineNumber());
        assertEquals(2, reader.readLine() == null ? 0 : reader.getLineNumber());
        assertEquals(3, reader.readLine() == null ? 0 : reader.getLineNumber());
        assertEquals(4, reader.readLine() == null ? 0 : reader.getLineNumber());
        assertEquals(5, reader.readLine() == null ? 0 : reader.getLineNumber());
        assertEquals(-1, reader.getLineNumber());
    }
}