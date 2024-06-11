package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testExtendedBufferedReader() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\nline3\n"));

        // Test read method
        assertEquals('l', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals('1', reader.read()); // readAgain()
        assertEquals('\n', reader.read());
        assertEquals('l', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals('2', reader.read()); // readAgain()
        assertEquals('\n', reader.read());
        assertEquals('l', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals('3', reader.read()); // readAgain()
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        // Test readLine method
        assertEquals("line1", reader.readLine());
        assertEquals("line2", reader.readLine());
        assertEquals("line3", reader.readLine());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());

        // Test lookAhead method
        assertEquals('l', reader.lookAhead());
        assertEquals('i', reader.lookAhead());
        assertEquals('n', reader.lookAhead());
        assertEquals('e', reader.lookAhead());
        assertEquals('1', reader.lookAhead()); // EOF

        // Test getLineNumber method
        assertEquals(3, reader.getLineNumber());
    }
}