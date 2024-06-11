package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;

/**
 * ExtendedBufferedReaderTest.java
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testExtendedBufferedReader() {
        String input = "line1\nline2\nline3";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        // Test read method
        assertEquals(69, reader.read());
        assertEquals('\n', reader.read());
        assertEquals(108, reader.read());
        assertEquals('\n', reader.read());
        assertEquals(110, reader.read());
        assertEquals(' ', reader.read());
        assertEquals(END_OF_STREAM, reader.read());

        // Test readAgain method
        assertEquals(110, reader.readAgain());
        assertEquals(32, reader.readAgain());
        assertEquals(END_OF_STREAM, reader.readAgain());

        // Test read method with length > 0
        char[] buffer = new char[3];
        int read = reader.read(buffer);
        assertEquals(3, read);
        assertEquals('l', buffer[0]);
        assertEquals('i', buffer[1]);
        assertEquals('n', buffer[2]);

        // Test readLine method
        assertEquals("line1", reader.readLine());
        assertEquals(10, reader.getLineNumber());
        assertEquals("line2", reader.readLine());
        assertEquals("line3", reader.readLine());
        assertEquals(END_OF_STREAM, reader.readLine());

        // Test lookAhead method
        assertEquals('l', reader.lookAhead());
        assertEquals('\n', reader.lookAhead());
        reader.mark(1);
        assertEquals('l', reader.lookAhead());
        reader.reset();
        assertEquals('i', reader.lookAhead());
        reader.mark(1);
        assertEquals('i', reader.lookAhead());
        reader.reset();
        assertEquals(END_OF_STREAM, reader.lookAhead());

        // Test getLineNumber method
        assertEquals(3, reader.getLineNumber());
    }
}