package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExtendedBufferedReaderTest {

    private Reader reader;
    private ExtendedBufferedReader extReader;

    @Before
    public void setup() {
        String testString = "line1\r\nline2\r\nline3\r\n";
        reader = new InputStreamReader(new ByteArrayInputStream(testString.getBytes()));
        extReader = new ExtendedBufferedReader(reader);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('l', extReader.read());
        assertEquals('i', extReader.read());
        assertEquals('n', extReader.read());
        assertEquals('e', extReader.read());
        assertEquals('1', extReader.read());
        assertEquals('\r', extReader.read());
        assertEquals(' ', extReader.read());
        assertEquals('\n', extReader.read());
        assertEquals('l', extReader.read());
        assertEquals('i', extReader.read());
        assertEquals('n', extReader.read());
        assertEquals('e', extReader.read());
        assertEquals('2', extReader.read());
        assertEquals('\r', extReader.read());
        assertEquals(' ', extReader.read());
        assertEquals('\n', extReader.read());
        assertEquals('l', extReader.read());
        assertEquals('i', extReader.read());
        assertEquals('n', extReader.read());
        assertEquals('e', extReader.read());
        assertEquals('3', extReader.read());
        assertEquals('\r', extReader.read());
        assertEquals('\n', extReader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extReader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        assertEquals('l', extReader.read());
        assertEquals('l', extReader.readAgain());
        assertEquals('i', extReader.read());
        assertEquals('i', extReader.readAgain());
        assertEquals('n', extReader.read());
        assertEquals('n', extReader.readAgain());
        assertEquals('e', extReader.read());
        assertEquals('e', extReader.readAgain());
        assertEquals('1', extReader.read());
        assertEquals('1', extReader.readAgain());
        assertEquals('\r', extReader.read());
        assertEquals('\r', extReader.readAgain());
        assertEquals('\n', extReader.read());
        assertEquals('\n', extReader.readAgain());
        assertEquals('l', extReader.read());
        assertEquals('l', extReader.readAgain());
        assertEquals('i', extReader.read());
        assertEquals('i', extReader.readAgain());
        assertEquals('n', extReader.read());
        assertEquals('n', extReader.readAgain());
        assertEquals('e', extReader.read());
        assertEquals('e', extReader.readAgain());
        assertEquals('2', extReader.read());
        assertEquals('2', extReader.readAgain());
        assertEquals('\r', extReader.read());
        assertEquals('\r', extReader.readAgain());
        assertEquals('\n', extReader.read());
        assertEquals('\n', extReader.readAgain());
        assertEquals('l', extReader.read());
        assertEquals('l', extReader.readAgain());
        assertEquals('i', extReader.read());
        assertEquals('i', extReader.readAgain());
        assertEquals('n', extReader.read());
        assertEquals('n', extReader.readAgain());
        assertEquals('e', extReader.read());
        assertEquals('e', extReader.readAgain());
        assertEquals('3', extReader.read());
        assertEquals('3', extReader.readAgain());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extReader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extReader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        assertEquals("line1", extReader.readLine());
        assertEquals("line2", extReader.readLine());
        assertEquals("line3", extReader.readLine());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extReader.readLine());

        assertTrue(extReader.getLineNumber() == 3);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extReader.readAgain());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals('l', extReader.lookAhead());
        assertEquals('l', extReader.lookAhead());
        assertEquals('i', extReader.lookAhead());
        assertEquals('i', extReader.lookAhead());
        assertEquals('n', extReader.lookAhead());
        assertEquals('n', extReader.lookAhead());
        assertEquals('e', extReader.lookAhead());
        assertEquals('e', extReader.lookAhead());
        assertEquals('1', extReader.lookAhead());
        assertEquals('1', extReader.lookAhead());
        assertEquals('\r', extReader.lookAhead());
        assertEquals('\r', extReader.lookAhead());
        assertEquals('\n', extReader.lookAhead());
        assertEquals('\n', extReader.lookAhead());
        assertEquals('l', extReader.lookAhead());
        assertEquals('l', extReader.lookAhead());
        assertEquals('i', extReader.lookAhead());
        assertEquals('i', extReader.lookAhead());
        assertEquals('n', extReader.lookAhead());
        assertEquals('n', extReader.lookAhead());
        assertEquals('e', extReader.lookAhead());
        assertEquals('e', extReader.lookAhead());
        assertEquals('2', extReader.lookAhead());
        assertEquals('2', extReader.lookAhead());
        assertEquals('\r', extReader.lookAhead());
        assertEquals('\r', extReader.lookAhead());
        assertEquals('\n', extReader.lookAhead());
        assertEquals('\n', extReader.lookAhead());
        assertEquals('l', extReader.lookAhead());
        assertEquals('l', extReader.lookAhead());
        assertEquals('i', extReader.lookAhead());
        assertEquals('i', extReader.lookAhead());
        assertEquals('n', extReader.lookAhead());
        assertEquals('n', extReader.lookAhead());
        assertEquals('e', extReader.lookAhead());
        assertEquals('e', extReader.lookAhead());
        assertEquals('3', extReader.lookAhead());
        assertEquals('3', extReader.lookAhead());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extReader.lookAhead());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extReader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(0, extReader.getLineNumber());
        assertEquals("line1", extReader.readLine());
        assertEquals(1, extReader.getLineNumber());
        assertEquals("line2", extReader.readLine());
        assertEquals(2, extReader.getLineNumber());
        assertEquals("line3", extReader.readLine());
        assertEquals(3, extReader.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extReader.readLine());
        assertEquals(3, extReader.getLineNumber());
    }
}