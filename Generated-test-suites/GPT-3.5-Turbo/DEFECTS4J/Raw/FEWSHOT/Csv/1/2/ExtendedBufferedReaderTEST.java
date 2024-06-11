package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() {
        // Test case: Read a single character
        ExtendedBufferedReader reader = new ExtendedBufferedReader(null);
        int result = reader.read();
        assertEquals(ExtendedBufferedReader.UNDEFINED, result);

        // Test case: Read a new line character
        reader = new ExtendedBufferedReader(null);
        int lineCounter = reader.getLineNumber();
        int result = reader.read();
        assertEquals('\n', result);
        assertEquals(lineCounter + 1, reader.getLineNumber());

        // Test case: Read a character other than new line
        reader = new ExtendedBufferedReader(null);
        lineCounter = reader.getLineNumber();
        result = reader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
        assertEquals(lineCounter, reader.getLineNumber());
    }

    @Test
    public void testReadAgain() {
        // Test case: Read character again
        ExtendedBufferedReader reader = new ExtendedBufferedReader(null);
        reader.read();
        int result = reader.readAgain();
        assertEquals(ExtendedBufferedReader.UNDEFINED, result);
    }

    @Test
    public void testReadCharArray() {
        // Test case: Read characters into array
        ExtendedBufferedReader reader = new ExtendedBufferedReader(null);
        char[] buf = new char[3];
        int result = reader.read(buf, 0, 3);
        assertEquals(0, result);
    }

    @Test
    public void testReadLine() {
        // Test case: Read a line
        ExtendedBufferedReader reader = new ExtendedBufferedReader(null);
        String result = reader.readLine();
        assertEquals(null, result);
    }

    @Test
    public void testLookAhead() {
        // Test case: Look ahead
        ExtendedBufferedReader reader = new ExtendedBufferedReader(null);
        int result = reader.lookAhead();
        assertEquals(ExtendedBufferedReader.UNDEFINED, result);
    }

    @Test
    public void testGetLineNumber() {
        // Test case: Get line number
        ExtendedBufferedReader reader = new ExtendedBufferedReader(null);
        int result = reader.getLineNumber();
        assertEquals(0, result);
    }

}