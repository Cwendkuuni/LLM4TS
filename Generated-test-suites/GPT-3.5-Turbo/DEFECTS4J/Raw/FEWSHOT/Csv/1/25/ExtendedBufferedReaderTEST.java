
package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class ExtendedBufferedReaderTest {
    
    private ExtendedBufferedReader reader;

    @Before
    public void setup() {
        String testString = "Line 1\nLine 2\r\nLine 3";
        reader = new ExtendedBufferedReader(new StringReader(testString));
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('L', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('1', reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        assertEquals(' ', reader.readAgain());
    }

    @Test
    public void testReadCharArray() throws IOException {
        char[] buf = new char[5];
        assertEquals(5, reader.read(buf, 0, 5));
        assertEquals('L', buf[0]);
        assertEquals('i', buf[1]);
        assertEquals('n', buf[2]);
        assertEquals('e', buf[3]);
        assertEquals(' ', buf[4]);
    }

    @Test
    public void testReadLine() throws IOException {
        assertEquals("Line 1", reader.readLine());
        assertEquals("Line 2", reader.readLine());
        assertEquals("Line 3", reader.readLine());
        assertNull(reader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals('L', reader.lookAhead());
        assertEquals('L', reader.read());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(0, reader.getLineNumber());
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
        reader.readLine();
        assertEquals(2, reader.getLineNumber());
        reader.readLine();
        assertEquals(3, reader.getLineNumber());
    }

}

